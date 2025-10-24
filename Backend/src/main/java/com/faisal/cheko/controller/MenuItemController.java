package com.faisal.cheko.controller;

import com.faisal.cheko.dto.MenuItemRequest;
import com.faisal.cheko.dto.MenuItemResponse;
import com.faisal.cheko.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.faisal.cheko.dto.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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


    @GetMapping
    @Operation(summary = "Get all menu items", description = "Returns a list of all menu items")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu items",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuItemResponse.class)))
    })
    public ResponseEntity<List<MenuItemResponse>> getAllMenuItems() {
        List<MenuItemResponse> menuItems = menuItemService.getAllMenuItems();
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a menu item by ID", description = "Returns a menu item as per the ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu item",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuItemResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    public ResponseEntity<MenuItemResponse> getMenuItemById(
            @Parameter(description = "Menu item ID", required = true)
            @PathVariable Long id) {
        MenuItemResponse menuItem = menuItemService.getMenuItemById(id);
        return ResponseEntity.ok(menuItem);
    }

    @GetMapping("/section/{sectionId}")
    @Operation(summary = "Get menu items by section ID", description = "Returns a list of menu items for a specific section")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu items",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuItemResponse.class))
            )
    })
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsBySectionId(
            @Parameter(description = "Section ID", required = true)
            @PathVariable Long sectionId) {
        List<MenuItemResponse> menuItems = menuItemService.getMenuItemsBySectionId(sectionId);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get menu items by branch ID", description = "Returns a list of menu items for a specific branch")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu items",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuItemResponse.class))
            )
    })
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByBranchId(
            @Parameter(description = "Branch ID", required = true)
            @PathVariable Long branchId) {
        List<MenuItemResponse> menuItems = menuItemService.getMenuItemsByBranchId(branchId);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/branch/{branchId}/available")
    @Operation(summary = "Get available menu items by branch ID", description = "Returns a list of available menu items for a specific branch")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu items",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuItemResponse.class)))
    })
    public ResponseEntity<List<MenuItemResponse>> getAvailableMenuItemsByBranchId(
            @Parameter(description = "Branch ID", required = true)
            @PathVariable Long branchId) {
        List<MenuItemResponse> menuItems = menuItemService.getAvailableMenuItemsByBranchId(branchId);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/price/max/{price}")
    @Operation(summary = "Get menu items by maximum price", description = "Returns a list of menu items with a price less than or equal to the specified amount")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu items",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuItemResponse.class))
            )
    })
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByMaxPrice(
            @Parameter(description = "Maximum price", required = true)
            @PathVariable BigDecimal price) {
        List<MenuItemResponse> menuItems = menuItemService.getMenuItemsByMaxPrice(price);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/price/range")
    @Operation(summary = "Get menu items by price range", description = "Returns a list of menu items with a price between the specified minimum and maximum amounts")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu items",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuItemResponse.class))
            )
    })
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByPriceRange(
            @Parameter(description = "Minimum price", required = true)
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price", required = true)
            @RequestParam BigDecimal maxPrice) {
        List<MenuItemResponse> menuItems = menuItemService.getMenuItemsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(menuItems);
    }

    @PostMapping
    @Operation(summary = "Create a new menu item", description = "Creates a new menu item and returns the created menu item")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Menu item created successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuItemResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Section not found")
    })
    public ResponseEntity<MenuItemResponse> createMenuItem(
            @Parameter(description = "Menu item data", required = true)
            @Valid @RequestBody MenuItemRequest menuItemRequest) {
        MenuItemResponse createdMenuItem = menuItemService.createMenuItem(menuItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenuItem);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a menu item", description = "Updates a menu item and returns the updated menu item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MenuItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Menu item or section not found")
    })
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @Parameter(description = "Menu item ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated menu item data", required = true)
            @Valid @RequestBody MenuItemRequest menuItemRequest) {
        MenuItemResponse updatedMenuItem = menuItemService.updateMenuItem(id, menuItemRequest);
        return ResponseEntity.ok(updatedMenuItem);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a menu item", description = "Deletes a menu item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Menu item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    public ResponseEntity<Void> deleteMenuItem(
            @Parameter(description = "Menu item ID", required = true)
            @PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}/availability")
    @Operation(summary = "Update menu item availability", description = "Updates the availability of a menu item and returns the updated menu item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item availability updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MenuItemResponse.class))),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    public ResponseEntity<MenuItemResponse> updateMenuItemAvailability(
            @Parameter(description = "Menu item ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Availability status", required = true)
            @RequestParam Boolean isAvailable) {
        MenuItemResponse updatedMenuItem = menuItemService.updateMenuItemAvailability(id, isAvailable);
        return ResponseEntity.ok(updatedMenuItem);
    }
    

    @GetMapping("/paginated")
    @Operation(summary = "Get all menu items with pagination", description = "Returns a paginated list of menu items")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu items",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageResponse.class))
            )
    })
    public ResponseEntity<PageResponse<MenuItemResponse>> getAllMenuItemsPaginated(
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
        
        Page<MenuItemResponse> menuItemsPage = menuItemService.getAllMenuItemsPaginated(pageable);
        PageResponse<MenuItemResponse> response = PageResponse.from(menuItemsPage);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/section/{sectionId}/paginated")
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
    
    @GetMapping("/section/{sectionId}/available")
    @Operation(summary = "Get available menu items by section ID with pagination and search",
               description = "Returns a paginated list of available menu items for a specific section, optionally filtered by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved menu items",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Section not found")
    })
    public ResponseEntity<PageResponse<MenuItemResponse>> getAvailableMenuItemsBySectionIdPaginated(
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
        
        Page<MenuItemResponse> menuItemsPage = menuItemService.getAvailableMenuItemsBySectionIdPaginated(sectionId, search, pageable);
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
