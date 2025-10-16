package com.ecom.orders.service;

import com.ecom.orders.clients.CartRestClient;
import com.ecom.orders.clients.ProductRestClient;
import com.ecom.orders.dto.OrderDto;
import com.ecom.orders.dto.ProductAnalyticsDto;
import com.ecom.orders.entity.Order;
import com.ecom.orders.enums.OrderStatus;
import com.ecom.orders.repository.OrderRepository;
import com.ecom.orders.services.AdminService;
import com.ecom.orders.services.TokenTechnicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminServiceIntegrationTest {

    @Autowired
    private AdminService adminService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private CartRestClient cartRestClient;

    @MockBean
    private ProductRestClient productRestClient;

    @MockBean
    private TokenTechnicService tokenTechnicService;

    private Order order;

    @BeforeEach
    void setUp() {
        // Création d'une commande
        order = new Order();
        order.setId(1L);
        order.setAmount(100L);
        order.setTotalAmount(100L);
        order.setOrderStatus(OrderStatus.Valider);
        order.setUserId(1L);
    }

    // 1 : Vérifie récupération de toutes les commandes validées
    @Test
    void getAllPlacedOrders_shouldReturnList() {
        // On simule la recherche
        when(orderRepository.findByOrderStatusIn(List.of(OrderStatus.Valider))).thenReturn(List.of(order));

        // Appel du service
        List<OrderDto> result = adminService.getAllPlacedOrders();

        // Vérification
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getId()).isEqualTo(order.getId());
        verify(orderRepository, times(1)).findByOrderStatusIn(anyList());
    }

    // 2 : Vérifie calcul des analytics
    @Test
    void calculateAnalytics_shouldReturnAnalytics() {
        // On simule la recherche des stats
        when(orderRepository.countByOrderStatus(OrderStatus.Valider)).thenReturn(1L);
        when(orderRepository.findByDateBetweenAndOrderStatus(any(), any(), eq(OrderStatus.Valider)))
                .thenReturn(List.of(order));

        // Appel du service
        var result = adminService.calculateAnalytics();

        // Vérification, analytics = ok
        assertThat(result).isNotNull();
        assertThat(result.getPlaced()).isEqualTo(1L);
        assertThat(result.getCurrentMonthOrders()).isEqualTo(1L);
        assertThat(result.getPreviousMonthOrders()).isEqualTo(1L);
    }

    // 3 : Vérifie récupération des stats produits par mois
    @Test
    void getProductStatsByMonth_shouldReturnList() {
        // On simule la liste des articles vendus
        when(tokenTechnicService.getTechnicalToken()).thenReturn("token");
        when(cartRestClient.findByQrCodeIsNotNull(anyString())).thenReturn(Collections.emptyList());

        // Appel du service
        List<ProductAnalyticsDto> stats = adminService.getProductStatsByMonth();

        // Vérification, liste non nulle
        assertThat(stats).isNotNull();
        assertThat(stats).isEmpty();
    }
}
