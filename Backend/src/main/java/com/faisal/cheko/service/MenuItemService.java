package com.faisal.cheko.service;

import com.faisal.cheko.dto.MenuItemRequest;
import com.faisal.cheko.dto.MenuItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface MenuItemService {

    List<MenuItemResponse> getAllMenuItems();
    MenuItemResponse getMenuItemById(Long id);
    List<MenuItemResponse> getMenuItemsBySectionId(Long sectionId);
    List<MenuItemResponse> getMenuItemsByBranchId(Long branchId);
    List<MenuItemResponse> getAvailableMenuItemsByBranchId(Long branchId);
    List<MenuItemResponse> getMenuItemsByMaxPrice(BigDecimal price);
    List<MenuItemResponse> getMenuItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    MenuItemResponse createMenuItem(MenuItemRequest menuItemRequest);
    MenuItemResponse updateMenuItem(Long id, MenuItemRequest menuItemRequest);
    void deleteMenuItem(Long id);
    MenuItemResponse updateMenuItemAvailability(Long id, Boolean isAvailable);
    Page<MenuItemResponse> getAllMenuItemsPaginated(Pageable pageable);
    Page<MenuItemResponse> getMenuItemsBySectionIdPaginated(Long sectionId, Pageable pageable);
    Page<MenuItemResponse> getMenuItemsByBranchIdPaginated(Long branchId, Pageable pageable);
    Page<MenuItemResponse> getAvailableMenuItemsByBranchIdPaginated(Long branchId, String nameQuery, Pageable pageable);
    Page<MenuItemResponse> getAvailableMenuItemsBySectionIdPaginated(Long sectionId, String nameQuery, Pageable pageable);
    List<MenuItemResponse> getSecondHighestCalorieMealPerCategory();
}