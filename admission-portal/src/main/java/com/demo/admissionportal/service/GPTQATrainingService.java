package com.demo.admissionportal.service;

import com.demo.admissionportal.entity.GPTQATraining;

import java.util.Optional;

public interface GPTQATrainingService {
    Optional<String> getAnswer(String question);
    GPTQATraining addQA(String question, String answer);
}
