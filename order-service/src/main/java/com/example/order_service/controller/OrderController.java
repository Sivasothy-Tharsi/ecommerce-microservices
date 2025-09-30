package com.example.order_service.controller;

import com.example.order_service.dto.OrderItemRequest;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.ProductDTO;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public Order createOrder(@RequestBody OrderRequest request) {

        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();

        // Fetch product details from Product Service
        List<String> productIds = request.items.stream()
                .map(i -> i.productId)
                .toList();

        List<ProductDTO> products = webClientBuilder.build()
                .get()
                .uri("http://PRODUCT-SERVICE/products/list?ids={ids}", String.join(",", productIds))
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .collectList()
                .block();

        // Map products to order items
        for (OrderItemRequest itemReq : request.items) {
            ProductDTO product = products.stream()
                    .filter(p -> p.getId().equals(itemReq.productId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.productId));

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(itemReq.quantity);
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        double total = orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        order.setTotal(total);
        return orderRepository.save(order);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
