package com.smartinsure.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Application {
    @Id
    // Removed @GeneratedValue(strategy= GenerationType.IDENTITY)
    private String id;

    private String userId;
    private String policyType; // This will now hold the AI's 'bestOption'
    private String recommendation; // This holds the full combined AI message

    // New fields to store individual AI recommendations
    private String bestOption;
    private String betterOption;
    private String goodOption;

    private String status;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}