package com.ecom.orders.service;

import com.ecom.orders.entity.Order;
import com.ecom.orders.enums.OrderStatus;
import com.ecom.orders.repository.OrderRepository;
import com.ecom.orders.services.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceUnitTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private OrderRepository orderRepository;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Création d'une commande
        order = new Order();
        order.setId(1L);
        order.setAmount(100L);
        order.setTotalAmount(100L);
        order.setOrderStatus(OrderStatus.Valider);

    }

    // 1 : calcule la somme des montants des commandes validées sur un mois
    @Test
    void getTotalEarningsForMonth_shouldReturnCorrectSum() {
        int month = 10;
        int year = 2025;

        // date de la commande
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 15);
        order.setDate(cal.getTime());

        List<Order> orders = List.of(order);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startOfMonth = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endOfMonth = calendar.getTime();

        when(orderRepository.findByDateBetweenAndOrderStatus(any(Date.class), any(Date.class), eq(OrderStatus.Valider)))
                .thenReturn(orders);

        Long total = adminService.getTotalEarningsForMonth(month, year);

        assertEquals(100L, total);
        verify(orderRepository, times(1))
                .findByDateBetweenAndOrderStatus(any(Date.class), any(Date.class), eq(OrderStatus.Valider));

    }

    // 2 : Calcule le nombre de commandes validées sur un mois
    @Test
    void getTotalOrdersForMonth_shouldReturnCorrectCount() {
        int month = 10;
        int year = 2025;

        List<Order> orders = List.of(order, new Order());

        // Calcul du début et de la fin du mois
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startOfMonth = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endOfMonth = calendar.getTime();

        System.out.println(">>> startOfMonth = " + startOfMonth);
        System.out.println(">>> endOfMonth   = " + endOfMonth);

        // on simule la recherche
        when(orderRepository.findByDateBetweenAndOrderStatus(any(Date.class), any(Date.class), eq(OrderStatus.Valider)))
                .thenReturn(orders);

        // Appel de la méthode testée
        Long count = adminService.getTotalOrdersForMonth(month, year);

        // Vérification du résultat
        System.out.println(">>> count = " + count);
        assertEquals(2L, count);
        verify(orderRepository, times(1))
                .findByDateBetweenAndOrderStatus(any(Date.class), any(Date.class), eq(OrderStatus.Valider));

    }

}
