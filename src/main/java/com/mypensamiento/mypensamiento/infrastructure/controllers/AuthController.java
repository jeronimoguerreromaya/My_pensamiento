package com.mypensamiento.mypensamiento.infrastructure.controllers;


import com.mypensamiento.mypensamiento.application.dto.request.LoginRequest;
import com.mypensamiento.mypensamiento.application.dto.request.RegisterUserRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.usecase.Auth.LoginUseCase;
import com.mypensamiento.mypensamiento.application.usecase.Auth.LogoutAllUseCase;
import com.mypensamiento.mypensamiento.application.usecase.Auth.RefreshUseCase;
import com.mypensamiento.mypensamiento.application.usecase.Auth.RegisterUseCase;
import com.mypensamiento.mypensamiento.application.usecase.Auth.resetPasswor.SendCodeUseCase;
import com.mypensamiento.mypensamiento.application.usecase.Auth.resetPasswor.VerifyCodeUseCase;
import com.mypensamiento.mypensamiento.infrastructure.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    RegisterUseCase registerUseCase;

    @Autowired
    LoginUseCase loginUseCase;

    @Autowired
    RefreshUseCase refreshUseCase;

    @Autowired
    LogoutAllUseCase logoutAllUseCase;

    @Autowired
    SendCodeUseCase requestPasswordResetUseCase;

    @Autowired
    VerifyCodeUseCase verifyCodeUseCase;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Validated @RequestBody RegisterUserRequest request){
        AuthResponse authResponse = this.registerUseCase.execute(request);

       /*   Http-only
            ResponseCookie cookie = ResponseCookie.from("refreshTokenCookie", authResponse.refreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/auth/refresh")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);*/
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Validated @RequestBody LoginRequest request){
        AuthResponse authResponse = this.loginUseCase.execute(request);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("Authorization") String refreshToken){
        AuthResponse authResponse = this.refreshUseCase.execute(refreshToken);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(@RequestHeader("Authorization") String refreshToken){
        this.logoutAllUseCase.execute(refreshToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/r/{email}")
    public ResponseEntity<AuthResponse> r(@Validated @PathVariable String email){
        this.requestPasswordResetUseCase.execute(email);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/v/{opt}/{email}")
    public ResponseEntity<TokenResponse> r(@Validated @PathVariable String opt, @Validated @PathVariable String email){
        TokenResponse authResponse= this.verifyCodeUseCase.execute(opt,email);
        return ResponseEntity.ok(authResponse);

    }

}
