package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.Message;
import com.example.pi_back.Services.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bot")
@RequiredArgsConstructor
public class BotController {
private final ChatBotService chatbot;


    @PostMapping("/send")
    public ResponseEntity sendMessage(@RequestBody Message message) throws Exception {
        /*TODO Not HARDCODED*/
        String contxt="Context:This project is a Study project for ESPRIT which is a school of engineering , it is a Spring and Angular Web application project the members of the team are :  Moetez Khemissi(creator of this chatbot ) , aziz jaziri , cyrine chouchane , mariem gnaoui and yassine gharbi you will be answering everything after this context , Begining :what school does moetez khemissi study in ?";

        String Actual_question = contxt+message.getMessage();

        chatbot.send(Actual_question);
        return new ResponseEntity<>("Already Existing Id", HttpStatus.BAD_REQUEST);
    }
}