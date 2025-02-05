package com.example.lime.auth;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping()
    public ResponseEntity<String> auth(
            @RequestParam AuthTypes authType
    ) throws BadRequestException {
        String response = authService.auth(authType);
        return ResponseEntity.ok(response);
    }

}
