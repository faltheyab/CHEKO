package com.faisal.cheko.repository;

import com.faisal.cheko.model.Branch;
import com.faisal.cheko.model.Customer;
import com.faisal.cheko.model.Order;
import com.faisal.cheko.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}