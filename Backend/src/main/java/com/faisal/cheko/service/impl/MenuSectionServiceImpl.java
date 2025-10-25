package com.faisal.cheko.service.impl;

import com.faisal.cheko.dto.MenuSectionRequest;
import com.faisal.cheko.dto.MenuSectionResponse;
import com.faisal.cheko.dto.MenuSectionWithCountResponse;
import com.faisal.cheko.exception.ResourceNotFoundException;
import com.faisal.cheko.model.Branch;
import com.faisal.cheko.model.MenuSection;
import com.faisal.cheko.repository.BranchRepository;
import com.faisal.cheko.repository.MenuSectionRepository;
import com.faisal.cheko.service.MenuSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
    public List<MenuSectionWithCountResponse> getMenuSectionsByBranchIdWithCounts(Long branchId) {
        List<Object[]> sectionsWithCounts = menuSectionRepository.findByBranchIdWithItemCounts(branchId);
        return sectionsWithCounts.stream()
                .map(this::mapToWithCountResponse)
                .collect(Collectors.toList());
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
    
    private MenuSectionWithCountResponse mapToWithCountResponse(Object[] result) {
        MenuSection menuSection = (MenuSection) result[0];
        Long count = (Long) result[1];
        
        return MenuSectionWithCountResponse.builder()
                .id(menuSection.getId())
                .branchId(menuSection.getBranch().getId())
                .branchName(menuSection.getBranch().getBranchName())
                .name(menuSection.getName())
                .description(menuSection.getDescription())
                .itemCount(count)
                .build();
    }
}
