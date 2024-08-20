package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.constants.UniversityTrainingProgramStatus;
import com.demo.admissionportal.dto.entity.university_training_program.FullUniversityTrainingProgramDTO;
import com.demo.admissionportal.dto.entity.university_training_program.InfoUniversityTrainingProgramDTO;
import com.demo.admissionportal.dto.response.university_training_program.GetFullUniversityTrainingProgramResponse;
import com.demo.admissionportal.dto.response.university_training_program.GetInfoUniversityTrainingProgramResponse;
import com.demo.admissionportal.entity.UniversityTrainingProgram;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import com.demo.admissionportal.exception.exceptions.BadRequestException;
import com.demo.admissionportal.repository.UniversityTrainingProgramRepository;
import com.demo.admissionportal.service.UniversityTrainingProgramService;
import com.demo.admissionportal.util.impl.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UniversityTrainingProgramServiceImpl implements UniversityTrainingProgramService {
    private final UniversityTrainingProgramRepository universityTrainingProgramRepository;

    public List<UniversityTrainingProgram> findByUniversityId(Integer universityId) {
        return universityTrainingProgramRepository.findByUniversityId(universityId);
    }

    public GetFullUniversityTrainingProgramResponse getUniversityTrainingPrograms(Integer universityId) {
        List<UniversityTrainingProgram> universityTrainingPrograms = findByUniversityId(universityId);

        return new GetFullUniversityTrainingProgramResponse(fullMapping(universityTrainingPrograms));
    }

    public GetInfoUniversityTrainingProgramResponse getInfoUniversityTrainingPrograms(Integer universityId) {
        List<UniversityTrainingProgram> universityTrainingPrograms = findByUniversityId(universityId);

        return new GetInfoUniversityTrainingProgramResponse(infoMapping(universityTrainingPrograms));
    }

    public List<UniversityTrainingProgram> updateUniversityProgramStatus(List<Integer> inactiveIds, List<Integer> activeIds){

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

        List<UniversityTrainingProgram> universityTrainingPrograms = universityTrainingProgramRepository.findByIdInAndUniversityId( combinedIds, uniId);

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

        for (AdmissionTrainingProgram admission : admissionTrainingPrograms) {
            boolean isExisted = false;

            for (UniversityTrainingProgram uni : universityTrainingPrograms) {
                if (uni.compareWithAdmissionTrainingProgram(admission)) {
                    isExisted = true;
                    activeUniversityTrainingPrograms.add(uni);
                    break;
                }
            }

            if (!isExisted) {
                activeUniversityTrainingPrograms.add(new UniversityTrainingProgram(admission, universityId, userId));
            }
        }

        universityTrainingPrograms.removeAll(activeUniversityTrainingPrograms);

        activeUniversityTrainingPrograms.forEach(uni -> uni.updateStatus(UniversityTrainingProgramStatus.ACTIVE, userId));

        universityTrainingPrograms.forEach(uni -> uni.updateStatus(UniversityTrainingProgramStatus.INACTIVE, userId));

        List<UniversityTrainingProgram> newList =  Stream
                .concat(activeUniversityTrainingPrograms.stream(), universityTrainingPrograms.stream())
                .collect(Collectors.toList());

        return universityTrainingProgramRepository.saveAll(newList);
    }

    public FullUniversityTrainingProgramDTO fullMapping(UniversityTrainingProgram universityTrainingProgram){
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

    public List<FullUniversityTrainingProgramDTO> fullMapping(List<UniversityTrainingProgram> universityTrainingPrograms){
        return universityTrainingPrograms.stream().map(this::fullMapping).collect(Collectors.toList());
    }

    public InfoUniversityTrainingProgramDTO infoMapping(UniversityTrainingProgram universityTrainingProgram){
        return new InfoUniversityTrainingProgramDTO(
                universityTrainingProgram.getId(),
                universityTrainingProgram.getUniversityId(),
                universityTrainingProgram.getMajorId(),
                universityTrainingProgram.getTrainingSpecific(),
                universityTrainingProgram.getLanguage(),
                universityTrainingProgram.getStatus().name()
        );
    }

    public List<InfoUniversityTrainingProgramDTO> infoMapping(List<UniversityTrainingProgram> universityTrainingPrograms){
        return universityTrainingPrograms.stream().map(this::infoMapping).collect(Collectors.toList());
    }

    public void inactiveAll(Integer id) {
        List<UniversityTrainingProgram> universityTrainingPrograms = findByUniversityId(id);

        if (universityTrainingPrograms.isEmpty()) {
            return;
        }

        universityTrainingPrograms.forEach(uni -> uni.updateStatus(UniversityTrainingProgramStatus.INACTIVE, id));

        universityTrainingProgramRepository.saveAll(universityTrainingPrograms);
    }
}
