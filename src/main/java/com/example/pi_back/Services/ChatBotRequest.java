package com.example.pi_back.Services;

public class ChatBotRequest {

    private String message;
    private String projectName;

    public ChatBotRequest(String message, String projectName) {
        this.message = message;
        this.projectName = projectName;
    }

    public String getMessage() {
        return message;
    }

    public String getProjectName() {
        return projectName;
    }

}