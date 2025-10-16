package com.ecom.security.controller;

import com.ecom.security.dto.LoginActivationDto;
import com.ecom.security.service.ActivationDeviceIdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MicroServiceController {

    private final ActivationDeviceIdService activationDeviceIdService;

    public MicroServiceController(ActivationDeviceIdService activationDeviceIdService) {
        this.activationDeviceIdService = activationDeviceIdService;
    }

    @PostMapping("/_internal/login-activation-deviceId")
    public void activationDeviceId(@RequestBody LoginActivationDto loginActivationDto){
        this.activationDeviceIdService.activationDeviceIdService(loginActivationDto);
    }

}
