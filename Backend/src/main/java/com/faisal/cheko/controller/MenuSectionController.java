package com.faisal.cheko.controller;

import com.faisal.cheko.dto.MenuSectionRequest;
import com.faisal.cheko.dto.MenuSectionResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/menu-sections")
@Tag(name = "Menu Section", description = "Menu section management APIs")
public class MenuSectionController {

    private final MenuSectionService menuSectionService;

    @Autowired
    public MenuSectionController(MenuSectionService menuSectionService) {
        this.menuSectionService = menuSectionService;
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
    @Operation(summary = "Get a menu section by ID", description = "Returns a menu section as per the ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu section",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MenuSectionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Menu section not found")
    })
    public ResponseEntity<MenuSectionResponse> getMenuSectionById(
            @Parameter(description = "Menu section ID", required = true)
            @PathVariable Long id) {
        MenuSectionResponse menuSection = menuSectionService.getMenuSectionById(id);
        return ResponseEntity.ok(menuSection);
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
}
