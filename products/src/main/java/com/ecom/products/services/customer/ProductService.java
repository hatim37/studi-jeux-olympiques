package com.ecom.products.services.customer;

import com.ecom.products.dto.ProductDto;
import com.ecom.products.entity.Product;
import com.ecom.products.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public ResponseEntity<List<ProductDto>> findListById(List<Long> ids) {
        List<Product> products = productRepository.findAllById(ids);
        List<ProductDto> result = products.stream()
                .map(p -> new ProductDto(p.getId(), p.getName(), p.getPrice()))
                .toList();
        return ResponseEntity.ok(result);
    }

}
