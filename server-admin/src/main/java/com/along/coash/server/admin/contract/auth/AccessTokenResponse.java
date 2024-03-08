package com.along.coash.server.admin.contract.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenResponse {

    private String id;

    private String accessToken;

    private String refreshToken;

    private String userId;

    private Integer userType;

    private String clientId;

    private LocalDateTime createTime;

    private LocalDateTime expiresTime;

}
