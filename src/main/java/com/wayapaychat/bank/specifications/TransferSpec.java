package com.wayapaychat.bank.specifications;

import com.wayapaychat.bank.usecases.dtos.request.TransactionDto;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransferSpec {

    public static boolean isSatisfied(TransactionDto transaction) {
        try {

            Objects.requireNonNull(transaction.getAmount());
            Objects.requireNonNull(transaction.getBenefAccountNo());
            Objects.requireNonNull(transaction.getTranType());
            Objects.requireNonNull(transaction.getDebitAccountNo());
            Objects.requireNonNull(transaction.getTranCrncy());
            Objects.requireNonNull(transaction.getPaymentReference());
            Objects.requireNonNull(transaction.getUserId());

            log.info("::: Transaction request body: [{}] is satisfied", transaction);
            return true;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.debug("::: Transaction request body: [{}] not satisfied :::", transaction);
            return false;
        }

    }
}
