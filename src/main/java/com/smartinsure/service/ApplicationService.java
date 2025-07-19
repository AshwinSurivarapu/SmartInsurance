package com.smartinsure.service;// application-service/src/main/java/com/smartinsure/applicationservice/service/ApplicationService.java

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID; // For generating a unique ID for the application

@Service
public class ApplicationService {

    @Value("${plan.recommender.service.url}")
    private String planRecommenderServiceUrl;

    private final RestTemplate restTemplate;

    public ApplicationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // DTO for incoming request from GraphQL/REST controllers
    @Data
    public static class ApplicationRequest {
        public String customerUsername;
        public int customerAge;
        public double customerIncome;
        // Removed: public String policyType;
    }

    // DTO for response from Python service
    @Data
    public static class RecommendationResponse {
        public String best_option;
        public String better_option;
        public String good_option;
        public String message;
    }

    public com.smartinsure.model.Application processApplication(ApplicationRequest request) throws Exception {
        System.out.println("Processing application for user: " + request.customerUsername + " in ApplicationService");

        String recommendUrl = planRecommenderServiceUrl + "/recommend";

        Map<String, Object> pythonRequestPayload = new HashMap<>();
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", request.customerUsername);
        userDetails.put("age", request.customerAge);
        userDetails.put("income", request.customerIncome);
        pythonRequestPayload.put("userDetails", userDetails);

        RecommendationResponse recommendationResponse = null;
        try {
            recommendationResponse = restTemplate.postForObject(
                    recommendUrl,
                    pythonRequestPayload,
                    RecommendationResponse.class
            );

            System.out.println("Received ranked recommendations from Python service:");
            System.out.println("Best Option: " + recommendationResponse.best_option);
            System.out.println("Better Option: " + recommendationResponse.better_option);
            System.out.println("Good Option: " + recommendationResponse.good_option);
            System.out.println(" AI Message: " + recommendationResponse.message);

        } catch (Exception e) {
            System.err.println("Error calling plan-recommender-service in ApplicationService: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Failed to get plan recommendation from Python service.", e);
        }

        com.smartinsure.model.Application application = new com.smartinsure.model.Application();
        application.setId(UUID.randomUUID().toString()); // Generate a unique ID
        application.setUserId(request.customerUsername); // Using username as userId for now

        // Set the policyType to the AI's best option, as per the new requirement
        application.setPolicyType(recommendationResponse.best_option);

        // Set the full recommendation message for the 'recommendation' field
        String combinedRecommendation = String.format(
                "Best: %s, Better: %s, Good: %s. AI Feedback: %s",
                recommendationResponse.best_option,
                recommendationResponse.better_option,
                recommendationResponse.good_option,
                recommendationResponse.message
        );
        application.setRecommendation(combinedRecommendation);

        // Populate the new individual option fields from the Python response
        application.setBestOption(recommendationResponse.best_option);
        application.setBetterOption(recommendationResponse.better_option);
        application.setGoodOption(recommendationResponse.good_option);

        application.setStatus("PENDING_REVIEW"); // Default status

        return application;
    }
}