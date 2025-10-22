package com.faisal.cheko.state;

import com.faisal.cheko.model.Order;
import org.springframework.stereotype.Component;

@Component
public class PendingOrderState implements OrderState {
    
    private static final String STATUS_CODE = "PENDING";
    private static final String NEXT_STATUS_CODE = "PREPARING";
    private static final String CANCELLED_STATUS_CODE = "CANCELLED";
    
    @Override
    public Order process(Order order) {
        // Processing logic for pending orders
        return order;
    }
    
    @Override
    public void next(OrderStateContext context, Order order) {
        // Transition to the next state (PREPARING)
        context.updateOrderStatus(order, NEXT_STATUS_CODE);
        // In a complete implementation, we would set the next state here
        // context.setState(preparingOrderState);
    }
    
    @Override
    public void cancel(OrderStateContext context, Order order) {
        // Cancel the order
        context.updateOrderStatus(order, CANCELLED_STATUS_CODE);
        // In a complete implementation, we would set the cancelled state here
        // context.setState(cancelledOrderState);
    }
    
    @Override
    public String getStatusCode() {
        return STATUS_CODE;
    }
}
