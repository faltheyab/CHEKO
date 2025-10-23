package com.faisal.cheko.service;

import com.faisal.cheko.dto.MenuSectionRequest;
import com.faisal.cheko.dto.MenuSectionResponse;
import com.faisal.cheko.dto.MenuSectionWithCountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuSectionService {
    List<MenuSectionResponse> getAllMenuSections();
    MenuSectionResponse getMenuSectionById(Long id);
    Page<MenuSectionResponse> getMenuSectionByIdPaginated(Long id, String name, Pageable pageable);
    List<MenuSectionResponse> getMenuSectionsByBranchId(Long branchId);
    MenuSectionResponse createMenuSection(MenuSectionRequest menuSectionRequest);
    MenuSectionResponse updateMenuSection(Long id, MenuSectionRequest menuSectionRequest);
    void deleteMenuSection(Long id);
    List<MenuSectionWithCountResponse> getAllMenuSectionsWithCounts();
    List<MenuSectionWithCountResponse> getMenuSectionsByBranchIdWithCounts(Long branchId);
}