package com.sankore.bank.controllers;

import com.sankore.bank.dtos.request.SignUpDto;
import com.sankore.bank.dtos.request.UserInfoDto;
import com.sankore.bank.dtos.response.User;
import com.sankore.bank.dtos.response.UserNotFoundException;
import com.sankore.bank.services.UserJOOQService;
import com.sankore.bank.services.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("users")
public class UserController {

    private final UserJOOQService userJOOQService;

    //    @Async
    @CrossOrigin
    @PostMapping("/signup")
    @ApiOperation(value = "::: createUser :::", notes = "API for user creation with login credentials")
    public CompletableFuture<ResponseEntity<User>> createUser(@RequestBody SignUpDto signUpDto)
            throws UserNotFoundException, NoSuchAlgorithmException {

        final User user = userJOOQService.registerUser(signUpDto);

        return CompletableFuture.completedFuture(new ResponseEntity<>(user, HttpStatus.CREATED));
    }

    //    @Async
    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER')")
    @ApiOperation(value = "::: upgradeUserInfo :::", notes = "API to upgrade user Level based on the"
            + " amount of fund capable to transact")
    @PatchMapping("/{id}")
    public CompletableFuture<ResponseEntity<User>> upgradeUserInfo(@PathVariable("id") Long userId, @RequestBody UserInfoDto userDto)
            throws UserNotFoundException {

        final User user = userJOOQService.upgradeUser(userId, userDto);

        return CompletableFuture.completedFuture(new ResponseEntity<>(user, HttpStatus.CREATED));
    }

}
