package com.faisal.cheko.state;

import com.faisal.cheko.model.Order;
import com.faisal.cheko.model.Status;
import com.faisal.cheko.repository.StatusRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class OrderStateContext {
    
    @Autowired
    private StatusRepository statusRepository;
    
    @Autowired
    private PendingOrderState pendingOrderState;
    
    @Getter
    private OrderState currentState;
    
    @PostConstruct
    public void init() {
        // Initialize with pending state
        this.currentState = pendingOrderState;
    }
    
    public void setState(OrderState state) {
        this.currentState = state;
    }
    
    public Order process(Order order) {
        return currentState.process(order);
    }
    
    public void next(Order order) {
        currentState.next(this, order);
    }
    
    public void cancel(Order order) {
        currentState.cancel(this, order);
    }
    
    public void updateOrderStatus(Order order, String statusCode) {
        Status status = statusRepository.findByCode(statusCode)
                .orElseThrow(() -> new RuntimeException("Status not found: " + statusCode));
        order.setStatus(status);
    }
}
