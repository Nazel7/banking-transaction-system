package com.sankore.bank.entities.builder;
import com.sankore.bank.entities.models.TransactionModel;
import com.sankore.bank.dtos.response.Transaction;
import com.sankore.bank.dtos.request.TransferDto;

public class TransactionMapper {

    public static TransactionModel mapToModel(TransferDto transferDto, String token){

        return TransactionModel
                .builder()
                .amount(transferDto.getAmount())
                .tranxRef(transferDto.getTranxRef())
                .paymentReference(transferDto.getPaymentReference())
                .benefAccountNo(transferDto.getBenefAccountNo())
                .debitAccountNo(transferDto.getDebitAccountNo())
                .tranCrncy(transferDto.getTranCrncy())
                .tranType(transferDto.getTranType())
                .userToken(token)
                .tranNarration(transferDto.getTranNarration())
                .userId(transferDto.getUserId())
                .build();
    }

    public static Transaction mapToDomain(TransactionModel transactionModel){

        return Transaction
                .builder()
                .amount(transactionModel.getAmount())
                .paymentReference(transactionModel.getPaymentReference())
                .benefAccountNo(transactionModel.getBenefAccountNo())
                .debitAccountNo(transactionModel.getDebitAccountNo())
                .tranxRef(transactionModel.getTranxRef())
                .tranCrncy(transactionModel.getTranCrncy())
                .tranType(transactionModel.getTranType())
                .tranNarration(transactionModel.getTranNarration())
                .userId(transactionModel.getUserId())
                .build();
    }
}
