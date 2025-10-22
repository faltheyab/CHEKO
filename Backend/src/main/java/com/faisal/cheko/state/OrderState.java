package com.faisal.cheko.state;

import com.faisal.cheko.model.Order;

public interface OrderState {
    
    Order process(Order order);
    
    void next(OrderStateContext context, Order order);
    
    void cancel(OrderStateContext context, Order order);
    
    String getStatusCode();
}