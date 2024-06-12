package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.consultant.RegisterConsultantRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Consultant;
import com.demo.admissionportal.repository.ConsultantRepository;
import com.demo.admissionportal.service.ConsultantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsultantServiceImpl implements ConsultantService {
    private final ConsultantRepository consultantRepository;

    public ResponseData<?> registerConsultant(RegisterConsultantRequestDTO request) {
        try {
            //TODO check university id
            {}

            request.Trim();
            log.info("Starting registration process for email: {}", request.getEmail());
            Optional<Consultant> oConsultant = consultantRepository.findByEmailOrUsername(request.getEmail().trim(), request.getUsername().trim());
            if (oConsultant.isEmpty()) {
                log.warn("Consultant with email: {} or username: {} already exists", request.getEmail(), request.getUsername());
                return new ResponseData<>(ResponseCode.C204.getCode(), "Tên tài khoản hoặc email của tư vấn viên đã tồn tại!");
            }
            oConsultant = consultantRepository.findByPhone(request.getPhone());
            if (oConsultant.isEmpty()) {
                log.warn("Consultant with phone: {} already registered", request.getPhone().trim());
                return new ResponseData<>(ResponseCode.C204.getCode(), "Số điện thoại này đã được đăng kí bởi tư vấn viên khác !");
            }
            Consultant consultantRegisted = new Consultant(request.getUsername(), request.getName(), request.getEmail(), request.getPassword(), request.getPhone());
            consultantRepository.save(consultantRegisted);
            log.info("Consultant registered successfully with email: {}", request.getEmail());

            return new ResponseData<>(ResponseCode.C200.getCode(), "Nhân viên được tạo thành công !", consultantRegisted);
        } catch (Exception e) {
            log.error("Registering Consultant with email failed: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Tạo nhân viên thất bại, vui lòng kiểm tra lại !");
        }
    }
}
