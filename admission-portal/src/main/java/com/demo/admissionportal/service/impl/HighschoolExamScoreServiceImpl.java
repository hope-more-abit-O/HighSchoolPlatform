package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.HighschoolExamScoreStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.ExamYearData;
import com.demo.admissionportal.dto.YearlyExamScoreResponse;
import com.demo.admissionportal.dto.entity.SubjectDTO;
import com.demo.admissionportal.dto.request.CreateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.request.UpdateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.response.HighschoolExamScoreResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.SubjectScoreDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.sub_entity.SubjectGroupSubject;
import com.demo.admissionportal.repository.HighschoolExamScoreRepository;
import com.demo.admissionportal.repository.SubjectGroupRepository;
import com.demo.admissionportal.repository.SubjectRepository;
import com.demo.admissionportal.repository.UserInfoRepository;
import com.demo.admissionportal.repository.sub_repository.SubjectGroupSubjectRepository;
import com.demo.admissionportal.service.HighschoolExamScoreService;
import com.demo.admissionportal.util.impl.EmailUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public ResponseData<List<HighschoolExamScoreResponse>> findAllWithFilter(Integer identificationNumber) {
        try {
            List<HighschoolExamScore> examScoresPage = highschoolExamScoreRepository.findAll(identificationNumber);
            if (identificationNumber == null || highschoolExamScoreRepository.countByIdentificationNumber(identificationNumber) == 0) {
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
                String local = null;
                String examiner = null;
                String dateOfBirth = null;
                Integer year = 2024;

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
                    }   if (local == null) {
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
                        year,
                        subjectScores
                ));
            }

            return new ResponseData<>(ResponseCode.C200.getCode(), "Tra cứu điểm thành công !", responseList);
        } catch (Exception e) {
            log.error("Error fetching exam scores", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình tra cứu điểm, vui lòng thử lại sau.");
        }
    }

    private static final List<Integer> ALLOWED_SUBJECT_IDS = List.of(9, 27, 28, 34, 36, 38, 16, 23, 54);

    @Transactional
    public ResponseData<List<YearlyExamScoreResponse>> createExamScores(List<ExamYearData> examYearDataList) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Người tham chiếu không hợp lệ !");
            }
            User staff = (User) principal;
            Integer staffId = staff.getId();

            List<YearlyExamScoreResponse> yearlyResponses = examYearDataList.stream().map(examYearData -> {
                int year = examYearData.getYear();
                List<HighschoolExamScoreResponse> yearResponses = examYearData.getExamScoreData().stream().map(request -> {
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

                    List<SubjectScoreDTO> allSubjectScores = examScores.stream().map(examScore -> {
                        String subjectName = subjectRepository.findById(examScore.getSubjectId())
                                .map(subject -> subject.getName())
                                .orElse(null);
                        return new SubjectScoreDTO(examScore.getSubjectId(), subjectName, examScore.getScore());
                    }).collect(Collectors.toList());

                    return new HighschoolExamScoreResponse(
                            request.getIdentificationNumber(),
                            request.getLocal(),
                            request.getExaminationBoard(),
                            request.getDateOfBirth(),
                            request.getExaminer(),
                            year,
                            allSubjectScores
                    );
                }).collect(Collectors.toList());

                return new YearlyExamScoreResponse(examYearData.getTitle(), year, yearResponses);
            }).collect(Collectors.toList());

            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo điểm thi thành công!", yearlyResponses);

        } catch (Exception e) {
            log.error("Error creating exam scores", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình tạo điểm, vui lòng thử lại sau. Lỗi: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseData<List<HighschoolExamScoreResponse>> updateExamScores(List<UpdateHighschoolExamScoreRequest> requests) {
        List<HighschoolExamScoreResponse> responses = new ArrayList<>();

        try {
            for (UpdateHighschoolExamScoreRequest request : requests) {
                Integer identificationNumber = request.getIdentificationNumber();
                if (request != null && identificationNumber != null) {
                    List<HighschoolExamScore> existingScores = highschoolExamScoreRepository.findByIdentificationNumberAndYear(
                            identificationNumber, 2024);
                    if (existingScores.isEmpty()) {
                        log.error("Not found for identification number {} in year {}", identificationNumber, 2024);
                        return new ResponseData<>(ResponseCode.C204.getCode(), "Không tìm số báo danh này !");
                    }

                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    Object principal = authentication.getPrincipal();
                    if (!(principal instanceof User)) {
                        return new ResponseData<>(ResponseCode.C205.getCode(), "Người tham chiếu không hợp lệ !");
                    }
                    User staff = (User) principal;
                    Integer staffId = staff.getId();

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
                            newScore.setYear(2024);
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

                    HighschoolExamScoreResponse response = modelMapper.map(existingScores.get(0), HighschoolExamScoreResponse.class);
                    response.setSubjectScores(allSubjectScores);
                    responses.add(response);

                } else {
                    return new ResponseData<>(ResponseCode.C204.getCode(), "Yêu cầu không hợp lệ!");
                }
            }

            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật điểm thi thành công!", responses);

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
            List<Integer> topStudents = highschoolExamScoreRepository.findTop100StudentsBySubject(subjectName, local);

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

                    for (Integer subjectId : finalSubjectOrder) {
                        boolean exists = sortedScores.stream()
                                .anyMatch(score -> score.getSubjectId().equals(subjectId));
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
    public ResponseData<String> publishExamScores() {
        try {
            List<HighschoolExamScore> examScoresStatus = highschoolExamScoreRepository.findAllByStatus(HighschoolExamScoreStatus.INACTIVE);
            if (examScoresStatus.isEmpty()) {
                return new ResponseData<>(ResponseCode.C200.getCode(), "Điểm thi đã được công bố từ trước đó.");
            }
            List<HighschoolExamScore> examScores = highschoolExamScoreRepository.findAll();

            examScores.forEach(score -> score.setStatus(HighschoolExamScoreStatus.ACTIVE));
            highschoolExamScoreRepository.saveAll(examScores);

            Map<Integer, List<HighschoolExamScore>> scoresByIdentificationNumber = examScores.stream()
                    .collect(Collectors.groupingBy(HighschoolExamScore::getIdentificationNumber));

            Map<Integer, String> subjectIdToNameMap = subjectRepository.findAll().stream()
                    .collect(Collectors.toMap(Subject::getId, Subject::getName));

            List<Integer> identificationNumbers = new ArrayList<>(scoresByIdentificationNumber.keySet());
            List<UserInfo> matchingUserInfos = userInfoRepository.findAllByIdentificationNumberIn(identificationNumbers);

            for (UserInfo userInfo : matchingUserInfos) {
                Integer identificationNumber = userInfo.getIdentificationNumber();
                List<HighschoolExamScore> userScores = scoresByIdentificationNumber.get(identificationNumber);

                String email = userInfo.getUser().getEmail();
                String subject = "Kết quả thi tốt nghiệp THPT 2024";
                StringBuilder message = new StringBuilder();
                message.append("<h1> Cổng thông tin tuyển sinh trường đại học - UAP</h1>");
                message.append("<h3>Xin trân trọng thông báo kết quả thi tốt nghiệp THPT 2024 của bạn:</h2>");
                for (HighschoolExamScore score : userScores) {
                    if (score.getScore() != null) {
                        String subjectName = subjectIdToNameMap.get(score.getSubjectId());
                        message.append("<p>Môn: ").append(subjectName)
                                .append(" - Điểm: ").append(score.getScore())
                                .append("</p>");
                    }
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
            return new ResponseData<>(ResponseCode.C200.getCode(), "Công bố điểm thi THPT 2024 và gửi email thành công");
        } catch (Exception e) {
            log.error("Error publishing exam scores", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Có lỗi xảy ra khi công bố điểm thi THPT 2024");
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
