package com.along.coash.server.admin.contract.auth;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResponse {
    private String userId;

    private String accessToken;

    private String refreshToken;

    private LocalDateTime expiresTime;
}
