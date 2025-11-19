package com.ecom.security.service;

import com.ecom.security.dto.LoginActivationDto;
import com.ecom.security.entity.DevicesId;
import com.ecom.security.repository.DevicesIdRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivationDeviceIdServiceTest {

    @Mock
    private DevicesIdRepository devicesRepository;

    @InjectMocks
    private ActivationDeviceIdService activationDeviceIdService;

    // 1 : activation d'un device ID
    @Test
    void activationDeviceIdService() {
        // Préparation des données d’entrée
        LoginActivationDto dto = new LoginActivationDto();
        dto.setDeviceId(1L);
        dto.setUserId(10L);
        dto.setActive(false);

        DevicesId device = new DevicesId();
        device.setId(1L);
        device.setActive(false);

        // on récupère un device id
        when(devicesRepository.findById(1L)).thenReturn(Optional.of(device));

        // Appel de la méthode
        activationDeviceIdService.activationDeviceIdService(dto);

        // Vérification : le device = true
        assertTrue(device.getActive());

        // Vérification : la sauvegarde du device a bien été effectuée
        verify(devicesRepository, times(1)).save(device);
    }

    // 2 : activation device id inexistant
    @Test
    void activationDeviceIdService_shouldDoNothingWhenDeviceNotFound() {
        // Préparation d’un DTO avec un ID inexistant
        LoginActivationDto dto = new LoginActivationDto();
        dto.setDeviceId(999L);

        // Simulation : aucun device trouvé
        when(devicesRepository.findById(999L)).thenReturn(Optional.empty());

        // Appel de la méthode
        activationDeviceIdService.activationDeviceIdService(dto);

        // Vérification : aucune sauvegarde effectuée
        verify(devicesRepository, never()).save(any());
    }
}