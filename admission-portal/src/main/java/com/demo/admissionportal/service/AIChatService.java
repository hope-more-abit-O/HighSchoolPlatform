package com.demo.admissionportal.service;

/**
 * The interface Ai chat service.
 */
public interface AIChatService {
    /**
     * Create a new session.
     *
     * @return the session id
     */
    String createSession();

    /**
     * Send a message to the chat session.
     *
     * @param widgetUid the widget uid
     * @param sessionUid the session uid
     * @param message the message
     * @return the response from the chatbot
     */
    String sendMessage(String widgetUid, String sessionUid, String message);
}
