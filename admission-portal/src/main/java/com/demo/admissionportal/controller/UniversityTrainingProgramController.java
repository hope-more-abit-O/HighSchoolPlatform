package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.response.university_training_program.GetFullUniversityTrainingProgramResponse;
import com.demo.admissionportal.dto.response.university_training_program.GetInfoUniversityTrainingProgramResponse;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.exceptions.BadRequestException;
import com.demo.admissionportal.exception.exceptions.NotAllowedException;
import com.demo.admissionportal.service.UniversityTrainingProgramService;
import com.demo.admissionportal.util.impl.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UniversityTrainingProgramController {
    private final UniversityTrainingProgramService universityTrainingProgramService;


    @GetMapping("/info")
    public GetInfoUniversityTrainingProgramResponse getInfoUniversityTrainingPrograms(Integer universityId) {
        return universityTrainingProgramService.getInfoUniversityTrainingPrograms(universityId);
    }

    @GetMapping("/full")
    public GetFullUniversityTrainingProgramResponse getUniversityTrainingPrograms(Integer universityId) {
        User user = ServiceUtils.getUser();

        if (user.getRole().equals(Role.UNIVERSITY))
            return universityTrainingProgramService.getUniversityTrainingPrograms(user.getId());
        else if (user.getRole().equals(Role.CONSULTANT)) {
            if (!user.getCreateBy().equals(universityId))
                throw new NotAllowedException(Map.of("universityId", universityId.toString()));
            return universityTrainingProgramService.getUniversityTrainingPrograms(user.getCreateBy());
        }
        else
            return universityTrainingProgramService.getUniversityTrainingPrograms(universityId);
    }
}
