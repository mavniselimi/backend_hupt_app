package com.hupt.hupt_backend.dto;

import com.hupt.hupt_backend.entities.DevicePlatform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DeviceTokenRegisterRequestDto {

    @NotNull
    private DevicePlatform platform;

    @NotBlank
    private String token;

    public DevicePlatform getPlatform() {
        return platform;
    }

    public void setPlatform(DevicePlatform platform) {
        this.platform = platform;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}