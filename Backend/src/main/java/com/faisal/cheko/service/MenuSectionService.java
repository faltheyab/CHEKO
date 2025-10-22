package com.faisal.cheko.service;

import com.faisal.cheko.dto.MenuSectionRequest;
import com.faisal.cheko.dto.MenuSectionResponse;

import java.util.List;

public interface MenuSectionService {
    List<MenuSectionResponse> getAllMenuSections();
    MenuSectionResponse getMenuSectionById(Long id);
    List<MenuSectionResponse> getMenuSectionsByBranchId(Long branchId);
    MenuSectionResponse createMenuSection(MenuSectionRequest menuSectionRequest);
    MenuSectionResponse updateMenuSection(Long id, MenuSectionRequest menuSectionRequest);
    void deleteMenuSection(Long id);
}