package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.HighschoolExamScoreStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.ExamYearData;
import com.demo.admissionportal.dto.YearlyExamScoreResponse;
import com.demo.admissionportal.dto.entity.SubjectDTO;
import com.demo.admissionportal.dto.request.CreateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.response.*;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.sub_entity.ListExamScoreHighSchoolExamScore;
import com.demo.admissionportal.entity.sub_entity.SubjectGroupSubject;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.repository.sub_repository.ListExamScoreHighSchoolExamScoreRepository;
import com.demo.admissionportal.repository.sub_repository.SubjectGroupSubjectRepository;
import com.demo.admissionportal.service.HighschoolExamScoreService;
import com.demo.admissionportal.util.impl.EmailUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HighschoolExamScoreServiceImpl implements HighschoolExamScoreService {
    @Autowired
    private HighschoolExamScoreRepository highschoolExamScoreRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SubjectGroupRepository subjectGroupRepository;
    @Autowired
    private SubjectGroupSubjectRepository subjectGroupSubjectRepository;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private ListExamScoreHighSchoolExamScoreRepository listExamScoreHighSchoolExamScoreRepository;
    @Autowired
    private ListExamScoreByYearRepository listExamScoreByYearRepository;

    @Override
    public ResponseData<List<HighschoolExamScoreResponse>> findAllWithFilter(Integer identificationNumber, Integer year) {
        try {
            List<HighschoolExamScore> examScoresPage = highschoolExamScoreRepository.findAll(identificationNumber, year);
            if (identificationNumber == null || highschoolExamScoreRepository.countByIdentificationNumberAndYear(identificationNumber, year) == 0) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy số báo danh này !");
            }
            boolean statusScore = examScoresPage.stream()
                    .allMatch(score -> score.getStatus().equals(HighschoolExamScoreStatus.INACTIVE));

            if (statusScore) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Điểm thi chưa được công bố!");
            }

            Map<Integer, List<HighschoolExamScore>> groupedById = examScoresPage.stream()
                    .collect(Collectors.groupingBy(HighschoolExamScore::getIdentificationNumber));

            List<HighschoolExamScoreResponse> responseList = new ArrayList<>();
            for (Map.Entry<Integer, List<HighschoolExamScore>> entry : groupedById.entrySet()) {
                List<SubjectScoreDTO> subjectScores = new ArrayList<>();
                String examinationBoard = null;
                String examiner = null;
                String dateOfBirth = null;
                Integer resultYear = null;
                String local = null;

                BigDecimal khtnTotalScore = BigDecimal.ZERO;
                BigDecimal khxhTotalScore = BigDecimal.ZERO;
                boolean hasKHTN = false;
                boolean hasKHXH = false;

                for (HighschoolExamScore score : entry.getValue()) {
                    if (score.getScore() != null) {
                        SubjectDTO subjectDTO = getSubjectDetails(score.getSubjectId());
                        if (subjectDTO != null) {
                            subjectScores.add(new SubjectScoreDTO(subjectDTO.getSubjectId(), subjectDTO.getSubjectName(), score.getScore()));

                            if (Set.of(27, 16, 23).contains(subjectDTO.getSubjectId())) {
                                khtnTotalScore = khtnTotalScore.add(BigDecimal.valueOf(score.getScore()).divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP));
                                hasKHTN = true;
                            } else if (Set.of(34, 9, 54).contains(subjectDTO.getSubjectId())) {
                                khxhTotalScore = khxhTotalScore.add(BigDecimal.valueOf(score.getScore()).divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP));
                                hasKHXH = true;
                            }
                        }
                    }
                    if (resultYear == null) {
                        resultYear = score.getYear();
                    }
                    if (local == null) {
                        local = score.getLocal();
                    }
                    if (examinationBoard == null) {
                        examinationBoard = score.getExaminationBoard();
                    }
                    if (dateOfBirth == null) {
                        dateOfBirth = score.getDateOfBirth();
                    }
                    if (examiner == null) {
                        examiner = score.getExaminer();
                    }
                }

                if (hasKHTN) {
                    subjectScores.add(new SubjectScoreDTO(null, "KHTN", khtnTotalScore.setScale(2, RoundingMode.HALF_UP).floatValue()));
                }

                if (hasKHXH) {
                    subjectScores.add(new SubjectScoreDTO(null, "KHXH", khxhTotalScore.setScale(2, RoundingMode.HALF_UP).floatValue()));
                }

                responseList.add(new HighschoolExamScoreResponse(
                        entry.getKey(),
                        local,
                        examinationBoard,
                        dateOfBirth,
                        examiner,
                        resultYear,
                        subjectScores
                ));
            }

            return new ResponseData<>(ResponseCode.C200.getCode(), "Tra cứu điểm thành công !", responseList);
        } catch (Exception e) {
            log.error("Error fetching exam scores", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình tra cứu điểm, vui lòng thử lại sau.");
        }
    }


    private static final List<Integer> ALLOWED_SUBJECT_IDS = List.of(36, 28, 38, 27, 16, 23, 34, 9, 54);

    @Override
    @Transactional
    public ResponseData<List<YearlyExamScoreResponse>> createExamScores(List<ExamYearData> examYearDataList) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Người tham chiếu không hợp lệ!");
            }
            User staff = (User) principal;
            Integer staffId = staff.getId();

            Map<Integer, List<ExamYearData>> examYearDataGroupedByYear = examYearDataList.stream()
                    .collect(Collectors.groupingBy(ExamYearData::getYear));

            List<YearlyExamScoreResponse> yearlyResponses = new ArrayList<>();

            for (Map.Entry<Integer, List<ExamYearData>> entry : examYearDataGroupedByYear.entrySet()) {
                int year = entry.getKey();
                List<ExamYearData> groupedExamYearData = entry.getValue();

                ListExamScoreByYear listExamScoreByYear = listExamScoreByYearRepository.findByYear(year);
                if (listExamScoreByYear == null) {
                    listExamScoreByYear = new ListExamScoreByYear();
                    listExamScoreByYear.setTitle(groupedExamYearData.get(0).getTitle());
                    listExamScoreByYear.setYear(year);
                    listExamScoreByYear.setStatus("INACTIVE");
                    listExamScoreByYear = listExamScoreByYearRepository.save(listExamScoreByYear);
                }
                ListExamScoreByYear finalListExamScoreByYear = listExamScoreByYear;

                List<HighschoolExamScoreResponse> yearResponses = new ArrayList<>();

                for (ExamYearData examYearData : groupedExamYearData) {
                    for (CreateHighschoolExamScoreRequest request : examYearData.getExamScoreData()) {
                        List<HighschoolExamScore> existIdentificationNumber = highschoolExamScoreRepository.findByIdentificationNumber(request.getIdentificationNumber());
                        if (existIdentificationNumber != null && !existIdentificationNumber.isEmpty()) {
                            log.error("Identification Number {} is already existed for year {}", request.getIdentificationNumber(), year);
                            throw new IllegalStateException("Số báo danh thí sinh đã tồn tại");
                        }

                        Map<Integer, SubjectScoreDTO> subjectScoreMap = request.getSubjectScores().stream()
                                .collect(Collectors.toMap(SubjectScoreDTO::getSubjectId, score -> score));

                        List<HighschoolExamScore> examScores = ALLOWED_SUBJECT_IDS.stream().map(subjectId -> {
                            SubjectScoreDTO subjectScore = subjectScoreMap.getOrDefault(subjectId, new SubjectScoreDTO(subjectId, null, null));
                            HighschoolExamScore examScore = new HighschoolExamScore();
                            examScore.setIdentificationNumber(request.getIdentificationNumber());
                            examScore.setLocal(request.getLocal());
                            examScore.setExaminationBoard(request.getExaminationBoard());
                            examScore.setExaminer(request.getExaminer());
                            examScore.setDateOfBirth(request.getDateOfBirth());
                            examScore.setSubjectId(subjectId);
                            examScore.setYear(year);
                            examScore.setScore(subjectScore.getScore());
                            examScore.setCreateTime(new Date());
                            examScore.setCreateBy(staffId);
                            examScore.setStatus(HighschoolExamScoreStatus.INACTIVE);
                            return examScore;
                        }).collect(Collectors.toList());

                        highschoolExamScoreRepository.saveAll(examScores);
                        examScores.forEach(savedExamScore -> {
                            ListExamScoreHighSchoolExamScore listExamScoreHighSchoolExamScore = new ListExamScoreHighSchoolExamScore();
                            listExamScoreHighSchoolExamScore.setListExamScoreByYearId(finalListExamScoreByYear.getId());
                            listExamScoreHighSchoolExamScore.setHighschoolExamScoreId(savedExamScore.getId());
                            listExamScoreHighSchoolExamScore.setStatus("INACTIVE");
                            listExamScoreHighSchoolExamScoreRepository.save(listExamScoreHighSchoolExamScore);
                        });

                        List<SubjectScoreDTO> allSubjectScores = ALLOWED_SUBJECT_IDS.stream().map(subjectId -> {
                            String subjectName = subjectRepository.findById(subjectId)
                                    .map(subject -> subject.getName())
                                    .orElse(null);
                            SubjectScoreDTO scoreDTO = subjectScoreMap.getOrDefault(subjectId, new SubjectScoreDTO(subjectId, null, null));
                            return new SubjectScoreDTO(subjectId, subjectName, scoreDTO.getScore());
                        }).collect(Collectors.toList());

                        BigDecimal khtnTotalScore = BigDecimal.ZERO;
                        BigDecimal khxhTotalScore = BigDecimal.ZERO;
                        boolean hasKHTN = false;
                        boolean hasKHXH = false;

                        for (SubjectScoreDTO scoreDTO : allSubjectScores) {
                            if (scoreDTO.getScore() != null) {
                                if (Set.of(27, 16, 23).contains(scoreDTO.getSubjectId())) {
                                    khtnTotalScore = khtnTotalScore.add(BigDecimal.valueOf(scoreDTO.getScore()));
                                    hasKHTN = true;
                                } else if (Set.of(34, 9, 54).contains(scoreDTO.getSubjectId())) {
                                    khxhTotalScore = khxhTotalScore.add(BigDecimal.valueOf(scoreDTO.getScore()));
                                    hasKHXH = true;
                                }
                            }
                        }

                        if (hasKHTN) {
                            khtnTotalScore = khtnTotalScore.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
                            allSubjectScores.add(new SubjectScoreDTO(999999, "KHTN", khtnTotalScore.floatValue()));
                        } else {
                            allSubjectScores.add(new SubjectScoreDTO(999999, "KHTN", null));
                        }

                        if (hasKHXH) {
                            khxhTotalScore = khxhTotalScore.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
                            allSubjectScores.add(new SubjectScoreDTO(999998, "KHXH", khxhTotalScore.floatValue()));
                        } else {
                            allSubjectScores.add(new SubjectScoreDTO(999998, "KHXH", null));
                        }

                        yearResponses.add(new HighschoolExamScoreResponse(
                                request.getIdentificationNumber(),
                                request.getLocal(),
                                request.getExaminationBoard(),
                                request.getDateOfBirth(),
                                request.getExaminer(),
                                year,
                                allSubjectScores
                        ));
                    }
                }

                yearlyResponses.add(new YearlyExamScoreResponse(finalListExamScoreByYear.getTitle(), year, yearResponses));
            }

            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo điểm thi thành công!", yearlyResponses);

        } catch (Exception e) {
            log.error("Error creating exam scores", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình tạo điểm, vui lòng thử lại sau. Lỗi: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseData<List<YearlyExamScoreResponse>> updateExamScores(Integer listExamScoreByYearId, List<ExamYearData> examYearDataList) {
        List<YearlyExamScoreResponse> yearlyResponses = new ArrayList<>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Người tham chiếu không hợp lệ!");
            }
            User staff = (User) principal;
            Integer staffId = staff.getId();

            ListExamScoreByYear listExamScoreByYear = listExamScoreByYearRepository.findById(listExamScoreByYearId)
                    .orElseThrow(() -> new IllegalStateException("ListExamScoreByYear ID không tồn tại"));

            for (ExamYearData examYearData : examYearDataList) {
                int year = examYearData.getYear();

                List<HighschoolExamScoreResponse> yearResponses = new ArrayList<>();

                for (CreateHighschoolExamScoreRequest request : examYearData.getExamScoreData()) {
                    Integer identificationNumber = request.getIdentificationNumber();
                    if (identificationNumber != null) {
                        List<HighschoolExamScore> existingScores = highschoolExamScoreRepository.findByIdentificationNumberAndYear(
                                identificationNumber, year);

                        Map<Integer, HighschoolExamScore> existingScoresMap = existingScores.stream()
                                .collect(Collectors.toMap(HighschoolExamScore::getSubjectId, score -> score));

                        for (SubjectScoreDTO score : request.getSubjectScores()) {
                            if (!ALLOWED_SUBJECT_IDS.contains(score.getSubjectId())) {
                                return new ResponseData<>(ResponseCode.C201.getCode(), "Mã môn học không hợp lệ: " + score.getSubjectId());
                            }
                            if (score.getScore() != null && (score.getScore() < 0 || score.getScore() > 10)) {
                                return new ResponseData<>(ResponseCode.C201.getCode(), "Điểm không hợp lệ: " + score.getScore());
                            }

                            HighschoolExamScore existingScore = existingScoresMap.get(score.getSubjectId());
                            if (existingScore != null) {
                                existingScore.setScore(score.getScore());
                                existingScore.setUpdateTime(new Date());
                                existingScore.setUpdateBy(staffId);
                            } else {
                                HighschoolExamScore newScore = new HighschoolExamScore();
                                newScore.setIdentificationNumber(identificationNumber);
                                newScore.setSubjectId(score.getSubjectId());
                                newScore.setYear(year);
                                newScore.setScore(score.getScore());
                                newScore.setCreateTime(new Date());
                                newScore.setCreateBy(staffId);
                                existingScores.add(newScore);
                            }
                        }

                        highschoolExamScoreRepository.saveAll(existingScores);

                        Map<Integer, SubjectScoreDTO> subjectScoreMap = request.getSubjectScores().stream()
                                .collect(Collectors.toMap(SubjectScoreDTO::getSubjectId, score -> score));

                        List<SubjectScoreDTO> allSubjectScores = ALLOWED_SUBJECT_IDS.stream()
                                .map(subjectId -> {
                                    SubjectScoreDTO subjectScore = subjectScoreMap.getOrDefault(subjectId, new SubjectScoreDTO(subjectId, null, null));
                                    String subjectName = subjectRepository.findById(subjectId)
                                            .map(subject -> subject.getName())
                                            .orElse(null);
                                    subjectScore.setSubjectName(subjectName);
                                    return subjectScore;
                                })
                                .collect(Collectors.toList());

                        HighschoolExamScoreResponse response = new HighschoolExamScoreResponse();
                        response.setIdentificationNumber(identificationNumber);
                        response.setLocal(request.getLocal());
                        response.setExaminationBoard(request.getExaminationBoard());
                        response.setDateOfBirth(request.getDateOfBirth());
                        response.setExaminer(request.getExaminer());
                        response.setYear(year);
                        response.setSubjectScores(allSubjectScores);

                        yearResponses.add(response);

                    } else {
                        return new ResponseData<>(ResponseCode.C204.getCode(), "Yêu cầu không hợp lệ!");
                    }
                }

                YearlyExamScoreResponse yearlyResponse = new YearlyExamScoreResponse(examYearData.getTitle(), year, yearResponses);
                yearlyResponses.add(yearlyResponse);
            }

            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật điểm thi thành công!", yearlyResponses);

        } catch (Exception e) {
            log.error("Error updating exam scores", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình cập nhật điểm, vui lòng thử lại sau. Lỗi: " + e.getMessage());
        }
    }



    @Override
    public ResponseData<Map<String, Map<String, Float>>> getScoreDistributionByLocal(String subjectName) {
        try {
            Map<String, Map<String, Float>> scoreDistribution = new LinkedHashMap<>();
            List<Integer> subjectIds = new ArrayList<>();

            if (subjectName != null && !subjectName.isEmpty()) {
                if (subjectName.equals("KHTN")) {
                    subjectIds = List.of(27, 16, 23);
                    Map<String, Float> khtnScoresByLocal = calculateScoresByLocal(subjectIds);
                    scoreDistribution.put("KHTN", khtnScoresByLocal);
                } else if (subjectName.equals("KHXH")) {
                    subjectIds = List.of(34, 9, 54);
                    Map<String, Float> khxhScoresByLocal = calculateScoresByLocal(subjectIds);
                    scoreDistribution.put("KHXH", khxhScoresByLocal);
                } else {
                    Optional<Subject> subjectOpt = subjectRepository.findByName(subjectName);
                    if (subjectOpt.isPresent()) {
                        Integer subjectId = subjectOpt.get().getId();
                        if (!ALLOWED_SUBJECT_IDS.contains(subjectId)) {
                            return new ResponseData<>(ResponseCode.C201.getCode(), "Mã môn học không hợp lệ: " + subjectName);
                        }
                        subjectIds = List.of(subjectId);
                        Map<String, Float> subjectScoresByLocal = calculateScoresByLocal(subjectIds);
                        scoreDistribution.put(subjectName, subjectScoresByLocal);
                    } else {
                        return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy môn học này !");
                    }
                }
            } else {
                subjectIds = ALLOWED_SUBJECT_IDS;
                for (Integer subjectId : subjectIds) {
                    Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
                    if (subjectOpt.isPresent()) {
                        String subjectNameKey = subjectOpt.get().getName();
                        Map<String, Float> subjectScoresByLocal = calculateScoresByLocal(List.of(subjectId));
                        scoreDistribution.put(subjectNameKey, subjectScoresByLocal);
                    }
                }
                Map<String, Float> khtnScoresByLocal = calculateScoresByLocal(List.of(27, 16, 23));
                Map<String, Float> khxhScoresByLocal = calculateScoresByLocal(List.of(34, 9, 54));
                scoreDistribution.put("KHTN", khtnScoresByLocal);
                scoreDistribution.put("KHXH", khxhScoresByLocal);
            }

            Map<String, Map<String, Float>> orderedScoreDistribution = new LinkedHashMap<>();
            String[] subjectsOrder = {"Toán", "Văn", "Tiếng Anh", "Vật lý", "Hóa học", "Sinh học", "Lịch Sử", "Địa Lí", "Giáo dục công dân", "KHTN", "KHXH"};

            for (String subject : subjectsOrder) {
                if (scoreDistribution.containsKey(subject)) {
                    orderedScoreDistribution.put(subject, scoreDistribution.get(subject));
                }
            }

            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy phổ điểm thành công", orderedScoreDistribution);
        } catch (Exception e) {
            log.error("Error fetching score distribution by local", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình lấy phổ điểm, vui lòng thử lại sau.");
        }
    }

    private Map<String, Float> calculateScoresByLocal(List<Integer> subjectIds) {
        Map<String, Map<Integer, List<Float>>> scoresByLocalAndSubject = new HashMap<>();

        for (Integer subjectId : subjectIds) {
            List<Object[]> scoresData = highschoolExamScoreRepository.findScoresBySubjectId(subjectId);
            for (Object[] data : scoresData) {
                if (data[0] != null && data[1] != null) {
                    String local = (String) data[0];
                    Float score = ((Number) data[1]).floatValue();

                    scoresByLocalAndSubject.computeIfAbsent(local, k -> new HashMap<>())
                            .computeIfAbsent(subjectId, k -> new ArrayList<>())
                            .add(score);
                }
            }
        }

        Map<String, Float> scoreByLocal = new HashMap<>();
        for (Map.Entry<String, Map<Integer, List<Float>>> entry : scoresByLocalAndSubject.entrySet()) {
            String local = entry.getKey();
            Map<Integer, List<Float>> scoresBySubject = entry.getValue();

            float totalAverageScore = 0;
            int count = 0;
            for (List<Float> scores : scoresBySubject.values()) {
                float subjectAverageScore = (float) scores.stream().mapToDouble(Float::doubleValue).average().orElse(0.0);
                totalAverageScore += subjectAverageScore;
                count++;
            }

            float averageScore = totalAverageScore / count;
            scoreByLocal.put(local, averageScore);
        }
        return scoreByLocal;
    }

    @Override
    public ResponseData<Map<String, Map<Float, Integer>>> getScoreDistributionBySubject(String local, String subjectName) {
        try {
            Map<String, Map<Float, Integer>> scoreDistribution = new LinkedHashMap<>();

            if (subjectName != null && !subjectName.isEmpty()) {
                log.debug("Fetching scores for subject: {}", subjectName);

                if (subjectName.equalsIgnoreCase("KHTN")) {
                    scoreDistribution.put("KHTN", fetchScoresForSubjectGroup(List.of(27, 16, 23), local));
                } else if (subjectName.equalsIgnoreCase("KHXH")) {
                    scoreDistribution.put("KHXH", fetchScoresForSubjectGroup(List.of(34, 9, 54), local));
                } else {
                    Optional<Subject> subjectOpt = subjectRepository.findByName(subjectName);

                    if (subjectOpt.isPresent()) {
                        Integer subjectId = subjectOpt.get().getId();

                        if (ALLOWED_SUBJECT_IDS.contains(subjectId)) {
                            Map<Float, Integer> scores = fetchAllScoresBySubject(subjectId, local);
                            scoreDistribution.put(subjectName, scores);
                        } else {
                            return new ResponseData<>(ResponseCode.C201.getCode(), "Mã môn học không hợp lệ: " + subjectName);
                        }
                    } else {
                        return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy môn học này !");
                    }
                }
            } else {
                List<Subject> allSubjects = subjectRepository.findAllById(ALLOWED_SUBJECT_IDS);
                for (Subject subject : allSubjects) {
                    Integer subjectId = subject.getId();
                    Map<Float, Integer> scores = fetchAllScoresBySubject(subjectId, local);
                    scoreDistribution.put(subject.getName(), scores);
                }

                scoreDistribution.put("KHTN", fetchScoresForSubjectGroup(List.of(27, 16, 23), local));
                scoreDistribution.put("KHXH", fetchScoresForSubjectGroup(List.of(34, 9, 54), local));
            }

            Map<String, Map<Float, Integer>> orderedScoreDistribution = new LinkedHashMap<>();
            String[] subjectsOrder = {"Toán", "Văn", "Tiếng Anh", "Vật lý", "Hóa học", "Sinh học", "Lịch Sử", "Địa Lí", "Giáo dục công dân", "KHTN", "KHXH"};

            for (String subject : subjectsOrder) {
                if (scoreDistribution.containsKey(subject)) {
                    orderedScoreDistribution.put(subject, scoreDistribution.get(subject));
                }
            }

            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy phổ điểm thành công", orderedScoreDistribution);
        } catch (Exception e) {
            log.error("Error fetching score distribution", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình lấy phổ điểm, vui lòng thử lại sau.");
        }
    }

    private Map<Float, Integer> fetchAllScoresBySubject(Integer subjectId, String local) {
        List<Object[]> scoresData;
        if (local != null && !local.isEmpty()) {
            scoresData = highschoolExamScoreRepository.findScoresBySubjectIdAndLocal(subjectId, local);
        } else {
            scoresData = highschoolExamScoreRepository.findScoresBySubjectId(subjectId);
        }

        Map<Float, Integer> scoreCountMap = new HashMap<>();

        for (Object[] row : scoresData) {
            if (row.length > 1) {
                Float score = (Float) row[1];
                if (score != null) {
                    scoreCountMap.put(score, scoreCountMap.getOrDefault(score, 0) + 1);
                }
            }
        }

        return scoreCountMap;
    }

    private Map<Float, Integer> fetchScoresForSubjectGroup(List<Integer> subjectIds, String local) {
        Map<Float, Integer> scoreCountMap = new HashMap<>();

        for (Integer subjectId : subjectIds) {
            List<Object[]> scoresData;
            if (local != null && !local.isEmpty()) {
                scoresData = highschoolExamScoreRepository.findScoresBySubjectIdAndLocal(subjectId, local);
            } else {
                scoresData = highschoolExamScoreRepository.findScoresBySubjectId(subjectId);
            }

            for (Object[] row : scoresData) {
                if (row.length > 1) {
                    Float score = (Float) row[1];
                    if (score != null) {
                        scoreCountMap.put(score, scoreCountMap.getOrDefault(score, 0) + 1);
                    }
                }
            }
        }

        return scoreCountMap;
    }

    @Override
    public ResponseData<Map<String, Map<Float, Integer>>> getScoreDistributionBySubjectGroup(String local, String subjectGroup) {
        try {
            Map<String, List<String>> subjectGroupsMap = Map.of(
                    "A", List.of("A00", "A01", "A02"),
                    "B", List.of("B00", "B03", "B08"),
                    "C", List.of("C00", "C03", "C04"),
                    "D", List.of("D01", "D09", "D10", "D14")
            );

            Map<String, Map<Float, Integer>> getGroupScoreDistribution = new HashMap<>();

            if (subjectGroup != null) {
                boolean found = false;
                for (Map.Entry<String, List<String>> entry : subjectGroupsMap.entrySet()) {
                    if (entry.getValue().contains(subjectGroup)) {
                        getAndGroupScores(local, List.of(subjectGroup), getGroupScoreDistribution, subjectGroup);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy nhóm môn học này !");
                }
            } else {
                for (Map.Entry<String, List<String>> entry : subjectGroupsMap.entrySet()) {
                    getAndGroupScores(local, entry.getValue(), getGroupScoreDistribution, entry.getKey());
                }
            }

            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy phổ điểm thành công", getGroupScoreDistribution);
        } catch (Exception e) {
            log.error("Error fetching score distribution", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình lấy phổ điểm, vui lòng thử lại sau.");
        }
    }

    private void getAndGroupScores(String local, List<String> groupCodes, Map<String, Map<Float, Integer>> groupScoreDistribution, String groupName) {
        Map<Float, Integer> aggregatedScoreDistribution = new HashMap<>();
        for (String groupCode : groupCodes) {
            List<SubjectGroup> subjectGroups = subjectGroupRepository.findByNameGroup(groupCode);
            for (SubjectGroup sg : subjectGroups) {
                List<Integer> subjectIds = getSubjectIdsForGroup(sg.getId());
                Map<Float, Integer> scoreDistribution = calculateScoresDistribution(local, subjectIds);

                scoreDistribution.forEach((score, count) ->
                        aggregatedScoreDistribution.merge(score, count, Integer::sum));
            }
        }
        groupScoreDistribution.put(groupName, aggregatedScoreDistribution);
    }

    private List<Integer> getSubjectIdsForGroup(Integer subjectGroupId) {
        return subjectGroupSubjectRepository.findBySubjectGroupId(subjectGroupId)
                .stream()
                .map(SubjectGroupSubject::getSubjectId)
                .collect(Collectors.toList());
    }

    private Map<Float, Integer> calculateScoresDistribution(String local, List<Integer> subjectIds) {
        List<Object[]> scoresData = highschoolExamScoreRepository.findScoresForSubjects(subjectIds, local);

        Map<Integer, Float> totalScoresByStudent = new HashMap<>();
        for (Object[] data : scoresData) {
            if (data[0] != null && data[2] != null) {
                Integer identificationNumber = (Integer) data[0];
                Float score = ((Number) data[2]).floatValue();

                if (score != null) {
                    BigDecimal roundedScore = new BigDecimal(score).setScale(2, RoundingMode.DOWN);
                    totalScoresByStudent.merge(identificationNumber, roundedScore.floatValue(), Float::sum);
                }
            }
        }

        Map<Float, Integer> scoreDistribution = new HashMap<>();
        for (Float totalScore : totalScoresByStudent.values()) {
            if (totalScore <= 30 && totalScore > 10) {
                BigDecimal roundedTotalScore = new BigDecimal(totalScore).setScale(2, RoundingMode.DOWN);
                scoreDistribution.merge(roundedTotalScore.floatValue(), 1, Integer::sum);
            }
        }

        return scoreDistribution;
    }

    @Override
    public ResponseData<List<HighschoolExamScoreResponse>> getAllTop100HighestScoreBySubject(String subjectName, String local) {
        List<HighschoolExamScoreResponse> responseList = new ArrayList<>();
        try {
            List<Integer> topStudents;
            boolean isKHTN = "KHTN".equalsIgnoreCase(subjectName);
            boolean isKHXH = "KHXH".equalsIgnoreCase(subjectName);
            if (isKHTN) {
                topStudents = highschoolExamScoreRepository.findTop100StudentsBySubjects(Arrays.asList(27, 16, 23), local);
            } else if (isKHXH) {
                topStudents = highschoolExamScoreRepository.findTop100StudentsBySubjects(Arrays.asList(34, 9, 54), local);
            } else {
                topStudents = highschoolExamScoreRepository.findTop100StudentsBySubject(subjectName, local);
            }

            if (topStudents.isEmpty()) {
                return new ResponseData<>(ResponseCode.C200.getCode(), "Không có dữ liệu", responseList);
            }

            List<HighschoolExamScore> allScores = highschoolExamScoreRepository.findScoresByIdentificationNumbers(topStudents);

            List<Integer> subjectOrder = Arrays.asList(36, 28, 38, 27, 16, 23, 34, 9, 54);
            SubjectDTO mainSubject = getSubjectDetailsByName(subjectName);
            if (mainSubject != null && !subjectOrder.contains(mainSubject.getSubjectId())) {
                subjectOrder.add(0, mainSubject.getSubjectId());
            } else if (mainSubject != null) {
                subjectOrder = new ArrayList<>(subjectOrder);
                subjectOrder.remove(mainSubject.getSubjectId());
                subjectOrder.add(0, mainSubject.getSubjectId());
            }

            List<Integer> finalSubjectOrder = new ArrayList<>(subjectOrder);

            Map<Integer, List<SubjectScoreDTO>> scoresByStudent = allScores.stream()
                    .collect(Collectors.groupingBy(
                            HighschoolExamScore::getIdentificationNumber,
                            Collectors.mapping(score -> {
                                SubjectDTO subjectDetails = getSubjectDetails(score.getSubjectId());
                                return new SubjectScoreDTO(
                                        score.getSubjectId(),
                                        subjectDetails != null ? subjectDetails.getSubjectName() : null,
                                        score.getScore()
                                );
                            }, Collectors.toList())
                    ));

            for (Integer identificationNumber : topStudents) {
                HighschoolExamScore firstScore = allScores.stream()
                        .filter(score -> score.getIdentificationNumber().equals(identificationNumber))
                        .findFirst()
                        .orElse(null);

                if (firstScore != null) {
                    List<SubjectScoreDTO> sortedScores = scoresByStudent.get(identificationNumber);
                    sortedScores.sort(Comparator.comparingInt(
                            score -> {
                                int index = finalSubjectOrder.indexOf(score.getSubjectId());
                                return index == -1 ? Integer.MAX_VALUE : index;
                            }
                    ));

                    BigDecimal khtnTotalScore = BigDecimal.ZERO;
                    BigDecimal khxhTotalScore = BigDecimal.ZERO;
                    boolean hasKHTN = false;
                    boolean hasKHXH = false;

                    for (SubjectScoreDTO scoreDTO : sortedScores) {
                        if (scoreDTO.getScore() != null) {
                            if (Set.of(27, 16, 23).contains(scoreDTO.getSubjectId())) {
                                khtnTotalScore = khtnTotalScore.add(BigDecimal.valueOf(scoreDTO.getScore()));
                                hasKHTN = true;
                            } else if (Set.of(34, 9, 54).contains(scoreDTO.getSubjectId())) {
                                khxhTotalScore = khxhTotalScore.add(BigDecimal.valueOf(scoreDTO.getScore()));
                                hasKHXH = true;
                            }
                        }
                    }

                    if (hasKHTN) {
                        khtnTotalScore = khtnTotalScore.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
                        sortedScores.add(new SubjectScoreDTO(999999, "KHTN", khtnTotalScore.floatValue()));
                    } else {
                        sortedScores.add(new SubjectScoreDTO(999999, "KHTN", null));
                    }

                    if (hasKHXH) {
                        khxhTotalScore = khxhTotalScore.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
                        sortedScores.add(new SubjectScoreDTO(999998, "KHXH", khxhTotalScore.floatValue()));
                    } else {
                        sortedScores.add(new SubjectScoreDTO(999998, "KHXH", null));
                    }

                    if (isKHTN) {
                        sortedScores.removeIf(score -> "KHTN".equalsIgnoreCase(score.getSubjectName()));
                        sortedScores.add(0, new SubjectScoreDTO(999999, "KHTN", khtnTotalScore.floatValue()));
                    } else if (isKHXH) {
                        sortedScores.removeIf(score -> "KHXH".equalsIgnoreCase(score.getSubjectName()));
                        sortedScores.add(0, new SubjectScoreDTO(999998, "KHXH", khxhTotalScore.floatValue()));
                    }

                    for (Integer subjectId : finalSubjectOrder) {
                        boolean exists = sortedScores.stream()
                                .anyMatch(score -> score.getSubjectId() != null && score.getSubjectId().equals(subjectId));
                        if (!exists) {
                            SubjectDTO subjectDetails = getSubjectDetails(subjectId);
                            sortedScores.add(new SubjectScoreDTO(
                                    subjectId,
                                    subjectDetails != null ? subjectDetails.getSubjectName() : null,
                                    null
                            ));
                        }
                    }

                    HighschoolExamScoreResponse response = new HighschoolExamScoreResponse(
                            firstScore.getIdentificationNumber(),
                            firstScore.getLocal(),
                            firstScore.getExaminationBoard(),
                            firstScore.getDateOfBirth(),
                            firstScore.getExaminer(),
                            firstScore.getYear(),
                            sortedScores
                    );
                    responseList.add(response);
                }
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy Top 100 thành công", responseList);
        } catch (Exception e) {
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình lấy Top 100, vui lòng thử lại sau.");
        }
    }

    @Override
    @Transactional
    public ResponseData<String> publishExamScores(Integer listExamScoreByYearId) {
        try {
            ListExamScoreByYear listExamScoreByYear = listExamScoreByYearRepository.findById(listExamScoreByYearId)
                    .orElseThrow(() -> new IllegalStateException("ListExamScoreByYear ID không tồn tại"));

            List<ListExamScoreByYear> activeScores = listExamScoreByYearRepository.findAllByStatus("ACTIVE");
            if (!activeScores.isEmpty()) {
                activeScores.forEach(activeScore -> {
                    activeScore.setStatus("INACTIVE");
                    listExamScoreByYearRepository.save(activeScore);
                });
            }

            List<HighschoolExamScore> examScoresStatus = highschoolExamScoreRepository.findAllByYearAndStatus(
                    listExamScoreByYear.getYear(), HighschoolExamScoreStatus.INACTIVE);

            if (examScoresStatus.isEmpty()) {
                return new ResponseData<>(ResponseCode.C200.getCode(), "Điểm thi đã được công bố từ trước đó.");
            }

            examScoresStatus.forEach(score -> score.setStatus(HighschoolExamScoreStatus.ACTIVE));
            highschoolExamScoreRepository.saveAll(examScoresStatus);

            listExamScoreByYear.setStatus("ACTIVE");
            listExamScoreByYearRepository.save(listExamScoreByYear);

            Map<Integer, List<HighschoolExamScore>> scoresByIdentificationNumber = examScoresStatus.stream()
                    .collect(Collectors.groupingBy(HighschoolExamScore::getIdentificationNumber));

            Map<Integer, String> subjectIdToNameMap = subjectRepository.findAll().stream()
                    .collect(Collectors.toMap(Subject::getId, Subject::getName));

            List<Integer> identificationNumbers = new ArrayList<>(scoresByIdentificationNumber.keySet());
            List<UserInfo> matchingUserInfos = userInfoRepository.findAllByIdentificationNumberIn(identificationNumbers);

            for (UserInfo userInfo : matchingUserInfos) {
                Integer identificationNumber = userInfo.getIdentificationNumber();
                List<HighschoolExamScore> userScores = scoresByIdentificationNumber.get(identificationNumber);

                BigDecimal khtnTotalScore = BigDecimal.ZERO;
                BigDecimal khxhTotalScore = BigDecimal.ZERO;
                boolean hasKHTN = false;
                boolean hasKHXH = false;

                String email = userInfo.getUser().getEmail();
                String subject = "Kết quả thi tốt nghiệp THPT " + listExamScoreByYear.getYear();
                StringBuilder message = new StringBuilder();
                message.append("<h1>Cổng thông tin tuyển sinh trường đại học - UAP</h1>");
                message.append("<h3>Xin trân trọng thông báo kết quả thi tốt nghiệp THPT ")
                        .append(listExamScoreByYear.getYear())
                        .append(" của bạn:</h3>");

                for (HighschoolExamScore score : userScores) {
                    if (score.getScore() != null) {
                        String subjectName = subjectIdToNameMap.get(score.getSubjectId());
                        message.append("<p>Môn: ").append(subjectName)
                                .append("- Điểm: ").append("<b>" + score.getScore() + "</b>")
                                .append("</p>");

                        if (Set.of(27, 16, 23).contains(score.getSubjectId())) {
                            khtnTotalScore = khtnTotalScore.add(BigDecimal.valueOf(score.getScore()));
                            hasKHTN = true;
                        } else if (Set.of(34, 9, 54).contains(score.getSubjectId())) {
                            khxhTotalScore = khxhTotalScore.add(BigDecimal.valueOf(score.getScore()));
                            hasKHXH = true;
                        }
                    }
                }
                if (hasKHTN && khtnTotalScore.compareTo(BigDecimal.ZERO) > 0) {
                    khtnTotalScore = khtnTotalScore.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
                    message.append("<p>Bài thi Khoa học tự nhiên")
                            .append("- Điểm: ").append("<b>" + khtnTotalScore.floatValue() + "</b>")
                            .append("</p>");
                }
                if (hasKHXH && khxhTotalScore.compareTo(BigDecimal.ZERO) > 0) {
                    khxhTotalScore = khxhTotalScore.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
                    message.append("<p>Bài thi Khoa học xã hội")
                            .append("- Điểm: ").append("<b>" + khxhTotalScore.floatValue() + "</b>")
                            .append("</p>");
                }
                if (message.length() > "<h2>Kết quả thi của bạn:</h2>".length()) {
                    boolean emailSent = emailUtil.sendExamScoreEmail(email, subject, message.toString());
                    if (!emailSent) {
                        log.error("Failed to send email to {}", email);
                    } else {
                        log.info("Successfully sent email to {}", email);
                    }
                }
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Công bố điểm thi THPT " + listExamScoreByYear.getYear() + " và gửi email thành công");
        } catch (Exception e) {
            log.error("Error publishing exam scores", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Có lỗi xảy ra khi công bố điểm thi THPT " + listExamScoreByYearId);
        }
    }

    @Override
    public ResponseData<Page<ListExamScoreByYearResponse>> getAllListExamScoresByYear(Pageable pageable) {
        try {
            Page<ListExamScoreByYear> pageResult = listExamScoreByYearRepository.findAll(pageable);
            Page<ListExamScoreByYearResponse> responsePage = pageResult.map(listExamScore ->
                    new ListExamScoreByYearResponse(
                            listExamScore.getId(),
                            listExamScore.getTitle(),
                            listExamScore.getYear(),
                            listExamScore.getStatus()
                    )
            );
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách thành công", responsePage);
        } catch (Exception e) {
            log.error("Error fetching exam scores by year", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình lấy danh sách, vui lòng thử lại sau.");
        }
    }

    @Override
    public ResponseData<ListExamScoreByYearResponseV2> getListExamScoreById(Integer id, int page, int size) {
        try {
            Optional<ListExamScoreByYear> optionalExamScore = listExamScoreByYearRepository.findById(id);

            if (optionalExamScore.isPresent()) {
                ListExamScoreByYear listExamScore = optionalExamScore.get();

                PageRequest pageRequest = PageRequest.of(page, size);
                Page<HighschoolExamScore> highschoolExamScoresPage = highschoolExamScoreRepository.findByYear(listExamScore.getYear(), pageRequest);

                List<HighschoolExamScoreResponse> examScoreResponses = new ArrayList<>();

                for (HighschoolExamScore score : highschoolExamScoresPage) {
                    List<SubjectScoreDTO> subjectScores = new ArrayList<>();

                    List<HighschoolExamScore> examinerScore = highschoolExamScoreRepository.findByIdentificationNumber(score.getIdentificationNumber());

                    BigDecimal khtnTotalScore = BigDecimal.ZERO;
                    BigDecimal khxhTotalScore = BigDecimal.ZERO;
                    boolean hasKHTN = false;
                    boolean hasKHXH = false;

                    for (HighschoolExamScore subjectScore : examinerScore) {
                        SubjectDTO subjectDTO = getSubjectDetails(subjectScore.getSubjectId());
                        if (subjectDTO != null) {
                            Float scoreValue = subjectScore.getScore();
                            subjectScores.add(new SubjectScoreDTO(subjectDTO.getSubjectId(), subjectDTO.getSubjectName(), scoreValue));

                            if (scoreValue != null) {
                                if (Set.of(27, 16, 23).contains(subjectDTO.getSubjectId())) {
                                    khtnTotalScore = khtnTotalScore.add(BigDecimal.valueOf(scoreValue));
                                    hasKHTN = true;
                                } else if (Set.of(34, 9, 54).contains(subjectDTO.getSubjectId())) {
                                    khxhTotalScore = khxhTotalScore.add(BigDecimal.valueOf(scoreValue));
                                    hasKHXH = true;
                                }
                            }
                        }
                    }
                    if (hasKHTN) {
                        khtnTotalScore = khtnTotalScore.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
                        subjectScores.add(new SubjectScoreDTO(999999, "KHTN", khtnTotalScore.floatValue()));
                    } else {
                        subjectScores.add(new SubjectScoreDTO(999999, "KHTN", null));
                    }
                    if (hasKHXH) {
                        khxhTotalScore = khxhTotalScore.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
                        subjectScores.add(new SubjectScoreDTO(999998, "KHXH", khxhTotalScore.floatValue()));
                    } else {
                        subjectScores.add(new SubjectScoreDTO(999998, "KHXH", null));
                    }

                    examScoreResponses.add(new HighschoolExamScoreResponse(
                            score.getIdentificationNumber(),
                            score.getLocal(),
                            score.getExaminationBoard(),
                            score.getDateOfBirth(),
                            score.getExaminer(),
                            score.getYear(),
                            subjectScores
                    ));
                }

                ListExamScoreByYearResponseV2 response = new ListExamScoreByYearResponseV2(
                        listExamScore.getId(),
                        listExamScore.getTitle(),
                        listExamScore.getYear(),
                        examScoreResponses
                );

                return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy thông tin thành công", response);
            } else {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy thông tin danh sách bài kiểm tra đã cung cấp");
            }
        } catch (Exception e) {
            log.error("Error fetching exam score by ID", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình lấy thông tin, vui lòng thử lại sau.");
        }
    }



    private SubjectDTO getSubjectDetailsByName(String subjectName) {
        return subjectRepository.findByName(subjectName)
                .map(subject -> new SubjectDTO(subject.getId(), subject.getName()))
                .orElse(null);
    }

    private SubjectDTO getSubjectDetails(Integer subjectId) {
        return subjectRepository.findById(subjectId)
                .map(subject -> new SubjectDTO(subject.getId(), subject.getName()))
                .orElse(null);
    }
}
