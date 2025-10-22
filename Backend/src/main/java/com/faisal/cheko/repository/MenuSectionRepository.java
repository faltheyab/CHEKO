package com.faisal.cheko.repository;

import com.faisal.cheko.model.Branch;
import com.faisal.cheko.model.MenuSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MenuSectionRepository extends JpaRepository<MenuSection, Long> {
    

    List<MenuSection> findByBranch(Branch branch);
    List<MenuSection> findByBranchId(Long branchId);
    Optional<MenuSection> findByNameAndBranch(String name, Branch branch);
    Optional<MenuSection> findByNameAndBranchId(String name, Long branchId);
}