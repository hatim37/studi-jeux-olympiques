package com.ecom.users.service;

import com.ecom.users.controller.MicroServiceController;
import com.ecom.users.dto.NewPasswordDto;
import com.ecom.users.dto.UserDto;
import com.ecom.users.dto.UserLoginDto;
import com.ecom.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class MicroServiceControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private MicroServiceController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 1 : Test de l'activation d'un utilisateur
    @Test
    void testActivationDeviceId() {
        // Préparation d'un DTO pour l'activation
        var userActivationDto = new com.ecom.users.dto.UserActivationDto();
        userActivationDto.setUserId(1L);

        // Appel de la méthode
        controller.activationDeviceId(userActivationDto);

        // Vérifie que le service a été appelé exactement une fois avec le DTO
        verify(userService, times(1)).activationUser(userActivationDto);
    }

    // 2 : Test récupération d'un utilisateur par email (login)
    @Test
    void testUserLogin() {
        String email = "test@example.com";

        // Création d'un utilisateur mocké
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("Test");
        mockUser.setUsername("testuser");
        mockUser.setEmail(email);
        mockUser.setPassword("password");

        // Simulation du service
        when(userService.findByEmail(email)).thenReturn(mockUser);

        // Appel de la méthode
        UserLoginDto result = controller.userLogin(email);

        // Vérifications
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test", result.getName());
        assertEquals("test@example.com", result.getEmail());

        // Vérifie que le service a bien été appelé
        verify(userService, times(1)).findByEmail(email);
    }

    // 3 : Test récupération d'un utilisateur par ID
    @Test
    void testFindById() {
        Long userId = 1L;
        // Création d'un utilisateur
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setName("Test");

        // Simulation du service
        when(userService.findById(userId)).thenReturn(mockUser);

        // Appel du controller
        User result = controller.findById(userId);

        // Vérification
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Test", result.getName());

        // Vérifie que le service a été appelé
        verify(userService, times(1)).findById(userId);
    }

    // 4 : Test récupération de tous les utilisateurs
    @Test
    void testGetAllUsers() {
        // Création d'utilisateurs
        User mockUser1 = new User();
        mockUser1.setId(1L);
        mockUser1.setName("User1");

        User mockUser2 = new User();
        mockUser2.setId(2L);
        mockUser2.setName("User2");

        // Création d'une liste de DTO
        List<UserDto> mockUsers = List.of(new UserDto(mockUser1), new UserDto(mockUser2));

        // Simulation du service
        when(userService.findAll()).thenReturn(mockUsers);

        // Appel du controller
        List<UserDto> result = controller.getUsers();

        // Vérification
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("User1", result.get(0).getName());

        // Vérifie que le service a été appelé
        verify(userService, times(1)).findAll();
    }

    // 5 : Test modification du mot de passe
    @Test
    void testNewPassword() {
        NewPasswordDto newPasswordDto = new NewPasswordDto();
        newPasswordDto.setUserId(1L);
        newPasswordDto.setPassword("newpass");

        // Simulation du service
        ResponseEntity<String> mockResponse = new ResponseEntity<>("Votre mot de passe a été modifié avec succès !", HttpStatus.OK);
        when(userService.newPassword(newPasswordDto))
                .thenReturn((ResponseEntity) mockResponse);

        // Appel du controller
        ResponseEntity<?> response = controller.newPassword(newPasswordDto);

        // Vérification
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Votre mot de passe a été modifié avec succès !", response.getBody());

        // Vérifie que le service a été appelé
        verify(userService, times(1)).newPassword(newPasswordDto);
    }
}
