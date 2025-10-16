package com.ecom.users.controller;


import com.ecom.users.dto.NewPasswordDto;
import com.ecom.users.dto.UserActivationDto;
import com.ecom.users.dto.UserDto;
import com.ecom.users.dto.UserLoginDto;
import com.ecom.users.entity.User;
import com.ecom.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MicroServiceController {

    private final UserService userService;

    public MicroServiceController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/_internal/user-activation")
    public void activationDeviceId(@RequestBody UserActivationDto userActivationDto){
        this.userService.activationUser(userActivationDto);
    }

    @GetMapping("/_internal/users-login/{email}")
    public UserLoginDto userLogin(@PathVariable String email){
        User user = userService.findByEmail(email);
        return new UserLoginDto(user);
    }

    @GetMapping("/_internal/users/{id}")
    public User findById(@PathVariable Long id){
        return (userService.findById(id));
    }

    @GetMapping(path = "/_internal/allUsers")
    public List<UserDto> getUsers() {
        return userService.findAll();
    }

    @PostMapping(path = "/_internal/new-password")
    public ResponseEntity<?> newPassword(@RequestBody NewPasswordDto newPasswordDto) {
        return ResponseEntity.ok().body(this.userService.newPassword(newPasswordDto));
    }

}

