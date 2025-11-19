package com.ecom.validation.service;

import com.ecom.validation.controller.ValidationController;
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

class ValidationControllerTest {

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private ValidationController validationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    // 1 : TEST envoyer un nouveau code
    @Test
    void testNewSendCode() {
        Long validationId = 10L;
        Map<String, Long> body = Map.of("id", validationId);

        // Réponse simulée du service
        ResponseEntity<?> mockResponse = ResponseEntity.status(HttpStatus.CREATED)
                .body("Votre code a été envoyé");

        // Simule de l’appel service
        when(validationService.sendNewCode(anyLong()))
                .thenReturn((ResponseEntity) mockResponse);

        // Appel du controller
        ResponseEntity<?> response = validationController.newSendCode(body);

        // Vérifications
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Votre code a été envoyé", response.getBody());

        // Vérifie que le service a été appelé
        verify(validationService, times(1)).sendNewCode(validationId);
    }
}
