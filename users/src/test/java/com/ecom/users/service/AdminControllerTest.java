package com.ecom.users.service;

import com.ecom.users.controller.AdminController;
import com.ecom.users.dto.UpdateRolesDto;
import com.ecom.users.entity.User;
import com.ecom.users.repository.RoleRepository;
import com.ecom.users.repository.UserRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private User testUser;
    private UpdateRolesDto rolesDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setUsername("test");
        testUser.setEmail("test@test.com");

        rolesDto = new UpdateRolesDto();
        rolesDto.setRoleNames(List.of("USER", "ADMIN"));
    }

    // 1 : Test récupérer tous les utilisateurs
    @Test
    void testGetUsers() {
        // Mock du repository pour retourner une liste contenant testUser
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        // Appel de la méthode du controller
        List<?> result = adminController.getUsers();

        // Vérifications
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    // 2 : Test mise à jour des rôles d'un utilisateur
    @Test
    void testUpdateRoles() {
        Long userId = 1L;

        // Préparation
        ResponseEntity<?> mockResponse = ResponseEntity.ok("Mise à jour effectuée !");

        // Simulation du service
        when(adminService.adminAddRoleUsers(anyLong(), any(UpdateRolesDto.class)))
                .thenReturn((ResponseEntity) mockResponse);

        // Appel du controller
        ResponseEntity<?> response = adminController.updateRoles(userId, rolesDto);

        // Vérifications
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mise à jour effectuée !", response.getBody());

        // Vérifie que le service a bien été appelé
        verify(adminService, times(1)).adminAddRoleUsers(userId, rolesDto);
    }
}
