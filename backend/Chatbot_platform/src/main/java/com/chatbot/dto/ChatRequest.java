package com.chatbot.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private Long projectId;
    private String userMessage;
}