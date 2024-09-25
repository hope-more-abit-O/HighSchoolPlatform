package com.demo.admissionportal.dto;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.admission.FullAdmissionDTO;
import com.demo.admissionportal.entity.admission.AdmissionUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUpdateAdmissionDetailResponse {
    private Integer admissionUpdateId;
    private String note;
    private ActionerDTO createBy;
    private ActionerDTO updateBy;
    private Date createTime;
    private Date updateTime;
    private String status;
    private FullAdmissionDTO oldAdmission;
    private FullAdmissionDTO newAdmission;

    public GetUpdateAdmissionDetailResponse(FullAdmissionDTO oldAdmission, FullAdmissionDTO newAdmission, AdmissionUpdate admissionUpdate, ActionerDTO createBy, ActionerDTO updateBy) {
        this.admissionUpdateId = admissionUpdate.getId();
        this.note = admissionUpdate.getNote();
        this.createBy = createBy;
        this.updateBy = updateBy;
        this.createTime = admissionUpdate.getCreateTime();
        this.updateTime = admissionUpdate.getUpdateTime();
        this.status = admissionUpdate.getStatus().name;
        this.oldAdmission = oldAdmission;
        this.newAdmission = newAdmission;
    }
}
