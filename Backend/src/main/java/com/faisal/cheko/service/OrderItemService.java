package com.faisal.cheko.service;

import com.faisal.cheko.dto.OrderItemRequest;
import com.faisal.cheko.dto.OrderItemResponse;
import com.faisal.cheko.model.OrderItem;

import java.util.List;


public interface OrderItemService {

    List<OrderItemResponse> getAllOrderItems();
    OrderItemResponse getOrderItemById(Long id);
    List<OrderItemResponse> getOrderItemsByOrderId(Long orderId);
    OrderItemResponse createOrderItem(Long orderId, OrderItemRequest orderItemRequest);
    OrderItemResponse updateOrderItem(Long id, OrderItemRequest orderItemRequest);
    void deleteOrderItem(Long id);
    void deleteOrderItemsByOrderId(Long orderId);
    OrderItemResponse mapToResponse(OrderItem orderItem);
}