package com.faisal.cheko.service;

import com.faisal.cheko.dto.MenuSectionWithCountResponse;

import java.util.List;

public interface MenuSectionService {
    List<MenuSectionWithCountResponse> getMenuSectionsByBranchIdWithCounts(Long branchId);
}