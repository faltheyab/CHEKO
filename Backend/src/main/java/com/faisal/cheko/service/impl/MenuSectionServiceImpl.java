package com.faisal.cheko.service.impl;

import com.faisal.cheko.dto.MenuSectionRequest;
import com.faisal.cheko.dto.MenuSectionResponse;
import com.faisal.cheko.exception.ResourceNotFoundException;
import com.faisal.cheko.model.Branch;
import com.faisal.cheko.model.MenuSection;
import com.faisal.cheko.repository.BranchRepository;
import com.faisal.cheko.repository.MenuSectionRepository;
import com.faisal.cheko.service.MenuSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class MenuSectionServiceImpl implements MenuSectionService {

    private final MenuSectionRepository menuSectionRepository;
    private final BranchRepository branchRepository;

    @Autowired
    public MenuSectionServiceImpl(MenuSectionRepository menuSectionRepository, BranchRepository branchRepository) {
        this.menuSectionRepository = menuSectionRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public List<MenuSectionResponse> getAllMenuSections() {
        return menuSectionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MenuSectionResponse getMenuSectionById(Long id) {
        MenuSection menuSection = menuSectionRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("MenuSection", "id", id));
        return mapToResponse(menuSection);
    }

    @Override
    public List<MenuSectionResponse> getMenuSectionsByBranchId(Long branchId) {
        return menuSectionRepository.findByBranchId(branchId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MenuSectionResponse createMenuSection(MenuSectionRequest menuSectionRequest) {
        MenuSection menuSection = mapToEntity(menuSectionRequest);
        MenuSection savedMenuSection = menuSectionRepository.save(menuSection);
        return mapToResponse(savedMenuSection);
    }

    @Override
    @Transactional
    public MenuSectionResponse updateMenuSection(Long id, MenuSectionRequest menuSectionRequest) {
        MenuSection menuSection = menuSectionRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("MenuSection", "id", id));
        
        updateMenuSectionFromRequest(menuSection, menuSectionRequest);
        MenuSection updatedMenuSection = menuSectionRepository.save(menuSection);
        return mapToResponse(updatedMenuSection);
    }

    @Override
    @Transactional
    public void deleteMenuSection(Long id) {
        if (!menuSectionRepository.existsById(id)) {
            throw ResourceNotFoundException.create("MenuSection", "id", id);
        }
        menuSectionRepository.deleteById(id);
    }


    private MenuSectionResponse mapToResponse(MenuSection menuSection) {
        return MenuSectionResponse.builder()
                .id(menuSection.getId())
                .branchId(menuSection.getBranch().getId())
                .branchName(menuSection.getBranch().getBranchName())
                .name(menuSection.getName())
                .description(menuSection.getDescription())
                .build();
    }


    private MenuSection mapToEntity(MenuSectionRequest menuSectionRequest) {
        MenuSection menuSection = new MenuSection();
        updateMenuSectionFromRequest(menuSection, menuSectionRequest);
        return menuSection;
    }


    private void updateMenuSectionFromRequest(MenuSection menuSection, MenuSectionRequest menuSectionRequest) {
        Branch branch = branchRepository.findById(menuSectionRequest.getBranchId())
                .orElseThrow(() -> ResourceNotFoundException.create("Branch", "id", menuSectionRequest.getBranchId()));
        
        menuSection.setBranch(branch);
        menuSection.setName(menuSectionRequest.getName());
        menuSection.setDescription(menuSectionRequest.getDescription());
    }
}
