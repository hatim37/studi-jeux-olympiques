package com.ecom.orders.service;

import com.ecom.orders.controller.customer.CartController;
import com.ecom.orders.dto.OrderDto;
import com.ecom.orders.dto.PlaceOrderDto;
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

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private CartController cartController;

    private PlaceOrderDto placeOrderDto;
    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        // Création d'un DTO PlaceOrder
        placeOrderDto = new PlaceOrderDto();
        placeOrderDto.setUserId(1L);

        // Création d'un OrderDto fictif
        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(1L);
        orderDto.setAmount(100L);
        orderDto.setOrderStatus(OrderStatus.Valider);
        orderDto.setTrackingId(UUID.randomUUID());
    }

    // 1 : Test passer une commande
    @Test
    void placeOrder() throws Exception {
        // Simulation du service
        when(orderService.placeOrder(placeOrderDto)).thenReturn(orderDto);

        // Appel direct du contrôleur
        ResponseEntity<OrderDto> response = cartController.placeOrder(placeOrderDto);

        // Vérification du code HTTP et du contenu
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderDto.getId(), response.getBody().getId());

        // Vérification que le service a été appelé
        verify(orderService, times(1)).placeOrder(placeOrderDto);
    }

    // 2 : Test récupération des commandes d'un utilisateur
    @Test
    void getMyOrdersById() {
        // Simulation du service
        when(orderService.getMyPlacedOrders(1L)).thenReturn(List.of(orderDto));

        // Appel direct du contrôleur
        ResponseEntity<List<OrderDto>> response = cartController.getMyOrders(1L);

        // Vérification du code HTTP et du contenu
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(orderDto.getId(), response.getBody().get(0).getId());

        // Vérification que le service a été appelé
        verify(orderService, times(1)).getMyPlacedOrders(1L);
    }
}
