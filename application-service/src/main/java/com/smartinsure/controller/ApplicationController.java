package com.smartinsure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/applications") // Base path for application endpoints
public class ApplicationController {

    // We'll configure this URL in application.properties
    @Value("${plan.recommender.service.url}") // Default to 5001
    private String planRecommenderServiceUrl;

    @Autowired
    private  RestTemplate restTemplate;

    // Constructor injection for RestTemplate is preferred
    public ApplicationController() {
        this.restTemplate = new RestTemplate();
    }

    // DTO for incoming application request
    static class ApplicationRequest {
        public String customerUsername; // Example field
        public int customerAge;         // Example field
        public double customerIncome;   // Example field
        // Add other relevant application details here
    }

    // DTO for the response we expect from plan-recommender-service
    static class RecommendationResponse {
        public String recommendation;
        public String message;
        // Add other fields if the Python service returns more
    }

    @PostMapping // Handles POST requests to /api/applications
    public ResponseEntity<?> createApplication(@RequestBody ApplicationRequest request) {
        System.out.println("Received application request for user: " + request.customerUsername);

        // --- Step 1: Call the Plan Recommender Service ---
        String recommendUrl = planRecommenderServiceUrl + "/recommend";

        // Prepare the payload for the Python service
        Map<String, Object> pythonRequestPayload = new HashMap<>();
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", request.customerUsername);
        userDetails.put("age", request.customerAge);
        userDetails.put("income", request.customerIncome);
        // Add other details that the Python service might need
        pythonRequestPayload.put("userDetails", userDetails);

        RecommendationResponse recommendation = null;
        try {
            // Make the POST request to the Python service
            recommendation = restTemplate.postForObject(
                    recommendUrl,
                    pythonRequestPayload,
                    RecommendationResponse.class
            );
            System.out.println("Received recommendation: " + recommendation.recommendation);
        } catch (Exception e) {
            System.err.println("Error calling plan-recommender-service: " + e.getMessage());
            // Handle the error: you might return an error, retry, or proceed without a recommendation
            return new ResponseEntity<>(
                    Map.of("message", "Application received, but failed to get plan recommendation."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        // --- Step 2: (Future) Save application details to database ---
        // For now, we'll just acknowledge receipt and the recommendation.
        // In a real scenario, you'd save `request` details and `recommendation`
        // to a database (e.g., using a JPA repository here).

        String responseMessage = "Application created for " + request.customerUsername + ". Recommended Plan: " + recommendation.recommendation;
        return new ResponseEntity<>(Map.of("message", responseMessage), HttpStatus.CREATED);
    }
}