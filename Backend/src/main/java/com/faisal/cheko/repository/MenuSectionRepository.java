package com.faisal.cheko.repository;

import com.faisal.cheko.model.Branch;
import com.faisal.cheko.model.MenuSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuSectionRepository extends JpaRepository<MenuSection, Long>, JpaSpecificationExecutor<MenuSection> {
    
    

    List<MenuSection> findByBranch(Branch branch);
    List<MenuSection> findByBranchId(Long branchId);
    Optional<MenuSection> findByNameAndBranch(String name, Branch branch);
    Optional<MenuSection> findByNameAndBranchId(String name, Long branchId);
    
    @Query("SELECT ms, COUNT(mi) FROM MenuSection ms LEFT JOIN MenuItem mi ON mi.section = ms GROUP BY ms")
    List<Object[]> findAllWithItemCounts();
    
    @Query("SELECT ms, COUNT(mi) FROM MenuSection ms LEFT JOIN MenuItem mi ON mi.section = ms WHERE ms.branch.id = ?1 GROUP BY ms")
    List<Object[]> findByBranchIdWithItemCounts(Long branchId);
}