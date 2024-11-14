package com.e.d.model.entity;

import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CHAT_MESSAGE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private int messageId;

    @Column(name = "roomid")
    private int roomid;

    @Column(name = "userid")
    private int userid;

    @Lob
    @Column(name = "chattext", nullable = false)
    private String chattext;

    @Column(name = "date_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp dateTime;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "type")
    private String type;  // 메시지 타입 (예: enter, chat)

    @Column(name = "sender")
    private String sender; // 발신자

    @Column(name = "receiver")
    private String receiver; // 수신자 (옵션)

    @Lob
    @Column(name = "data")
    private String data;   // 메시지 내용
}