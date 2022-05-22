package com.sankore.bank.controllers;

import com.sankore.bank.dtos.request.*;
import com.sankore.bank.dtos.response.Account;
import com.sankore.bank.dtos.response.Investment;
import com.sankore.bank.dtos.response.Transaction;
import com.sankore.bank.dtos.response.TransferNotValidException;
import com.sankore.bank.entities.models.InvestmentModel;
import com.sankore.bank.services.TransactionJOOQService;
import com.sankore.bank.services.TransactionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("transactions")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionController {

    private final TransactionJOOQService transactionJOOQService;

    @Async
    @CrossOrigin
    @PostMapping("/transfer")
    @ApiOperation(value = "::: transferFund :::", notes = "APi for fund transfer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public CompletableFuture<ResponseEntity<Transaction>> transferFund(@RequestBody TransferDto transferDto, HttpServletRequest request)
            throws TransferNotValidException {

        final Transaction transaction = transactionJOOQService.doFundTransfer(transferDto, request);

        return CompletableFuture.completedFuture(new ResponseEntity<>(transaction, HttpStatus.OK));
    }

    @Async
    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER')")
    @ApiOperation(value = "::: fundAccount :::", notes = "Api for quick account topUp")
    @PutMapping("/savings")
    public CompletableFuture<ResponseEntity<Account>> fundAccount(@RequestBody TopupDto topupDto, HttpServletRequest request)
            throws TransferNotValidException {

        final Account account = transactionJOOQService.doFundAccount(topupDto, request);

        return CompletableFuture.completedFuture(new ResponseEntity<>(account, HttpStatus.OK));
    }

    @Async
    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER')")
    @ApiOperation(value = "::: withrawal :::", notes = "Api for quick account withrawal")
    @PutMapping("/withdraw")
    public CompletableFuture<ResponseEntity<Account>> withdrawAmount(@RequestBody WithrawalDto withrawalDto, HttpServletRequest request)
            throws TransferNotValidException {

        final Account account = transactionJOOQService.doFundWithdrawal(withrawalDto, request);

        return CompletableFuture.completedFuture(new ResponseEntity<>(account, HttpStatus.OK));
    }

    @Async
    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER')")
    @ApiOperation(value = "::: liquidateAccount :::", notes = "Api for quick account liquidity")
    @PutMapping("/liquidate")
    public CompletableFuture<ResponseEntity<Account>> liquidateAccount(@RequestBody LiquidateDto liquidateDto, HttpServletRequest request)
            throws TransferNotValidException {

        final Account account = transactionJOOQService.doLiquidateAccount(liquidateDto, request);

        return CompletableFuture.completedFuture(new ResponseEntity<>(account, HttpStatus.OK));
    }

    @Async
    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER')")
    @ApiOperation(value = "::: InvestAmount :::", notes = "Api for quick account Investment")
    @PostMapping("/invest")
    public CompletableFuture<ResponseEntity<Investment>> InvestAmount(@RequestBody InvestmentmentDto investmentmentDto, HttpServletRequest request)
            throws TransferNotValidException {

        final Investment investment = transactionJOOQService.doInvestment(investmentmentDto, request);

        return CompletableFuture.completedFuture(new ResponseEntity<>(investment, HttpStatus.CREATED));
    }

}
