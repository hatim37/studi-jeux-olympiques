package com.ecom.validation.service;

import com.ecom.validation.controller.ActivationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ActivationControllerTest {

    @Mock
    private ActivationService activationService;

    @InjectMocks
    private ActivationController activationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test valider un code
    @Test
    void testValidCode() {

        Map<String, String> requestMap = Map.of(
                "code", "ABC123",
                "password", "secretPwd"
        );

        // simulation du service
        ResponseEntity<Object> mockResponse = ResponseEntity.ok("Activation réussie");

        // Simulation de la réponse
        when(activationService.activation(anyString(), anyString()))
                .thenReturn((ResponseEntity) mockResponse);

        // 3) Appel du controller
        ResponseEntity<?> response = activationController.validCode(requestMap);

        // 4) Vérifications
        assertNotNull(response, "La réponse du controller ne doit pas être null");
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "Le controller doit renvoyer HTTP 200 OK");

        assertEquals("Activation réussie", response.getBody(),
                "Le body retourné est incorrect");

        // Vérifie que le service a été appelé
        verify(activationService, times(1))
                .activation("ABC123", "secretPwd");
    }
}
