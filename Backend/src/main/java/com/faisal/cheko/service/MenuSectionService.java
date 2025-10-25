package com.faisal.cheko.service;

import com.faisal.cheko.dto.MenuSectionRequest;
import com.faisal.cheko.dto.MenuSectionResponse;
import com.faisal.cheko.dto.MenuSectionWithCountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuSectionService {
    List<MenuSectionWithCountResponse> getMenuSectionsByBranchIdWithCounts(Long branchId);
}