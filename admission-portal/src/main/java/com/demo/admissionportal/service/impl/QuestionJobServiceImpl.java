package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.dto.request.holland_test.UpdateQuestionRequest;
import com.demo.admissionportal.entity.Job;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.sub_entity.QuestionJob;
import com.demo.admissionportal.repository.JobRepository;
import com.demo.admissionportal.repository.sub_repository.QuestionJobRepository;
import com.demo.admissionportal.service.QuestionJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionJobServiceImpl implements QuestionJobService {
    private final QuestionJobRepository questionJobRepository;
    private final JobRepository jobRepository;

    @Override
    public void updateQuestionJob(Integer questionId, UpdateQuestionRequest request, Integer staffId) {
        List<QuestionJob> checkQuestionJobExisted = questionJobRepository.findQuestionJobByQuestionId(questionId);
        for (QuestionJob questionJob : checkQuestionJobExisted) {
            if (!request.getJobIds().contains(questionJob.getJobId())) {
                questionJobRepository.delete(questionJob);
            }
        }

        // Update question job
        for (Integer questionJobId : request.getJobIds()) {
            Job job = jobRepository.findById(questionJobId).orElseThrow(() -> new RuntimeException("Job not found"));
            QuestionJob questionJob = questionJobRepository.findQuestionJobByJobId(job.getId());
            if (questionJob == null) {
                questionJob = new QuestionJob();
                questionJob.setJobId(job.getId());
                questionJob.setQuestionId(questionId);
                questionJob.setCreateBy(staffId);
                questionJob.setCreateTime(new Date());
                questionJobRepository.save(questionJob);
            } else {
                questionJob.setUpdateBy(staffId);
                questionJob.setUpdateTime(new Date());
            }
            questionJobRepository.save(questionJob);
        }
    }
}
