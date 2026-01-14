package com.chatbot.controller;

import com.chatbot.dto.ChatRequest;
import com.chatbot.entity.Message;
import com.chatbot.entity.Project;
import com.chatbot.repository.MessageRepository;
import com.chatbot.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired private ProjectRepository projectRepository;
    @Autowired private MessageRepository messageRepository;

    @Value("${openai.api.key}") private String apiKey;
    @Value("${openai.model}") private String modelName;
    @Value("${groq.api.url}") private String groqUrl;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody ChatRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // SAVE USER MESSAGE (For internal logging only)
        saveMessage(project, "user", request.getUserMessage());

        // STATELESS LOGIC: We pass an empty list to prevent "Collection Framework" hallucinations
        String aiResponse = generateResponse(project.getSystemPrompt(), new ArrayList<>(), request.getUserMessage());

        // SAVE AI MESSAGE
        saveMessage(project, "assistant", aiResponse);

        return ResponseEntity.ok(aiResponse);
    }

    private String generateResponse(String systemPrompt, List<Message> history, String userMessage) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            List<Map<String, String>> apiMessages = new ArrayList<>();
            apiMessages.add(Map.of("role", "system", "content", (systemPrompt != null ? systemPrompt : "Helpful assistant")));
            apiMessages.add(Map.of("role", "user", "content", userMessage));

            Map<String, Object> body = Map.of("model", modelName, "messages", apiMessages, "temperature", 0.7);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(groqUrl, entity, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
        } catch (Exception e) {
            return "AI Engine is busy. Please try asking again shortly!";
        }
    }

    private void saveMessage(Project project, String role, String content) {
        Message msg = new Message();
        msg.setProject(project);
        msg.setSenderRole(role);
        msg.setContent(content);
        msg.setTimestamp(LocalDateTime.now());
        messageRepository.save(msg);
    }
}