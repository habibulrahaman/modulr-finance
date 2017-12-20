package com.modulr.account;

import java.math.BigDecimal;

public interface AccountService {
    /**
     * Check account balance for the given account number
     * @param accountNumber - account number
     * @return - account balance amount in string representation
     */
    String checkBalance(String accountNumber);

    /**
     * update withdraw amount operation
     * @param accountNumber - The account number
     * @param withdrawalAmount - the specified withdraw amount
     * @return - true if the withdraw operation is successful or else false
     */
    boolean withdrawAmount(String accountNumber, BigDecimal withdrawalAmount);
}
