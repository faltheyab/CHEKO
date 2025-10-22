package com.faisal.cheko.controller;

import com.faisal.cheko.dto.BranchRequest;
import com.faisal.cheko.dto.BranchResponse;
import com.faisal.cheko.service.BranchService;
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
@RequestMapping("/api/branches")
@Tag(name = "Branch", description = "Branch management APIs")
public class BranchController {

    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    @Operation(summary = "Get all branches", description = "Returns a list of all branches")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved branches",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BranchResponse.class))
            )
    })
    public ResponseEntity<List<BranchResponse>> getAllBranches() {
        List<BranchResponse> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a branch by ID", description = "Returns a branch as per the ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved branch",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BranchResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Branch not found"
            )
    })
    public ResponseEntity<BranchResponse> getBranchById(
            @Parameter(description = "Branch ID", required = true)
            @PathVariable Long id) {
        BranchResponse branch = branchService.getBranchById(id);
        return ResponseEntity.ok(branch);
    }

    @PostMapping
    @Operation(summary = "Create a new branch", description = "Creates a new branch and returns the created branch")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Branch created successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BranchResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input"
            )
    })
    public ResponseEntity<BranchResponse> createBranch(
            @Parameter(description = "Branch data", required = true)
            @Valid @RequestBody BranchRequest branchRequest) {
        BranchResponse createdBranch = branchService.createBranch(branchRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBranch);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a branch", description = "Updates a branch and returns the updated branch")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Branch updated successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BranchResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    public ResponseEntity<BranchResponse> updateBranch(
            @Parameter(description = "Branch ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated branch data", required = true)
            @Valid @RequestBody BranchRequest branchRequest) {
        BranchResponse updatedBranch = branchService.updateBranch(id, branchRequest);
        return ResponseEntity.ok(updatedBranch);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a branch", description = "Deletes a branch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Branch deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    public ResponseEntity<Void> deleteBranch(
            @Parameter(description = "Branch ID", required = true)
            @PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nearby")
    @Operation(summary = "Find nearby branches", description = "Returns a list of branches near a specific location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved branches",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BranchResponse.class)))
    })
    public ResponseEntity<List<BranchResponse>> findNearbyBranches(
            @Parameter(description = "Latitude", required = true)
            @RequestParam Double latitude,
            @Parameter(description = "Longitude", required = true)
            @RequestParam Double longitude,
            @Parameter(description = "Distance in meters", required = true)
            @RequestParam double distance) {
        List<BranchResponse> branches = branchService.findNearbyBranches(latitude, longitude, distance);
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/active")
    @Operation(summary = "Find active branches", description = "Returns a list of active branches")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved branches",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BranchResponse.class))
            )
    })
    public ResponseEntity<List<BranchResponse>> findActiveBranches() {
        List<BranchResponse> branches = branchService.findActiveBranches();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/main")
    @Operation(summary = "Find the main branch", description = "Returns the main branch")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved branch",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BranchResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Main branch not found")
    })
    public ResponseEntity<BranchResponse> findMainBranch() {
        BranchResponse branch = branchService.findMainBranch();
        return ResponseEntity.ok(branch);
    }
}
