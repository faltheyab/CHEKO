package com.faisal.cheko.service;

import com.faisal.cheko.dto.CustomerRequest;
import com.faisal.cheko.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {

    List<CustomerResponse> getAllCustomers();
    CustomerResponse getCustomerById(Long id);
    CustomerResponse getCustomerByEmail(String email);
    CustomerResponse getCustomerByPhone(String phone);
    List<CustomerResponse> findCustomersByName(String name);
    List<CustomerResponse> findNearbyCustomers(Double latitude, Double longitude, double distance);
    CustomerResponse createCustomer(CustomerRequest customerRequest);
    CustomerResponse updateCustomer(Long id, CustomerRequest customerRequest);
    void deleteCustomer(Long id);
}
