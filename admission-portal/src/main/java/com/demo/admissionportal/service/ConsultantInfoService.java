package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.CreateConsultantRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.exception.DataExistedException;

public interface ConsultantInfoService {
    public ResponseData createConsultant(CreateConsultantRequest request) throws DataExistedException;
}
