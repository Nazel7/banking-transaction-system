package com.sankore.bank.controllers;


import com.sankore.bank.entities.builder.UserMapper;
import com.sankore.bank.services.CustomUserDetailsService;
import com.sankore.bank.auth.util.JwtUtil;
import com.sankore.bank.auth.util.LoginRequestUtil;

import com.sankore.bank.entities.models.SecureUserModel;
import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.repositories.SecureUserRepo;
import com.sankore.bank.dtos.response.LogginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService mCustomUserDetailsService;

    private final SecureUserRepo mSecureUserRepo;

    @Async
    @ApiOperation(value = "::: welcome :::", notes = "application health check")
    @GetMapping(" ")
    public CompletableFuture<String> welcome() {
        return CompletableFuture.completedFuture("Welcome to Sankore-Gafar Banking Service !!");
    }

    @Async
    @ApiOperation(value = "::: generateToken :::", notes = "Login channel")
    @PostMapping(" ")
    public CompletableFuture<ResponseEntity<LogginResponse>> generateToken(@RequestBody LoginRequestUtil loginRequestUtil) throws Exception {
        log.info("::: Login request processing :::");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestUtil.getUsername(), loginRequestUtil
                            .getPassword())
            );
        } catch (Exception ex) {
            throw new Exception(" Invalid username or password");
        }

        final SecureUserModel
                secureUserModel = mSecureUserRepo.getSecureUserByUsername(
                loginRequestUtil.getUsername());
        final UserDetails userDetails= mCustomUserDetailsService.loadUserByUsername(
                loginRequestUtil.getUsername());
        final UserModel userModel= secureUserModel.getUserModel();
        final LogginResponse
                logginResponse = UserMapper.mapToDto(userModel, jwtUtil.generateToken(userDetails));

        return CompletableFuture.completedFuture(new ResponseEntity<>(logginResponse, HttpStatus.OK));
    }
}
