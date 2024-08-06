package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.SubjectDTO;
import com.demo.admissionportal.dto.request.CreateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.request.UpdateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.response.HighschoolExamScoreResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.SubjectScoreDTO;
import com.demo.admissionportal.entity.HighschoolExamScore;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.HighschoolExamScoreRepository;
import com.demo.admissionportal.repository.SubjectRepository;
import com.demo.admissionportal.service.HighschoolExamScoreService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    @Override
    public ResponseData<List<HighschoolExamScoreResponse>> findAllWithFilter(Integer identificationNumber) {
        try {
            List<HighschoolExamScore> examScoresPage = highschoolExamScoreRepository.findAll(identificationNumber);
            if (identificationNumber == null || highschoolExamScoreRepository.countByIdentificationNumber(identificationNumber) == 0) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy số báo danh này !");
            }

            Map<Integer, List<HighschoolExamScore>> groupedById = examScoresPage.stream()
                    .collect(Collectors.groupingBy(HighschoolExamScore::getIdentificationNumber));

            List<HighschoolExamScoreResponse> responseList = new ArrayList<>();
            for (Map.Entry<Integer, List<HighschoolExamScore>> entry : groupedById.entrySet()) {
                List<SubjectScoreDTO> subjectScores = new ArrayList<>();
                String examinationBoard = null;
                String examiner = null;
                String dateOfBirth = null;
                Integer year = 2024;

                for (HighschoolExamScore score : entry.getValue()) {
                    if (score.getScore() != null) {
                        SubjectDTO subjectDTO = getSubjectDetails(score.getSubjectId());
                        if (subjectDTO != null) {
                            subjectScores.add(new SubjectScoreDTO(subjectDTO.getSubjectId(), subjectDTO.getSubjectName(), score.getScore()));
                        }
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

                responseList.add(new HighschoolExamScoreResponse(
                        entry.getKey(),
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

    @Override
    @Transactional
    public ResponseData<HighschoolExamScoreResponse> createExamScore(CreateHighschoolExamScoreRequest request) {
        try {
            if (request != null) {
                List<HighschoolExamScore> existIdentificationNumber = highschoolExamScoreRepository.findByIdentificationNumber(request.getIdentificationNumber());
                if (existIdentificationNumber != null && !existIdentificationNumber.isEmpty()) {
                    log.error("Identification Number {} is already existed", request.getIdentificationNumber());
                    return new ResponseData<>(ResponseCode.C204.getCode(), "Số báo danh thí sinh đã tồn tại");
                }
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Người tham chiếu không hợp lệ !");
            }
            User staff = (User) principal;
            Integer staffId = staff.getId();

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
                examScore.setYear(2024);
                examScore.setScore(subjectScore.getScore());
                examScore.setCreateTime(new Date());
                examScore.setCreateBy(staffId);
                return examScore;
            }).collect(Collectors.toList());

            highschoolExamScoreRepository.saveAll(examScores);

            List<SubjectScoreDTO> allSubjectScores = examScores.stream().map(examScore -> {
                String subjectName = subjectRepository.findById(examScore.getSubjectId())
                        .map(subject -> subject.getName())
                        .orElse(null);
                return new SubjectScoreDTO(examScore.getSubjectId(), subjectName, examScore.getScore());
            }).collect(Collectors.toList());

            HighschoolExamScoreResponse response = new HighschoolExamScoreResponse(
                    request.getIdentificationNumber(),
                    request.getExaminationBoard(),
                    request.getDateOfBirth(),
                    request.getExaminer(),
                    2024,
                    allSubjectScores
            );

            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo điểm thi thành công!", response);

        } catch (Exception e) {
            log.error("Error creating exam scores", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình tạo điểm, vui lòng thử lại sau. Lỗi: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseData<HighschoolExamScoreResponse> updateExamScore(Integer identificationNumber, UpdateHighschoolExamScoreRequest request) {
        try {
            if (request != null && identificationNumber != null) {
                List<HighschoolExamScore> existingScores = highschoolExamScoreRepository.findByIdentificationNumberAndYear(
                        identificationNumber, 2024);
                if (existingScores.isEmpty()) {
                    log.error("No exam scores found for identification number {} in year {}", identificationNumber, 2024);
                    return new ResponseData<>(ResponseCode.C204.getCode(), "Không tìm thấy điểm thi cho số báo danh và năm này");
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

                return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật điểm thi thành công!", response);

            } else {
                return new ResponseData<>(ResponseCode.C204.getCode(), "Yêu cầu không hợp lệ!");
            }
        } catch (Exception e) {
            log.error("Error updating exam scores", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình cập nhật điểm, vui lòng thử lại sau. Lỗi: " + e.getMessage());
        }
    }

    @Override
    public ResponseData<Map<String, Map<Float, Integer>>> getScoreDistributionForAGroup(String subjectGroup) {
        try {
            Map<String, List<Integer>> groupSubjectIds = new HashMap<>();
            groupSubjectIds.put("A00", List.of(27, 36, 16));
            groupSubjectIds.put("A01", List.of(27, 36, 38));
            groupSubjectIds.put("A02", List.of(16, 36, 23));

            Map<String, Map<Float, Integer>> combinedScoreDistribution = new HashMap<>();

            if (subjectGroup == null) {
                for (Map.Entry<String, List<Integer>> entry : groupSubjectIds.entrySet()) {
                    String groupName = entry.getKey();
                    List<Object[]> scoresData = highschoolExamScoreRepository.findScoresForSubjects(entry.getValue());

                    Map<Integer, Float> totalScoresByStudent = new HashMap<>();

                    for (Object[] data : scoresData) {
                        if (data[0] != null && data[2] != null) {
                            Integer identificationNumber = (Integer) data[0];
                            Float score = ((Number) data[2]).floatValue();

                            BigDecimal roundedScore = new BigDecimal(score).setScale(2, RoundingMode.DOWN);

                            totalScoresByStudent.merge(identificationNumber, roundedScore.floatValue(), Float::sum);
                        }
                    }

                    Map<Float, Integer> scoreDistribution = new HashMap<>();
                    for (Float totalScore : totalScoresByStudent.values()) {
                        BigDecimal roundedTotalScore = new BigDecimal(totalScore).setScale(2, RoundingMode.DOWN);
                        scoreDistribution.merge(roundedTotalScore.floatValue(), 1, Integer::sum);
                    }

                    combinedScoreDistribution.put(groupName, scoreDistribution);
                }
            } else {
                List<Integer> subjectIds = groupSubjectIds.get(subjectGroup);
                if (subjectIds == null) {
                    return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy nhóm môn học này !");
                }

                List<Object[]> scoresData = highschoolExamScoreRepository.findScoresForSubjects(subjectIds);

                Map<Integer, Float> totalScoresByStudent = new HashMap<>();

                for (Object[] data : scoresData) {
                    if (data[0] != null && data[2] != null) {
                        Integer identificationNumber = (Integer) data[0];
                        Float score = ((Number) data[2]).floatValue();

                        BigDecimal roundedScore = new BigDecimal(score).setScale(2, RoundingMode.DOWN);

                        totalScoresByStudent.merge(identificationNumber, roundedScore.floatValue(), Float::sum);
                    }
                }

                Map<Float, Integer> scoreDistribution = new HashMap<>();
                for (Float totalScore : totalScoresByStudent.values()) {
                    BigDecimal roundedTotalScore = new BigDecimal(totalScore).setScale(2, RoundingMode.DOWN);
                    scoreDistribution.merge(roundedTotalScore.floatValue(), 1, Integer::sum);
                }

                combinedScoreDistribution.put(subjectGroup, scoreDistribution);
            }

            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy phổ điểm thành công", combinedScoreDistribution);
        } catch (Exception e) {
            log.error("Error fetching score distribution for group " + subjectGroup, e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình lấy phổ điểm, vui lòng thử lại sau.");
        }
    }

    @Override
    public ResponseData<Map<String, Map<Float, Integer>>> getScoreDistributionForBGroup(String subjectGroup) {
        try {
            Map<String, List<Integer>> groupSubjectIds = new HashMap<>();
            groupSubjectIds.put("B00", List.of(16, 36, 23));
            groupSubjectIds.put("B03", List.of(23, 28, 36));
            groupSubjectIds.put("B08", List.of(23, 36, 38));

            Map<String, Map<Float, Integer>> combinedScoreDistribution = new HashMap<>();

            if (subjectGroup == null) {
                for (Map.Entry<String, List<Integer>> entry : groupSubjectIds.entrySet()) {
                    String groupName = entry.getKey();
                    List<Object[]> scoresData = highschoolExamScoreRepository.findScoresForSubjects(entry.getValue());

                    Map<Integer, Float> totalScoresByStudent = new HashMap<>();

                    for (Object[] data : scoresData) {
                        if (data[0] != null && data[2] != null) {
                            Integer identificationNumber = (Integer) data[0];
                            Float score = ((Number) data[2]).floatValue();

                            BigDecimal roundedScore = new BigDecimal(score).setScale(2, RoundingMode.DOWN);

                            totalScoresByStudent.merge(identificationNumber, roundedScore.floatValue(), Float::sum);
                        }
                    }

                    Map<Float, Integer> scoreDistribution = new HashMap<>();
                    for (Float totalScore : totalScoresByStudent.values()) {
                        BigDecimal roundedTotalScore = new BigDecimal(totalScore).setScale(2, RoundingMode.DOWN);
                        scoreDistribution.merge(roundedTotalScore.floatValue(), 1, Integer::sum);
                    }

                    combinedScoreDistribution.put(groupName, scoreDistribution);
                }
            } else {
                List<Integer> subjectIds = groupSubjectIds.get(subjectGroup);
                if (subjectIds == null) {
                    return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy nhóm môn học này !");
                }

                List<Object[]> scoresData = highschoolExamScoreRepository.findScoresForSubjects(subjectIds);

                Map<Integer, Float> totalScoresByStudent = new HashMap<>();

                for (Object[] data : scoresData) {
                    if (data[0] != null && data[2] != null) {
                        Integer identificationNumber = (Integer) data[0];
                        Float score = ((Number) data[2]).floatValue();

                        BigDecimal roundedScore = new BigDecimal(score).setScale(2, RoundingMode.DOWN);

                        totalScoresByStudent.merge(identificationNumber, roundedScore.floatValue(), Float::sum);
                    }
                }

                Map<Float, Integer> scoreDistribution = new HashMap<>();
                for (Float totalScore : totalScoresByStudent.values()) {
                    BigDecimal roundedTotalScore = new BigDecimal(totalScore).setScale(2, RoundingMode.DOWN);
                    scoreDistribution.merge(roundedTotalScore.floatValue(), 1, Integer::sum);
                }

                combinedScoreDistribution.put(subjectGroup, scoreDistribution);
            }

            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy phổ điểm thành công", combinedScoreDistribution);
        } catch (Exception e) {
            log.error("Error fetching score distribution for group " + subjectGroup, e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình lấy phổ điểm, vui lòng thử lại sau.");
        }
    }

    @Override
    public ResponseData<Map<String, Map<Float, Integer>>> getScoreDistributionForCGroup(String subjectGroup) {
        try {
            Map<String, List<Integer>> groupSubjectIds = new HashMap<>();
            groupSubjectIds.put("C00", List.of(9, 28, 34));
            groupSubjectIds.put("C03", List.of(28, 36, 34));
            groupSubjectIds.put("C04", List.of(9, 28, 36));

            Map<String, Map<Float, Integer>> combinedScoreDistribution = new HashMap<>();

            if (subjectGroup == null) {
                for (Map.Entry<String, List<Integer>> entry : groupSubjectIds.entrySet()) {
                    String groupName = entry.getKey();
                    List<Object[]> scoresData = highschoolExamScoreRepository.findScoresForSubjects(entry.getValue());

                    Map<Integer, Float> totalScoresByStudent = new HashMap<>();

                    for (Object[] data : scoresData) {
                        if (data[0] != null && data[2] != null) {
                            Integer identificationNumber = (Integer) data[0];
                            Float score = ((Number) data[2]).floatValue();

                            BigDecimal roundedScore = new BigDecimal(score).setScale(2, RoundingMode.DOWN);

                            totalScoresByStudent.merge(identificationNumber, roundedScore.floatValue(), Float::sum);
                        }
                    }

                    Map<Float, Integer> scoreDistribution = new HashMap<>();
                    for (Float totalScore : totalScoresByStudent.values()) {
                        BigDecimal roundedTotalScore = new BigDecimal(totalScore).setScale(2, RoundingMode.DOWN);
                        scoreDistribution.merge(roundedTotalScore.floatValue(), 1, Integer::sum);
                    }

                    combinedScoreDistribution.put(groupName, scoreDistribution);
                }
            } else {
                List<Integer> subjectIds = groupSubjectIds.get(subjectGroup);
                if (subjectIds == null) {
                    return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy nhóm môn học này !");
                }

                List<Object[]> scoresData = highschoolExamScoreRepository.findScoresForSubjects(subjectIds);

                Map<Integer, Float> totalScoresByStudent = new HashMap<>();

                for (Object[] data : scoresData) {
                    if (data[0] != null && data[2] != null) {
                        Integer identificationNumber = (Integer) data[0];
                        Float score = ((Number) data[2]).floatValue();

                        BigDecimal roundedScore = new BigDecimal(score).setScale(2, RoundingMode.DOWN);

                        totalScoresByStudent.merge(identificationNumber, roundedScore.floatValue(), Float::sum);
                    }
                }

                Map<Float, Integer> scoreDistribution = new HashMap<>();
                for (Float totalScore : totalScoresByStudent.values()) {
                    BigDecimal roundedTotalScore = new BigDecimal(totalScore).setScale(2, RoundingMode.DOWN);
                    scoreDistribution.merge(roundedTotalScore.floatValue(), 1, Integer::sum);
                }

                combinedScoreDistribution.put(subjectGroup, scoreDistribution);
            }

            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy phổ điểm thành công", combinedScoreDistribution);
        } catch (Exception e) {
            log.error("Error fetching score distribution for group " + subjectGroup, e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình lấy phổ điểm, vui lòng thử lại sau.");
        }
    }




    private SubjectDTO getSubjectDetails(Integer subjectId) {
        return subjectRepository.findById(subjectId)
                .map(subject -> {
                    return new SubjectDTO(subject.getId(), subject.getName());
                }).orElse(null);
    }
}
