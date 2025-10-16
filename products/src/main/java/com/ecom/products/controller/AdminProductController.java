package com.ecom.products.controller;

import com.ecom.products.dto.ProductDto;
import com.ecom.products.repository.ProductRepository;
import com.ecom.products.services.admin.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductRepository productRepository;
    private final AdminProductService productService;

    @PostMapping("/new-product")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ProductDto> createProduct(@ModelAttribute ProductDto productDto) throws IOException {
        ProductDto product = productService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/products")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }


    @DeleteMapping("/delete-product/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long id) {
        boolean delete = productService.deleteProductById(id);
        if (delete) {
            return ResponseEntity.noContent().build();
        } return ResponseEntity.notFound().build();
    }


    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        ProductDto productDto = productService.getProductById(productId);
        if (productDto != null) {
            return ResponseEntity.ok(productService.getProductById(productId));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/product/{productId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId, @ModelAttribute ProductDto productDto) throws IOException {
        ProductDto updateProduct = productService.updateProduct(productId, productDto);
        if (updateProduct != null) {
            return ResponseEntity.ok(updateProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
