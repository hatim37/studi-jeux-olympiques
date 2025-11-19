package com.ecom.orders.service;

import com.ecom.orders.controller.admin.AdminOrderController;
import com.ecom.orders.dto.AnalyticsResponse;
import com.ecom.orders.dto.OrderDto;
import com.ecom.orders.enums.OrderStatus;
import com.ecom.orders.services.AdminService;
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
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminOrderController adminOrderController;

    // DTO factice pour les tests
    private OrderDto orderDto;
    private AnalyticsResponse analyticsResponse;

    @BeforeEach
    void setUp() {
        // Création d'un OrderDto
        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(1L);
        orderDto.setAmount(100L);
        orderDto.setTotalAmount(200L);
        orderDto.setOrderStatus(OrderStatus.Valider);
        orderDto.setTrackingId(UUID.randomUUID());

        // Création d'un AnalyticsResponse
        analyticsResponse = new AnalyticsResponse();
        analyticsResponse.setPlaced(5L);
        analyticsResponse.setCurrentMonthOrders(3L);
        analyticsResponse.setPreviousMonthOrders(2L);
        analyticsResponse.setCurrentMonthEarnings(300L);
        analyticsResponse.setPreviousMonthEarnings(200L);
        analyticsResponse.setProductStats(List.of());
    }

    // 1 : Test récupération de toutes les commandes validées
    @Test
    void getAllPlacedOrders_shouldReturnListOfOrderDto() {
        // Simulation du service
        when(adminService.getAllPlacedOrders()).thenReturn(List.of(orderDto));

        // Appel direct du contrôleur
        ResponseEntity<List<OrderDto>> response = adminOrderController.getAllPlacedOrders();

        // Vérification du code HTTP et du contenu
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(orderDto.getId(), response.getBody().get(0).getId());

        // Vérification que le service a été appelé
        verify(adminService, times(1)).getAllPlacedOrders();
    }

    // 2 : Test récupération des analytics
    @Test
    void getAnalyticsResponse_shouldReturnAnalytics() {
        // Simulation du service
        when(adminService.calculateAnalytics()).thenReturn(analyticsResponse);

        // Appel direct du contrôleur
        ResponseEntity<AnalyticsResponse> response = adminOrderController.getAnalyticsResponse();

        // Vérification du code HTTP et du contenu
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(analyticsResponse.getPlaced(), response.getBody().getPlaced());

        // Vérification que le service a été appelé
        verify(adminService, times(1)).calculateAnalytics();
    }
}
