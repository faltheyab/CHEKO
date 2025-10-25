package com.faisal.cheko.service;

import com.faisal.cheko.dto.MenuItemRequest;
import com.faisal.cheko.dto.MenuItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface MenuItemService {

    Page<MenuItemResponse> getMenuItemsBySectionIdPaginated(Long sectionId, Pageable pageable);
    Page<MenuItemResponse> getMenuItemsByBranchIdPaginated(Long branchId, Pageable pageable);
    Page<MenuItemResponse> getAvailableMenuItemsByBranchIdPaginated(Long branchId, String nameQuery, Pageable pageable);
    Page<MenuItemResponse> getAvailableMenuItemsBySectionIdPaginated(Long sectionId, String nameQuery, Pageable pageable);
    List<MenuItemResponse> getSecondHighestCalorieMealPerCategory();
}