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


}
