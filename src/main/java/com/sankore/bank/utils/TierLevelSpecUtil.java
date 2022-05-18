package com.sankore.bank.utils;


import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.enums.TierLevel;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TierLevelSpecUtil {

    public static Boolean validate(UserModel userModel, BigDecimal amount) {

        switch (userModel.getTierLevel()) {

            case TierLevelConstant.LEVEL_ONE:

                if (TierLevel.LEVEL_ONE.getMaxAmount() >= amount.doubleValue() && isLevelOneSatisfied(userModel)) {

                    return true;
                }

            case TierLevelConstant.LEVEL_TWO:

                if (TierLevel.LEVEL_TWO.getMaxAmount() >= amount.doubleValue()
                        && isLevelTwoSatisfied(userModel)) {

                    return true;


                }

            case TierLevelConstant.LEVEL_THREE:

                if (amount.doubleValue() > TierLevel.LEVEL_TWO.getMaxAmount()
                        && TierLevel.LEVEL_THREE.getMaxAmount() >= amount.doubleValue()
                        && isLevelThreeSatisfied(userModel)) {

                    return true;

                }

            default:
                return false;
        }

    }

    private static Boolean isLevelOneSatisfied(UserModel userModel) {

        try {
            if (userModel.getVerifiedEmail() && userModel.getVerifiedBvn() ||
                    userModel.getVerifiedPhone() && userModel.getVerifiedBvn()) {

                log.info("::: Account with Iban: [{}] is [{}] verified :::",
                        userModel.getAccount().getIban(),
                        TierLevelConstant.LEVEL_ONE);

                return true;
            }

            log.error("::: Account with Iban: [{}] is not [{}] verified",
                    userModel.getAccount().getIban(),
                    TierLevelConstant.LEVEL_ONE
            );

            return false;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.error("::: Account with Iban: [{}] is not [{}] verified",
                    userModel.getAccount().getIban(),
                    TierLevelConstant.LEVEL_ONE
            );

            return false;
        }
    }

    private static Boolean isLevelTwoSatisfied(UserModel userModel) {

        try {
            if (userModel.getVerifiedEmail() && userModel.getVerifiedPhone() && userModel.getVerifiedBvn()) {

                log.info("::: Account with Iban: [{}] is [{}] verified :::",
                         userModel.getAccount().getIban(),
                        TierLevelConstant.LEVEL_TWO);

                return true;
            }

            log.error("::: Account with Iban: [{}] is not [{}] verified",
                      userModel.getAccount().getIban(),
                    TierLevelConstant.LEVEL_TWO
                     );

            return false;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.error("::: Account with Iban: [{}] is not [{}] verified",
                      userModel.getAccount().getIban(),
                    TierLevelConstant.LEVEL_TWO
                     );

            return false;
        }
    }

    private static Boolean isLevelThreeSatisfied(UserModel userModel) {

        try {
            if (isLevelTwoSatisfied(userModel) && userModel.getHomeAddress() != null && userModel.getVerifiedHomeAddress()) {

                log.info("::: Account with Iban: [{}] is [{}] verified :::",
                         userModel.getAccount().getIban(),
                        TierLevelConstant.LEVEL_THREE);

                return true;

            }

            log.error("::: Account with Iban: [{}] is not [{}] verified",
                      userModel.getAccount().getIban(),
                    TierLevelConstant.LEVEL_THREE
                     );

            return false;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.error("::: Account with Iban: [{}] is not [{}] verified",
                      userModel.getAccount().getIban(),
                    TierLevelConstant.LEVEL_THREE
                     );

            return false;
        }
    }

    public static String getLevel(UserModel userModel){


        if (userModel.getVerifiedPhone() && userModel.getVerifiedEmail() && userModel.getVerifiedBvn()){

            return TierLevelConstant.LEVEL_TWO;
        }
        else if(userModel.getVerifiedPhone() && userModel.getVerifiedEmail() &&
                userModel.getVerifiedBvn() && userModel.getVerifiedHomeAddress()){

            return TierLevelConstant.LEVEL_THREE;
        }
        else {

            return TierLevelConstant.LEVEL_ONE;
        }
    }

    interface TierLevelConstant {

        String LEVEL_ONE = "LEVEL_ONE";

        String LEVEL_TWO = "LEVEL_TWO";

        String LEVEL_THREE = "LEVEL_THREE";
    }
}

