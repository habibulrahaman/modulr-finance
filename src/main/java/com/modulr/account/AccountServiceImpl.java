package com.modulr.account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AccountServiceImpl implements AccountService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //Sample repository
    private Map<String, BigDecimal> accountRepository;

    public AccountServiceImpl()
    {
        //Note: new BigDecimal(2738.59) is that the argument is a double
        //and it happens that doubles can't represent 2738.59 exactly.
        //so BigDecimal.valueOf is used instead
        accountRepository = new HashMap<>();
        accountRepository.put("01001", BigDecimal.valueOf(2738.59));
        accountRepository.put("01002", BigDecimal.valueOf(23.00));
        accountRepository.put("01003", BigDecimal.valueOf(0.00));
    }

    @Override
    public String checkBalance(String accountNumber) {
        logger.info("checking balance for the account number {}", accountNumber);

        validateAccountNumber(accountNumber);

        return accountRepository.get(accountNumber).toPlainString();
    }

    @Override
    public boolean withdrawAmount(String accountNumber, BigDecimal withdrawalAmount) {
        logger.info("withdrawing {} amount for the account number - {}", withdrawalAmount, accountNumber);

        validateAccountNumber(accountNumber);

        validateWithdrawAmount(withdrawalAmount);

        BigDecimal balanceAmount = accountRepository.get(accountNumber);

        BigDecimal newBalanceAmount = balanceAmount.subtract(withdrawalAmount);

        //If the newBalanceAmount is -ve means we have Insufficient funds
        if(newBalanceAmount.signum() == -1)
        {
            logger.error("Insufficient funds to withdraw - {}", withdrawalAmount);
            throw new InsufficientFundException("Insufficient funds");
        }

        accountRepository.put(accountNumber, newBalanceAmount);

        logger.info("withdrawn successfully {} amount from the account number - {}", withdrawalAmount, accountNumber);

        return true;
    }

    /**
     * validate withdraw amount
     * @param withdrawalAmount - the specified withdrawal amount
     */
    private void validateWithdrawAmount(BigDecimal withdrawalAmount)
    {
        Objects.requireNonNull(withdrawalAmount, "The specified amount to withdraw is null");

        //Note: if signum is -1 then withdrawal amount are invalid
        if(withdrawalAmount.signum() == -1)
        {
            logger.error("The specified amount to withdraw is negative - {}", withdrawalAmount);
            throw new IllegalArgumentException("The specified amount to withdraw is negative");
        }
    }

    /**
     * validate account number
     * @param accountNumber - the account number
     */
    private void validateAccountNumber(String accountNumber)
    {
        Objects.requireNonNull(accountNumber, "The account number is null");

        if(!accountRepository.containsKey(accountNumber))
        {
            throw new IllegalArgumentException("Could not found the given account number.");
        }
    }
}
