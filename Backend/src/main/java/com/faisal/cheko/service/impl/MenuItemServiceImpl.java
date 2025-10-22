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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public List<MenuItemResponse> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MenuItemResponse getMenuItemById(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("MenuItem", "id", id));
        return mapToResponse(menuItem);
    }

    @Override
    public List<MenuItemResponse> getMenuItemsBySectionId(Long sectionId) {
        return menuItemRepository.findBySectionId(sectionId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemResponse> getMenuItemsByBranchId(Long branchId) {
        return menuItemRepository.findByBranchId(branchId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemResponse> getAvailableMenuItemsByBranchId(Long branchId) {
        return menuItemRepository.findAvailableByBranchId(branchId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemResponse> getMenuItemsByMaxPrice(BigDecimal price) {
        return menuItemRepository.findByPriceLessThanEqual(price).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemResponse> getMenuItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return menuItemRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MenuItemResponse createMenuItem(MenuItemRequest menuItemRequest) {
        MenuItem menuItem = mapToEntity(menuItemRequest);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return mapToResponse(savedMenuItem);
    }

    @Override
    @Transactional
    public MenuItemResponse updateMenuItem(Long id, MenuItemRequest menuItemRequest) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("MenuItem", "id", id));
        
        updateMenuItemFromRequest(menuItem, menuItemRequest);
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return mapToResponse(updatedMenuItem);
    }

    @Override
    @Transactional
    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw ResourceNotFoundException.create("MenuItem", "id", id);
        }
        menuItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public MenuItemResponse updateMenuItemAvailability(Long id, Boolean isAvailable) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("MenuItem", "id", id));
        
        menuItem.setIsAvailable(isAvailable);
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return mapToResponse(updatedMenuItem);
    }
    
    @Override
    public Page<MenuItemResponse> getAllMenuItemsPaginated(Pageable pageable) {
        return menuItemRepository.findAll(pageable)
                .map(this::mapToResponse);
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
    public Page<MenuItemResponse> getMenuItemsByBranchIdPaginated(Long branchId, Pageable pageable) {
        if (!branchRepository.existsById(branchId)) {
            throw ResourceNotFoundException.create("Branch", "id", branchId);
        }
        
        return menuItemRepository.findByBranchId(branchId, pageable)
                .map(this::mapToResponse);
    }
    
    @Override
    public Page<MenuItemResponse> getAvailableMenuItemsByBranchIdPaginated(Long branchId, Pageable pageable) {
        if (!branchRepository.existsById(branchId)) {
            throw ResourceNotFoundException.create("Branch", "id", branchId);
        }
        
        return menuItemRepository.findAvailableByBranchId(branchId, pageable)
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
