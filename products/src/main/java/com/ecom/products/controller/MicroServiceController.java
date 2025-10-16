package com.ecom.products.controller;


import com.ecom.products.dto.ProductDto;
import com.ecom.products.entity.Product;
import com.ecom.products.services.customer.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MicroServiceController {

    private final ProductService productService;

    public MicroServiceController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/_internal/productFindById/{id}")
    public Optional<Product> findById(@PathVariable Long id){
        return this.productService.findById(id);
    }

    @PostMapping("/_internal/productFindListById")
    public ResponseEntity<List<ProductDto>> findListById(@RequestBody List<Long> ids) {
        return productService.findListById(ids);
    }

}

