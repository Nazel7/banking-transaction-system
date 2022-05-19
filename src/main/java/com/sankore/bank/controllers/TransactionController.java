package com.sankore.bank.controllers;

import com.sankore.bank.dtos.request.*;
import com.sankore.bank.dtos.response.*;
import com.sankore.bank.services.TransactionService;

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

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("transactions")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionController {

    private final TransactionService mTransactionService;

    @CrossOrigin
    @PostMapping("/transfer")
    @ApiOperation(value = "::: transferFund :::", notes = "APi for fund transfer")
    @PreAuthorize("hasRole('CUSTOMER')")
   public ResponseEntity<Transaction>  transferFund(@RequestBody TransferDto transferDto, HttpServletRequest request)
            throws TransferNotValidException {

       final Transaction transaction= mTransactionService.doFundTransfer(transferDto, request);

       return new ResponseEntity<>(transaction, HttpStatus.OK);
   }

   @CrossOrigin
   @PreAuthorize("hasRole('CUSTOMER')")
   @ApiOperation(value = "::: fundAccount :::", notes = "Api for quick account topUp")
   @PutMapping("/savings")
public ResponseEntity<Account>  fundAccount(@RequestBody TopupDto topupDto, HttpServletRequest request)
           throws TransferNotValidException {

        final Account account= mTransactionService.doFundAccount(topupDto, request);

       return new ResponseEntity<>(account, HttpStatus.OK);
}


    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER')")
    @ApiOperation(value = "::: fundAccount :::", notes = "Api for quick account topUp")
    @PutMapping("/withdraw")
    public ResponseEntity<Account>  withdrawAmount(@RequestBody WithrawalDto withrawalDto, HttpServletRequest request)
            throws TransferNotValidException {

        final Account account= mTransactionService.doFundWithdrawal(withrawalDto, request);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER')")
    @ApiOperation(value = "::: fundAccount :::", notes = "Api for quick account topUp")
    @PutMapping("/liquidate")
    public ResponseEntity<Account>  liquidateAccount(@RequestBody LiquidateDto liquidateDto, HttpServletRequest request)
            throws TransferNotValidException {

        final Account account= mTransactionService.doLiquidateAccount(liquidateDto, request);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER')")
    @ApiOperation(value = "::: fundAccount :::", notes = "Api for quick account topUp")
    @PostMapping("/invest")
    public ResponseEntity<Investment>  InvestAmount(@RequestBody InvestmentmentDto investmentmentDto, HttpServletRequest request)
            throws TransferNotValidException {

        final Investment investment= mTransactionService.doInvestment(investmentmentDto, request);

        return new ResponseEntity<>(investment, HttpStatus.CREATED);
    }


}
