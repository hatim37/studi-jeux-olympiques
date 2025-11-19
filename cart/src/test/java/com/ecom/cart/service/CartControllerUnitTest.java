package com.ecom.cart.service;

import com.ecom.cart.controller.CartController;
import com.ecom.cart.dto.AddProductInCartDto;
import com.ecom.cart.dto.CartItemsDto;
import com.ecom.cart.dto.DecryptDto;
import com.ecom.cart.dto.OrderDto;
import com.ecom.cart.response.UserNotFoundException;
import com.ecom.cart.services.CartService;
import com.ecom.cart.services.QrCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerUnitTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    @Mock
    private QrCodeService qrCodeService;


    private AddProductInCartDto addProductDto;
    private OrderDto orderDto;
    private CartItemsDto cartItemsDto;
    private DecryptDto decryptDto;

    @BeforeEach
    void setUp() {
        // Initialisation d'un produit
        addProductDto = new AddProductInCartDto();
        addProductDto.setUserId(1L);
        addProductDto.setProductId(1L);
        addProductDto.setQuantity(2L);

        // Initialisation d'un panier fictif
        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(1L);

        cartItemsDto = new CartItemsDto();
        cartItemsDto.setId(1L);
        cartItemsDto.setUserId(1L);

        decryptDto = new DecryptDto();
        decryptDto.setUserId(1L);
        decryptDto.setOrderId(1L);
        decryptDto.setInputCode("fakeCode");
    }

    // 1 : Ajout de produits dans le panier
    @Test
    void addCaddies_ReturnOk() {
        // On simule le service pour qu'il retourne OK
        when(cartService.addCaddies(anyLong(), anyList())).thenReturn(ResponseEntity.ok().build());

        // Appel du contrôleur
        ResponseEntity<?> response = cartController.addCaddies(1L, List.of(addProductDto));

        // Vérification que le statut HTTP est OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Vérification que le service a bien été appelé avec les bons paramètres
        verify(cartService, times(1)).addCaddies(1L, List.of(addProductDto));
    }

    // 2 : Ajout de produits, exception si commande introuvable
    @Test
    void addCaddies_ThrowException() {
        // On simule que le service lance une exception UserNotFoundException
        when(cartService.addCaddies(anyLong(), anyList())).thenThrow(UserNotFoundException.class);

        // Vérification que le contrôleur relaie bien l'exception
        assertThrows(UserNotFoundException.class, () -> cartController.addCaddies(1L, List.of(addProductDto)));
    }

    // 3 : Récupération du panier d'un utilisateur
    @Test
    void getCartByUserId_shouldReturnOrderDto() {
        // On simule le service pour qu'il retourne un OrderDto
        when(cartService.getCartByUserId(1L)).thenReturn(orderDto);

        // Appel du contrôleur
        ResponseEntity<?> response = cartController.getCartByUserId(1L);

        // Vérification que le statut HTTP est OK et que le body est correct
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDto, response.getBody());

        // Vérification que le service a été appelé une seule fois avec le bon userId
        verify(cartService, times(1)).getCartByUserId(1L);
    }

    // 4 : Ajout d'un produit au panier via addProductToCart = succès
    @Test
    void addProductToCart_shouldReturnOk() {
        // On simule le service pour qu'il retourne OK
        when(cartService.addProductToCart(addProductDto)).thenReturn(ResponseEntity.ok().build());

        // Appel du contrôleur
        ResponseEntity<?> response = cartController.addProductToCart(addProductDto);

        // Vérification du statut HTTP OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Vérification que le service a été appelé avec le DTO correct
        verify(cartService, times(1)).addProductToCart(addProductDto);
    }

    // 5 : Suppression d'un produit = succès
    @Test
    void deleteProduct_returnTrue() {
        // On simule que le service supprime avec succès le produit
        when(cartService.deleteCartById(1L)).thenReturn(true);

        // Appel du contrôleur
        ResponseEntity<?> response = cartController.deleteProduct(1L);

        // Vérification que le statut HTTP = NO_CONTENT
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Vérification que le service a été appelé avec le bon ID
        verify(cartService, times(1)).deleteCartById(1L);
    }

    // 6 : Suppression d'un produit = produit introuvable
    @Test
    void deleteProduct_returnFalse() {
        // On simule que le service n'a pas trouvé le produit à supprimer
        when(cartService.deleteCartById(1L)).thenReturn(false);

        // Appel du contrôleur
        ResponseEntity<?> response = cartController.deleteProduct(1L);

        // Vérification, statut HTTP = NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Vérification que le service a été appelé exactement une fois
        verify(cartService, times(1)).deleteCartById(1L);
    }

    // 7 : getQrCodeById = succès
    @Test
    void getQrCodeById_returnQrCodeDto() {
        // Simule le retour du service avec un CartItemsDto
        when(cartService.getQrCodeById(1L)).thenReturn(cartItemsDto);

        // Appel du contrôleur
        ResponseEntity<?> response = cartController.getQrCodeById(1L);

        // Vérifie que le status HTTP est 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Vérifie que le body retourné correspond au DTO attendu
        assertEquals(cartItemsDto, response.getBody());

        // Vérifie que le service a été appelé une fois
        verify(cartService, times(1)).getQrCodeById(1L);
    }


    // 8 : decryptKeyInQrCode = succès
    @Test
    void decryptKeyInQrCode_returnDecryptDto() throws Exception {
        // Création d'un DTO de retour simulé pour le décryptage
        DecryptDto returnedDto = new DecryptDto();
        returnedDto.setOutputCode("decryptedValue");

        // Simule le retour du service QrCodeService
        when(qrCodeService.decryptKey(anyLong(), anyLong(), anyString())).thenReturn(ResponseEntity.ok(returnedDto));

        // Appel du contrôleur
        ResponseEntity<DecryptDto> response = cartController.decryptKeyInQrCode(decryptDto);

        // Vérifie que le status HTTP = 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Vérifie la valeur décryptée
        assertEquals("decryptedValue", response.getBody().getOutputCode());

        // Vérifie que le service a été appelé exactement une fois
        verify(qrCodeService, times(1))
                .decryptKey(decryptDto.getUserId(), decryptDto.getOrderId(), decryptDto.getInputCode());
    }
}
