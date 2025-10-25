package com.faisal.cheko.repository;

import com.faisal.cheko.model.MenuItem;
import com.faisal.cheko.model.Order;
import com.faisal.cheko.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
