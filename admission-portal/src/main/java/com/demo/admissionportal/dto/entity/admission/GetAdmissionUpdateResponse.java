package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.admission.AdmissionUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.PrimitiveIterator;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAdmissionUpdateResponse {
    private Integer admissionUpdateId;
    private Integer universityId;
    private String universityName;
    private String universityCode;
    private String universityUsername;
    private Integer oldAdmissionId;
    private Integer newAdmissionId;
    private ActionerDTO createBy;
    private Date createTime;
    private ActionerDTO updateBy;
    private Date updateTime;
    private String status;
    private String note;

    public GetAdmissionUpdateResponse(User account, UniversityInfo universityInfo, AdmissionUpdate admissionUpdate) {
        this.universityId = account.getId();
        this.universityName = universityInfo.getName();
        this.universityCode = universityInfo.getCode();
        this.universityUsername = account.getUsername();
        this.oldAdmissionId = admissionUpdate.getBeforeAdmissionId();
        this.newAdmissionId = admissionUpdate.getAfterAdmissionId();
    }

    public GetAdmissionUpdateResponse(User account, UniversityInfo universityInfo, AdmissionUpdate admissionUpdate, ActionerDTO createBy, ActionerDTO updateBy) {
        this.admissionUpdateId = admissionUpdate.getId();
        this.universityId = account.getId();
        this.universityName = universityInfo.getName();
        this.universityCode = universityInfo.getCode();
        this.universityUsername = account.getUsername();
        this.oldAdmissionId = admissionUpdate.getBeforeAdmissionId();
        this.newAdmissionId = admissionUpdate.getAfterAdmissionId();
        this.createBy = createBy;
        this.createTime = admissionUpdate.getCreateTime();
        this.updateBy = updateBy;
        this.updateTime = admissionUpdate.getUpdateTime();
        this.status = admissionUpdate.getStatus().name;
        this.note = admissionUpdate.getNote();
    }
}
