package com.modulr.finance;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
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
        return null;
    }

    @Override
    public boolean withdrawAmount(String accountNumber, BigDecimal withdrawalAmount) {
        return false;
    }
}
