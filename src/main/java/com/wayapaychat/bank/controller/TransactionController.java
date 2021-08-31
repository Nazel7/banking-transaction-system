package com.wayapaychat.bank.controller;

import com.wayapaychat.bank.services.TransactionService;
import com.wayapaychat.bank.usecases.domain.Account;
import com.wayapaychat.bank.usecases.domain.Transaction;
import com.wayapaychat.bank.usecases.dtos.request.TopupDto;
import com.wayapaychat.bank.usecases.dtos.request.TransactionDto;
import com.wayapaychat.bank.usecases.dtos.response.TransferNotValidException;
import com.wayapaychat.bank.usecases.dtos.response.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.NotActiveException;
import java.math.BigDecimal;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("transactions")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionController {

    private final TransactionService mTransactionService;

    @CrossOrigin
    @PostMapping(" ")
    @PreAuthorize("hasRole('CUSTOMER')")
   public ResponseEntity<Transaction>  transferFund(@RequestBody TransactionDto transactionDto)
            throws AccountException, TransferNotValidException, UserNotFoundException,
                   NotActiveException {

       final Transaction transaction= mTransactionService.tranferFund(transactionDto);

       return new ResponseEntity<>(transaction, HttpStatus.OK);
   }

   @CrossOrigin
   @PreAuthorize("hasRole('CUSTOMER')")
   @PutMapping(" ")
public ResponseEntity<Account>  fundAccount(@RequestBody TopupDto topupDto)
           throws AccountNotFoundException {

        final Account account= mTransactionService.fundAccount(topupDto);

       return new ResponseEntity<>(account, HttpStatus.OK);
}
}
