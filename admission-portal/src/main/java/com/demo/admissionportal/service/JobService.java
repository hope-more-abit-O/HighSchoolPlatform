package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.holland_test.CreateJobRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.CreateJobResponse;
import com.demo.admissionportal.dto.response.holland_test.JobResponse;

import java.util.List;

/**
 * The interface Job service.
 */
public interface JobService {
    /**
     * Gets all job.
     *
     * @return the all job
     */
    ResponseData<List<JobResponse>> getAllJob();

    /**
     * Create job response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<List<CreateJobResponse>> createJob(List<CreateJobRequest> request);
}
