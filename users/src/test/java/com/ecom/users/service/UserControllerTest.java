package com.ecom.users.service;

import com.ecom.users.controller.UserController;
import com.ecom.users.dto.EditPasswordDto;
import com.ecom.users.dto.UserDto;
import com.ecom.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createMockUser(Long id, String name, String username, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        return user;
    }

    // 1 : Test inscription
    @Test
    void testInscription() throws Exception {
        // Création d’un utilisateur
        User user = createMockUser(1L, "John", "john123", "test@example.com");

        // Simule de la réponse du service
        ResponseEntity<String> mockResponse = new ResponseEntity<>("Utilisateur créé", HttpStatus.CREATED);

        // Simulation du service
        when(userService.registration(any(User.class))).thenReturn((ResponseEntity) mockResponse);

        // Appel du controller
        ResponseEntity<?> response = userController.inscription(user);

        // Vérification du statut HTTP et du contenu
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Utilisateur créé", response.getBody());

        // Vérifie que le service a bien été appelé une fois
        verify(userService, times(1)).registration(any(User.class));
    }


    // 2 : Test supprimer un utilisateur
    @Test
    void testRemoveUser() {
        String email = "test@example.com";

        // Simule la réponse du service
        ResponseEntity<String> mockResponse = new ResponseEntity<>("Le compte est supprimé", HttpStatus.OK);
        when(userService.removeUser(email)).thenReturn((ResponseEntity) mockResponse);

        // Appel du controller
        ResponseEntity<?> response = userController.removeUser(email);

        // Vérification
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Le compte est supprimé", response.getBody());
        verify(userService, times(1)).removeUser(email);
    }

    // 3 : Test recherche d'un utilisateur par son email
    @Test
    void testCustomerByEmail() {
        String email = "test@example.com";

        // Création d’un utilisateur mock
        User mockUser = createMockUser(1L, "John", "john123", email);
        UserDto mockUserDto = new UserDto(mockUser);

        // Simulation du service
        when(userService.userByEmail(email)).thenReturn(mockUserDto);

        // Appel du controller
        UserDto response = userController.customerByEmail(email);

        // Vérifications
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John", response.getName());
        assertEquals("john123", response.getUsername());
        assertEquals(email, response.getEmail());
        verify(userService, times(1)).userByEmail(email);
    }

    // 4 : Test modifier son mot de passe
    @Test
    void testEditPassword() {
        EditPasswordDto dto = new EditPasswordDto();
        dto.setEmail("test@example.com");

        // Simule la réponse du service
        ResponseEntity<String> mockResponse = new ResponseEntity<>("Mot de passe modifié", HttpStatus.CREATED);
        when(userService.editPassword(any(EditPasswordDto.class))).thenReturn((ResponseEntity) mockResponse);

        // Appel du controller
        ResponseEntity<?> response = userController.editPassword(dto);

        // Vérifications
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Mot de passe modifié", response.getBody());
        verify(userService, times(1)).editPassword(any(EditPasswordDto.class));
    }

    // 5 : Test modifier un utilisateur
    @Test
    void testUpdateUser() {
        User mockUser = createMockUser(1L, "John", "john123", "test@example.com");
        UserDto userDto = new UserDto(mockUser);

        // Simule la réponse du service
        ResponseEntity<String> mockResponse = new ResponseEntity<>("Votre compte a été modifié avec succès !", HttpStatus.OK);
        when(userService.updateUser(any(UserDto.class))).thenReturn((ResponseEntity) mockResponse);

        // Appel du controller
        ResponseEntity<?> response = userController.updateProduct(userDto);

        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Votre compte a été modifié avec succès !", response.getBody());
        verify(userService, times(1)).updateUser(any(UserDto.class));
    }
}