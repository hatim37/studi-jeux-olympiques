package com.ecom.products.services;

import com.ecom.products.controller.MicroServiceController;
import com.ecom.products.dto.ProductDto;
import com.ecom.products.entity.Product;
import com.ecom.products.services.customer.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MicroServiceControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private MicroServiceController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 1 : Rechercher un produit par son ID
    @Test
    void FindById() {
        // Création d’un produit
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Solo");

        // Simulation du service
        when(productService.findById(1L)).thenReturn(Optional.of(mockProduct));

        // Appel du contrôleur
        Optional<Product> result = controller.findById(1L);

        // Vérifications
        assertTrue(result.isPresent());
        assertEquals("Solo", result.get().getName());

        // Vérifie que le service a été appelé
        verify(productService, times(1)).findById(1L);
    }

    // 2 : rechercher une liste de produits par leurs ID
    @Test
    void FindListById() {
        // Préparation d’une liste d’IDs
        List<Long> ids = List.of(1L, 2L);

        // Liste simulée de produits DTO
        List<ProductDto> mockList = List.of(
                new ProductDto(1L, "Solo", 50L),
                new ProductDto(2L, "Duo", 80L)
        );

        // Simulation du service
        when(productService.findListById(ids)).thenReturn(ResponseEntity.ok(mockList));

        // Appel du contrôleur
        ResponseEntity<List<ProductDto>> response = controller.findListById(ids);

        // Vérifications
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Solo", response.getBody().get(0).getName());

        // Vérifie que le service a bien été appelé
        verify(productService, times(1)).findListById(ids);
    }
}
