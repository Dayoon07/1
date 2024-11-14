package com.e.d.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e.d.model.entity.ChatMessageEntity;
import com.e.d.model.repository.ChatMessageRepository;

@Service
public class ChatMessageService {
	
	@Autowired
    private ChatMessageRepository chatMessageRepository; // JPA 리포지토리

    public void saveMessage(ChatMessageEntity chatMessage) {
        chatMessageRepository.save(chatMessage); // 메시지 저장
    }
    
}
