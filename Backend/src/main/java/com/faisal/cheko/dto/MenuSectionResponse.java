package com.faisal.cheko.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuSectionResponse {
    private Long id;
    private Long branchId;
    private String branchName;
    private String name;
    private String description;
}
