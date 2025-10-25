package com.faisal.cheko.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemResponse {

    private Long id;
    private Long sectionId;
    private String sectionName;
    private Long branchId;
    private String branchName;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer calories;
    private String imageUrl;
    private Boolean isAvailable;
}