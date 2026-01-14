package com.chatbot.controller;

import com.chatbot.entity.Project;
import com.chatbot.entity.User;
import com.chatbot.repository.ProjectRepository;
import com.chatbot.repository.UserRepository;
import com.chatbot.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 1. Get all projects for the logged-in user
    @GetMapping
    public List<Project> getUserProjects(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7)); // Remove "Bearer "
        User user = userRepository.findByEmail(email).orElseThrow();
        return projectRepository.findByUserId(user.getId());
    }

    // 2. Create a new Project (Agent)
    @PostMapping
    public Project createProject(@RequestHeader("Authorization") String token, @RequestBody Project project) {
        String email = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByEmail(email).orElseThrow();

        project.setUser(user);
        project.setCreatedAt(LocalDateTime.now());
        
        return projectRepository.save(project);
    }
}