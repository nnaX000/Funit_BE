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
    @JoinColumn(name = "sender_id", nullable = false) // 외래 키: sender_id
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false) // 외래 키: receiver_id
    private User receiver;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private String paperColor;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    // 기본 생성자 (JPA에서 필요)
    public Letter() {
    }

    // 매개변수를 받는 생성자 추가
    public Letter(User sender, User receiver, String content, String paperColor) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.paperColor = paperColor;
        this.sentAt = LocalDateTime.now(); // 현재 시간으로 설정
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
