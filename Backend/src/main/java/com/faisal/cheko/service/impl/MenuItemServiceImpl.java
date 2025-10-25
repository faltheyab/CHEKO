package com.faisal.cheko.service.impl;

import com.faisal.cheko.dto.MenuItemRequest;
import com.faisal.cheko.dto.MenuItemResponse;
import com.faisal.cheko.exception.ResourceNotFoundException;
import com.faisal.cheko.model.MenuItem;
import com.faisal.cheko.model.MenuSection;
import com.faisal.cheko.repository.BranchRepository;
import com.faisal.cheko.repository.MenuItemRepository;
import com.faisal.cheko.repository.MenuSectionRepository;
import com.faisal.cheko.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuSectionRepository menuSectionRepository;
    private final BranchRepository branchRepository;

    @Autowired
    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, 
                              MenuSectionRepository menuSectionRepository,
                              BranchRepository branchRepository) {
        this.menuItemRepository = menuItemRepository;
        this.menuSectionRepository = menuSectionRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public Page<MenuItemResponse> getMenuItemsBySectionIdPaginated(Long sectionId, Pageable pageable) {
        if (!menuSectionRepository.existsById(sectionId)) {
            throw ResourceNotFoundException.create("MenuSection", "id", sectionId);
        }
        
        return menuItemRepository.findBySectionId(sectionId, pageable)
                .map(this::mapToResponse);
    }
    
    @Override
    public Page<MenuItemResponse> getMenuItemsByBranchIdAndBySectionIdPaginated(Long branchId, Long sectionId, Pageable pageable) {
        if (!branchRepository.existsById(branchId)) {
            throw ResourceNotFoundException.create("Branch", "id", branchId);
        }
        
        if (!menuSectionRepository.existsById(sectionId)) {
            throw ResourceNotFoundException.create("MenuSection", "id", sectionId);
        }
        
        return menuItemRepository.findByBranchIdAndSectionId(branchId, sectionId, pageable)
                .map(this::mapToResponse);
    }
    
    @Override
    public Page<MenuItemResponse> getMenuItemsByBranchIdPaginated(Long branchId, Pageable pageable) {
        if (!branchRepository.existsById(branchId)) {
            throw ResourceNotFoundException.create("Branch", "id", branchId);
        }
        
        return menuItemRepository.findByBranchId(branchId, pageable)
                .map(this::mapToResponse);
    }
    
    @Override
    public Page<MenuItemResponse> getAvailableMenuItemsByBranchIdPaginated(Long branchId, String nameQuery, Pageable pageable) {
        if (!branchRepository.existsById(branchId)) {
            throw ResourceNotFoundException.create("Branch", "id", branchId);
        }
        
        if (nameQuery != null && !nameQuery.isEmpty()) {
            // Search by name using LIKE %nameQuery%
            return menuItemRepository.findAvailableByBranchIdAndNameContainingIgnoreCase(branchId, nameQuery, pageable)
                    .map(this::mapToResponse);
        } else {
            // If no search query, return all available items
            return menuItemRepository.findAvailableByBranchId(branchId, pageable)
                    .map(this::mapToResponse);
        }
    }
    
    @Override
    public Page<MenuItemResponse> getAvailableMenuItemsByBranchIdAndBySectionIdPaginated(Long branchId, Long sectionId, String nameQuery, Pageable pageable) {
        if (!branchRepository.existsById(branchId)) {
            throw ResourceNotFoundException.create("Branch", "id", branchId);
        }
        
        if (!menuSectionRepository.existsById(sectionId)) {
            throw ResourceNotFoundException.create("MenuSection", "id", sectionId);
        }
        
        return menuItemRepository.findAvailableByBranchIdAndSectionIdWithSearch(branchId, sectionId, nameQuery, pageable)
                .map(this::mapToResponse);
    }


    @Override
    public List<MenuItemResponse> getSecondHighestCalorieMealPerCategory() {
        return menuItemRepository.findSecondHighestCalorieMealPerCategory().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MenuItemResponse mapToResponse(MenuItem menuItem) {
        return MenuItemResponse.builder()
                .id(menuItem.getId())
                .sectionId(menuItem.getSection().getId())
                .sectionName(menuItem.getSection().getName())
                .branchId(menuItem.getSection().getBranch().getId())
                .branchName(menuItem.getSection().getBranch().getBranchName())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .calories(menuItem.getCalories())
                .imageUrl(menuItem.getImageUrl())
                .isAvailable(menuItem.getIsAvailable())
                .build();
    }

    private MenuItem mapToEntity(MenuItemRequest menuItemRequest) {
        MenuItem menuItem = new MenuItem();
        updateMenuItemFromRequest(menuItem, menuItemRequest);
        return menuItem;
    }

    private void updateMenuItemFromRequest(MenuItem menuItem, MenuItemRequest menuItemRequest) {
        MenuSection section = menuSectionRepository.findById(menuItemRequest.getSectionId())
                .orElseThrow(() -> ResourceNotFoundException.create("MenuSection", "id", menuItemRequest.getSectionId()));
        
        menuItem.setSection(section);
        menuItem.setName(menuItemRequest.getName());
        menuItem.setDescription(menuItemRequest.getDescription());
        menuItem.setPrice(menuItemRequest.getPrice());
        menuItem.setCalories(menuItemRequest.getCalories());
        menuItem.setImageUrl(menuItemRequest.getImageUrl());
        
        if (menuItemRequest.getIsAvailable() != null) {
            menuItem.setIsAvailable(menuItemRequest.getIsAvailable());
        }
    }
}
