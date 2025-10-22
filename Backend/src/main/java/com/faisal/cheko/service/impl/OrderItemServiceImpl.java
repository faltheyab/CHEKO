package com.faisal.cheko.service.impl;

import com.faisal.cheko.dto.OrderItemRequest;
import com.faisal.cheko.dto.OrderItemResponse;
import com.faisal.cheko.exception.ResourceNotFoundException;
import com.faisal.cheko.model.MenuItem;
import com.faisal.cheko.model.Order;
import com.faisal.cheko.model.OrderItem;
import com.faisal.cheko.repository.MenuItemRepository;
import com.faisal.cheko.repository.OrderItemRepository;
import com.faisal.cheko.repository.OrderRepository;
import com.faisal.cheko.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, 
                              OrderRepository orderRepository,
                              MenuItemRepository menuItemRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public List<OrderItemResponse> getAllOrderItems() {
        return orderItemRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemResponse getOrderItemById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("OrderItem", "id", id));
        return mapToResponse(orderItem);
    }

    @Override
    public List<OrderItemResponse> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderItemResponse createOrderItem(Long orderId, OrderItemRequest orderItemRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> ResourceNotFoundException.create("Order", "id", orderId));
        
        MenuItem menuItem = menuItemRepository.findById(orderItemRequest.getMenuItemId())
                .orElseThrow(() -> ResourceNotFoundException.create("MenuItem", "id", orderItemRequest.getMenuItemId()));
        
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(orderItemRequest.getQuantity());
        
        // Use the price from the request if provided, otherwise use the menu item price
        BigDecimal price = orderItemRequest.getPrice() != null ? 
                orderItemRequest.getPrice() : menuItem.getPrice();
        orderItem.setPrice(price);
        
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return mapToResponse(savedOrderItem);
    }

    @Override
    @Transactional
    public OrderItemResponse updateOrderItem(Long id, OrderItemRequest orderItemRequest) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("OrderItem", "id", id));
        
        if (orderItemRequest.getMenuItemId() != null) {
            MenuItem menuItem = menuItemRepository.findById(orderItemRequest.getMenuItemId())
                    .orElseThrow(() -> ResourceNotFoundException.create("MenuItem", "id", orderItemRequest.getMenuItemId()));
            orderItem.setMenuItem(menuItem);
        }
        
        if (orderItemRequest.getQuantity() != null) {
            orderItem.setQuantity(orderItemRequest.getQuantity());
        }
        
        if (orderItemRequest.getPrice() != null) {
            orderItem.setPrice(orderItemRequest.getPrice());
        }
        
        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
        return mapToResponse(updatedOrderItem);
    }

    @Override
    @Transactional
    public void deleteOrderItem(Long id) {
        if (!orderItemRepository.existsById(id)) {
            throw ResourceNotFoundException.create("OrderItem", "id", id);
        }
        orderItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteOrderItemsByOrderId(Long orderId) {
        orderItemRepository.deleteByOrderId(orderId);
    }

    @Override
    public OrderItemResponse mapToResponse(OrderItem orderItem) {
        BigDecimal subtotal = orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()));
        
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrder().getId())
                .menuItemId(orderItem.getMenuItem().getId())
                .menuItemName(orderItem.getMenuItem().getName())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .subtotal(subtotal)
                .build();
    }
}