package com.sankore.bank.entities.builder;
import com.sankore.bank.dtos.request.LiquidateDto;
import com.sankore.bank.dtos.request.TopupDto;
import com.sankore.bank.dtos.request.WithrawalDto;
import com.sankore.bank.entities.models.TransactionModel;
import com.sankore.bank.dtos.response.Transaction;
import com.sankore.bank.dtos.request.TransferDto;
import com.sankore.bank.enums.TransType;

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
                .channelCode(transferDto.getChannelCode())
                .build();
    }

    public static TransactionModel mapToModel(WithrawalDto withrawalDto, String token){

        return TransactionModel
                .builder()
                .amount(withrawalDto.getAmount())
                .tranxRef(withrawalDto.getTranxRef())
                .benefAccountNo(withrawalDto.getIban())
                .debitAccountNo(withrawalDto.getIban())
                .tranCrncy(withrawalDto.getTranxCrncy())
                .tranType(withrawalDto.getTranxType().equals(TransType.WITHDRAWAL.name()) ?
                        withrawalDto.getTranxType(): TransType.WITHDRAWAL.name())
                .userToken(token)
                .tranNarration(withrawalDto.getTranxNaration())
                .channelCode(withrawalDto.getChannelCode())
                .build();
    }
    public static TransactionModel mapToModel(LiquidateDto liquidateDto, String token){

        return TransactionModel
                .builder()
                .tranxRef(liquidateDto.getTranxRef())
                .benefAccountNo(liquidateDto.getIban())
                .debitAccountNo(liquidateDto.getIban())
                .tranCrncy(liquidateDto.getTranxCrncy())
                .tranType(!liquidateDto.getTranxType().equals(TransType.LIQUIDATE.name()) ?
                        TransType.LIQUIDATE.name(): liquidateDto.getTranxType())
                .userToken(token)
                .tranNarration(liquidateDto.getTranxNaration())
                .liquidityApproval(liquidateDto.getIsLiquidityApproval())
                .isLiquidate(liquidateDto.getIsLiquidate())
                .channelCode(liquidateDto.getChannelCode())
                .build();
    }

    public static TransactionModel mapToModel(TopupDto topupDto, String token){

        return TransactionModel
                .builder()
                .tranxRef(topupDto.getTranxRef())
                .benefAccountNo(topupDto.getIban())
                .debitAccountNo(topupDto.getIban())
                .tranCrncy(topupDto.getTrnaxCrcy())
                .tranType(topupDto.getTranxType())
                .userToken(token)
                .tranNarration(topupDto.getTranxNaration())
                .channelCode(topupDto.getChannelCode())
                .build();
    }

    public static Transaction mapToDomain(TransactionModel transactionModel){

        return Transaction
                .builder()
                .status(transactionModel.getStatus())
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
