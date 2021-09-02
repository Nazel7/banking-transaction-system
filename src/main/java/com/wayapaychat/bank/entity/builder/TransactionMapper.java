package com.wayapaychat.bank.entity.builder;
import com.wayapaychat.bank.entity.models.TransactionModel;
import com.wayapaychat.bank.dtos.response.Transaction;
import com.wayapaychat.bank.dtos.request.TransactionDto;

public class TransactionMapper {

    public static TransactionModel mapToModel(TransactionDto transactionDto){

        return TransactionModel
                .builder()
                .amount(transactionDto.getAmount())
                .paymentReference(transactionDto.getPaymentReference())
                .benefAccountNo(transactionDto.getBenefAccountNo())
                .debitAccountNo(transactionDto.getDebitAccountNo())
                .tranCrncy(transactionDto.getTranCrncy())
                .tranType(transactionDto.getTranType())
                .tranNarration(transactionDto.getTranNarration())
                .userId(transactionDto.getUserId())
                .build();
    }

    public static Transaction mapToDomain(TransactionModel transactionModel){

        return Transaction
                .builder()
                .amount(transactionModel.getAmount())
                .paymentReference(transactionModel.getPaymentReference())
                .benefAccountNo(transactionModel.getBenefAccountNo())
                .debitAccountNo(transactionModel.getDebitAccountNo())
                .tranCrncy(transactionModel.getTranCrncy())
                .tranType(transactionModel.getTranType())
                .tranNarration(transactionModel.getTranNarration())
                .userId(transactionModel.getUserId())
                .build();
    }
}
