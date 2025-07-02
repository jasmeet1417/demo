package com.corp.match.controller;



import com.corp.match.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/send")        // maps to /app/send
    @SendTo("/topic/messages")      // sent to subscribers of /topic/messages
    public ChatMessage send(ChatMessage message) {
        return message; // echo or process here
    }
}
