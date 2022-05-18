package com.sankore.bank.dtos.response;

public class TransferNotValidException extends Exception{

    public TransferNotValidException(String message){

        super(message);
    }
}
