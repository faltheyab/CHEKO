package com.faisal.cheko.controller;

import com.faisal.cheko.dto.MenuItemResponse;
import com.faisal.cheko.dto.PageResponse;
import com.faisal.cheko.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/menu-items")
@Tag(name = "Menu Item", description = "Menu item management APIs")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/branch/{branchId}/paginated")
    @Operation(summary = "Get menu items by branch ID with pagination", description = "Returns a paginated list of menu items for a specific branch")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu items",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    public ResponseEntity<PageResponse<MenuItemResponse>> getMenuItemsByBranchIdPaginated(
            @Parameter(description = "Branch ID", required = true)
            @PathVariable Long branchId,
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
        
        Page<MenuItemResponse> menuItemsPage = menuItemService.getMenuItemsByBranchIdPaginated(branchId, pageable);
        PageResponse<MenuItemResponse> response = PageResponse.from(menuItemsPage);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/branch/{branchId}/available/paginated")
    @Operation(summary = "Get available menu items by branch ID with pagination and search",
               description = "Returns a paginated list of available menu items for a specific branch, optionally filtered by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved menu items",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    public ResponseEntity<PageResponse<MenuItemResponse>> getAvailableMenuItemsByBranchIdPaginated(
            @Parameter(description = "Branch ID", required = true)
            @PathVariable Long branchId,
            @Parameter(description = "Search by menu item name", required = false)
            @RequestParam(required = false) String search,
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
        
        Page<MenuItemResponse> menuItemsPage = menuItemService.getAvailableMenuItemsByBranchIdPaginated(branchId, search, pageable);
        PageResponse<MenuItemResponse> response = PageResponse.from(menuItemsPage);
        
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/branch/{branchId}/section/{sectionId}/paginated")
    @Operation(summary = "Get menu items by branch ID and section ID with pagination",
               description = "Returns a paginated list of menu items for a specific branch and section")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu items",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Branch or Section not found")
    })
    public ResponseEntity<PageResponse<MenuItemResponse>> getMenuItemsByBranchIdAndBySectionIdPaginated(
            @Parameter(description = "Branch ID", required = true)
            @PathVariable Long branchId,
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
        
        Page<MenuItemResponse> menuItemsPage = menuItemService.getMenuItemsByBranchIdAndBySectionIdPaginated(branchId, sectionId, pageable);
        PageResponse<MenuItemResponse> response = PageResponse.from(menuItemsPage);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/branch/{branchId}/section/{sectionId}/available")
    @Operation(summary = "Get available menu items by branch ID and section ID with pagination and search",
               description = "Returns a paginated list of available menu items for a specific branch and section, optionally filtered by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved menu items",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Branch or Section not found")
    })
    public ResponseEntity<PageResponse<MenuItemResponse>> getAvailableMenuItemsByBranchIdAndBySectionIdPaginated(
            @Parameter(description = "Branch ID", required = true)
            @PathVariable Long branchId,
            @Parameter(description = "Section ID", required = true)
            @PathVariable Long sectionId,
            @Parameter(description = "Search by menu item name", required = false)
            @RequestParam(required = false) String search,
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
        
        Page<MenuItemResponse> menuItemsPage = menuItemService.getAvailableMenuItemsByBranchIdAndBySectionIdPaginated(branchId, sectionId, search, pageable);
        PageResponse<MenuItemResponse> response = PageResponse.from(menuItemsPage);
        
        return ResponseEntity.ok(response);
    }

    // second highest calroie API
    @GetMapping("/second-highest-calorie")
    @Operation(summary = "Get second-highest calorie meal per category",
               description = "Returns a list of menu items that have the second-highest calories in their respective sections")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu items",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuItemResponse.class)))
    })
    public ResponseEntity<List<MenuItemResponse>> getSecondHighestCalorieMealPerCategory() {
        List<MenuItemResponse> menuItems = menuItemService.getSecondHighestCalorieMealPerCategory();
        return ResponseEntity.ok(menuItems);
    }
}
