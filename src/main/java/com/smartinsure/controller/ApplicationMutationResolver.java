package com.smartinsure.controller;// smartinsure-platform/services/java/application-service/src/main/java/com/smartinsure/graphql/ApplicationMutationResolver.java
import com.smartinsure.DTO.DocumentAnalysisRequest;
import com.smartinsure.DTO.DocumentAnalysisResponse;
import com.smartinsure.model.Application;
import com.smartinsure.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Controller
public class ApplicationMutationResolver {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationMutationResolver.class);

    @Autowired
    private RestTemplate restTemplate; // Inject RestTemplate

    @Autowired
    private ApplicationService applicationService; // Inject your ApplicationService

    private final String DOCUMENT_ANALYSIS_SERVICE_URL = "http://localhost:5002/analyze"; // URL for the new service

    @MutationMapping
    public Application createApplication(@Argument ApplicationInput input) { // Argument name 'input' matches schema
        logger.info("Received request to create application for user: {}", input.getUserId());

        // --- Call ApplicationService to process the application and get recommendations ---
        Application processedApplication;
        try {
            // Map GraphQL input to ApplicationService's internal ApplicationRequest DTO
            ApplicationService.ApplicationRequest serviceRequest = new ApplicationService.ApplicationRequest();
            serviceRequest.setCustomerUsername(input.getUserId());
            serviceRequest.setCustomerAge(input.getCustomerAge());
            serviceRequest.setCustomerIncome(input.getCustomerIncome());

            processedApplication = applicationService.processApplication(serviceRequest);
            logger.info("Application processed by service, ID: {}", processedApplication.getId());

        } catch (Exception e) {
            logger.error("Error processing application with ApplicationService: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process application: " + e.getMessage(), e);
        }

        // --- New logic: Call Document Analysis Service (placeholder) ---
        // This part comes AFTER the application is "processed" and has an ID
        try {
            // Prepare the request to the document analysis service
            DocumentAnalysisRequest analysisRequest = new DocumentAnalysisRequest(
                    processedApplication.getId(), // Use the ID from the processed application
                    "dummy document content for user " + processedApplication.getUserId() // Placeholder content
            );

            logger.info("Calling document-analysis-service at {}", DOCUMENT_ANALYSIS_SERVICE_URL);

            // Make the POST request
            ResponseEntity<DocumentAnalysisResponse> responseEntity = restTemplate.postForEntity(
                    DOCUMENT_ANALYSIS_SERVICE_URL,
                    analysisRequest,
                    DocumentAnalysisResponse.class
            );

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                DocumentAnalysisResponse analysisResponse = responseEntity.getBody();
                if (analysisResponse != null) {
                    logger.info("Document analysis service responded: Status={}, Summary={}",
                            analysisResponse.getStatus(), analysisResponse.getSummary());
                    // Update application status based on analysis response
                    processedApplication.setStatus("ANALYSIS_COMPLETED: " + analysisResponse.getStatus());
                    // You might want to store analysisId or summary in Application model too
                } else {
                    logger.warn("Document analysis service returned empty body for application: {}", processedApplication.getId());
                    processedApplication.setStatus("ANALYSIS_FAILED_NO_RESPONSE");
                }
            } else {
                logger.error("Failed to call document analysis service for application {}. Status code: {}",
                        processedApplication.getId(), responseEntity.getStatusCode());
                processedApplication.setStatus("ANALYSIS_FAILED_HTTP_ERROR");
            }

        } catch (Exception e) {
            logger.error("Error calling document analysis service for application {}: {}",
                    processedApplication.getId(), e.getMessage(), e);
            processedApplication.setStatus("ANALYSIS_FAILED_EXCEPTION");
        }

        return processedApplication; // Return the updated application object
    }
}