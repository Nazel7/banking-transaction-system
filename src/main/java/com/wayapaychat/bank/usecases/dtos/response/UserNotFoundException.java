package com.wayapaychat.bank.usecases.dtos.response;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(final String message){
        super(message);
    }
}
