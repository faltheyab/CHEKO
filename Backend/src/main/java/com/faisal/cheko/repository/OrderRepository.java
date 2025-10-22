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

    List<Order> findByCustomer(Customer customer);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByBranch(Branch branch);
    List<Order> findByBranchId(Long branchId);
    List<Order> findByStatus(Status status);
    List<Order> findByStatusId(Long statusId);
    List<Order> findByCreatedAtAfter(ZonedDateTime createdAt);
    List<Order> findByCreatedAtBefore(ZonedDateTime createdAt);
    List<Order> findByCreatedAtBetween(ZonedDateTime startDate, ZonedDateTime endDate);
    List<Order> findByTotalPriceGreaterThanEqual(BigDecimal totalPrice);
    List<Order> findByTotalPriceLessThanEqual(BigDecimal totalPrice);
    List<Order> findByTotalPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    @Query("SELECT COUNT(o) FROM Order o WHERE o.branch.id = :branchId")
    Long countByBranchId(@Param("branchId") Long branchId);
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.branch.id = :branchId")
    BigDecimal getTotalRevenueByBranchId(@Param("branchId") Long branchId);
}