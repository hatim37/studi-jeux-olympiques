package com.ecom.security.service;


import com.ecom.security.clients.UserRestClient;
import com.ecom.security.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Mock
    private UserRestClient userRestClient;

    @Mock
    private TokenMicroService tokenMicroService;

    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDetailsService = new UserDetailsServiceImpl();
        userDetailsService.userRepository = userRestClient;
        userDetailsService.tokenMicroService = tokenMicroService;
    }

    // 1 : retourne un User
    @Test
    void loadUserByUsername() {
        // Préparation
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setActive(true);

        when(tokenMicroService.tokenService()).thenReturn("mock-token");
        when(userRestClient.findByEmailLogin("Bearer mock-token", "test@example.com")).thenReturn(user);

        // 🔹 Appel de la méthode
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        // 🔹 Vérifications
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertTrue(userDetails instanceof UserDetailsImpl);

        verify(tokenMicroService, times(1)).tokenService();
        verify(userRestClient, times(1)).findByEmailLogin("Bearer mock-token", "test@example.com");
    }

    // 2 : recherche d'un utilisateur non trouvé, exception levée
    @Test
    void loadUserByUsername_whenUserNotFound() {
        // 🔹 Préparation du mock
        User user = new User();
        user.setName("non trouvée");
        when(tokenMicroService.tokenService()).thenReturn("mock-token");
        when(userRestClient.findByEmailLogin("Bearer mock-token", "unknown@example.com")).thenReturn(user);

        // 🔹 Vérification de l’exception
        assertThrows(RuntimeException.class, () ->
                userDetailsService.loadUserByUsername("unknown@example.com"));

        verify(tokenMicroService, times(1)).tokenService();
        verify(userRestClient, times(1)).findByEmailLogin("Bearer mock-token", "unknown@example.com");
    }
}
