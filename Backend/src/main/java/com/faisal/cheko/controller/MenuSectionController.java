package com.faisal.cheko.controller;

import com.faisal.cheko.dto.MenuItemResponse;
import com.faisal.cheko.dto.MenuSectionRequest;
import com.faisal.cheko.dto.MenuSectionResponse;
import com.faisal.cheko.dto.MenuSectionWithCountResponse;
import com.faisal.cheko.dto.PageResponse;
import com.faisal.cheko.service.MenuItemService;
import com.faisal.cheko.service.MenuSectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/menu-sections")
@Tag(name = "Menu Section", description = "Menu section APIs")
public class MenuSectionController {

    private final MenuSectionService menuSectionService;
    private final MenuItemService menuItemService;

    @Autowired
    public MenuSectionController(MenuSectionService menuSectionService, MenuItemService menuItemService) {
        this.menuSectionService = menuSectionService;
        this.menuItemService = menuItemService;
    }

    @GetMapping("/branch/{branchId}/with-counts")
    @Operation(summary = "Get menu sections by branch ID with item counts", description = "Returns a list of menu sections with their item counts for a specific branch")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu sections with counts",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuSectionWithCountResponse.class))
            )
    })
    public ResponseEntity<List<MenuSectionWithCountResponse>> getMenuSectionsByBranchIdWithCounts(
            @Parameter(description = "Branch ID", required = true)
            @PathVariable Long branchId) {
        List<MenuSectionWithCountResponse> menuSections = menuSectionService.getMenuSectionsByBranchIdWithCounts(branchId);
        return ResponseEntity.ok(menuSections);
    }

}
