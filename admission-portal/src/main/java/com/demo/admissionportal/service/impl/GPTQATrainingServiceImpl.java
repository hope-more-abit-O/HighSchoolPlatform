package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.entity.GPTQATraining;
import com.demo.admissionportal.repository.GPTQATrainingRepository;
import com.demo.admissionportal.service.GPTQATrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GPTQATrainingServiceImpl implements GPTQATrainingService {

    @Autowired
    private GPTQATrainingRepository repository;

    @Override
    public Optional<String> getAnswer(String question) {
        // Debug log for checking question in the repository
        System.out.println("Checking for question in repository: " + question);

        Optional<GPTQATraining> training = repository.findByQuestion(question);
        if (training.isPresent()){
            return null;
        }
        return training.map(GPTQATraining::getAnswer);
    }

    @Override
    public GPTQATraining addQA(String question, String answer) {
        GPTQATraining training = new GPTQATraining();
        training.setQuestion(question);
        training.setAnswer(answer);
        return repository.save(training);
    }
}
