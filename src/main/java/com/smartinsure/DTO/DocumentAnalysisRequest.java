package com.smartinsure.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentAnalysisRequest {
    private String applicationId;
    private String documentContent;
}
