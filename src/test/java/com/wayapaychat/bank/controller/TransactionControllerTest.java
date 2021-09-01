package com.wayapaychat.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wayapaychat.bank.services.TransactionService;
import com.wayapaychat.bank.usecases.dtos.request.TopupDto;
import com.wayapaychat.bank.usecases.dtos.request.TransactionDto;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
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
    private TransactionService transactionService;

    @Test
    public void testFundAccount() throws Exception {
        TopupDto topupDto = new TopupDto();
        topupDto.setAmount(BigDecimal.valueOf(42L));
        topupDto.setIban("Iban");
        String content = (new ObjectMapper()).writeValueAsString(topupDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/transactions/ ")
                                                                             .contentType(
                                                                                     MediaType.APPLICATION_JSON)
                                                                             .content(content);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(this.transactionController)
                               .build()
                               .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testTransferFund() throws Exception {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setBenefAccountNo("Benef Account No");
        transactionDto.setDebitAccountNo("Debit Account No");
        transactionDto.setAmount(BigDecimal.valueOf(42L));
        transactionDto.setPaymentReference("Payment Reference");
        transactionDto.setTranNarration("Tran Narration");
        transactionDto.setUserId(123L);
        transactionDto.setTranType("Tran Type");
        transactionDto.setTranCrncy("Tran Crncy");
        String content = (new ObjectMapper()).writeValueAsString(transactionDto);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/transactions/ ")
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(content);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(this.transactionController)
                               .build()
                               .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

