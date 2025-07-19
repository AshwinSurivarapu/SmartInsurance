package com.smartinsure.controller;// You'll need an ApplicationInput class matching your GraphQL input type
// application-service/src/main/java/com/smartinsure/applicationservice/controller/ApplicationInput.java

import lombok.Data;

@Data
public class ApplicationInput {
    private String userId;
    // Removed: private String policyType;
    private int customerAge;
    private double customerIncome;
    // Add getters and setters for all fields defined in your GraphQL ApplicationInput
    // public String getVehicleDetails() { ... }
    // public void setVehicleDetails(String vehicleDetails) { ... }


}