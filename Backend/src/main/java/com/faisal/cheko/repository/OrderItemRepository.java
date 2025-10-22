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

    List<OrderItem> findByOrder(Order order);
    List<OrderItem> findByOrderId(Long orderId);
    List<OrderItem> findByMenuItem(MenuItem menuItem);
    List<OrderItem> findByMenuItemId(Long menuItemId);
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.menuItem.id = :menuItemId")
    Integer getTotalQuantityByMenuItemId(@Param("menuItemId") Long menuItemId);
    @Query("SELECT oi.menuItem, SUM(oi.quantity) as totalQuantity " +
           "FROM OrderItem oi " +
           "GROUP BY oi.menuItem " +
           "ORDER BY totalQuantity DESC")
    List<Object[]> findMostOrderedMenuItems(@Param("limit") int limit);
    void deleteByOrderId(Long orderId);
}
