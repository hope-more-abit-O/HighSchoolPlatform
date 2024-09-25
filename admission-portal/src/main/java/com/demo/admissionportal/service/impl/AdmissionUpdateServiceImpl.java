package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AdmissionUpdateStatus;
import com.demo.admissionportal.entity.admission.AdmissionUpdate;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.AdmissionUpdateRepository;
import com.demo.admissionportal.service.AdmissionUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdmissionUpdateServiceImpl implements AdmissionUpdateService {
    private final AdmissionUpdateRepository admissionUpdateRepository;

    public AdmissionUpdate save(AdmissionUpdate admissionUpdate) {
        return admissionUpdateRepository.save(admissionUpdate);
    }

    public List<AdmissionUpdate> saveAll(List<AdmissionUpdate> admissionUpdates) {
        return admissionUpdateRepository.saveAll(admissionUpdates);
    }

    public AdmissionUpdate findById(Integer id) {
        return admissionUpdateRepository.findById(id).orElse(null);
    }

    public AdmissionUpdate getById(Integer id) {
        return admissionUpdateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy yêu cầu cập nhập đề án."));
    }

    public AdmissionUpdate findByBeforeAdmissionIdAndStatus(Integer beforeAdmissionId, AdmissionUpdateStatus status){
        return admissionUpdateRepository.findFirstByBeforeAdmissionIdAndStatus(beforeAdmissionId, status).orElse(null);
    }

    public AdmissionUpdate findByBeforeAdmissionIdAndStatus(Integer beforeAdmissionId, AdmissionUpdateStatus status, AdmissionUpdateStatus status2){
        return admissionUpdateRepository.findFirstByBeforeAdmissionIdAndStatus(beforeAdmissionId, status.name(), status2.name()).orElse(null);
    }

    public List<AdmissionUpdate> findAllByBeforeAdmissionIdAndStatus(Integer beforeAdmissionId, AdmissionUpdateStatus status){
        return admissionUpdateRepository.findByBeforeAdmissionIdAndStatus(beforeAdmissionId, status);
    }

    public List<AdmissionUpdate> expiredAllPendingAdmissionUpdate(Integer oldAdmissionId, Integer consultantId){
        List<AdmissionUpdate> admissionUpdateAvailables = this.findAllByBeforeAdmissionIdAndStatus(oldAdmissionId, AdmissionUpdateStatus.PENDING);
        if (admissionUpdateAvailables.isEmpty()){
            return null;
        }
        admissionUpdateAvailables.forEach((ele) -> ele.setExpired(consultantId));
        return this.saveAll(admissionUpdateAvailables);
    }

    public Page<AdmissionUpdate> findAllBy(Pageable pageable, Integer id, Integer beforeAdmissionId, Date createTime, Integer createBy, Integer universityId, List<String> statuses) {
        return admissionUpdateRepository.findAllBy(pageable, id, beforeAdmissionId, createTime, createBy, universityId, statuses);
    }

    public Page<AdmissionUpdate> findAllBy(Pageable pageable, Integer id, Integer beforeAdmissionId, Date createTime, Integer createBy, Integer universityId) {
        return admissionUpdateRepository.findAllBy(pageable, id, beforeAdmissionId, createTime, createBy, universityId);
    }
}
