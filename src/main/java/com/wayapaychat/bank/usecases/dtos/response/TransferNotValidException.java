package com.wayapaychat.bank.usecases.dtos.response;

public class TransferNotValidException extends Exception{

    public TransferNotValidException(String message){

        super(message);
    }
}
