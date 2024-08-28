package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.constants.UniversityTrainingProgramStatus;
import com.demo.admissionportal.dto.entity.university_training_program.FullUniversityTrainingProgramDTO;
import com.demo.admissionportal.dto.entity.university_training_program.InfoUniversityTrainingProgramDTO;
import com.demo.admissionportal.dto.response.university_training_program.GetFullUniversityTrainingProgramResponse;
import com.demo.admissionportal.dto.response.university_training_program.GetInfoUniversityTrainingProgramResponse;
import com.demo.admissionportal.entity.ConsultantInfo;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.UniversityTrainingProgram;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import com.demo.admissionportal.exception.exceptions.BadRequestException;
import com.demo.admissionportal.exception.exceptions.QueryException;
import com.demo.admissionportal.exception.exceptions.StoreDataFailedException;
import com.demo.admissionportal.repository.UniversityTrainingProgramRepository;
import com.demo.admissionportal.service.UniversityInfoService;
import com.demo.admissionportal.service.UniversityTrainingProgramService;
import com.demo.admissionportal.util.impl.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UniversityTrainingProgramServiceImpl implements UniversityTrainingProgramService {
    private final UniversityTrainingProgramRepository universityTrainingProgramRepository;
    private final ConsultantInfoServiceImpl consultantInfoServiceImpl;
    private final UniversityInfoService universityInfoService;

    public List<UniversityTrainingProgram> findByUniversityId(Integer universityId) {
        return universityTrainingProgramRepository.findByUniversityId(universityId);
    }

    public List<UniversityTrainingProgram> findByUniversityIdWithStatus(Integer universityId, UniversityTrainingProgramStatus status) {
        return universityTrainingProgramRepository.findByUniversityIdAndStatus(universityId, status);
    }

    public GetFullUniversityTrainingProgramResponse getUniversityTrainingPrograms(Integer universityId) {
        List<UniversityTrainingProgram> universityTrainingPrograms = findByUniversityId(universityId);

        return new GetFullUniversityTrainingProgramResponse(fullMapping(universityTrainingPrograms));
    }

    public GetInfoUniversityTrainingProgramResponse getInfoUniversityTrainingPrograms(Integer universityId) {
        List<UniversityTrainingProgram> universityTrainingPrograms = findByUniversityId(universityId);

        return new GetInfoUniversityTrainingProgramResponse(infoMapping(universityTrainingPrograms));
    }

    @Override
    public GetFullUniversityTrainingProgramResponse getFullUniversityTrainingPrograms(Integer universityId) {
        User user = ServiceUtils.getUser();

        if (user.getRole().equals(Role.UNIVERSITY) && !user.getId().equals(universityId)) {
            throw new BadRequestException("Không thể truy cập thông tin chương trình đào tạo của trường khác", Map.of("universityId", universityId.toString()));
        }

        if (user.getRole().equals(Role.CONSULTANT)) {
            ConsultantInfo consultantInfo = consultantInfoServiceImpl.findById(user.getId());
            if (!consultantInfo.getUniversityId().equals(universityId)) {
                throw new BadRequestException("Không thể truy cập thông tin chương trình đào tạo của trường khác", Map.of("universityId", universityId.toString()));
            }
        }

        if (user.getRole().equals(Role.STAFF)){
            UniversityInfo universityInfo = universityInfoService.findById(universityId);
            if (!universityInfo.getStaffId().equals(user.getId())) {
                throw new BadRequestException("Trường này không thuộc thẩm quyền của bạn", Map.of("universityId", universityId.toString()));
            }
        }

        try {
            return this.getUniversityTrainingPrograms(universityId);
        } catch (Exception e) {
            throw new QueryException(e.getMessage());
        }
    }

    public List<UniversityTrainingProgram> updateUniversityProgramStatus(List<Integer> inactiveIds, List<Integer> activeIds) {

        String existedIds = activeIds.stream().filter(inactiveIds::contains).map(String::valueOf).collect(Collectors.joining(", "));

        if (!existedIds.isEmpty()) {
            throw new BadRequestException("Some ids are existed in both active and inactive list", Map.of("existedIds", existedIds));
        }

        Integer uniId = null;

        User user = ServiceUtils.getUser();

        if (user.getRole().equals(Role.UNIVERSITY))
            uniId = user.getId();
        else
            uniId = user.getCreateBy();

        List<Integer> combinedIds = Stream.concat(inactiveIds.stream(), activeIds.stream()).collect(Collectors.toList());

        List<UniversityTrainingProgram> universityTrainingPrograms = universityTrainingProgramRepository.findByIdInAndUniversityId(combinedIds, uniId);

        if (universityTrainingPrograms.size() != combinedIds.size()) {
            throw new BadRequestException("Some ids are not existed", Map.of("notExistedIds", combinedIds.stream().filter(id -> universityTrainingPrograms.stream().noneMatch(uni -> uni.getId().equals(id))).map(String::valueOf).collect(Collectors.joining(", "))));
        }

        universityTrainingPrograms.forEach(uni -> {
            if (inactiveIds.contains(uni.getId())) {
                uni.updateStatus(UniversityTrainingProgramStatus.INACTIVE, user.getId());
            } else {
                uni.updateStatus(UniversityTrainingProgramStatus.ACTIVE, user.getId());
            }
        });

        return universityTrainingProgramRepository.saveAll(universityTrainingPrograms);
    }

    public List<UniversityTrainingProgram> createFromAdmission(List<AdmissionTrainingProgram> admissionTrainingPrograms, Integer universityId, Integer userId) {

        List<UniversityTrainingProgram> universityTrainingPrograms = universityTrainingProgramRepository.findByUniversityId(universityId);

        List<UniversityTrainingProgram> activeUniversityTrainingPrograms = new ArrayList<>();

        for (AdmissionTrainingProgram admissionTrainingProgram : admissionTrainingPrograms) {
            boolean isExisted = false;

            for (UniversityTrainingProgram uni : universityTrainingPrograms) {
                if (uni.compareWithAdmissionTrainingProgram(admissionTrainingProgram)) {
                    isExisted = true;
                    activeUniversityTrainingPrograms.add(uni);
                    break;
                }
            }

            if (!isExisted) {
                activeUniversityTrainingPrograms.add(new UniversityTrainingProgram(admissionTrainingProgram, universityId, userId));
            }
        }

        removeAllMatching(universityTrainingPrograms, activeUniversityTrainingPrograms);

        activeUniversityTrainingPrograms.forEach(uni -> uni.updateStatus(UniversityTrainingProgramStatus.ACTIVE, userId));

        universityTrainingPrograms.forEach(uni -> uni.updateStatus(UniversityTrainingProgramStatus.INACTIVE, userId));

        List<UniversityTrainingProgram> newList = Stream
                .concat(activeUniversityTrainingPrograms.stream(), universityTrainingPrograms.stream())
                .collect(Collectors.toList());

        try {
            return universityTrainingProgramRepository.saveAll(newList);
        } catch (Exception e) {
            throw new StoreDataFailedException("Lưu thông tin chương trình đào tạo thất bại", Map.of("error", e.getMessage()));
        }
    }

    public FullUniversityTrainingProgramDTO fullMapping(UniversityTrainingProgram universityTrainingProgram) {
        return new FullUniversityTrainingProgramDTO(
                universityTrainingProgram.getId(),
                universityTrainingProgram.getUniversityId(),
                universityTrainingProgram.getMajorId(),
                universityTrainingProgram.getTrainingSpecific(),
                universityTrainingProgram.getLanguage(),
                universityTrainingProgram.getStatus().name(),
                universityTrainingProgram.getCreateTime(),
                universityTrainingProgram.getCreateBy(),
                universityTrainingProgram.getUpdateTime(),
                universityTrainingProgram.getUpdateBy()
        );
    }

    public List<FullUniversityTrainingProgramDTO> fullMapping(List<UniversityTrainingProgram> universityTrainingPrograms) {
        return universityTrainingPrograms.stream().map(this::fullMapping).collect(Collectors.toList());
    }

    public InfoUniversityTrainingProgramDTO infoMapping(UniversityTrainingProgram universityTrainingProgram) {
        return new InfoUniversityTrainingProgramDTO(
                universityTrainingProgram.getId(),
                universityTrainingProgram.getUniversityId(),
                universityTrainingProgram.getMajorId(),
                universityTrainingProgram.getTrainingSpecific(),
                universityTrainingProgram.getLanguage(),
                universityTrainingProgram.getStatus().name()
        );
    }

    public List<InfoUniversityTrainingProgramDTO> infoMapping(List<UniversityTrainingProgram> universityTrainingPrograms) {
        return universityTrainingPrograms.stream().map(this::infoMapping).collect(Collectors.toList());
    }

    public void inactiveAll(Integer universityId, Integer updateBy) {
        List<UniversityTrainingProgram> universityTrainingPrograms = findByUniversityIdWithStatus(universityId, UniversityTrainingProgramStatus.ACTIVE);

        if (universityTrainingPrograms.isEmpty()) {
            return;
        }

        universityTrainingPrograms.forEach(uni -> uni.updateStatus(UniversityTrainingProgramStatus.INACTIVE, updateBy));

        universityTrainingProgramRepository.saveAll(universityTrainingPrograms);
    }

    public static void removeAllMatching(List<UniversityTrainingProgram> universityTrainingPrograms, List<UniversityTrainingProgram> activeUniversityTrainingPrograms) {
        Iterator<UniversityTrainingProgram> iterator = universityTrainingPrograms.iterator();
        while (iterator.hasNext()) {
            UniversityTrainingProgram program = iterator.next();
            for (UniversityTrainingProgram activeProgram : activeUniversityTrainingPrograms) {
                if (isMatching(program, activeProgram)) {
                    iterator.remove();
                    break; // Break to avoid removing the same item multiple times
                }
            }
        }
    }

    private static boolean isMatching(UniversityTrainingProgram program, UniversityTrainingProgram activeProgram) {
        return program.getMajorId().equals(activeProgram.getMajorId())
                && (program.getTrainingSpecific() == null ? activeProgram.getTrainingSpecific() == null : program.getTrainingSpecific().equals(activeProgram.getTrainingSpecific()))
                && (program.getLanguage() == null ? activeProgram.getLanguage() == null : program.getLanguage().equals(activeProgram.getLanguage()));
    }
}
