package com.ecom.products.services;


import com.ecom.products.controller.AdminProductController;
import com.ecom.products.dto.ProductDto;
import com.ecom.products.services.admin.AdminProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


class AdminProductControllerTest {

    @Mock
    private AdminProductService productService;

    @InjectMocks
    private AdminProductController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 1 : création d’un produit
    @Test
    void createProduct() throws IOException {
        // Préparation du DTO
        ProductDto inputDto = new ProductDto();
        inputDto.setName("Nouveau produit");
        inputDto.setPrice(100L);

        // DTO
        ProductDto outputDto = new ProductDto();
        outputDto.setId(1L);
        outputDto.setName("Nouveau produit");
        outputDto.setPrice(100L);

        // Simulation du service
        when(productService.addProduct(inputDto)).thenReturn(outputDto);

        // Appel de la méthode
        ResponseEntity<ProductDto> response = controller.createProduct(inputDto);

        // Vérifications
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(outputDto, response.getBody());
        verify(productService, times(1)).addProduct(inputDto);
    }

    // 2 : récupération de tous les produits
    @Test
    void getAllProducts() {
        // Liste simulée renvoyée par le service
        List<ProductDto> mockProducts = List.of(
                new ProductDto(1L, "Solo", 50L),
                new ProductDto(2L, "Duo", 80L)
        );

        // Simulation du service
        when(productService.getAllProducts()).thenReturn(mockProducts);

        // Appel du contrôleur
        ResponseEntity<List<ProductDto>> response = controller.getAllProducts();

        // Vérifications
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Duo", response.getBody().get(1).getName());
    }

    // 3 : suppression d’un produit existant
    @Test
    void deleteProduct() {
        // Simulation du service
        when(productService.deleteProductById(1L)).thenReturn(true);

        // Appel de la méthode
        ResponseEntity<ProductDto> response = controller.deleteProduct(1L);

        // Vérifications
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(productService, times(1)).deleteProductById(1L);
    }

    // 4 : suppression d’un produit inexistant
    @Test
    void deleteProduct_notFound() {
        // Simulation du service
        when(productService.deleteProductById(99L)).thenReturn(false);

        // Appel de la méthode
        ResponseEntity<ProductDto> response = controller.deleteProduct(99L);

        // Vérifications
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(productService, times(1)).deleteProductById(99L);
    }

    // 5 : récupération d’un produit par ID
    @Test
    void getProductById() {
        // Simulation d’un produit existant
        ProductDto mockDto = new ProductDto(1L, "Famille", 120L);
        when(productService.getProductById(1L)).thenReturn(mockDto);

        // Appel de la méthode
        ResponseEntity<ProductDto> response = controller.getProductById(1L);

        // Vérifications
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Famille", response.getBody().getName());
        verify(productService, times(2)).getProductById(1L);
    }

    // 6 : récupération d’un produit par ID non trouvé
    @Test
    void getProductById_notFound() {
        // Simulation : produit non trouvé
        when(productService.getProductById(42L)).thenReturn(null);

        // Appel de la méthode
        ResponseEntity<ProductDto> response = controller.getProductById(42L);

        // Vérifications
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(productService, times(1)).getProductById(42L);
    }
}