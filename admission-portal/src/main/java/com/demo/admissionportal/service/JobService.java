package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.holland_test.CreateJobRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.CreateJobResponse;
import com.demo.admissionportal.dto.response.holland_test.JobResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Job service.
 */
public interface JobService {
    /**
     * Gets all job.
     *
     * @param jobName  the job name
     * @param status   the status
     * @param pageable the pageable
     * @return the all job
     */
    ResponseData<Page<JobResponse>> getAllJob(String jobName, String status, Pageable pageable);

    /**
     * Create job response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<List<CreateJobResponse>> createJob(List<CreateJobRequest> request);

    /**
     * Delete job response data.
     *
     * @param jobId the job id
     * @return the response data
     */
    ResponseData<String> deleteJob(Integer jobId);

    /**
     * Gets list job.
     *
     * @return the list job
     */
    ResponseData<List<JobResponse>> getListJob();
}
