package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "letters")
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String senderNickname;

    @Column(nullable = false)
    private String receiverNickname;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private String paperColor;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    public Letter() {}

    public Letter(String senderNickname, String receiverNickname, String content, String paperColor) {
        this.senderNickname = senderNickname;
        this.receiverNickname = receiverNickname;
        this.content = content;
        this.paperColor = paperColor;
        this.sentAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getReceiverNickname() {
        return receiverNickname;
    }

    public void setReceiverNickname(String receiverNickname) {
        this.receiverNickname = receiverNickname;
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
