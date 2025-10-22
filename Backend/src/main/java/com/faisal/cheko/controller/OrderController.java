package com.faisal.cheko.controller;

import com.faisal.cheko.dto.OrderRequest;
import com.faisal.cheko.dto.OrderResponse;
import com.faisal.cheko.service.OrderService;
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

/**
 * REST controller for order operations.
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Order management APIs")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping
    @Operation(summary = "Get all orders", description = "Returns a list of all orders")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved orders",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponse.class)))
    })
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an order by ID", description = "Returns an order as per the ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved order",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderResponse> getOrderById(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get orders by customer ID", description = "Returns a list of orders for a specific customer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved orders",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponse.class))
            )
    })
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomerId(
            @Parameter(description = "Customer ID", required = true)
            @PathVariable Long customerId) {
        List<OrderResponse> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get orders by branch ID", description = "Returns a list of orders for a specific branch")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved orders",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponse.class))
            )
    })
    public ResponseEntity<List<OrderResponse>> getOrdersByBranchId(
            @Parameter(description = "Branch ID", required = true)
            @PathVariable Long branchId) {
        List<OrderResponse> orders = orderService.getOrdersByBranchId(branchId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{statusId}")
    @Operation(summary = "Get orders by status ID", description = "Returns a list of orders with a specific status")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved orders",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponse.class))
            )
    })
    public ResponseEntity<List<OrderResponse>> getOrdersByStatusId(
            @Parameter(description = "Status ID", required = true)
            @PathVariable Long statusId) {
        List<OrderResponse> orders = orderService.getOrdersByStatusId(statusId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    @Operation(summary = "Create a new order", description = "Creates a new order and returns the created order")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Order created successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Customer, branch, status, or menu item not found")
    })
    public ResponseEntity<OrderResponse> createOrder(
            @Parameter(description = "Order data", required = true)
            @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse createdOrder = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update an order", description = "Updates an order and returns the updated order")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order updated successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Order, customer, branch, status, or menu item not found")
    })
    public ResponseEntity<OrderResponse> updateOrder(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated order data", required = true)
            @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse updatedOrder = orderService.updateOrder(id, orderRequest);
        return ResponseEntity.ok(updatedOrder);
    }


    @PatchMapping("/{id}/status/{statusId}")
    @Operation(summary = "Update order status", description = "Updates the status of an order and returns the updated order")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order status updated successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order or status not found")
    })
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Status ID", required = true)
            @PathVariable Long statusId) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(id, statusId);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order", description = "Deletes an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
