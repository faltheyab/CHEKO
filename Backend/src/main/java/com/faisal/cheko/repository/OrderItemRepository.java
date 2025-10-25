package com.faisal.cheko.repository;

import com.faisal.cheko.model.MenuItem;
import com.faisal.cheko.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Get top ordered menu items across all categories
    @Query("SELECT oi.menuItem, COUNT(oi) as orderCount FROM OrderItem oi " +
           "GROUP BY oi.menuItem " +
           "ORDER BY orderCount DESC " +
           "LIMIT :limit")
    List<Object[]> findTopOrderedItems(@Param("limit") int limit);
    
    // Get top ordered menu items for a specific category
    @Query("SELECT oi.menuItem, COUNT(oi) as orderCount FROM OrderItem oi " +
           "WHERE oi.menuItem.section.id = :sectionId " +
           "GROUP BY oi.menuItem " +
           "ORDER BY orderCount DESC " +
           "LIMIT :limit")
    List<Object[]> findTopOrderedItemsBySection(@Param("sectionId") Long sectionId, @Param("limit") int limit);
}
