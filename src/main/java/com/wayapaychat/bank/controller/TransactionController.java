package com.wayapaychat.bank.controller;

import com.wayapaychat.bank.services.TransactionService;
import com.wayapaychat.bank.dtos.response.Account;
import com.wayapaychat.bank.dtos.response.Transaction;
import com.wayapaychat.bank.dtos.request.TopupDto;
import com.wayapaychat.bank.dtos.request.TransactionDto;
import com.wayapaychat.bank.dtos.response.TransferNotValidException;
import com.wayapaychat.bank.dtos.response.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.NotActiveException;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("transactions")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionController {

    private final TransactionService mTransactionService;

    @CrossOrigin
    @PostMapping(" ")
    @ApiOperation(value = "::: transferFund :::", notes = "APi for fund transfer")
    @PreAuthorize("hasRole('CUSTOMER')")
   public ResponseEntity<Transaction>  transferFund(@RequestBody TransactionDto transactionDto)
            throws AccountException, TransferNotValidException, UserNotFoundException,
                   NotActiveException {

       final Transaction transaction= mTransactionService.tranferFund(transactionDto);

       return new ResponseEntity<>(transaction, HttpStatus.OK);
   }

   @CrossOrigin
   @PreAuthorize("hasRole('CUSTOMER')")
   @ApiOperation(value = "::: fundAccount :::", notes = "Api for quick account topUp")
   @PutMapping(" ")
public ResponseEntity<Account>  fundAccount(@RequestBody TopupDto topupDto)
           throws AccountNotFoundException {

        final Account account= mTransactionService.fundAccount(topupDto);

       return new ResponseEntity<>(account, HttpStatus.OK);
}
}
