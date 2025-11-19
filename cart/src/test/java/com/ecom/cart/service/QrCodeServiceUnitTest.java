package com.ecom.cart.service;

import com.ecom.cart.clients.OrderRestClient;
import com.ecom.cart.clients.ProductRestClient;
import com.ecom.cart.clients.UserRestClient;
import com.ecom.cart.dto.DecryptDto;
import com.ecom.cart.entity.CartItems;
import com.ecom.cart.model.Order;
import com.ecom.cart.model.Product;
import com.ecom.cart.model.User;
import com.ecom.cart.repository.CartRepository;
import com.ecom.cart.response.UserNotFoundException;
import com.ecom.cart.services.QrCodeService;
import com.ecom.cart.services.TokenTechnicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class QrCodeServiceUnitTest {

    @InjectMocks
    private QrCodeService qrCodeService;

    @Mock
    private UserRestClient userRestClient;

    @Mock
    private OrderRestClient orderRestClient;

    @Mock
    private TokenTechnicService tokenTechnicService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRestClient productRestClient;

    private User user;
    private Order order;
    private Product product;
    private CartItems cartItem;

    @BeforeEach
    void setUp() {
        // Initialisation
        MockitoAnnotations.openMocks(this);

        // Création d’un utilisateur avec clé secrète
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSecretKey(Base64.getEncoder().encodeToString("userKey12345678".getBytes()));

        // Création d’une commande avec clé secrète
        order = new Order();
        order.setId(1L);
        order.setUserId(user.getId());
        order.setSecretKey(Base64.getEncoder().encodeToString("orderKey12345678".getBytes()));

        // Produit pour QR code
        product = new Product();
        product.setId(1L);
        product.setName("Ticket");
        product.setPrice(50L);

        // CartItem pour QR code
        cartItem = new CartItems();
        cartItem.setId(1L);
        cartItem.setUserId(user.getId());
        cartItem.setOrderId(order.getId());
        cartItem.setProductId(product.getId());
        cartItem.setQuantity(2L);
    }

    // 1 : Test de génération de la clé secrète combinée
    @Test
    void getKeyFormUserAndOrder_shouldReturnSecretKey() throws Exception {
        // On simule le token
        when(tokenTechnicService.getTechnicalToken()).thenReturn("token");

        // On simule la récupération des clés de l’utilisateur et de la commande
        when(userRestClient.findUserById(anyString(), eq(user.getId()))).thenReturn(user);
        when(orderRestClient.findById(anyString(), eq(order.getId()))).thenReturn(order);

        // Appel de la méthode testée
        SecretKeySpec key = qrCodeService.getKeyFormUserAndOrder(user.getId(), order.getId());

        // Vérification, clé non nulle et type AES
        assertThat(key).isNotNull();
        assertThat(key.getAlgorithm()).isEqualTo("AES");
    }

    // 2 : Test execption UserNotFoundException si utilisateur inexistant
    @Test
    void getKeyFormUserAndOrder_shouldThrowUserNotFound() {
        // On simule le token
        when(tokenTechnicService.getTechnicalToken()).thenReturn("token");

        // On simule utilisateur sans Id
        when(userRestClient.findUserById(anyString(), eq(user.getId()))).thenReturn(new User());

        // Vérification, exception levée avec message
        assertThatThrownBy(() -> qrCodeService.getKeyFormUserAndOrder(user.getId(), order.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Utilisateur introuvable");
    }

    // 3 : Test exception UserNotFoundException si commande inexistante
    @Test
    void getKeyFormUserAndOrder_shouldThrowOrderNotFound() {
        // On simule le token
        when(tokenTechnicService.getTechnicalToken()).thenReturn("token");

        // On simule utilisateur valide
        when(userRestClient.findUserById(anyString(), eq(user.getId()))).thenReturn(user);

        // On simule commande inexistante
        when(orderRestClient.findById(anyString(), eq(order.getId()))).thenReturn(new Order());

        // Vérification, exception levée avec message
        assertThatThrownBy(() -> qrCodeService.getKeyFormUserAndOrder(user.getId(), order.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("N° de commande introuvable");
    }

    // 4 : Test génération d’un QR code pour un utilisateur et une commande valides
    @Test
    void generateQrCode_shouldCreateQrCodeForCartItems() throws Exception {

        // Simulation du token technique
        when(tokenTechnicService.getTechnicalToken()).thenReturn("token");

        // Simulation de la récupération de l'utilisateur
        when(userRestClient.findUserById(anyString(), eq(user.getId()))).thenReturn(user);

        // Simulation de la récupération de l'Order avec une clé valide
        Order orderMock = new Order();
        orderMock.setId(order.getId());
        orderMock.setSecretKey(Base64.getEncoder().encodeToString("orderKey12345678".getBytes()));
        when(orderRestClient.findById(anyString(), eq(order.getId()))).thenReturn(orderMock);

        // Simulation des cartItems existants pour cette commande
        when(cartRepository.findByOrderId(order.getId())).thenReturn(List.of(cartItem));

        // Simulation de récupération du produit correspondant au cartItem
        when(productRestClient.findById(anyString(), eq(product.getId()))).thenReturn(product);

        // Simulation de la sauvegarde du cartItem
        when(cartRepository.save(any(CartItems.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //Appel de la méthode testée
        qrCodeService.generateQrCode(user.getId(), order.getId());

        // Vérification : le QR code = ok
        assertThat(cartItem.getQrCode()).isNotNull();
        assertThat(cartItem.getQrCode().length).isGreaterThan(0);
    }

    // 5 : Test exception si l'utilisateur est introuvable lors de la génération du QR code
    @Test
    void generateQrCode() {
        // Simulation du token technique
        when(tokenTechnicService.getTechnicalToken()).thenReturn("token");

        // Simulation d'un utilisateur inexistant (id null)
        when(userRestClient.findUserById(anyString(), eq(user.getId()))).thenReturn(new User());

        // Vérification : exception UserNotFoundException levée avec message attendu
        assertThatThrownBy(() -> qrCodeService.generateQrCode(user.getId(), order.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Service indisponible");
    }

    // 6 : Test decryptQrCode avec un fichier QR code invalide
    @Test
    void decryptQrCode() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("img", "qr.png", "image/png", new byte[10]);

        assertThatThrownBy(() -> qrCodeService.decryptQrCode(mockFile))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erreur lecture QR code");
    }

    // 7 : Test decryptKey avec un code chiffré valide et vérification du résultat
    @Test
    void decryptKey() throws Exception {
        // Simulation du token technique
        when(tokenTechnicService.getTechnicalToken()).thenReturn("token");

        // Simulation récupération des clés utilisateur et commande
        when(userRestClient.findUserById(anyString(), eq(user.getId()))).thenReturn(user);
        when(orderRestClient.findById(anyString(), eq(order.getId()))).thenReturn(order);

        // Génération d’un code chiffré
        String encryptedCode = qrCodeService.encryptKey(user.getId(), order.getId(), user.getName());

        // Appel de la méthode decryptKey
        DecryptDto result = qrCodeService.decryptKey(user.getId(), order.getId(), encryptedCode).getBody();

        // Vérification : résultat non null + nom de l’utilisateur = ok
        assertThat(result).isNotNull();
        assertThat(result.getOutputCode()).contains(user.getName());
    }
}