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
    String createSession();

    /**
     * Send message string.
     *
     * @param sessionUid the session uid
     * @param message    the message
     * @return the string
     */
    String sendMessage(String widgetUid, String sessionUid, String message);
}
