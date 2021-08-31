package com.wayapaychat.bank.auth.controller;


import com.wayapaychat.bank.auth.logon.AuthRequest;
import com.wayapaychat.bank.auth.secureuser.CustomUserDetailsService;
import com.wayapaychat.bank.auth.util.JwtUtil;
import com.wayapaychat.bank.entity.SecureUser;
import com.wayapaychat.bank.entity.UserModel;
import com.wayapaychat.bank.mappers.UserMapper;
import com.wayapaychat.bank.repository.SecureUserRepo;
import com.wayapaychat.bank.usecases.dtos.request.LoginDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class HealthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService mCustomUserDetailsService;

    @Autowired
    private SecureUserRepo mSecureUserRepo;

    @GetMapping(" ")
    public String welcome() {
        return "Welcome to Wayapaychat Banking Service !!";
    }

    @PostMapping(" ")
    public ResponseEntity<LoginDto> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        log.info("::: Login request processing :::");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception(" Inavalid username or password");
        }

        final SecureUser secureUser = mSecureUserRepo.getSecureUserByUsername(authRequest.getUsername());
        final UserDetails userDetails= mCustomUserDetailsService.loadUserByUsername(authRequest.getUsername());
        final UserModel userModel= secureUser.getUserModel();
        final LoginDto loginDto= UserMapper.mapToDto(userModel, jwtUtil.generateToken(userDetails));

        return new ResponseEntity<>(loginDto, HttpStatus.OK);
    }
}
