package com.ecom.products.services;


import com.ecom.products.dto.ProductDto;
import com.ecom.products.entity.Product;
import com.ecom.products.repository.ProductRepository;
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
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test : récupération de tous les produits
    @Test
    void testGetAllProducts() {
        // Préparation des données
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product1");
        product1.setPrice(100L);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product2");
        product2.setPrice(200L);

        // On simule liste des produits créée
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        // On appelle la méthode
        List<ProductDto> result = productService.getAllProducts();

        // Vérification, liste non null + 2 éléments présent
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product1", result.get(0).getName());
        assertEquals(100L, result.get(0).getPrice());
        assertEquals("Product2", result.get(1).getName());
        assertEquals(200L, result.get(1).getPrice());
    }

    // Test : recherche d’un produit existant par id
    @Test
    void testFindById_found() {
        // Préparation
        Product product = new Product();
        product.setId(1L);
        product.setName("Product1");
        product.setPrice(100L);

        // On simule produit avec Optional
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Appel de la méthode du service
        Optional<Product> result = productService.findById(1L);

        // Vérification, produit ok + nom ok
        assertTrue(result.isPresent());
        assertEquals("Product1", result.get().getName());
    }

    // Test : recherche d’un produit inexistant par id
    @Test
    void testFindById_notFound() {
        // On simule un produit inconnu
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Appel de la méthode
        Optional<Product> result = productService.findById(1L);

        // Vérification, produit =null
        assertFalse(result.isPresent());
    }

    // Test : récupération d’une liste de produits par une liste d’Ids
    @Test
    void testFindListById() {
        // Préparation de 2 produits
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product1");
        product1.setPrice(100L);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product2");
        product2.setPrice(200L);

        // Liste d'IDs à rechercher
        List<Long> ids = List.of(1L, 2L);

        // On simule liste de produits
        when(productRepository.findAllById(ids)).thenReturn(List.of(product1, product2));

        // Appel de la méthode
        ResponseEntity<List<ProductDto>> response = productService.findListById(ids);

        // Vérifications, not null + le body not null
        assertNotNull(response);
        assertNotNull(response.getBody());

        // Vérifications, on doit avoir 2 produits dans la liste
        List<ProductDto> result = response.getBody();
        assertEquals(2, result.size());
        assertEquals("Product1", result.get(0).getName());
        assertEquals(100L, result.get(0).getPrice());
        assertEquals("Product2", result.get(1).getName());
        assertEquals(200L, result.get(1).getPrice());
    }
}
