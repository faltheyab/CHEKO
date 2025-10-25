package com.faisal.cheko.service.impl;

import com.faisal.cheko.dto.BranchRequest;
import com.faisal.cheko.dto.BranchResponse;
import com.faisal.cheko.exception.ResourceNotFoundException;
import com.faisal.cheko.model.Branch;
import com.faisal.cheko.repository.BranchRepository;
import com.faisal.cheko.service.BranchService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final GeometryFactory geometryFactory;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
        // Create a geometry factory with SRID 4326 (WGS84)
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Override
    public List<BranchResponse> getAllBranches() {
        return branchRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BranchResponse createBranch(BranchRequest branchRequest) {
        Branch branch = mapToEntity(branchRequest);
        Branch savedBranch = branchRepository.save(branch);
        return mapToResponse(savedBranch);
    }

    private BranchResponse mapToResponse(Branch branch) {
        Double latitude = null;
        Double longitude = null;
        
        if (branch.getLocation() != null) {
            latitude = branch.getLocation().getY();
            longitude = branch.getLocation().getX();
        }
        
        return BranchResponse.builder()
                .id(branch.getId())
                .branchName(branch.getBranchName())
                .address(branch.getAddress())
                .city(branch.getCity())
                .country(branch.getCountry())
                .latitude(latitude)
                .longitude(longitude)
                .phone(branch.getPhone())
                .email(branch.getEmail())
                .openingHours(branch.getOpeningHours())
                .isMainBranch(branch.getIsMainBranch())
                .isActive(branch.getIsActive())
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .build();
    }


    private Branch mapToEntity(BranchRequest branchRequest) {
        Branch branch = new Branch();
        updateBranchFromRequest(branch, branchRequest);
        return branch;
    }


    private void updateBranchFromRequest(Branch branch, BranchRequest branchRequest) {
        branch.setBranchName(branchRequest.getBranchName());
        branch.setAddress(branchRequest.getAddress());
        branch.setCity(branchRequest.getCity());
        branch.setCountry(branchRequest.getCountry());
        
        // Create a Point from latitude and longitude if both are provided
        if (branchRequest.getLatitude() != null && branchRequest.getLongitude() != null) {
            Point location = geometryFactory.createPoint(
                    new Coordinate(branchRequest.getLongitude(), branchRequest.getLatitude()));
            branch.setLocation(location);
        }
        
        branch.setPhone(branchRequest.getPhone());
        branch.setEmail(branchRequest.getEmail());
        branch.setOpeningHours(branchRequest.getOpeningHours());
        
        if (branchRequest.getIsMainBranch() != null) {
            branch.setIsMainBranch(branchRequest.getIsMainBranch());
        }
        
        if (branchRequest.getIsActive() != null) {
            branch.setIsActive(branchRequest.getIsActive());
        }
    }
}
