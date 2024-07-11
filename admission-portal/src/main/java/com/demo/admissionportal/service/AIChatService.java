package com.demo.admissionportal.service;

/**
 * The interface Ai chat service.
 */
public interface AIChatService {
    /**
     * Gets chat response.
     *
     * @param prompt the prompt
     * @return the chat response
     */
    String getChatResponse(String prompt);
}
