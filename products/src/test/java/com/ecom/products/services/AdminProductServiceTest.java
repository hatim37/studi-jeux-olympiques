package com.ecom.products.services;

import com.ecom.products.dto.ProductDto;
import com.ecom.products.entity.Product;
import com.ecom.products.repository.ProductRepository;
import com.ecom.products.services.admin.AdminProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AdminProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private AdminProductService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 1 : Ajout d'un nouveau produit
    @Test
    void AddProduct() throws IOException {
        ProductDto dto = new ProductDto(null, "Solo", 50L);
        MultipartFile mockFile = mock(MultipartFile.class);
        dto.setImg(mockFile);
        when(mockFile.getBytes()).thenReturn(new byte[]{1, 2, 3});

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Solo");
        savedProduct.setPrice(50L);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDto result = service.addProduct(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Solo", result.getName());
    }

    // 2 : Récupération de tous les produits
    @Test
    void GetAllProducts() {
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Solo");
        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("Duo");

        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        List<ProductDto> result = service.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Solo", result.get(0).getName());
        assertEquals("Duo", result.get(1).getName());
    }

    // 3 : Suppression d'un produit par son ID
    @Test
    void deleteProductById() {
        Product product = new Product();
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        boolean deleted = service.deleteProductById(1L);

        assertTrue(deleted);
        verify(productRepository, times(1)).deleteById(1L);
    }

    // 4 : Suppression d'un produit non trouvé
    @Test
    void deleteProductById_notFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        boolean deleted = service.deleteProductById(1L);

        assertFalse(deleted);
        verify(productRepository, never()).deleteById(anyLong());
    }

    // 5 : mise à jour d'un produit
    @Test
    void updateProduct() throws IOException {
        Product existing = new Product();
        existing.setId(1L);
        existing.setName("Solo");
        existing.setPrice(50L);

        ProductDto dto = new ProductDto(null, "Solo Updated", 60L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductDto updated = service.updateProduct(1L, dto);

        assertNotNull(updated);
        assertEquals("Solo Updated", updated.getName());
        assertEquals(60L, updated.getPrice());
    }
}