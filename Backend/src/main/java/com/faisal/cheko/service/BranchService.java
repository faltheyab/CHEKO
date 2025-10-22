package com.faisal.cheko.service;

import com.faisal.cheko.dto.BranchRequest;
import com.faisal.cheko.dto.BranchResponse;
import org.locationtech.jts.geom.Point;

import java.util.List;


public interface BranchService {

    List<BranchResponse> getAllBranches();
    BranchResponse getBranchById(Long id);
    BranchResponse createBranch(BranchRequest branchRequest);
    BranchResponse updateBranch(Long id, BranchRequest branchRequest);
    void deleteBranch(Long id);
    List<BranchResponse> findNearbyBranches(Double latitude, Double longitude, double distance);
    List<BranchResponse> findActiveBranches();
    BranchResponse findMainBranch();
}
