package com.hupt.hupt_backend.auth.dto;

import com.hupt.hupt_backend.dto.UserSummaryDto;

public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private UserSummaryDto user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public UserSummaryDto getUser() {
        return user;
    }

    public void setUser(UserSummaryDto user) {
        this.user = user;
    }
}