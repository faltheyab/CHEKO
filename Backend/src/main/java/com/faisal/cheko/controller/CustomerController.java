package com.faisal.cheko.controller;

import com.faisal.cheko.dto.CustomerRequest;
import com.faisal.cheko.dto.CustomerResponse;
import com.faisal.cheko.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer", description = "Customer management APIs")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(summary = "Get all customers", description = "Returns a list of all customers")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved customers",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerResponse.class))
            )
    })
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a customer by ID", description = "Returns a customer as per the ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved customer",
                    content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = CustomerResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerResponse> getCustomerById(
            @Parameter(description = "Customer ID", required = true)
            @PathVariable Long id) {
        CustomerResponse customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get a customer by email", description = "Returns a customer as per the email")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved customer",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerResponse> getCustomerByEmail(
            @Parameter(description = "Customer email", required = true)
            @PathVariable String email) {
        CustomerResponse customer = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/phone/{phone}")
    @Operation(summary = "Get a customer by phone number", description = "Returns a customer as per the phone number")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved customer",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerResponse> getCustomerByPhone(
            @Parameter(description = "Customer phone number", required = true)
            @PathVariable String phone) {
        CustomerResponse customer = customerService.getCustomerByPhone(phone);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/search")
    @Operation(summary = "Find customers by name", description = "Returns a list of customers with a name containing the specified string")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved customers",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerResponse.class))
            )
    })
    public ResponseEntity<List<CustomerResponse>> findCustomersByName(
            @Parameter(description = "Name to search for", required = true)
            @RequestParam String name) {
        List<CustomerResponse> customers = customerService.findCustomersByName(name);
        return ResponseEntity.ok(customers);
    }


    @GetMapping("/nearby")
    @Operation(summary = "Find nearby customers", description = "Returns a list of customers near a specific location")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved customers",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerResponse.class))
            )
    })
    public ResponseEntity<List<CustomerResponse>> findNearbyCustomers(
            @Parameter(description = "Latitude", required = true)
            @RequestParam Double latitude,
            @Parameter(description = "Longitude", required = true)
            @RequestParam Double longitude,
            @Parameter(description = "Distance in meters", required = true)
            @RequestParam double distance) {
        List<CustomerResponse> customers = customerService.findNearbyCustomers(latitude, longitude, distance);
        return ResponseEntity.ok(customers);
    }

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Creates a new customer and returns the created customer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Customer created successfully",
                    content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = CustomerResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input or email/phone already exists")
    })
    public ResponseEntity<CustomerResponse> createCustomer(
            @Parameter(description = "Customer data", required = true)
            @Valid @RequestBody CustomerRequest customerRequest) {
        CustomerResponse createdCustomer = customerService.createCustomer(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a customer", description = "Updates a customer and returns the updated customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or email/phone already exists"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerResponse> updateCustomer(
            @Parameter(description = "Customer ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated customer data", required = true)
            @Valid @RequestBody CustomerRequest customerRequest) {
        CustomerResponse updatedCustomer = customerService.updateCustomer(id, customerRequest);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer", description = "Deletes a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "Customer ID", required = true)
            @PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}

