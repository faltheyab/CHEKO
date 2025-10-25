package com.faisal.cheko.service;

import com.faisal.cheko.dto.BranchRequest;
import com.faisal.cheko.dto.BranchResponse;
import org.locationtech.jts.geom.Point;

import java.util.List;


public interface BranchService {

    List<BranchResponse> getAllBranches();
    BranchResponse createBranch(BranchRequest branchRequest);
}
