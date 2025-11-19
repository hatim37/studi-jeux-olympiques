package com.ecom.security.service;

import com.ecom.security.clients.UserRestClient;
import com.ecom.security.controller.LoginController;
import com.ecom.security.dto.AuthentificationDTO;
import com.ecom.security.entity.JwtUser;
import com.ecom.security.repository.JwtRepository;
import com.ecom.security.response.UserNotFoundException;
import com.ecom.security.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Mock
    private UserRestClient userRestClient;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtRepository jwtRepository;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    // 1 : connexion d'un utilisateur
    @Test
    void signin() {
        // Préparation des données
        AuthentificationDTO dto = new AuthentificationDTO("user@test.com", "password123", "device123");

        // Simulation d’un utilisateur authentifié
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        // Simulation : authentification réussie
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Simulation : génération du token JWT
        Map<String, String> fakeToken = Map.of("bearer", "jwt_token_example");
        when(jwtService.generate(userDetails, dto.devices())).thenReturn(ResponseEntity.ok(fakeToken));

        // Appel de la méthode du contrôleur
        ResponseEntity<Map<String, String>> response = loginController.signin(dto);

        // Vérifications
        assertThat(response.getBody()).isEqualTo(fakeToken);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        // Vérification que les dépendances ont bien été appelées
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generate(any(), eq(dto.devices()));
    }

    // 2 : validation du token avec UUID
    @Test
    void signinValidation() {
        // Préparation d’un UUID
        UUID uuid = UUID.randomUUID();

        // Création d’un JWT en attente
        JwtUser jwtUser = new JwtUser();
        jwtUser.setUuid(uuid);
        jwtUser.setPending(true);
        jwtUser.setToken("jwt_token_value");

        // Simulation de JWT
        when(jwtRepository.findByUuid(uuid)).thenReturn(Optional.of(jwtUser));
        when(jwtRepository.save(any(JwtUser.class))).thenReturn(jwtUser);

        // Appel de la méthode
        Map<String, String> uuidMap = Map.of("uuid", uuid.toString());
        ResponseEntity<Map<String, String>> response = loginController.signinValidation(uuidMap);

        // Vérifications
        assertThat(response.getBody()).containsEntry("bearer", "jwt_token_value");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(jwtUser.getPending()).isFalse();

        // Vérification que la sauvegarde a été effectuée
        verify(jwtRepository, times(1)).save(jwtUser);
    }

    // 3 : Vérifie que l’exception UserNotFoundException est levée
    @Test
    void signin_withThrowException() {
        // Préparation
        AuthentificationDTO dto = new AuthentificationDTO("invalid@test.com", "wrongpass", "device123");

        // Simulation : authentification échoue
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        // Vérification de l’exception
        assertThrows(UserNotFoundException.class, () -> loginController.signin(dto));
    }
}