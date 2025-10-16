package com.ecom.products.config;

import com.ecom.products.entity.Product;
import com.ecom.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class SqlCreate {

    private final ProductRepository productRepository;

        @EventListener(ApplicationReadyEvent.class)
        public void onApplicationReady() throws URISyntaxException, IOException {
            byte[] Image = Files.readAllBytes(
                    Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("images/JO-Paris-2024.jpg")).toURI()));

            //on créer 3 offres au démarrage
            try {
                Product product = productRepository.findByName("Solo");
                if (product == null) {
                    Product solo = Product.builder()
                            .name("Solo")
                            .description("1 personne")
                            .price(50L)
                            .img(Image)
                            .quantity(1L)
                            .build();
                    productRepository.save(solo);
                }

            } catch (Exception e) {
                log.info(String.valueOf(e));
            }

            try {
                Product product = productRepository.findByName("Duo");
                if (product == null) {
                    Product duo = Product.builder()
                            .name("Duo")
                            .description("2 personnes")
                            .price(80L)
                            .img(Image)
                            .quantity(1L)
                            .build();
                    productRepository.save(duo);
                }

            } catch (Exception e) {
                log.info(String.valueOf(e));
            }

            try {
                Product product = productRepository.findByName("Famille");
                if (product == null) {
                    Product famille = Product.builder()
                            .name("Famille")
                            .description("4 personnes")
                            .price(120L)
                            .img(Image)
                            .quantity(1L)
                            .build();
                    productRepository.save(famille);
                }

            } catch (Exception e) {
                log.info(String.valueOf(e));
            }
        }

}
