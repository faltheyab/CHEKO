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

    @GetMapping
    @Operation(summary = "Get all menu sections", description = "Returns a list of all menu sections")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu sections",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuSectionResponse.class))
            )
    })
    public ResponseEntity<List<MenuSectionResponse>> getAllMenuSections() {
        List<MenuSectionResponse> menuSections = menuSectionService.getAllMenuSections();
        return ResponseEntity.ok(menuSections);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a menu section by ID with pagination and filtering", description = "Returns a paginated menu section as per the ID and filters")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu section",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Menu section not found")
    })
    public ResponseEntity<PageResponse<MenuSectionResponse>> getMenuSectionById(
            @Parameter(description = "Menu section ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Filter by name", required = false)
            @RequestParam(required = false) String name,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "name")
            @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction", example = "asc")
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<MenuSectionResponse> menuSectionPage = menuSectionService.getMenuSectionByIdPaginated(id, name, pageable);
        PageResponse<MenuSectionResponse> response = PageResponse.from(menuSectionPage);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get menu sections by branch ID", description = "Returns a list of menu sections for a specific branch")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu sections",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuSectionResponse.class))
            )
    })
    public ResponseEntity<List<MenuSectionResponse>> getMenuSectionsByBranchId(
            @Parameter(description = "Branch ID", required = true)
            @PathVariable Long branchId) {
        List<MenuSectionResponse> menuSections = menuSectionService.getMenuSectionsByBranchId(branchId);
        return ResponseEntity.ok(menuSections);
    }
    
    @GetMapping("/with-counts")
    @Operation(summary = "Get all menu sections with item counts", description = "Returns a list of all menu sections with their item counts")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu sections with counts",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuSectionWithCountResponse.class))
            )
    })
    public ResponseEntity<List<MenuSectionWithCountResponse>> getAllMenuSectionsWithCounts() {
        List<MenuSectionWithCountResponse> menuSections = menuSectionService.getAllMenuSectionsWithCounts();
        return ResponseEntity.ok(menuSections);
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

    @PostMapping
    @Operation(summary = "Create a new menu section", description = "Creates a new menu section and returns the created menu section")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Menu section created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MenuSectionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    public ResponseEntity<MenuSectionResponse> createMenuSection(
            @Parameter(description = "Menu section data", required = true)
            @Valid @RequestBody MenuSectionRequest menuSectionRequest) {
        MenuSectionResponse createdMenuSection = menuSectionService.createMenuSection(menuSectionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenuSection);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a menu section", description = "Updates a menu section and returns the updated menu section")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu section updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MenuSectionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Menu section or branch not found")
    })
    public ResponseEntity<MenuSectionResponse> updateMenuSection(
            @Parameter(description = "Menu section ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated menu section data", required = true)
            @Valid @RequestBody MenuSectionRequest menuSectionRequest) {
        MenuSectionResponse updatedMenuSection = menuSectionService.updateMenuSection(id, menuSectionRequest);
        return ResponseEntity.ok(updatedMenuSection);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a menu section", description = "Deletes a menu section")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Menu section deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Menu section not found")
    })
    public ResponseEntity<Void> deleteMenuSection(
            @Parameter(description = "Menu section ID", required = true)
            @PathVariable Long id) {
        menuSectionService.deleteMenuSection(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{sectionId}/menu-items")
    @Operation(summary = "Get menu items by section ID with pagination", description = "Returns a paginated list of menu items for a specific section")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu items",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Section not found")
    })
    public ResponseEntity<PageResponse<MenuItemResponse>> getMenuItemsBySectionIdPaginated(
            @Parameter(description = "Section ID", required = true)
            @PathVariable Long sectionId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "name")
            @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction", example = "asc")
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<MenuItemResponse> menuItemsPage = menuItemService.getMenuItemsBySectionIdPaginated(sectionId, pageable);
        PageResponse<MenuItemResponse> response = PageResponse.from(menuItemsPage);
        
        return ResponseEntity.ok(response);
    }
}
