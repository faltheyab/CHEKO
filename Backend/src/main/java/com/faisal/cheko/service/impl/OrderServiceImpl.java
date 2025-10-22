package com.faisal.cheko.service.impl;

import com.faisal.cheko.dto.OrderItemRequest;
import com.faisal.cheko.dto.OrderItemResponse;
import com.faisal.cheko.dto.OrderRequest;
import com.faisal.cheko.dto.OrderResponse;
import com.faisal.cheko.exception.ResourceNotFoundException;
import com.faisal.cheko.model.Branch;
import com.faisal.cheko.model.Customer;
import com.faisal.cheko.model.MenuItem;
import com.faisal.cheko.model.Order;
import com.faisal.cheko.model.Status;
import com.faisal.cheko.repository.BranchRepository;
import com.faisal.cheko.repository.CustomerRepository;
import com.faisal.cheko.repository.MenuItemRepository;
import com.faisal.cheko.repository.OrderRepository;
import com.faisal.cheko.repository.StatusRepository;
import com.faisal.cheko.service.OrderItemService;
import com.faisal.cheko.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final BranchRepository branchRepository;
    private final StatusRepository statusRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderItemService orderItemService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                          CustomerRepository customerRepository,
                          BranchRepository branchRepository,
                          StatusRepository statusRepository,
                          MenuItemRepository menuItemRepository,
                          OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.branchRepository = branchRepository;
        this.statusRepository = statusRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderItemService = orderItemService;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("Order", "id", id));
        return mapToResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersByBranchId(Long branchId) {
        return orderRepository.findByBranchId(branchId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersByStatusId(Long statusId) {
        return orderRepository.findByStatusId(statusId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Create the order
        Order order = new Order();
        
        // Set customer if provided
        if (orderRequest.getCustomerId() != null) {
            Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                    .orElseThrow(() -> ResourceNotFoundException.create("Customer", "id", orderRequest.getCustomerId()));
            order.setCustomer(customer);
        }
        
        // Set branch
        Branch branch = branchRepository.findById(orderRequest.getBranchId())
                .orElseThrow(() -> ResourceNotFoundException.create("Branch", "id", orderRequest.getBranchId()));
        order.setBranch(branch);
        
        // Set status if provided
        if (orderRequest.getStatusId() != null) {
            Status status = statusRepository.findById(orderRequest.getStatusId())
                    .orElseThrow(() -> ResourceNotFoundException.create("Status", "id", orderRequest.getStatusId()));
            order.setStatus(status);
        }
        
        // Calculate total price from order items
        BigDecimal totalPrice = calculateTotalPrice(orderRequest.getOrderItems());
        order.setTotalPrice(totalPrice);
        
        // Save the order
        Order savedOrder = orderRepository.save(order);
        
        // Create order items
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
            OrderItemResponse orderItemResponse = orderItemService.createOrderItem(savedOrder.getId(), orderItemRequest);
            orderItemResponses.add(orderItemResponse);
        }
        
        // Create the response
        OrderResponse orderResponse = mapToResponse(savedOrder);
        orderResponse.setOrderItems(orderItemResponses);
        
        return orderResponse;
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("Order", "id", id));
        
        // Update customer if provided
        if (orderRequest.getCustomerId() != null) {
            Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                    .orElseThrow(() -> ResourceNotFoundException.create("Customer", "id", orderRequest.getCustomerId()));
            order.setCustomer(customer);
        }
        
        // Update branch if provided
        if (orderRequest.getBranchId() != null) {
            Branch branch = branchRepository.findById(orderRequest.getBranchId())
                    .orElseThrow(() -> ResourceNotFoundException.create("Branch", "id", orderRequest.getBranchId()));
            order.setBranch(branch);
        }
        
        // Update status if provided
        if (orderRequest.getStatusId() != null) {
            Status status = statusRepository.findById(orderRequest.getStatusId())
                    .orElseThrow(() -> ResourceNotFoundException.create("Status", "id", orderRequest.getStatusId()));
            order.setStatus(status);
        }
        
        // Update order items if provided
        if (orderRequest.getOrderItems() != null && !orderRequest.getOrderItems().isEmpty()) {
            // Delete existing order items
            orderItemService.deleteOrderItemsByOrderId(id);
            
            // Create new order items
            List<OrderItemResponse> orderItemResponses = new ArrayList<>();
            for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
                OrderItemResponse orderItemResponse = orderItemService.createOrderItem(id, orderItemRequest);
                orderItemResponses.add(orderItemResponse);
            }
            
            // Calculate total price from order items
            BigDecimal totalPrice = calculateTotalPrice(orderRequest.getOrderItems());
            order.setTotalPrice(totalPrice);
            
            // Save the order
            Order updatedOrder = orderRepository.save(order);
            
            // Create the response
            OrderResponse orderResponse = mapToResponse(updatedOrder);
            orderResponse.setOrderItems(orderItemResponses);
            
            return orderResponse;
        } else {
            // Save the order without updating order items
            Order updatedOrder = orderRepository.save(order);
            return mapToResponse(updatedOrder);
        }
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, Long statusId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("Order", "id", id));
        
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> ResourceNotFoundException.create("Status", "id", statusId));
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        
        return mapToResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw ResourceNotFoundException.create("Order", "id", id);
        }
        
        // Delete order items first
        orderItemService.deleteOrderItemsByOrderId(id);
        
        // Delete the order
        orderRepository.deleteById(id);
    }


    private BigDecimal calculateTotalPrice(List<OrderItemRequest> orderItemRequests) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        
        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            BigDecimal price;
            
            if (orderItemRequest.getPrice() != null) {
                price = orderItemRequest.getPrice();
            } else {
                MenuItem menuItem = menuItemRepository.findById(orderItemRequest.getMenuItemId())
                        .orElseThrow(() -> ResourceNotFoundException.create("MenuItem", "id", orderItemRequest.getMenuItemId()));
                price = menuItem.getPrice();
            }
            
            BigDecimal itemTotal = price.multiply(new BigDecimal(orderItemRequest.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
        }
        
        return totalPrice;
    }


    private OrderResponse mapToResponse(Order order) {
        OrderResponse.OrderResponseBuilder builder = OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt());
        
        if (order.getCustomer() != null) {
            builder.customerId(order.getCustomer().getId())
                   .customerName(order.getCustomer().getFullName());
        }
        
        if (order.getBranch() != null) {
            builder.branchId(order.getBranch().getId())
                   .branchName(order.getBranch().getBranchName());
        }
        
        if (order.getStatus() != null) {
            builder.statusId(order.getStatus().getId())
                   .statusCode(order.getStatus().getCode())
                   .statusDisplayName(order.getStatus().getDisplayName());
        }
        
        // Get order items
        List<OrderItemResponse> orderItemResponses = orderItemService.getOrderItemsByOrderId(order.getId());
        builder.orderItems(orderItemResponses);
        
        return builder.build();
    }
}