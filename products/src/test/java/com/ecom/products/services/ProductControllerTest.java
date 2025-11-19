package com.ecom.products.services;

import com.ecom.products.controller.ProductController;
import com.ecom.products.dto.ProductDto;
import com.ecom.products.services.customer.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 1 : test récupérer tous les produits
    @Test
    void GetAllProducts() {
        // Préparation : liste de produits
        List<ProductDto> mockProducts = List.of(
                new ProductDto(1L, "Solo", 50L),
                new ProductDto(2L, "Duo", 80L)
        );

        // Simulation du service
        when(productService.getAllProducts()).thenReturn(mockProducts);

        // Appel de la méthode
        ResponseEntity<List<ProductDto>> response = controller.getAllProducts();

        // Vérifications
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Solo", response.getBody().get(0).getName());

        // Vérifie que le service a bien été appelé
        verify(productService, times(1)).getAllProducts();
    }
}
