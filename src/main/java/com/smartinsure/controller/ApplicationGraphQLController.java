package com.smartinsure.controller;

import com.smartinsure.model.Application;

import com.smartinsure.service.ApplicationService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ApplicationGraphQLController {

    private final ApplicationService applicationService;

    public ApplicationGraphQLController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @MutationMapping
    public Application createApplication(@Argument ApplicationInput input) {
        // Convert GraphQL input to the ApplicationService's internal ApplicationRequest DTO
        ApplicationService.ApplicationRequest serviceRequest = new ApplicationService.ApplicationRequest();

        // Map fields from GraphQL input to the service's request DTO
        serviceRequest.setCustomerUsername(input.getUserId());
        serviceRequest.setCustomerAge(input.getCustomerAge());
        serviceRequest.setCustomerIncome(input.getCustomerIncome());
        // Removed: serviceRequest.setPolicyType(input.getPolicyType()); // No longer part of input

        try {
            // Delegate the actual processing to the service layer's processApplication method
            Application processedApplication = applicationService.processApplication(serviceRequest);

            // In GraphQL, the return type is directly mapped, so we just return the processed Application object.
            return processedApplication;
        } catch (Exception e) {
            System.err.println("Error processing application via GraphQL: " + e.getMessage());
            e.printStackTrace();
            // In GraphQL, you typically throw an exception that a GraphQL exception handler would catch
            throw new RuntimeException("Failed to create application: " + e.getMessage(), e);
        }
    }
}