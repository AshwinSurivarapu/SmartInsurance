package com.smartinsure.controller;

import com.smartinsure.service.ApplicationService;
import lombok.Data;
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

    // Inject the new ApplicationService
    private final ApplicationService applicationService;

    // Constructor injection for ApplicationService
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // The DTOs can be imported from ApplicationService if you keep them as inner classes there,
    // or moved to a dedicated DTO package and imported from there.
    // If you moved them, remove these static class definitions here.

    // If you keep them as inner classes in ApplicationService, you'll need this import
    // import com.smartinsure.applicationservice.service.ApplicationService.ApplicationRequest;

    @PostMapping // Handles POST requests to /api/applications
    public ResponseEntity<?> createApplication(@RequestBody ApplicationService.ApplicationRequest request) {
        try {
            // Delegate the actual processing to the service layer
            com.smartinsure.model.Application processedApplication = applicationService.processApplication(request);

            // Build the response for the REST client
            Map<String, Object> finalResponse = new HashMap<>();
            finalResponse.put("status", "success");
            finalResponse.put("message", "Application processed successfully for " + request.customerUsername + ".");

            Map<String, Object> aiRecommendations = new HashMap<>();
            // Assuming recommendation in Application object is now a single string from the service
            aiRecommendations.put("fullRecommendation", processedApplication.getRecommendation());
            // If you want individual options, you'd need to parse the string or return a structured object from service
            finalResponse.put("recommendedPlans", aiRecommendations);

            Map<String, Object> appDetails = new HashMap<>();
            appDetails.put("username", processedApplication.getUserId());
            appDetails.put("policyType", processedApplication.getPolicyType());
            appDetails.put("status", processedApplication.getStatus());
            // Add original request details if needed, from 'request' object
            appDetails.put("customerAge", request.customerAge);
            appDetails.put("customerIncome", request.customerIncome);
            finalResponse.put("applicationDetails", appDetails);


            return new ResponseEntity<>(finalResponse, HttpStatus.CREATED);

        } catch (Exception e) {
            System.err.println("Error creating application via REST endpoint: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(
                    Map.of("message", "Application received, but failed to get plan recommendation: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}