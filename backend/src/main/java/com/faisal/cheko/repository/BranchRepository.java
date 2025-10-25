package com.faisal.cheko.repository;

import com.faisal.cheko.model.Branch;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findByIsActiveTrue();
    Optional<Branch> findByIsMainBranchTrue();
    @Query(value = "SELECT b.* FROM restaurant.branches b " +
                  "WHERE ST_DWithin(b.location, :location, :distance) " +
                  "ORDER BY ST_Distance(b.location, :location)", 
           nativeQuery = true)
    List<Branch> findNearbyBranches(@Param("location") Point location, @Param("distance") double distance);
}
