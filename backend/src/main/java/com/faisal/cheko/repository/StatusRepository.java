package com.faisal.cheko.repository;

import com.faisal.cheko.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    Optional<Status> findByCode(String code);
    boolean existsByCode(String code);
}