package com.ecom.orders.service;

import com.ecom.orders.entity.Order;
import com.ecom.orders.enums.OrderStatus;
import com.ecom.orders.repository.OrderRepository;
import com.ecom.orders.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceUnitTest {

    @InjectMocks
    private OrderService orderService;
    private Order existingOrder;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Création d'une commande existante
        existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setUserId(10L);
        existingOrder.setAmount(50L);
        existingOrder.setTotalAmount(60L);
        existingOrder.setOrderStatus(OrderStatus.EnCours);
        existingOrder.setTrackingId(UUID.randomUUID());
    }

    // 1 : génère une clé AES 256 bits et encode en Base64
    @Test
    void generateAndEncryptKeyForDB_shouldReturnNonEmptyString() throws Exception {
        String key = orderService.generateAndEncryptKeyForDB();
        assertNotNull(key);
        assertFalse(key.isEmpty());
    }

    // 2 : Mise à jour des informations d'une commande
    @Test
    void updateOrderTotal() {
        // Simulation de la présence en DB
        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));

        // Création d’une commande mise à jour
        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setUserId(11L);
        updatedOrder.setAmount(200L);
        updatedOrder.setTotalAmount(250L);

        // Appel méthode
        orderService.updateOrderTotal(updatedOrder);

        // Vérifications des champs mis à jour
        assertThat(existingOrder.getAmount()).isEqualTo(200L);
        assertThat(existingOrder.getTotalAmount()).isEqualTo(250L);
        assertThat(existingOrder.getUserId()).isEqualTo(11L);

        // Vérifie la sauvegarde
        verify(orderRepository, times(1)).save(existingOrder);
    }
}
