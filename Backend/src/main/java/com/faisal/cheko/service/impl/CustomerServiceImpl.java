package com.faisal.cheko.service.impl;

import com.faisal.cheko.dto.CustomerRequest;
import com.faisal.cheko.dto.CustomerResponse;
import com.faisal.cheko.exception.ResourceNotFoundException;
import com.faisal.cheko.model.Customer;
import com.faisal.cheko.repository.CustomerRepository;
import com.faisal.cheko.service.CustomerService;
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
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final GeometryFactory geometryFactory;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        // Create a geometry factory with SRID 4326 (WGS84)
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("Customer", "id", id));
        return mapToResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> ResourceNotFoundException.create("Customer", "email", email));
        return mapToResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerByPhone(String phone) {
        Customer customer = customerRepository.findByPhone(phone)
                .orElseThrow(() -> ResourceNotFoundException.create("Customer", "phone", phone));
        return mapToResponse(customer);
    }

    @Override
    public List<CustomerResponse> findCustomersByName(String name) {
        return customerRepository.findByFullNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> findNearbyCustomers(Double latitude, Double longitude, double distance) {
        Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        return customerRepository.findNearbyCustomers(location, distance).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        // Check if email already exists
        if (customerRequest.getEmail() != null && customerRepository.existsByEmail(customerRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Check if phone already exists
        if (customerRequest.getPhone() != null && customerRepository.existsByPhone(customerRequest.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists");
        }
        
        Customer customer = mapToEntity(customerRequest);
        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponse(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("Customer", "id", id));
        
        // Check if email already exists for another customer
        if (customerRequest.getEmail() != null && !customerRequest.getEmail().equals(customer.getEmail()) &&
                customerRepository.existsByEmail(customerRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Check if phone already exists for another customer
        if (customerRequest.getPhone() != null && !customerRequest.getPhone().equals(customer.getPhone()) &&
                customerRepository.existsByPhone(customerRequest.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists");
        }
        
        updateCustomerFromRequest(customer, customerRequest);
        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponse(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw ResourceNotFoundException.create("Customer", "id", id);
        }
        customerRepository.deleteById(id);
    }

    private CustomerResponse mapToResponse(Customer customer) {
        Double latitude = null;
        Double longitude = null;
        
        if (customer.getLocation() != null) {
            latitude = customer.getLocation().getY();
            longitude = customer.getLocation().getX();
        }
        
        return CustomerResponse.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .latitude(latitude)
                .longitude(longitude)
                .createdAt(customer.getCreatedAt())
                .build();
    }


    private Customer mapToEntity(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        updateCustomerFromRequest(customer, customerRequest);
        return customer;
    }


    private void updateCustomerFromRequest(Customer customer, CustomerRequest customerRequest) {
        customer.setFullName(customerRequest.getFullName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhone(customerRequest.getPhone());
        customer.setAddress(customerRequest.getAddress());
        
        // Create a Point from latitude and longitude if both are provided
        if (customerRequest.getLatitude() != null && customerRequest.getLongitude() != null) {
            Point location = geometryFactory.createPoint(
                    new Coordinate(customerRequest.getLongitude(), customerRequest.getLatitude()));
            customer.setLocation(location);
        }
    }
}
