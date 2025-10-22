package com.faisal.cheko.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Data Transfer Object for order responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private Long branchId;
    private String branchName;
    private Long statusId;
    private String statusCode;
    private String statusDisplayName;
    private BigDecimal totalPrice;
    private ZonedDateTime createdAt;
    private List<OrderItemResponse> orderItems;
}