package com.faisal.cheko.service;

import com.faisal.cheko.dto.OrderRequest;
import com.faisal.cheko.dto.OrderResponse;

import java.util.List;


public interface OrderService {

    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getOrdersByCustomerId(Long customerId);
    List<OrderResponse> getOrdersByBranchId(Long branchId);
    List<OrderResponse> getOrdersByStatusId(Long statusId);
    OrderResponse createOrder(OrderRequest orderRequest);
    OrderResponse updateOrder(Long id, OrderRequest orderRequest);
    OrderResponse updateOrderStatus(Long id, Long statusId);
    void deleteOrder(Long id);
}