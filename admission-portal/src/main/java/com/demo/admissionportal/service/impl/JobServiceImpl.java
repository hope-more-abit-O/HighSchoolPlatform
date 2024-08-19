package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.JobStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.holland_test.CreateJobRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.CreateJobResponse;
import com.demo.admissionportal.dto.response.holland_test.JobResponse;
import com.demo.admissionportal.entity.Job;
import com.demo.admissionportal.entity.StaffInfo;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.JobRepository;
import com.demo.admissionportal.repository.StaffInfoRepository;
import com.demo.admissionportal.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Job service.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;
    private final StaffInfoRepository staffInfoRepository;

    @Override
    public ResponseData<List<JobResponse>> getAllJob() {
        try {
            List<Job> jobResponses = jobRepository.findAll();
            List<JobResponse> jobResponseList = jobResponses.stream()
                    .map(job -> {
                        StaffInfo staffInfo = staffInfoRepository.findStaffInfoById(job.getCreateBy());
                        JobResponse mappedJob = modelMapper.map(job, JobResponse.class);
                        mappedJob.setCreateBy(staffInfo.getFirstName() + " " + staffInfo.getMiddleName() + " " + staffInfo.getLastName());
                        return mappedJob;
                    })
                    .collect(Collectors.toList());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách nghề nghiệp", jobResponseList);
        } catch (Exception e) {
            log.error("Error while get list job: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi lấy danh sách nghề nghiệp");
        }
    }

    @Override
    public ResponseData<List<CreateJobResponse>> createJob(List<CreateJobRequest> request) {
        try {
            if (request == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Request null");
            }
            List<CreateJobResponse> result = request.stream()
                    .map(this::addJob)
                    .collect(Collectors.toList());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Thêm nghề nghiệp thành công", result);
        } catch (Exception ex) {
            log.error("Error while add  job: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi thêm danh sách nghề nghiệp");
        }
    }

    private CreateJobResponse addJob(CreateJobRequest createJobRequest) {
        Integer staffId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Job job = modelMapper.map(createJobRequest, Job.class);
        job.setCreateBy(staffId);
        job.setCreateTime(new Date());
        job.setStatus(JobStatus.ACTIVE);
        jobRepository.save(job);
        StaffInfo staffInfo = staffInfoRepository.findStaffInfoById(job.getCreateBy());
        CreateJobResponse createJobResponse = modelMapper.map(job, CreateJobResponse.class);
        createJobResponse.setCreateBy(staffInfo.getFirstName() + " " + staffInfo.getMiddleName() + " " + staffInfo.getLastName());
        return createJobResponse;
    }
}
