package com.sankore.bank.controllers;

import com.sankore.bank.services.UserService;
import com.sankore.bank.dtos.response.User;
import com.sankore.bank.dtos.request.SignUpDto;
import com.sankore.bank.dtos.request.UserInfoDto;
import com.sankore.bank.dtos.response.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("users")
public class UserController {

    private final UserService mUserService;

    @CrossOrigin
    @PostMapping(" ")
    @ApiOperation(value = "::: createUser :::", notes = "API for user creation with login credentials")
    public ResponseEntity<User> createUser(@RequestBody SignUpDto signUpDto)
            throws UserNotFoundException {

        final User user= mUserService.registerUser(signUpDto);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER')")
    @ApiOperation(value = "::: upgradeUserInfo :::", notes = "API to upgrade user Level based on the"
            + " amount of fund capable to transact")
    @PatchMapping("/{id}")
    public ResponseEntity<User> upgradeUserInfo(@PathVariable("id") Long userId, @RequestBody UserInfoDto userDto)
            throws UserNotFoundException {

        final User user= mUserService.upgradeUser(userId, userDto);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}