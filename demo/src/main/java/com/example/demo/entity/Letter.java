package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "letters")
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private String paperColor;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column(name = "receiver_nickname", nullable = true)
    private String receiverNickname;

    @Column(name = "sender_nickname", nullable = true) // 새 필드 추가
    private String senderNickname;

    public Letter() {}

    public Letter(User sender, User receiver, String content, String paperColor) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderNickname = sender.getNickname(); // 닉네임 설정
        this.receiverNickname = receiver.getNickname();
        this.content = content;
        this.paperColor = paperColor;
        this.sentAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReceiverNickname() {
        return receiverNickname;
    }

    public void setReceiverNickname(String receiverNickname) {
        this.receiverNickname = receiverNickname;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPaperColor() {
        return paperColor;
    }

    public void setPaperColor(String paperColor) {
        this.paperColor = paperColor;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
