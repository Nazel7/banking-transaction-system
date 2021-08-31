package com.wayapaychat.bank.controller;

import com.wayapaychat.bank.services.UserService;
import com.wayapaychat.bank.usecases.domain.User;
import com.wayapaychat.bank.usecases.dtos.request.UserDto;
import com.wayapaychat.bank.usecases.dtos.request.UserInfoDto;
import com.wayapaychat.bank.usecases.dtos.response.UserNotFoundException;

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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("users")
public class UserController {

    private final UserService mUserService;

    @CrossOrigin
    @PostMapping(" ")
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto)
            throws UserNotFoundException {

        final User user= mUserService.registerUser(userDto);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER')")
    @PatchMapping("/{id}")
    public ResponseEntity<User> upgradeUserInfo(@PathVariable("id") Long userId, @RequestBody UserInfoDto userDto)
            throws UserNotFoundException {

        final User user= mUserService.upgradeUser(userId, userDto);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
