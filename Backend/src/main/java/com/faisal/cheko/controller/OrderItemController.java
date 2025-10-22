package com.faisal.cheko.controller;

import com.faisal.cheko.dto.OrderItemRequest;
import com.faisal.cheko.dto.OrderItemResponse;
import com.faisal.cheko.service.OrderItemService;
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
@RequestMapping("/api/order-items")
@Tag(name = "Order Item", description = "Order item management APIs")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }


    @GetMapping
    @Operation(summary = "Get all order items", description = "Returns a list of all order items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order items",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderItemResponse.class)))
    })
    public ResponseEntity<List<OrderItemResponse>> getAllOrderItems() {
        List<OrderItemResponse> orderItems = orderItemService.getAllOrderItems();
        return ResponseEntity.ok(orderItems);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an order item by ID", description = "Returns an order item as per the ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order item",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderItemResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    public ResponseEntity<OrderItemResponse> getOrderItemById(
            @Parameter(description = "Order item ID", required = true)
            @PathVariable Long id) {
        OrderItemResponse orderItem = orderItemService.getOrderItemById(id);
        return ResponseEntity.ok(orderItem);
    }


    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get order items by order ID", description = "Returns a list of order items for a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order items",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderItemResponse.class)))
    })
    public ResponseEntity<List<OrderItemResponse>> getOrderItemsByOrderId(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long orderId) {
        List<OrderItemResponse> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }


    @PostMapping("/order/{orderId}")
    @Operation(summary = "Create a new order item", description = "Creates a new order item and returns the created order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order item created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Order or menu item not found")
    })
    public ResponseEntity<OrderItemResponse> createOrderItem(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long orderId,
            @Parameter(description = "Order item data", required = true)
            @Valid @RequestBody OrderItemRequest orderItemRequest) {
        OrderItemResponse createdOrderItem = orderItemService.createOrderItem(orderId, orderItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderItem);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an order item", description = "Updates an order item and returns the updated order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Order item or menu item not found")
    })
    public ResponseEntity<OrderItemResponse> updateOrderItem(
            @Parameter(description = "Order item ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated order item data", required = true)
            @Valid @RequestBody OrderItemRequest orderItemRequest) {
        OrderItemResponse updatedOrderItem = orderItemService.updateOrderItem(id, orderItemRequest);
        return ResponseEntity.ok(updatedOrderItem);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order item", description = "Deletes an order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    public ResponseEntity<Void> deleteOrderItem(
            @Parameter(description = "Order item ID", required = true)
            @PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/order/{orderId}")
    @Operation(summary = "Delete all order items for an order", description = "Deletes all order items for a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order items deleted successfully")
    })
    public ResponseEntity<Void> deleteOrderItemsByOrderId(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long orderId) {
        orderItemService.deleteOrderItemsByOrderId(orderId);
        return ResponseEntity.noContent().build();
    }
}