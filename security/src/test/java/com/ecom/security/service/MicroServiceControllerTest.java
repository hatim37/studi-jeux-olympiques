package com.ecom.security.service;

import com.ecom.security.controller.MicroServiceController;
import com.ecom.security.dto.LoginActivationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MicroServiceControllerTest {

    @Mock
    private ActivationDeviceIdService activationDeviceIdService;

    @InjectMocks
    private MicroServiceController microServiceController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // 1 : activation d'un device ID
    @Test
    void activationDeviceId() {
        // Préparation des données d’entrée (DTO simulé)
        LoginActivationDto dto = new LoginActivationDto();
        dto.setUserId(1L);
        dto.setDeviceId(10L);
        dto.setActive(true);

        // Appel de la méthode du contrôleur
        microServiceController.activationDeviceId(dto);

        // Vérification : le service a bien été appelé
        verify(activationDeviceIdService, times(1))
                .activationDeviceIdService(dto);
    }
}

