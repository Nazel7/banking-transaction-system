package com.sankore.bank.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sankore.bank.dtos.request.InvestmentmentDto;
import com.sankore.bank.dtos.request.LiquidateDto;
import com.sankore.bank.dtos.request.OriginatorKyc;
import com.sankore.bank.dtos.request.TopupDto;
import com.sankore.bank.dtos.request.TransferDto;
import com.sankore.bank.dtos.request.WithrawalDto;
import com.sankore.bank.dtos.response.Account;
import com.sankore.bank.dtos.response.Investment;
import com.sankore.bank.dtos.response.Transaction;
import com.sankore.bank.services.TransactionJOOQService;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {TransactionController.class})
@ExtendWith(SpringExtension.class)
public class TransactionControllerTest {
    @Autowired
    private TransactionController transactionController;

    @MockBean
    private TransactionJOOQService transactionJOOQService;

    @Test
    public void testFundAccount() throws Exception {
        when(this.transactionJOOQService.doFundAccount((TopupDto) any(), (javax.servlet.http.HttpServletRequest) any()))
                .thenReturn(mock(Account.class));

        OriginatorKyc originatorKyc = new OriginatorKyc();
        originatorKyc.setEmail("jane.doe@example.org");
        originatorKyc.setBankCode("Bank Code");
        originatorKyc.setName("Name");
        originatorKyc.setPhoneNum("4105551212");
        originatorKyc.setIban("Iban");

        TopupDto topupDto = new TopupDto();
        topupDto.setAmount(BigDecimal.valueOf(42L));
        topupDto.setTranxNaration("Tranx Naration");
        topupDto.setTranxRef("Tranx Ref");
        topupDto.setOriginatorKyc(originatorKyc);
        topupDto.setCurrency("Currency");
        topupDto.setIban("Iban");
        topupDto.setTranxType("Tranx Type");
        topupDto.setChannelCode("Channel Code");
        String content = (new ObjectMapper()).writeValueAsString(topupDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/transactions/savings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testInvestAmount() throws Exception {
        when(this.transactionJOOQService.doInvestment((InvestmentmentDto) any(),
                (javax.servlet.http.HttpServletRequest) any())).thenReturn(mock(Investment.class));

        InvestmentmentDto investmentmentDto = new InvestmentmentDto();
        investmentmentDto.setLastName("Doe");
        investmentmentDto.setEndDate("2020-03-01");
        investmentmentDto.setBankCode("Bank Code");
        investmentmentDto.setAmount(BigDecimal.valueOf(42L));
        investmentmentDto.setTranxRef("Tranx Ref");
        investmentmentDto.setPlan("Plan");
        investmentmentDto.setCurrency("Currency");
        investmentmentDto.setIban("Iban");
        investmentmentDto.setMiddleName("Middle Name");
        investmentmentDto.setFirstName("Jane");
        investmentmentDto.setStartDate("2020-03-01");
        String content = (new ObjectMapper()).writeValueAsString(investmentmentDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/transactions/invest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testLiquidateAccount() throws Exception {
        when(this.transactionJOOQService.doLiquidateAccount((LiquidateDto) any(),
                (javax.servlet.http.HttpServletRequest) any())).thenReturn(mock(Account.class));

        LiquidateDto liquidateDto = new LiquidateDto();
        liquidateDto.setIsLiquidate(true);
        liquidateDto.setVerificationCode("Verification Code");
        liquidateDto.setIsLiquidityApproval(true);
        liquidateDto.setTranxNaration("Tranx Naration");
        liquidateDto.setTranxRef("Tranx Ref");
        liquidateDto.setTranxCrncy("Tranx Crncy");
        liquidateDto.setIban("Iban");
        liquidateDto.setTranxType("Tranx Type");
        liquidateDto.setChannelCode("Channel Code");
        String content = (new ObjectMapper()).writeValueAsString(liquidateDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/transactions/liquidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testTransferFund() throws Exception {
        when(this.transactionJOOQService.doFundTransfer((TransferDto) any(), (javax.servlet.http.HttpServletRequest) any()))
                .thenReturn(mock(Transaction.class));

        TransferDto transferDto = new TransferDto();
        transferDto.setBenefAccountNo("Benef Account No");
        transferDto.setVerificationCode("Verification Code");
        transferDto.setDebitAccountNo("Debit Account No");
        transferDto.setAmount(BigDecimal.valueOf(42L));
        transferDto.setPaymentReference("Payment Reference");
        transferDto.setTranxRef("Tranx Ref");
        transferDto.setTranNarration("Tran Narration");
        transferDto.setUserId(123L);
        transferDto.setChannelCode("Channel Code");
        transferDto.setTranType("Tran Type");
        transferDto.setTranCrncy("Tran Crncy");
        String content = (new ObjectMapper()).writeValueAsString(transferDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/transactions/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testWithdrawAmount() throws Exception {
        when(this.transactionJOOQService.doFundWithdrawal((WithrawalDto) any(),
                (javax.servlet.http.HttpServletRequest) any())).thenReturn(mock(Account.class));

        WithrawalDto withrawalDto = new WithrawalDto();
        withrawalDto.setVerificationCode("Verification Code");
        withrawalDto.setAmount(BigDecimal.valueOf(42L));
        withrawalDto.setTranxNaration("Tranx Naration");
        withrawalDto.setTranxRef("Tranx Ref");
        withrawalDto.setCurrency("Currency");
        withrawalDto.setIban("Iban");
        withrawalDto.setTranxType("Tranx Type");
        withrawalDto.setChannelCode("Channel Code");
        String content = (new ObjectMapper()).writeValueAsString(withrawalDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

