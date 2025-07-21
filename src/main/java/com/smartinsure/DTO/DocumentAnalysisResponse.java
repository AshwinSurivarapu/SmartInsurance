package com.smartinsure.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentAnalysisResponse {
    private String analysisId;
    private String status;
    private String summary;
}
