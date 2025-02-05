package com.example.lime.auth;

import com.example.lime.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;

    public String auth(AuthTypes authTypes) throws BadRequestException {
        return switch (authTypes) {
            case ADMIN -> jwtUtil.generateToken("admin", "ADMIN");
            case USER -> jwtUtil.generateToken("user", "USER");
            default -> throw new BadRequestException("Invalid Auth Type");
        };
    }
}

