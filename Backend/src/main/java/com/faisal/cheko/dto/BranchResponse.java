package com.faisal.cheko.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchResponse {

    private Long id;
    private String branchName;
    private String address;
    private String city;
    private String country;
    
    // Latitude and longitude for the location
    private Double latitude;
    private Double longitude;
    
    private String phone;
    private String email;
    private Map<String, Object> openingHours;
    private Boolean isMainBranch;
    private Boolean isActive;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}