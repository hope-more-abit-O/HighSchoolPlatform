package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.university_campus.UniversityCampusDTO;
import com.demo.admissionportal.dto.entity.university_campus.UniversityCampusProperties;
import com.demo.admissionportal.dto.request.university_campus.CreateCampusRequestDTO;
import com.demo.admissionportal.dto.request.university_campus.UpdateCampusRequestDTO;
import com.demo.admissionportal.dto.response.university_campus.DeleteCampusResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.UniversityCampus;
import com.demo.admissionportal.service.UniversityCampusService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/university-campus")
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasAuthority('UNIVERSITY')")
public class UniversityCampusController {
    private final UniversityCampusService universityCampusService;

    @GetMapping()
    public ResponseEntity<ResponseData<UniversityCampusDTO>> getUniversityCampus() {
        ResponseData<UniversityCampusDTO> result = universityCampusService.getUniversityCampus();
        if (result.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @PostMapping()
    public ResponseEntity<ResponseData<UniversityCampus>> createUniversityCampus(@RequestBody @Valid CreateCampusRequestDTO requestDTO) {
        if (requestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Request null"));
        }
        ResponseData<UniversityCampus> resultOfCreate = universityCampusService.createUniversityCampus(requestDTO);
        if (resultOfCreate.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultOfCreate);
        } else if (resultOfCreate.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfCreate);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfCreate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<DeleteCampusResponseDTO>> deleteUniversityCampus(@PathVariable("id") Integer id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Id null"));
        }
        ResponseData<DeleteCampusResponseDTO> resultOfDelete = universityCampusService.deleteUniversityCampus(id);
        if (resultOfDelete.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultOfDelete);
        } else if (resultOfDelete.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfDelete);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfDelete);
    }

    @PutMapping("/{campusID}")
    public ResponseEntity<ResponseData<UniversityCampusProperties>> updateUniversityCampus(@PathVariable("campusID") Integer campusId, @RequestBody @Valid UpdateCampusRequestDTO requestDTO) {
        if (requestDTO == null || campusId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Request null"));
        }
        ResponseData<UniversityCampusProperties> resultOfUpdate = universityCampusService.updateUniversityCampus(campusId, requestDTO);
        if (resultOfUpdate.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultOfUpdate);
        } else if (resultOfUpdate.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfUpdate);
        } else if (resultOfUpdate.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultOfUpdate);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfUpdate);
    }
}
