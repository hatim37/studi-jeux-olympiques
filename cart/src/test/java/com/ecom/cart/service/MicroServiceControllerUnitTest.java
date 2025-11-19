package com.ecom.cart.service;

import com.ecom.cart.controller.MicroServiceController;
import com.ecom.cart.entity.CartItems;
import com.ecom.cart.services.CartService;
import com.ecom.cart.services.QrCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MicroServiceControllerUnitTest {

    @InjectMocks
    private MicroServiceController microServiceController;

    @Mock
    private QrCodeService qrCodeService;

    @Mock
    private CartService cartService;

    private Map<String, Long> qrCodeRequest;
    private List<CartItems> cartItemsList;

    @BeforeEach
    void setUp() {
        // Création QR code
        qrCodeRequest = Map.of("userId", 1L, "orderId", 1L);

        // Création d'une liste de CartItems
        CartItems cartItem = new CartItems();
        cartItem.setId(1L);
        cartItem.setUserId(1L);
        cartItem.setOrderId(1L);
        cartItemsList = List.of(cartItem);
    }

    // 1 : generateQrCode = vérifie l'appel du service
    @Test
    void generateQrCode() {
        // Appel du contrôleur avec la requête simulée
        microServiceController.generateQrCode(qrCodeRequest);

        // Vérifie que la méthode generateQrCode
        verify(qrCodeService, times(1))
                .generateQrCode(qrCodeRequest.get("userId"), qrCodeRequest.get("orderId"));
    }

    // 2 : Recherche par QrCode
    @Test
    void findByQrCode() {
        // Simule le retour du service avec une liste de CartItems
        when(cartService.findByQrCodeIsNotNull()).thenReturn(cartItemsList);

        // Appel du contrôleur
        List<CartItems> response = microServiceController.findByQrCodeIsNotNull();

        // Vérifie la liste retournée
        assertEquals(cartItemsList, response);

        // Vérifie que le service a été appelé une fois
        verify(cartService, times(1)).findByQrCodeIsNotNull();
    }

}