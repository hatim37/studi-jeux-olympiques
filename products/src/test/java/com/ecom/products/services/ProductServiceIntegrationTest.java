package com.ecom.products.services;

import com.ecom.products.entity.Product;
import com.ecom.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // On vérifie que la BDD est vide avant
        productRepository.deleteAll();

        // On crée ici 3 produits sauvegardés dans la base H2
        Product solo = Product.builder()
                .name("Solo")
                .description("1 personne")
                .price(50L)
                .quantity(1L)
                .build();

        Product duo = Product.builder()
                .name("Duo")
                .description("2 personnes")
                .price(80L)
                .quantity(1L)
                .build();

        Product famille = Product.builder()
                .name("Famille")
                .description("4 personnes")
                .price(120L)
                .quantity(1L)
                .build();

        productRepository.saveAll(List.of(solo, duo, famille));
    }

    // On vérifie que la méthode findAll() fonctionne
    @Test
    void testGetAllProducts_simple() {
        // On récupère tous les produits de la base
        List<Product> products = productRepository.findAll();

        // Vérification,  liste non vide
        assertThat(products).isNotNull();

        // Vérification, 3 produits = ok
        assertThat(products).hasSize(3);

        // Vérification, noms des produits existent
        assertThat(products).extracting(Product::getName)
                .containsExactlyInAnyOrder("Solo", "Duo", "Famille");
    }
}

