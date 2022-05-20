package com.sankore.bank.controllers;

import com.sankore.bank.dtos.request.*;
import com.sankore.bank.dtos.response.Account;
import com.sankore.bank.dtos.response.Investment;
import com.sankore.bank.dtos.response.Transaction;
import com.sankore.bank.dtos.response.TransferNotValidException;
import com.sankore.bank.services.TransactionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("transactions")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionController {

    private final TransactionService mTransactionService;

    @Async
    @CrossOrigin
    @PostMapping("/transfer")
    @ApiOperation(value = "::: transferFund :::", notes = "APi for fund transfer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public CompletableFuture<ResponseEntity<Transaction>> transferFund(@RequestBody TransferDto transferDto, HttpServletRequest request)
            throws TransferNotValidException {

        final Transaction transaction = mTransactionService.doFundTransfer(transferDto, request);

        return CompletableFuture.completedFuture(new ResponseEntity<>(transaction, HttpStatus.OK));
    }

    @Async
    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER')")
    @ApiOperation(value = "::: fundAccount :::", notes = "Api for quick account topUp")
    @PutMapping("/savings")
    public CompletableFuture<ResponseEntity<Account>> fundAccount(@RequestBody TopupDto topupDto, HttpServletRequest request)
            throws TransferNotValidException {

        final Account account = mTransactionService.doFundAccount(topupDto, request);

        return CompletableFuture.completedFuture(new ResponseEntity<>(account, HttpStatus.OK));
    }

    @Async
    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER')")
    @ApiOperation(value = "::: fundAccount :::", notes = "Api for quick account topUp")
    @PutMapping("/withdraw")
    public CompletableFuture<ResponseEntity<Account>> withdrawAmount(@RequestBody WithrawalDto withrawalDto, HttpServletRequest request)
            throws TransferNotValidException {

        final Account account = mTransactionService.doFundWithdrawal(withrawalDto, request);

        return CompletableFuture.completedFuture(new ResponseEntity<>(account, HttpStatus.OK));
    }

    @Async
    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER')")
    @ApiOperation(value = "::: fundAccount :::", notes = "Api for quick account topUp")
    @PutMapping("/liquidate")
    public CompletableFuture<ResponseEntity<Account>> liquidateAccount(@RequestBody LiquidateDto liquidateDto, HttpServletRequest request)
            throws TransferNotValidException {

        final Account account = mTransactionService.doLiquidateAccount(liquidateDto, request);

        return CompletableFuture.completedFuture(new ResponseEntity<>(account, HttpStatus.OK));
    }

    @Async
    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER')")
    @ApiOperation(value = "::: fundAccount :::", notes = "Api for quick account topUp")
    @PostMapping("/invest")
    public CompletableFuture<ResponseEntity<Investment>> InvestAmount(@RequestBody InvestmentmentDto investmentmentDto, HttpServletRequest request)
            throws TransferNotValidException {

        final Investment investment = mTransactionService.doInvestment(investmentmentDto, request);

        return CompletableFuture.completedFuture(new ResponseEntity<>(investment, HttpStatus.CREATED));
    }


}
