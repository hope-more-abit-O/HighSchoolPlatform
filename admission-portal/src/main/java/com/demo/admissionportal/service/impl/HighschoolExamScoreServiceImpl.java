package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.*;
import com.demo.admissionportal.dto.*;
import com.demo.admissionportal.dto.request.AdmissionAnalysisRequest;
import com.demo.admissionportal.entity.ExamLocal;
import com.demo.admissionportal.entity.ExamYear;
import com.demo.admissionportal.dto.entity.SubjectDTO;
import com.demo.admissionportal.dto.request.CreateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.response.*;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.admission.Admission;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramSubjectGroup;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramSubjectGroupId;
import com.demo.admissionportal.entity.sub_entity.ListExamScoreHighSchoolExamScore;
import com.demo.admissionportal.entity.sub_entity.SubjectGroupSubject;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramMethodRepository;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramRepository;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramSubjectGroupRepository;
import com.demo.admissionportal.repository.sub_repository.ListExamScoreHighSchoolExamScoreRepository;
import com.demo.admissionportal.repository.sub_repository.SubjectGroupSubjectRepository;
import com.demo.admissionportal.service.HighschoolExamScoreService;
import com.demo.admissionportal.util.impl.EmailUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HighschoolExamScoreServiceImpl implements HighschoolExamScoreService {
    @Autowired
    private HighschoolExamScoreRepository highschoolExamScoreRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SubjectGroupRepository subjectGroupRepository;
    @Autowired
    private SubjectGroupSubjectRepository subjectGroupSubjectRepository;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private ListExamScoreHighSchoolExamScoreRepository listExamScoreHighSchoolExamScoreRepository;
    @Autowired
    private ListExamScoreByYearRepository listExamScoreByYearRepository;
    @Autowired
    private UserIdentificationNumberRegisterRepository userIdentificationNumberRegisterRepository;
    @Autowired
    private ExamLocalRepository examLocalRepository;
    @Autowired
    private ExamYearRepository examYearRepository;
    @Autowired
    private UniversityInfoRepository universityInfoRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private AdmissionTrainingProgramMethodRepository admissionTrainingProgramMethodRepository;
    @Autowired
    private AdmissionTrainingProgramSubjectGroupRepository admissionTrainingProgramSubjectGroupRepository;
    @Autowired
    private AdmissionTrainingProgramRepository admissionTrainingProgramRepository;

    @Override
    public ResponseData<List<HighschoolExamScoreResponse>> findAllWithFilter(String identificationNumber, Integer examYearId) {
        try {
            if (identificationNumber == null || !identificationNumber.matches("\\d+")) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Số báo danh chứa kí tự không hợp lệ.");
            }
            List<HighschoolExamScore> examScores = highschoolExamScoreRepository.findAll(identificationNumber, examYearId);
            if (identificationNumber == null || highschoolExamScoreRepository.countByIdentificationNumberAndExamYear(identificationNumber, examYearId) == 0) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy số báo danh này !");
            }

            boolean statusScore = examScores.stream()
                    .allMatch(score -> score.getStatus().equals(HighschoolExamScoreStatus.INACTIVE));

            if (statusScore) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Điểm thi chưa được công bố!");
            }

            Map<String, List<HighschoolExamScore>> groupedById = examScores.stream()
                    .collect(Collectors.groupingBy(HighschoolExamScore::getIdentificationNumber));

            List<HighschoolExamScoreResponse> responseList = new ArrayList<>();
            for (Map.Entry<String, List<HighschoolExamScore>> entry : groupedById.entrySet()) {
                List<SubjectScoreDTO> subjectScores = new ArrayList<>();
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
                        resultYear = score.getExamYear().getYear();
                    }
                    if (local == null) {
                        local = score.getExamLocal().getName();
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

            List<YearlyExamScoreResponse> yearlyResponses = new ArrayList<>();
            List<HighschoolExamScore> allExamScores = new ArrayList<>();
            List<ListExamScoreHighSchoolExamScore> allListExamScores = new ArrayList<>();

            for (ExamYearData examYearData : examYearDataList) {
                ExamYear examYear = examYearRepository.findByYear(examYearData.getYear());
                if (examYear == null) {
                    return new ResponseData<>(ResponseCode.C203.getCode(), "Năm thi không tồn tại: " + examYearData.getYear());
                }

                ListExamScoreByYear listExamScoreByYear = listExamScoreByYearRepository.findByYear(examYear.getYear());
                if (listExamScoreByYear == null) {
                    listExamScoreByYear = new ListExamScoreByYear();
                    listExamScoreByYear.setTitle(examYearData.getTitle());
                    listExamScoreByYear.setYear(examYear.getYear());
                    listExamScoreByYear.setStatus(ListExamScoreByYearStatus.INACTIVE);
                    listExamScoreByYear = listExamScoreByYearRepository.save(listExamScoreByYear);
                }

                List<HighschoolExamScoreResponse> yearResponses = new ArrayList<>();

                for (CreateHighschoolExamScoreRequest request : examYearData.getExamScoreData()) {
                    ExamLocal examLocal = examLocalRepository.findByName(request.getLocalName());
                    if (examLocal == null) {
                        return new ResponseData<>(ResponseCode.C203.getCode(), "Địa phương không tồn tại: " + request.getLocalName());
                    }

                    List<String> existingIdentificationNumbers = highschoolExamScoreRepository
                            .findByIdentificationNumberAndExamYear_Id(request.getIdentificationNumber(), examYear.getYear())
                            .stream()
                            .map(HighschoolExamScore::getIdentificationNumber)
                            .toList();

                    if (existingIdentificationNumbers.contains(request.getIdentificationNumber())) {
                        log.error("Identification Number {} is already existed for year {}", request.getIdentificationNumber(), examYear.getYear());
                        return new ResponseData<>(ResponseCode.C204.getCode(), "Số báo danh thí sinh " + request.getIdentificationNumber() + " đã tồn tại trong năm " + examYear.getYear());
                    }

                    Map<Integer, SubjectScoreDTO> subjectScoreMap = request.getSubjectScores().stream()
                            .collect(Collectors.toMap(SubjectScoreDTO::getSubjectId, score -> score));

                    List<HighschoolExamScore> examScores = ALLOWED_SUBJECT_IDS.stream().map(subjectId -> {
                        SubjectScoreDTO subjectScore = subjectScoreMap.getOrDefault(subjectId, new SubjectScoreDTO(subjectId, null, null));
                        HighschoolExamScore examScore = new HighschoolExamScore();
                        examScore.setIdentificationNumber(request.getIdentificationNumber());
                        examScore.setExamLocal(examLocal);
                        examScore.setExamYear(examYear);
                        examScore.setSubjectId(subjectId);
                        examScore.setScore(subjectScore.getScore());
                        examScore.setCreateTime(new Date());
                        examScore.setCreateBy(staffId);
                        examScore.setStatus(HighschoolExamScoreStatus.INACTIVE);
                        return examScore;
                    }).toList();

                    List<HighschoolExamScore> savedExamScores = highschoolExamScoreRepository.saveAll(examScores);
                    allExamScores.addAll(savedExamScores);

                    ListExamScoreByYear finalListExamScoreByYear = listExamScoreByYear;
                    examScores.forEach(savedExamScore -> {
                        ListExamScoreHighSchoolExamScore listExamScoreHighSchoolExamScore = new ListExamScoreHighSchoolExamScore();
                        listExamScoreHighSchoolExamScore.setListExamScoreByYearId(finalListExamScoreByYear.getId());
                        listExamScoreHighSchoolExamScore.setHighschoolExamScoreId(savedExamScore.getId());
                        listExamScoreHighSchoolExamScore.setStatus(HighschoolExamScoreStatus.INACTIVE);
                        allListExamScores.add(listExamScoreHighSchoolExamScore);
                    });

                    List<SubjectScoreDTO> allSubjectScores = ALLOWED_SUBJECT_IDS.stream().map(subjectId -> {
                        String subjectName = subjectRepository.findById(subjectId)
                                .map(Subject::getName)
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
                            examLocal.getName(),
                            examYear.getYear(),
                            allSubjectScores
                    ));
                }

                yearlyResponses.add(new YearlyExamScoreResponse(listExamScoreByYear.getTitle(), examYear.getYear(), yearResponses));
            }
            listExamScoreHighSchoolExamScoreRepository.saveAll(allListExamScores);

            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo điểm thi thành công!", yearlyResponses);

        } catch (Exception e) {
            log.error("Error creating exam scores", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình tạo điểm, vui lòng thử lại sau.");
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

            Optional<ListExamScoreByYear> listExamScoreByYear = listExamScoreByYearRepository.findById(listExamScoreByYearId);
            if (listExamScoreByYear.isEmpty()){
                return new ResponseData<>(ResponseCode.C203.getCode(), "Danh sách dữ liệu điểm thi với ID không tồn tại");
            }

            for (ExamYearData examYearData : examYearDataList) {
                ExamYear examYear = examYearRepository.findByYear(examYearData.getYear());
                if (examYear == null) {
                    return new ResponseData<>(ResponseCode.C203.getCode(), "Năm thi không tồn tại: " + examYearData.getYear());
                }

                List<HighschoolExamScoreResponse> yearResponses = new ArrayList<>();

                for (CreateHighschoolExamScoreRequest request : examYearData.getExamScoreData()) {
                    String identificationNumber = request.getIdentificationNumber();
                    if (identificationNumber != null) {
                        ExamLocal examLocal = examLocalRepository.findByName(request.getLocalName());
                        if (examLocal == null) {
                            return new ResponseData<>(ResponseCode.C203.getCode(), "Địa phương không tồn tại: " + request.getLocalName());
                        }

                        List<HighschoolExamScore> existingScores = highschoolExamScoreRepository.findByIdentificationNumberAndExamYear_Id(
                                identificationNumber, examYear.getYear());

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
                                newScore.setExamYear(examYear);
                                newScore.setExamLocal(examLocal);
                                newScore.setSubjectId(score.getSubjectId());
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
                                            .map(Subject::getName)
                                            .orElse(null);
                                    subjectScore.setSubjectName(subjectName);
                                    return subjectScore;
                                })
                                .toList();
                        HighschoolExamScoreResponse response = new HighschoolExamScoreResponse();
                        response.setIdentificationNumber(identificationNumber);
                        response.setLocal(examLocal.getName());
                        response.setYear(examYear.getYear());
                        response.setSubjectScores(allSubjectScores);
                        yearResponses.add(response);
                    } else {
                        return new ResponseData<>(ResponseCode.C204.getCode(), "Yêu cầu không hợp lệ!");
                    }
                }
                YearlyExamScoreResponse yearlyResponse = new YearlyExamScoreResponse(examYearData.getTitle(), examYear.getYear(), yearResponses);
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
                    String localName = (String) data[0]; // Corrected to String
                    Float score = ((Number) data[1]).floatValue();

                    scoresByLocalAndSubject.computeIfAbsent(localName, k -> new HashMap<>())
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

            Integer localId = null;
            String localName = null;
            if (local != null && !local.isEmpty()) {
                ExamLocal examLocal = examLocalRepository.findByName(local);
                if (examLocal != null) {
                    localId = examLocal.getId();
                    localName = examLocal.getName();
                } else {
                    return new ResponseData<>(ResponseCode.C203.getCode(), "Địa phương không hợp lệ hoặc không tồn tại: " + local);
                }
            }

            if (subjectName != null && !subjectName.isEmpty()) {
                log.debug("Fetching scores for subject: {}", subjectName);

                if (subjectName.equalsIgnoreCase("KHTN")) {
                    scoreDistribution.put("KHTN", fetchScoresForSubjectGroup(List.of(27, 16, 23), localId, localName));
                } else if (subjectName.equalsIgnoreCase("KHXH")) {
                    scoreDistribution.put("KHXH", fetchScoresForSubjectGroup(List.of(34, 9, 54), localId, localName));
                } else {
                    Optional<Subject> subjectOpt = subjectRepository.findByName(subjectName);

                    if (subjectOpt.isPresent()) {
                        Integer subjectId = subjectOpt.get().getId();

                        if (ALLOWED_SUBJECT_IDS.contains(subjectId)) {
                            Map<Float, Integer> scores = fetchAllScoresBySubject(subjectId, localId, localName);
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
                    Map<Float, Integer> scores = fetchAllScoresBySubject(subjectId, localId, localName);
                    scoreDistribution.put(subject.getName(), scores);
                }

                scoreDistribution.put("KHTN", fetchScoresForSubjectGroup(List.of(27, 16, 23), localId, localName));
                scoreDistribution.put("KHXH", fetchScoresForSubjectGroup(List.of(34, 9, 54), localId, localName));
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

    private Map<Float, Integer> fetchAllScoresBySubject(Integer subjectId, Integer localId, String localName) {
        List<Object[]> scoresData;
        if (localId != null) {
            scoresData = highschoolExamScoreRepository.findScoresBySubjectIdAndLocal(subjectId, localName);
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

    private Map<Float, Integer> fetchScoresForSubjectGroup(List<Integer> subjectIds, Integer localId, String localName) {
        Map<Float, Integer> scoreCountMap = new HashMap<>();

        for (Integer subjectId : subjectIds) {
            List<Object[]> scoresData;
            if (localId != null) {
                scoresData = highschoolExamScoreRepository.findScoresBySubjectIdAndLocal(subjectId, localName);
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

            Map<String, Map<Float, Integer>> groupScoreDistribution = new HashMap<>();

            Integer localId = resolveLocalId(local);

            if (subjectGroup != null) {
                boolean found = false;
                for (Map.Entry<String, List<String>> entry : subjectGroupsMap.entrySet()) {
                    if (entry.getValue().contains(subjectGroup)) {
                        getAndGroupScores(localId, List.of(subjectGroup), groupScoreDistribution, subjectGroup);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy nhóm môn học này !");
                }
            } else {
                for (Map.Entry<String, List<String>> entry : subjectGroupsMap.entrySet()) {
                    getAndGroupScores(localId, entry.getValue(), groupScoreDistribution, entry.getKey());
                }
            }

            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy phổ điểm thành công", groupScoreDistribution);
        } catch (Exception e) {
            log.error("Error fetching score distribution", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình lấy phổ điểm, vui lòng thử lại sau.");
        }
    }

    private void getAndGroupScores(Integer localId, List<String> groupCodes, Map<String, Map<Float, Integer>> groupScoreDistribution, String groupName) {
        Map<Float, Integer> aggregatedScoreDistribution = new HashMap<>();
        for (String groupCode : groupCodes) {
            List<SubjectGroup> subjectGroups = subjectGroupRepository.findByNameGroup(groupCode);
            for (SubjectGroup sg : subjectGroups) {
                List<Integer> subjectIds = getSubjectIdsForGroup(sg.getId());
                Map<Float, Integer> scoreDistribution = calculateScoresDistribution(localId, subjectIds);

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
                .toList();
    }

    private Map<Float, Integer> calculateScoresDistribution(Integer localId, List<Integer> subjectIds) {
        List<Object[]> scoresData = highschoolExamScoreRepository.findScoresForSubjects(subjectIds, localId);

        Map<String, Float> totalScoresByStudent = new HashMap<>();
        for (Object[] data : scoresData) {
            if (data[0] != null && data[2] != null) {
                String identificationNumber = (String) data[0];
                Float score = ((Number) data[2]).floatValue();

                totalScoresByStudent.merge(identificationNumber, score, Float::sum);
            }
        }

        Map<Float, Integer> scoreDistribution = new HashMap<>();
        for (Float totalScore : totalScoresByStudent.values()) {
            if (totalScore > 10) {
                BigDecimal roundedTotalScore = new BigDecimal(totalScore).setScale(1, RoundingMode.HALF_UP);
                scoreDistribution.merge(roundedTotalScore.floatValue(), 1, Integer::sum);
            }
        }

        return scoreDistribution;
    }

    public ResponseData<String> getRankingBySubjectGroupAndLocal(String identificationNumber, String subjectGroup, String local) {
        try {
            Integer localId = resolveLocalId(local);

            Map<String, Float> totalScoresByStudent = calculateScoresForStudent(localId, subjectGroup);

            if (localId == null) {
                boolean existsInAnyLocal = highschoolExamScoreRepository.existsByIdentificationNumber(identificationNumber);
                if (!existsInAnyLocal) {
                    return new ResponseData<>(ResponseCode.C204.getCode(), "Số báo danh: " + identificationNumber + " không có trong hệ thống.");
                }
            } else {
                boolean existsInLocal = highschoolExamScoreRepository.existsByIdentificationNumberAndExamLocalId(identificationNumber, localId);
                if (!existsInLocal) {
                    return new ResponseData<>(ResponseCode.C204.getCode(), "Số báo danh: " + identificationNumber + " không có trong " + local);
                }
            }

            filterStudentsWithoutCompleteScores(totalScoresByStudent, subjectGroup, localId);

            if (!totalScoresByStudent.containsKey(identificationNumber)) {
                return new ResponseData<>(ResponseCode.C204.getCode(), "Thí sinh không có điểm cho tất cả các môn trong tổ hợp môn này.");
            }

            List<Float> sortedScores = new ArrayList<>(totalScoresByStudent.values());
            Collections.sort(sortedScores, Collections.reverseOrder());

            Float studentScore = totalScoresByStudent.get(identificationNumber);
            int rank = sortedScores.indexOf(studentScore) + 1;

            int totalOfIdentificationNumber = totalScoresByStudent.size();

            String responseMessage = "Thứ hạng của bạn với tổ hợp môn " + subjectGroup + " " +
                    (local == null || local.isEmpty() ? "trên toàn quốc" : "tại " + local) +
                    " là " + rank + " trong tổng số " + totalOfIdentificationNumber + " thí sinh.";

            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy xếp hạng thành công!", responseMessage);

        } catch (Exception e) {
            log.error("Error fetching ranking by subject group and local", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình lấy xếp hạng, vui lòng thử lại sau.");
        }
    }

    private void filterStudentsWithoutCompleteScores(Map<String, Float> totalScoresByStudent, String subjectGroup, Integer localId) {
        List<SubjectGroup> subjectGroups = subjectGroupRepository.findByNameGroup(subjectGroup);
        List<Integer> subjectIds = new ArrayList<>();

        for (SubjectGroup sg : subjectGroups) {
            subjectIds.addAll(getSubjectIdsForGroup(sg.getId()));
        }

        totalScoresByStudent.entrySet().removeIf(entry -> {
            String idNumber = entry.getKey();
            List<Object[]> scores = localId == null
                    ? highschoolExamScoreRepository.findScoresForSubjectsOnly(subjectIds)
                    : highschoolExamScoreRepository.findScoresForSubjects(subjectIds, localId);

            long count = scores.stream()
                    .filter(score -> score[0].equals(idNumber) && score[2] != null)
                    .count();

            return count < subjectIds.size();
        });
    }

    private Map<String, Float> calculateScoresForStudent(Integer localId, String subjectGroup) {
        List<SubjectGroup> subjectGroups = subjectGroupRepository.findByNameGroup(subjectGroup);
        Map<String, Float> totalScoresByStudent = new HashMap<>();

        for (SubjectGroup sg : subjectGroups) {
            List<Integer> subjectIds = getSubjectIdsForGroup(sg.getId());
            List<Object[]> scoresData = localId == null
                    ? highschoolExamScoreRepository.findScoresForSubjectsOnly(subjectIds)
                    : highschoolExamScoreRepository.findScoresForSubjects(subjectIds, localId);

            for (Object[] data : scoresData) {
                if (data[0] != null && data[2] != null) {
                    String idNumber = (String) data[0];
                    Float score = ((Number) data[2]).floatValue();

                    totalScoresByStudent.merge(idNumber, score, Float::sum);
                }
            }
        }
        totalScoresByStudent.replaceAll((id, score) -> new BigDecimal(score).setScale(1, RoundingMode.HALF_UP).floatValue());
        return totalScoresByStudent;
    }

    private Integer resolveLocalId(String local) {
        if (local != null && !local.isEmpty()) {
            ExamLocal examLocal = examLocalRepository.findByName(local);
            if (examLocal != null) {
                return examLocal.getId();
            }
        }
        return null;
    }

    @Override
    public ResponseData<List<HighschoolExamScoreResponse>> getAllTop100HighestScoreBySubject(String subjectName, String local) {
        List<HighschoolExamScoreResponse> responseList = new ArrayList<>();
        try {
            Integer examLocalId = null;
            if (local != null && !local.isEmpty()) {
                ExamLocal examLocal = examLocalRepository.findByName(local);
                if (examLocal == null) {
                    return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy địa phương này !");
                }
                examLocalId = examLocal.getId();
            }

            List<String> topStudents;
            boolean isKHTN = "KHTN".equalsIgnoreCase(subjectName);
            boolean isKHXH = "KHXH".equalsIgnoreCase(subjectName);

            if (isKHTN) {
                topStudents = highschoolExamScoreRepository.findTop100StudentsBySubjects(Arrays.asList(27, 16, 23), examLocalId);
            } else if (isKHXH) {
                topStudents = highschoolExamScoreRepository.findTop100StudentsBySubjects(Arrays.asList(34, 9, 54), examLocalId);
            } else {
                topStudents = highschoolExamScoreRepository.findTop100StudentsBySubject(subjectName, examLocalId);
            }

            if (topStudents.isEmpty()) {
                return new ResponseData<>(ResponseCode.C200.getCode(), "Không có dữ liệu", responseList);
            }

            List<HighschoolExamScore> allScores = highschoolExamScoreRepository.findScoresByIdentificationNumbers(topStudents);

            Set<Integer> subjectIds = allScores.stream()
                    .map(HighschoolExamScore::getSubjectId)
                    .collect(Collectors.toSet());

            List<Subject> subjects = subjectRepository.findByIdIn(subjectIds);
            Map<Integer, Subject> subjectMap = subjects.stream()
                    .collect(Collectors.toMap(Subject::getId, Function.identity()));

            Map<String, List<SubjectScoreDTO>> scoresByStudent = new HashMap<>();
            for (HighschoolExamScore score : allScores) {
                 Subject subject = subjectMap.get(score.getSubjectId());
                SubjectScoreDTO subjectScoreDTO = new SubjectScoreDTO(
                        score.getSubjectId(),
                        subject != null ? subject.getName() : null,
                        score.getScore()
                );

                scoresByStudent.computeIfAbsent(score.getIdentificationNumber(), k -> new ArrayList<>())
                        .add(subjectScoreDTO);
            }

            List<Integer> subjectOrder = Arrays.asList(36, 28, 38, 27, 16, 23, 34, 9, 54);
            SubjectDTO mainSubject = getSubjectDetailsByName(subjectName);
            if (mainSubject != null && !subjectOrder.contains(mainSubject.getSubjectId())) {
                subjectOrder.add(0, mainSubject.getSubjectId());
            } else if (mainSubject != null) {
                subjectOrder = new ArrayList<>(subjectOrder);
                subjectOrder.remove(mainSubject.getSubjectId());
                subjectOrder.add(0, mainSubject.getSubjectId());
            }

            for (String identificationNumber : topStudents) {
                HighschoolExamScore firstScore = allScores.stream()
                        .filter(score -> score.getIdentificationNumber().equals(identificationNumber))
                        .findFirst()
                        .orElse(null);

                if (firstScore != null) {
                    List<SubjectScoreDTO> sortedScores = scoresByStudent.get(identificationNumber);
                    List<Integer> finalSubjectOrder = subjectOrder;
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

                    // Ensure all expected subjects are present
                    for (Integer subjectId : subjectOrder) {
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
                            firstScore.getExamLocal().getName(),
                            firstScore.getExamYear().getYear(),
                            sortedScores
                    );
                    responseList.add(response);
                }
            }

            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy Top 100 thành công", responseList);
        } catch (Exception e) {
            log.error("Error fetching Top 100 students", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình lấy Top 100, vui lòng thử lại sau.");
        }
    }


    @Override
    @Transactional
    public ResponseData<String> publishExamScores(Integer listExamScoreByYearId) {
        try {
            ListExamScoreByYear listExamScoreByYear = listExamScoreByYearRepository.findById(listExamScoreByYearId).orElse(null);
            if (listExamScoreByYear == null) {
                log.error("List exam score id {} not found", listExamScoreByYearId);
                return new ResponseData<>(ResponseCode.C203.getCode(), "Dữ liệu điểm thi không tồn tại!");
            }

            List<ListExamScoreByYear> activeScores = listExamScoreByYearRepository.findAllByStatus(ListExamScoreByYearStatus.ACTIVE);

            boolean alreadyActive = activeScores.stream()
                    .anyMatch(score -> score.getYear().equals(listExamScoreByYear.getYear()) && score.getStatus() == ListExamScoreByYearStatus.ACTIVE);

            if (alreadyActive) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Dữ liệu điểm thi năm " + listExamScoreByYear.getYear() + " đã được công bố từ trước đó.");
            }

            activeScores.forEach(activeScore -> activeScore.setStatus(ListExamScoreByYearStatus.ACTIVE));
            listExamScoreByYearRepository.saveAll(activeScores);

            ExamYear examYear = examYearRepository.findByYear(listExamScoreByYear.getYear());
            if (examYear == null) {
                return new ResponseData<>(ResponseCode.C204.getCode(), "Dữ liệu năm thi không tồn tại.");
            }

            highschoolExamScoreRepository.updateStatusByExamYear(HighschoolExamScoreStatus.ACTIVE, examYear, HighschoolExamScoreStatus.INACTIVE);

            listExamScoreByYear.setStatus(ListExamScoreByYearStatus.ACTIVE);
            listExamScoreByYearRepository.save(listExamScoreByYear);

            List<HighschoolExamScore> highschoolExamScores = highschoolExamScoreRepository.findAllByExamYearAndStatus(
                    examYear, HighschoolExamScoreStatus.ACTIVE);

            Set<String> scoreIdentificationNumbers = highschoolExamScores.stream()
                    .map(HighschoolExamScore::getIdentificationNumber)
                    .collect(Collectors.toSet());

            List<UserIdentificationNumberRegister> allRegisters = userIdentificationNumberRegisterRepository.findAll();
            allRegisters.parallelStream().forEach(register -> {
                if (scoreIdentificationNumbers.contains(register.getIdentificationNumber())) {
                    register.setStatus(IdentificationNumberRegisterStatus.SENDED);
                } else {
                    register.setStatus(IdentificationNumberRegisterStatus.NOT_FOUND);
                }
            });

            userIdentificationNumberRegisterRepository.saveAll(allRegisters);

            listExamScoreHighSchoolExamScoreRepository.updateStatusByListExamScoreByYearId(HighschoolExamScoreStatus.ACTIVE, listExamScoreByYearId);

            for (UserIdentificationNumberRegister register : allRegisters) {
                if (register.getStatus() == IdentificationNumberRegisterStatus.SENDED) {
                    String email = register.getEmail();
                    String subject = "Kết quả thi tốt nghiệp THPT " + listExamScoreByYear.getYear();

                    StringBuilder message = new StringBuilder();
                    message.append("<h1>Cổng thông tin tuyển sinh trường đại học - UAP</h1>");
                    message.append("<h3>Xin trân trọng thông báo kết quả thi tốt nghiệp THPT ")
                            .append(listExamScoreByYear.getYear())
                            .append(" của bạn:</h3>");

                    List<HighschoolExamScore> userScores = highschoolExamScores.stream()
                            .filter(score -> score.getIdentificationNumber().equals(register.getIdentificationNumber()))
                            .toList();

                    BigDecimal khtnTotalScore = BigDecimal.ZERO;
                    BigDecimal khxhTotalScore = BigDecimal.ZERO;
                    boolean hasKHTN = false;
                    boolean hasKHXH = false;

                    Map<Integer, String> subjectIdToNameMap = subjectRepository.findAll().stream()
                            .collect(Collectors.toMap(Subject::getId, Subject::getName));

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
                            listExamScore.getStatus().name()
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
                log.info("Fetched ListExamScoreByYear: {}", listExamScore);

                PageRequest pageRequest = PageRequest.of(page, size);
                Page<HighschoolExamScore> highschoolExamScoresPage = highschoolExamScoreRepository.findByExamYear(listExamScore.getYear(), pageRequest);

                Set<String> identificationNumbers = highschoolExamScoresPage
                        .stream()
                        .map(HighschoolExamScore::getIdentificationNumber)
                        .collect(Collectors.toSet());

                if (identificationNumbers.isEmpty()) {
                    return new ResponseData<>(ResponseCode.C200.getCode(), "Không có dữ liệu", new ListExamScoreByYearResponseV2());
                }

                List<HighschoolExamScore> allScores = highschoolExamScoreRepository.findByIdentificationNumberIn(identificationNumbers);
                log.info("Fetched All HighschoolExamScores: {} entries", allScores.size());

                Set<Integer> subjectIds = allScores.stream()
                        .map(HighschoolExamScore::getSubjectId)
                        .collect(Collectors.toSet());
                Set<Integer> examLocalIds = allScores.stream()
                        .map(score -> score.getExamLocal().getId())
                        .collect(Collectors.toSet());

                List<Subject> subjects = subjectRepository.findByIdIn(subjectIds);
                Map<Integer, Subject> subjectMap = subjects.stream()
                        .collect(Collectors.toMap(Subject::getId, Function.identity()));

                List<ExamLocal> examLocals = examLocalRepository.findByIdIn(examLocalIds);
                Map<Integer, ExamLocal> examLocalMap = examLocals.stream()
                        .collect(Collectors.toMap(ExamLocal::getId, Function.identity()));

                Map<String, List<HighschoolExamScore>> scoresGroupedById = allScores.stream()
                        .collect(Collectors.groupingBy(HighschoolExamScore::getIdentificationNumber));

                List<HighschoolExamScoreResponse> examScoreResponses = new ArrayList<>();
                Set<String> seenIdentificationNumbers = new HashSet<>();

                for (HighschoolExamScore score : highschoolExamScoresPage) {
                    if (seenIdentificationNumbers.contains(score.getIdentificationNumber())) {
                        continue;
                    }
                    seenIdentificationNumbers.add(score.getIdentificationNumber());

                    List<SubjectScoreDTO> subjectScores = new ArrayList<>();
                    List<HighschoolExamScore> examinerScores = scoresGroupedById.get(score.getIdentificationNumber());

                    BigDecimal khtnTotalScore = BigDecimal.ZERO;
                    BigDecimal khxhTotalScore = BigDecimal.ZERO;
                    boolean hasKHTN = false;
                    boolean hasKHXH = false;

                    for (HighschoolExamScore subjectScore : examinerScores) {
                        Subject subject = subjectMap.get(subjectScore.getSubjectId());
                        if (subject != null) {
                            Float scoreValue = subjectScore.getScore();
                            subjectScores.add(new SubjectScoreDTO(subject.getId(), subject.getName(), scoreValue));

                            if (scoreValue != null) {
                                if (Set.of(27, 16, 23).contains(subject.getId())) {
                                    khtnTotalScore = khtnTotalScore.add(BigDecimal.valueOf(scoreValue));
                                    hasKHTN = true;
                                } else if (Set.of(34, 9, 54).contains(subject.getId())) {
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
                    ExamLocal examLocal = examLocalMap.get(score.getExamLocal().getId());
                    String localName = examLocal != null ? examLocal.getName() : null;
                    examScoreResponses.add(new HighschoolExamScoreResponse(
                            score.getIdentificationNumber(),
                            localName,
                            listExamScore.getYear(),
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


    @Override
    public ResponseData<List<UserIdentificationResponseDTO>> getAllRegisteredIdentificationNumbers(Integer userId, String identificationNumber) {
        try {
            List<UserIdentificationNumberRegister> registers;

            if (userId != null && identificationNumber != null) {
                registers = userIdentificationNumberRegisterRepository.findByIdUserIdAndIdIdentificationNumber(userId, identificationNumber);
            } else if (userId != null) {
                registers = userIdentificationNumberRegisterRepository.findByIdUserId(userId);
            } else if (identificationNumber != null) {
                registers = userIdentificationNumberRegisterRepository.findByIdIdentificationNumber(identificationNumber);
            } else {
                registers = userIdentificationNumberRegisterRepository.findAll();
            }

            if (registers.isEmpty()) {
                log.error("No identification number registered found {}", registers);
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy số báo danh nào được đăng kí");
            }

            Map<Integer, List<UserIdentificationNumberRegister>> groupedByUserId = registers.stream()
                    .collect(Collectors.groupingBy(UserIdentificationNumberRegister::getUserId));

            List<UserIdentificationResponseDTO> userIdentificationResponseList = groupedByUserId.entrySet().stream()
                    .map(entry -> {
                        Integer userIdKey = entry.getKey();
                        List<UserIdentificationNumberRegister> userRegisters = entry.getValue();

                        List<RegisteredIdentificationNumberDTO> registeredIdentificationNumberList = userRegisters.stream()
                                .map(r -> new RegisteredIdentificationNumberDTO(
                                        r.getIdentificationNumber(),
                                        r.getYear(),
                                        r.getStatus().name()
                                ))
                                .toList();

                        UserIdentificationNumberRegister firstRegister = userRegisters.get(0);

                        return new UserIdentificationResponseDTO(
                                userIdKey,
                                firstRegister.getEmail(),
                                registeredIdentificationNumberList,
                                firstRegister.getCreateTime()
                        );
                    })
                    .toList();

            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách đăng kí số báo danh thành công !", userIdentificationResponseList);

        } catch (Exception e) {
            log.error("Error fetching registered identification numbers", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình lấy danh sách đăng kí số báo danh, vui lòng thử lại sau");
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

    private final Map<Integer, Float> avgScore2023 = new HashMap<>();
    private final Map<Integer, Float> avgScore2024 = new HashMap<>();
    private final Map<Integer, Integer> totalExaminer2023 = new HashMap<>();
    private final Map<Integer, Integer> totalExaminer2024 = new HashMap<>();

    public void AdmissionAnalysisService() {
        avgScore2023.put(1, 20.77f);
        avgScore2023.put(2, 20.28f);
        avgScore2023.put(19, 20.6f);
        avgScore2023.put(26, 18.98f);
        avgScore2023.put(46, 18.9f);

        totalExaminer2023.put(1, 325902);
        totalExaminer2023.put(2, 315146);
        totalExaminer2023.put(19, 324554);
        totalExaminer2023.put(26, 681723);
        totalExaminer2023.put(46, 877677);

        avgScore2024.put(1, 20.9f);
        avgScore2024.put(2, 20.47f);
        avgScore2024.put(19, 20.53f);
        avgScore2024.put(26, 20.95f);
        avgScore2024.put(46, 19.51f);

        totalExaminer2024.put(1, 343800);
        totalExaminer2024.put(2, 328761);
        totalExaminer2024.put(19, 342291);
        totalExaminer2024.put(26, 704008);
        totalExaminer2024.put(46, 908681);
    }

    public ResponseData<?> forecastScore2024(AdmissionAnalysisRequest request) {
        try {
            ResponseData<List<SubjectGroupDTO>> availableSubjectGroupsResponse = getAvailableSubjectGroupsForUser(request.getIdentificationNumber(), request.getUniversity(), request.getSubjectGroup());
            if (availableSubjectGroupsResponse.getData() == null || !request.getSubjectGroup().describeConstable().isPresent()) {
                throw new IllegalArgumentException("Không tìm thấy tổ hợp môn khả dụng cho số báo danh này.");
            }

            List<Integer> availableSubjectGroupIds = availableSubjectGroupsResponse.getData().stream()
                    .map(SubjectGroupDTO::getSubjectGroupId)
                    .toList();

            Integer subjectGroup = request.getSubjectGroup();
            if (!availableSubjectGroupIds.contains(subjectGroup)) {
                throw new IllegalArgumentException("Tổ hợp môn đã chọn không nằm trong danh sách các tổ hợp môn khả dụng cho thí sinh.");
            }

            Float examScore2023 = admissionTrainingProgramMethodRepository.findScoreFor2023(request.getUniversity(), request.getMajor(), subjectGroup);
            if (examScore2023 == null) {
                throw new IllegalArgumentException("Không tìm thấy điểm chuẩn cho năm 2023 cho tổ hợp môn và ngành đã chọn.");
            }
            List<Integer> subjectIds = subjectGroupSubjectRepository.findSubjectIdsBySubjectGroupId(subjectGroup);

            List<HighschoolExamScore> userScores = highschoolExamScoreRepository.findByIdentificationNumberAndSubjectIdIn(request.getIdentificationNumber(), subjectIds);

            float userScore2024 = userScores.stream()
                    .map(HighschoolExamScore::getScore)
                    .reduce(0f, Float::sum);

            float avgScore2023ForGroup;
            float avgScore2024ForGroup;

            switch (subjectGroup) {
                case 1:
                    avgScore2023ForGroup = 20.77f;
                    avgScore2024ForGroup = 20.9f;
                    break;
                case 2:
                    avgScore2023ForGroup = 20.28f;
                    avgScore2024ForGroup = 20.47f;
                    break;
                case 19:
                    avgScore2023ForGroup = 20.6f;
                    avgScore2024ForGroup = 20.53f;

                    break;
                case 26:
                    avgScore2023ForGroup = 18.98f;
                    avgScore2024ForGroup = 20.95f;
                    break;
                case 46:
                    avgScore2023ForGroup = 18.9f;
                    avgScore2024ForGroup = 19.51f;
                    break;
                default:
                    throw new IllegalArgumentException("Khối không hợp lệ: " + subjectGroup);
            }

            float chenhLechDTB = avgScore2024ForGroup - avgScore2023ForGroup;

            List<Object[]> dataWithQuota = getScoreAndSubjectGroupAndMajor(request);

            int quota2023 = 0, quota2024 = 0;
            for (Object[] row : dataWithQuota) {
                int year = (int) row[0];
                if (year == 2023) {
                    quota2023 = (int) row[2];
                } else if (year == 2024) {
                    quota2024 = (int) row[2];
                }
            }

            int chiTieuChenhLech = (quota2024 - quota2023) / quota2023 * 100;

            DiemTrungBinhStatus scoreStatus = analyzeScoreChange(chenhLechDTB);

            String scoreTrend = scoreStatus.getName();

            ChiTieuStatus quotaStatus = analyzeQuotaChange(chiTieuChenhLech);

            String quotaTrend = quotaStatus.getName();

            DiemChuanStatus finalStatus = getResult(scoreStatus, quotaStatus);

            String universityName = universityInfoRepository.findById(request.getUniversity())
                    .map(UniversityInfo::getName)
                    .orElse("Trường không hợp lệ");

            String majorName = majorRepository.findById(request.getMajor())
                    .map(Major::getName)
                    .orElse("Ngành không hợp lệ");

            String subjectGroupName = subjectGroupRepository.findById(request.getSubjectGroup())
                    .map(SubjectGroup::getName)
                    .orElse("Tổ hợp môn không hợp lệ");

            String advice = generateAdvice(finalStatus, userScore2024, examScore2023, universityName, majorName, subjectGroupName);

            String chenhLechDTBMessage = "Điểm trung bình tổ hợp môn " + subjectGroupName + " năm 2024 có xu hướng " + scoreTrend +
                    " với mức chênh lệch " + Math.abs(chenhLechDTB) + " điểm so với năm 2023.";

            String chiTieuChenhLechMessage = "Chỉ tiêu ngành " + majorName + " của trường " + universityName+
                    " năm 2024 có xu hướng " + quotaTrend + " so với năm 2023" + " với số chỉ tiêu chênh lệch là: " + chiTieuChenhLech;

            AdmissionAnalysisResponse response = new AdmissionAnalysisResponse(advice, chenhLechDTBMessage, chiTieuChenhLechMessage);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Dự đoán tỉ lệ đậu nguyện vọng thành công.", response);

        } catch (IllegalArgumentException ex) {
            log.error("Validate error: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Dự đoán tỉ lệ đậu nguyện vọng thất bại. " + ex.getMessage(), ex.getMessage());
        } catch (Exception ex) {
            log.error("Error while analyze: {}", ex.getMessage(), ex);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Dự đoán tỉ lệ đậu nguyện vọng thất bại. " + ex.getMessage(), ex.getMessage());
        }
    }

    private List<Object[]> getScoreAndSubjectGroupAndMajor(AdmissionAnalysisRequest request) {
        List<UniversityInfo> universityList = universityInfoRepository.findByUniversityId(request.getUniversity());
        if (universityList.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy trường đại học với tên: " + request.getUniversity());
        }

        List<Major> majorList = majorRepository.findAllById(request.getMajor());
        if (majorList.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy ngành học với tên: " + request.getMajor());
        }

        List<Integer> majorIds = majorList.stream()
                .map(Major::getId)
                .collect(Collectors.toList());

        Optional<SubjectGroup> subjectGroup = subjectGroupRepository.findById(request.getSubjectGroup());
        if (subjectGroup.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy tổ hợp môn với tên: " + request.getSubjectGroup().getClass().getName());
        }


        List<Object[]> dataWithQuota = null;

        for (UniversityInfo university : universityList) {
            if (majorIds.isEmpty()) {
                dataWithQuota = admissionTrainingProgramMethodRepository.findAdmissionDataFor2023And2024WithoutMajor(
                        university.getId(), subjectGroup.get().getId());
            } else {
                dataWithQuota = admissionTrainingProgramMethodRepository.findAdmissionDataFor2023And2024WithMajor(
                        university.getId(), subjectGroup.get().getId(), majorIds);
            }

            if (!dataWithQuota.isEmpty()) {
                break;
            }
        }

        if (dataWithQuota == null || dataWithQuota.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy dữ liệu cho trường, ngành học, và tổ hợp môn đã cung cấp");
        }

        return dataWithQuota;
    }

    private DiemTrungBinhStatus analyzeScoreChange(float chenhLechDTB) {
        if (chenhLechDTB >= 1) return DiemTrungBinhStatus.TangManh;
        if (chenhLechDTB >= 0.4) return DiemTrungBinhStatus.Tang;
        if (chenhLechDTB > 0) return DiemTrungBinhStatus.TangNhe;
        if (chenhLechDTB <= -1) return DiemTrungBinhStatus.GiamManh;
        if (chenhLechDTB <= -0.4) return DiemTrungBinhStatus.Giam;
        if (chenhLechDTB < 0) return DiemTrungBinhStatus.GiamNhe;
        return DiemTrungBinhStatus.KhongDoi;
    }

    private ChiTieuStatus analyzeQuotaChange(int chiTieuChenhLech) {
        if (chiTieuChenhLech >= 200) return ChiTieuStatus.TangManh;
        if (chiTieuChenhLech >= 100) return ChiTieuStatus.Tang;
        if (chiTieuChenhLech > 0) return ChiTieuStatus.TangNhe;
        if (chiTieuChenhLech <= -200) return ChiTieuStatus.GiamManh;
        if (chiTieuChenhLech <= -100) return ChiTieuStatus.Giam;
        if (chiTieuChenhLech < 0) return ChiTieuStatus.GiamNhe;
        return ChiTieuStatus.KhongDoi;
    }

    private DiemChuanStatus getResult(DiemTrungBinhStatus scoreStatus, ChiTieuStatus quotaStatus) {
        if (scoreStatus == DiemTrungBinhStatus.GiamManh || quotaStatus == ChiTieuStatus.GiamManh) {
            return DiemChuanStatus.Giam;
        }
        if (scoreStatus == DiemTrungBinhStatus.TangManh || quotaStatus == ChiTieuStatus.TangManh) {
            return DiemChuanStatus.Tang;
        }
        return DiemChuanStatus.KhongDoi;
    }

    private String generateAdvice(DiemChuanStatus finalStatus, float score2024, float score2023, String university, String major, String subjectGroupName) {
        double scoreDifference = score2024 - score2023;

        if (finalStatus == DiemChuanStatus.Giam) {
            if (scoreDifference >= -1.5 && scoreDifference < 0) {
                return "Với số liệu điểm trung bình và chỉ tiêu được phân tích thì khả năng đậu nguyện vào trường: " + university + " với ngành " + major + " của bạn vào năm 2024 với số điểm " + score2024 + " cho khối thi " + subjectGroupName + " là TRUNG BÌNH.";
            } else if (scoreDifference >= 0 && scoreDifference <= 1) {
                return "Với số liệu điểm trung bình và chỉ tiêu được phân tích thì khả năng đậu nguyện vào trường: " + university + " với ngành " + major +  " của bạn vào năm 2024 với số điểm " + score2024 + " cho khối thi " + subjectGroupName + " là KHÁ CAO.";
            } else if (scoreDifference <= -1.5) {
                return "Với số liệu điểm trung bình và chỉ tiêu được phân tích thì khả năng đậu nguyện vào trường: " + university + " với ngành " + major + " của bạn vào năm 2024 với số điểm " + score2024 + " cho khối thi " + subjectGroupName +" là RẤT THẤP.";
            } else {
                return "Với số liệu điểm trung bình và chỉ tiêu được phân tích thì khả năng đậu nguyện vào trường: " + university + " với ngành " + major + " của bạn vào năm 2024 với số điểm " + score2024 + " cho khối thi " + subjectGroupName + " là CAO.";
            }
        } else if (finalStatus == DiemChuanStatus.Tang) {
            if (scoreDifference < 0) {
                return "Với số liệu điểm trung bình và chỉ tiêu được phân tích thì khả năng đậu nguyện vào trường: " + university + " với ngành " + major + " của bạn vào năm 2024 với số điểm " + score2024 + " cho khối thi " + subjectGroupName + " là RẤT THẤP.";
            } else if (scoreDifference >= 0 && scoreDifference <= 0.5) {
                return "Với số liệu điểm trung bình và chỉ tiêu được phân tích thì khả năng đậu nguyện vào trường: " + university + " với ngành " + major + " của bạn vào năm 2024 với số điểm " + score2024 + " cho khối thi " + subjectGroupName + " là TRUNG BÌNH.";
            } else if (scoreDifference > 0.5 && scoreDifference <= 1.5) {
                return "Với số liệu điểm trung bình và chỉ tiêu được phân tích thì khả năng đậu nguyện vào trường: " + university + " với ngành " + major + " của bạn vào năm 2024 với số điểm " + score2024 + " cho khối thi " + subjectGroupName + " là KHÁ CAO.";
            } else {
                return "Với số liệu điểm trung bình và chỉ tiêu được phân tích thì khả năng đậu nguyện vào trường: " + university + " với ngành " + major + " của bạn vào năm 2024 với số điểm " + score2024 + " cho khối thi " + subjectGroupName + " là CAO.";
            }
        } else if (finalStatus == DiemChuanStatus.KhongDoi) {
            if (scoreDifference >= -0.5 && scoreDifference <= 0.5) {
                return "Với số liệu điểm trung bình và chỉ tiêu được phân tích thì khả năng đậu nguyện vào trường: " + university + " với ngành " + major + " của bạn vào năm 2024 với số điểm " + score2024 + " cho khối thi " + subjectGroupName + " là TRUNG BÌNH.";
            } else if (scoreDifference < -0.5) {
                return "Với số liệu điểm trung bình và chỉ tiêu được phân tích thì khả năng đậu nguyện vào trường: " + university + " với ngành " + major + " của bạn vào năm 2024 với số điểm " + score2024 + " cho khối thi " + subjectGroupName + " là RẤT THẤP.";
            } else if (scoreDifference > 0.5 && scoreDifference <= 1) {
                return "Với số liệu điểm trung bình và chỉ tiêu được phân tích thì khả năng đậu nguyện vào trường: " + university + " với ngành " + major + " của bạn vào năm 2024 với số điểm " + score2024 + " cho khối thi " + subjectGroupName + " là KHÁ CAO.";
            } else {
                return "Với số liệu điểm trung bình và chỉ tiêu được phân tích thì khả năng đậu nguyện vào trường: " + university + " với ngành " + major + " của bạn vào năm 2024 với số điểm " + score2024 + " cho khối thi " + subjectGroupName + " là CAO.";
            }
        }
        return "Không xác định được khả năng trúng tuyển.";
    }


    public ResponseData<List<SubjectGroupDTO>> getAvailableSubjectGroupsForUser(String identificationNumber, Integer universityId, Integer subjectGroupId) {
        try {
            List<HighschoolExamScore> examScores = highschoolExamScoreRepository.findByIdentificationNumberAndScoreIsNotNull(identificationNumber);
            if (examScores.isEmpty()) {
                return new ResponseData<>(ResponseCode.C204.getCode(), "Không tìm thấy điểm thi hợp lệ cho số báo danh này.");
            }

            List<Integer> subjectIdsFromUser = examScores.stream()
                    .map(HighschoolExamScore::getSubjectId)
                    .distinct()
                    .toList();

            List<SubjectGroup> availableSubjectGroups = subjectGroupSubjectRepository.findBySubjectIdIn(subjectIdsFromUser);
            if (availableSubjectGroups.isEmpty()) {
                return new ResponseData<>(ResponseCode.C204.getCode(), "Không tìm thấy tổ hợp môn hợp lệ cho điểm thi của thí sinh.");
            }

            List<SubjectGroup> filteredSubjectGroups = availableSubjectGroups.stream()
                    .filter(subjectGroup -> {
                        List<Integer> subjectGroupSubjectIds = subjectGroupSubjectRepository.findSubjectIdsBySubjectGroupId(subjectGroup.getId());
                        return subjectIdsFromUser.containsAll(subjectGroupSubjectIds);
                    })
                    .toList();

            if (filteredSubjectGroups.isEmpty()) {
                return new ResponseData<>(ResponseCode.C204.getCode(), "Không tìm thấy tổ hợp môn phù hợp với danh sách môn thí sinh đã thi.");
            }

            UniversityInfo universityInfo = universityInfoRepository.findUniversityInfoById(universityId);

            List<Integer> majorIds = admissionTrainingProgramRepository.findMajorIdsByUniversityId(universityId);

            List<AdmissionTrainingProgramSubjectGroup> availableSubjectGroupsFromAdmission = admissionTrainingProgramSubjectGroupRepository.findByUniversityIdAndSubjectGroupId(universityInfo.getId(), subjectGroupId);

            List<AdmissionTrainingProgramSubjectGroup> filteredAdmissionSubjectGroups = availableSubjectGroupsFromAdmission.stream()
                    .filter(atpsg -> {
                        Integer admissionTrainingProgramId = atpsg.getId().getAdmissionTrainingProgramId();

                        AdmissionTrainingProgram admissionTrainingProgram = admissionTrainingProgramRepository.findById(admissionTrainingProgramId).orElse(null);
                        if (admissionTrainingProgram == null || !majorIds.contains(admissionTrainingProgram.getMajorId())) {
                            return false;
                        }

                        Optional<AdmissionTrainingProgramMethod> admissionMethod2023 = admissionTrainingProgramMethodRepository.findByAdmissionTrainingProgramIdAndYearAndStatus(
                                admissionTrainingProgramId, 2023, AdmissionStatus.ACTIVE);

                        Optional<AdmissionTrainingProgramMethod> admissionMethod2024 = admissionTrainingProgramMethodRepository.findByAdmissionTrainingProgramIdAndYearAndStatus(
                                admissionTrainingProgramId, 2024, AdmissionStatus.ACTIVE);

                        return admissionMethod2023.isPresent() && admissionMethod2023.get().getAdmissionScore() != null
                                || admissionMethod2024.isPresent();
                    })
                    .toList();

            List<SubjectGroupDTO> subjectGroupDTOs = filteredAdmissionSubjectGroups.stream()
                    .map(atpsg -> {
                        SubjectGroup subjectGroup = subjectGroupRepository.findSubjectGroupById(atpsg.getId().getSubjectGroupId());
                        return new SubjectGroupDTO(subjectGroup.getId(), subjectGroup.getName(), subjectGroup.getStatus());
                    })
                    .filter(subjectGroupDTO -> subjectGroupDTO.getStatus().equals(SubjectStatus.ACTIVE))
                    .toList();


            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy tổ hợp môn thành công", subjectGroupDTOs);
        } catch (Exception e) {
            log.error("Error fetching subject groups for examiner", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình lấy tổ hợp môn, vui lòng thử lại sau.");
        }
    }

}
