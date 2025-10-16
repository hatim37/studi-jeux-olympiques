package com.ecom.products.controller;

import com.ecom.products.dto.ProductDto;
import com.ecom.products.services.customer.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class
ProductController {

    private final ProductService customerProductservice;

    public ProductController(ProductService customerProductservice) {
        this.customerProductservice = customerProductservice;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(customerProductservice.getAllProducts());
    }


}
