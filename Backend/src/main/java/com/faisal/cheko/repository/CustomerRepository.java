package com.faisal.cheko.repository;

import com.faisal.cheko.model.Customer;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhone(String phone);
    List<Customer> findByFullNameContainingIgnoreCase(String name);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    @Query(value = "SELECT c.* FROM restaurant.customers c " +
                  "WHERE ST_DWithin(c.location, :location, :distance) " +
                  "ORDER BY ST_Distance(c.location, :location)", 
           nativeQuery = true)
    List<Customer> findNearbyCustomers(@Param("location") Point location, @Param("distance") double distance);
}