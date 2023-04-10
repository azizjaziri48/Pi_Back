package com.example.pi_back.Controllers;

import com.example.pi_back.Services.ChatBotRequest;
import com.example.pi_back.Services.ChatBotResponse;
import com.example.pi_back.Services.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatBotController {

    private ChatBotService chatBotService;

    @Autowired
    public ChatBotController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping("/chatbot")
    public ResponseEntity<ChatBotResponse> generateResponse(@RequestBody ChatBotRequest request) {
        ChatBotResponse response = chatBotService.generateResponse(request);
        return ResponseEntity.ok(response);
    }
}
