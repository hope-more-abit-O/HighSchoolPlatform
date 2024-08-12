package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.dto.entity.admission.FullAdmissionDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.exception.exceptions.NotAllowedException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramMethodRepository;
import com.demo.admissionportal.service.AddressService;
import com.demo.admissionportal.service.ConsultantService;
import com.demo.admissionportal.service.impl.AddressServiceImpl;
import com.demo.admissionportal.service.impl.admission.AdmissionServiceImpl;
import com.demo.admissionportal.service.impl.admission.AdmissionTrainingProgramMethodServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.demo.admissionportal.service.CreateUniversityService;
import com.demo.admissionportal.service.impl.UniversityServiceImpl;
import com.demo.admissionportal.service.impl.ValidationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final ConsultantService consultantService;
    private final UniversityServiceImpl universityServiceImpl;
    private final CreateUniversityService createUniversityService;
    private final ValidationServiceImpl validationServiceImpl;
    private final AdmissionServiceImpl admissionService;
    private final AdmissionTrainingProgramMethodServiceImpl admissionTrainingProgramMethodService;

    @GetMapping("/")
    public String home(){
        return "Hello home";
    }

    @GetMapping("/secured")
    public String secured(){
        return "Hello, Secured";
    }

    @GetMapping("/consultants")
    public ResponseEntity<ResponseData> getConsultants(
            Pageable pageable,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String universityName,
            @RequestParam(required = false) Integer universityId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) List<AccountStatus> statuses,
            @RequestParam(required = false) Integer createBy,
            @RequestParam(required = false) Integer updateBy
    ) throws NotAllowedException, ResourceNotFoundException {

        return ResponseEntity.ok(
                ResponseData.ok("Tìm mọi tư vấn viên dưới quyền thành công.",
                        consultantService.getFullConsultants(
                                pageable, id, name, username, universityName, universityId, statuses, createBy, updateBy
                        )
                )
        );
    }
    @GetMapping("/validate-email/{email}")
    public Boolean validateEmail(@PathVariable String email){
        return validationServiceImpl.validateEmail(email);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<FullAdmissionDTO>> getAdmission(@PathVariable Integer id){
        return ResponseEntity.ok(admissionService.getById(id));
    }
    @PostMapping("/run-node")
    public String runNodeScript() {
        try {
            String scriptPath = "src/main/node/hello.js";

            ProcessBuilder processBuilder = new ProcessBuilder("node", scriptPath);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            process.waitFor();
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error running Node.js script: " + e.getMessage();
        }
    }

    @GetMapping("/atpgm")
    public ResponseEntity atpgm(@RequestParam(required = false) List<Integer> majorId,
                                @RequestParam(required = false) Float offset,
                                @RequestParam(required = false) Float score,
                                @RequestParam(required = false) List<Integer> subjectGroupId,
                                @RequestParam(required = false) List<Integer> methodId,
                                @RequestParam(required = false) List<Integer> provinceId){
        return ResponseEntity.ok(admissionTrainingProgramMethodService.findAdmissionData(subjectGroupId, score, offset, methodId, majorId, provinceId, 2024));
    }
}
