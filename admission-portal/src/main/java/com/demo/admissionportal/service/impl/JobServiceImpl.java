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
import com.demo.admissionportal.entity.sub_entity.QuestionJob;
import com.demo.admissionportal.exception.exceptions.DataExistedException;
import com.demo.admissionportal.repository.JobRepository;
import com.demo.admissionportal.repository.StaffInfoRepository;
import com.demo.admissionportal.repository.sub_repository.QuestionJobRepository;
import com.demo.admissionportal.service.JobService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
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
    private final QuestionJobRepository questionJobRepository;

    @Override
    public ResponseData<Page<JobResponse>> getAllJob(String jobName, String status, Pageable pageable) {
        try {
            Page<Job> jobResponses = jobRepository.findJobs(jobName, status, pageable);
            Page<JobResponse> jobResponseList = jobResponses
                    .map(job -> {
                        StaffInfo staffInfo = staffInfoRepository.findStaffInfoById(job.getCreateBy());
                        JobResponse mappedJob = modelMapper.map(job, JobResponse.class);
                        mappedJob.setCreateBy(staffInfo.getFirstName() + " " + staffInfo.getMiddleName() + " " + staffInfo.getLastName());
                        mappedJob.setStatus(job.getStatus().name);
                        return mappedJob;
                    });
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách nghề nghiệp", jobResponseList);
        } catch (Exception e) {
            log.error("Error while get list job: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi lấy danh sách nghề nghiệp");
        }
    }

    @Transactional(rollbackOn = {Exception.class, DataExistedException.class})
    @Override
    public ResponseData<List<CreateJobResponse>> createJob(List<CreateJobRequest> request) {
        try {
            if (request == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Request null");
            }
            Set<String> jobNames = request.stream()
                    .map(CreateJobRequest::getName)
                    .collect(Collectors.toSet());

            List<Job> existingJobs = jobRepository.findJobsByNameIn(jobNames);
            if (!existingJobs.isEmpty()) {
                String existingJobNames = existingJobs.stream()
                        .map(Job::getName)
                        .collect(Collectors.joining(", "));
                throw new DataExistedException(existingJobNames + " đã tồn tại trong hệ thống");
            }

            List<CreateJobResponse> result = request.stream()
                    .map(this::addJob)
                    .collect(Collectors.toList());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Thêm nghề nghiệp thành công", result);
        } catch (DataExistedException ex) {
            return new ResponseData<>(ResponseCode.C207.getCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("Error while add job: {}", ex.getMessage());
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

    @Override
    public ResponseData<String> deleteJob(Integer jobId) {
        try {
            if (jobId == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "jobId null");
            }
            Job job = jobRepository.findById(jobId).orElse(null);
            if (job == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy job với id:" + jobId);
            }
            QuestionJob questionJob = questionJobRepository.findQuestionJobByJobId(jobId);
            if (questionJob == null) {
                jobRepository.delete(job);
                return new ResponseData<>(ResponseCode.C200.getCode(), "Xoá job thành công");
            }
            return new ResponseData<>(ResponseCode.C207.getCode(), "Job đã có question khác sử dụng");
        } catch (Exception ex) {
            log.error("Error while delete job: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi xoá danh sách nghề nghiệp");
        }
    }
}
