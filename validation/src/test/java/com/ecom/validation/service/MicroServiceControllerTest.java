package com.ecom.validation.service;

import com.ecom.validation.controller.MicroServiceController;
import com.ecom.validation.dto.ValidationDto;
import com.ecom.validation.entity.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class MicroServiceControllerTest {

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private MicroServiceController microServiceController;

    private ValidationDto validationDto;
    private Validation validation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Préparation d’un DTO
        validationDto = new ValidationDto();
        validationDto.setUserId(1L);
        validationDto.setEmail("user@mail.com");
        validationDto.setUsername("john");
        validationDto.setType("registration");

        // Préparation d’un objet Validation simulé
        validation = new Validation();
        validation.setUserId(1L);
        validation.setEmail("user@mail.com");
        validation.setUsername("john");
        validation.setType("registration");
        validation.setActive(false);
    }

    // 1 : Test envoi d'un code validation
    @Test
    void testSendCode() {
        // Simulation du service
        ResponseEntity<Validation> mockResponse = ResponseEntity.ok(validation);
        when(validationService.save(any(ValidationDto.class))).thenReturn(mockResponse);

        // Appel du controller
        ResponseEntity<Validation> response = microServiceController.sendCode(validationDto);

        // Vérifications
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(validation, response.getBody());

        // Vérifie que le service a été appelé
        verify(validationService, times(1)).save(validationDto);
    }

}
