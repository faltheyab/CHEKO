package com.faisal.cheko.repository;

import com.faisal.cheko.model.MenuItem;
import com.faisal.cheko.model.MenuSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findBySection(MenuSection section);
    List<MenuItem> findBySectionId(Long sectionId);
    Optional<MenuItem> findByNameAndSection(String name, MenuSection section);
    List<MenuItem> findByIsAvailableTrue();
    List<MenuItem> findByPriceLessThanEqual(BigDecimal price);
    List<MenuItem> findByPriceGreaterThanEqual(BigDecimal price);
    List<MenuItem> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    @Query("SELECT mi FROM MenuItem mi JOIN mi.section ms WHERE ms.branch.id = :branchId")
    List<MenuItem> findByBranchId(@Param("branchId") Long branchId);
    @Query("SELECT mi FROM MenuItem mi JOIN mi.section ms WHERE ms.branch.id = :branchId AND mi.isAvailable = true")
    List<MenuItem> findAvailableByBranchId(@Param("branchId") Long branchId);
    Page<MenuItem> findAll(Pageable pageable);
    Page<MenuItem> findBySectionId(Long sectionId, Pageable pageable);
    @Query("SELECT mi FROM MenuItem mi JOIN mi.section ms WHERE ms.branch.id = :branchId")
    Page<MenuItem> findByBranchId(@Param("branchId") Long branchId, Pageable pageable);
    @Query("SELECT mi FROM MenuItem mi JOIN mi.section ms WHERE ms.branch.id = :branchId AND mi.isAvailable = true")
    Page<MenuItem> findAvailableByBranchId(@Param("branchId") Long branchId, Pageable pageable);
    
    @Query("SELECT mi FROM MenuItem mi JOIN mi.section ms WHERE ms.branch.id = :branchId AND mi.isAvailable = true AND LOWER(mi.name) LIKE LOWER(CONCAT('%', :nameQuery, '%'))")
    Page<MenuItem> findAvailableByBranchIdAndNameContainingIgnoreCase(@Param("branchId") Long branchId, @Param("nameQuery") String nameQuery, Pageable pageable);
    
    @Query("SELECT mi FROM MenuItem mi WHERE mi.section.id = :sectionId AND mi.isAvailable = true AND (:nameQuery IS NULL OR :nameQuery = '' OR LOWER(mi.name) LIKE LOWER(CONCAT('%', :nameQuery, '%')))")
    Page<MenuItem> findAvailableBySectionIdWithSearch(@Param("sectionId") Long sectionId, @Param("nameQuery") String nameQuery, Pageable pageable);
    
    @Query("SELECT mi FROM MenuItem mi JOIN mi.section ms WHERE ms.branch.id = :branchId AND ms.id = :sectionId")
    Page<MenuItem> findByBranchIdAndSectionId(@Param("branchId") Long branchId, @Param("sectionId") Long sectionId, Pageable pageable);
    
    @Query("SELECT mi FROM MenuItem mi JOIN mi.section ms WHERE ms.branch.id = :branchId AND ms.id = :sectionId AND mi.isAvailable = true AND (:nameQuery IS NULL OR :nameQuery = '' OR LOWER(mi.name) LIKE LOWER(CONCAT('%', :nameQuery, '%')))")
    Page<MenuItem> findAvailableByBranchIdAndSectionIdWithSearch(@Param("branchId") Long branchId, @Param("sectionId") Long sectionId, @Param("nameQuery") String nameQuery, Pageable pageable);

    /*

SELECT *
FROM menu_items mi
join menu_sections ms
ON mi.section_id = ms.id
WHERE mi.calories = (
	SELECT MAX(mi2.calories)
	FROM menu_items mi2
	WHERE mi2.section_id = mi.section_id AND mi2.calories < (
		SELECT MAX(mi3.calories) FROM menu_items mi3 WHERE mi3.section_id = mi.section_id
	)
)

     */
    @Query(value =
           "SELECT mi.* FROM restaurant.menu_items mi " +
           "JOIN restaurant.menu_sections ms ON mi.section_id = ms.id " +
           "WHERE mi.calories = (" +
           "    SELECT MAX(mi2.calories) FROM restaurant.menu_items mi2 " +
           "    WHERE mi2.section_id = mi.section_id " +
           "    AND mi2.calories < (" +
           "        SELECT MAX(mi3.calories) FROM restaurant.menu_items mi3 " +
           "        WHERE mi3.section_id = mi.section_id" +
           "    )" +
           ")",
           nativeQuery = true)
    List<MenuItem> findSecondHighestCalorieMealPerCategory();
}