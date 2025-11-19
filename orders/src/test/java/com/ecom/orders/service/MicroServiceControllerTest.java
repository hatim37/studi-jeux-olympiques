package com.ecom.orders.service;

import com.ecom.orders.config.UsersOrderInitializer;
import com.ecom.orders.controller.MicroServiceController;
import com.ecom.orders.entity.Order;
import com.ecom.orders.enums.OrderStatus;
import com.ecom.orders.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MicroServiceControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private UsersOrderInitializer usersOrderInitializer;

    @InjectMocks
    private MicroServiceController microServiceController;

    private Order order;

    @BeforeEach
    void setUp() {
        // Création d'une commande
        order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setAmount(100L);
        order.setTotalAmount(200L);
        order.setOrderStatus(OrderStatus.EnCours);
        order.setTrackingId(UUID.randomUUID());
    }

    // 1 : Test mise à jour totale d'une commande
    @Test
    void saveOrderTest() {
        // Appel direct du contrôleur
        microServiceController.save(order);

        // Vérification que le service a été appelé
        verify(orderService, times(1)).updateOrderTotal(order);
    }

    // 2 : Test création d'une nouvelle commande
    @Test
    void newOrderTest() {
        // Appel direct du contrôleur
        microServiceController.orderSave(order);

        // Vérification que le service a été appelé
        verify(orderService, times(1)).newOrder(order);
    }

    // 3 : Test recherche commande par userId + status
    @Test
    void findByUserIdAndOrderStatus() {

        Map<String, String> mapOrder = Map.of("userId", "1");

        // Simulation du service
        when(orderService.findByUserIdAndOrderStatus(mapOrder)).thenReturn(order);

        // Appel direct du contrôleur
        Order response = microServiceController.findByUserIdAndOrderStatus(mapOrder);

        // Vérification du résultat
        assertEquals(order.getId(), response.getId());

        // Vérification que le service a été appelé
        verify(orderService, times(1)).findByUserIdAndOrderStatus(mapOrder);
    }

    // 4 : Test synchronisation des commandes
    @Test
    void synchronizeOrders() {
        // Appel direct du contrôleur
        ResponseEntity<Void> response = microServiceController.synchronizeOrders();

        // Vérification du code HTTP
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Vérification que le synchronizer a été appelé
        verify(usersOrderInitializer, times(1)).synchronize();
    }

    // 5 : Test récupération commande par Id
    @Test
    void findById_shouldReturnOrder() {
        // Simulation du service
        when(orderService.findById(1L)).thenReturn(order);

        // Appel direct du contrôleur
        Order response = microServiceController.findById(1L);

        // Vérification du résultat
        assertEquals(order.getId(), response.getId());

        // Vérification que le service a été appelé
        verify(orderService, times(1)).findById(1L);
    }
}
