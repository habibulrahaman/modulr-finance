package com.modulr.finance;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountServiceImplTest {

    private AccountService accountService;

    @Before
    public void setup()
    {
        accountService = new AccountServiceImpl();
    }

    @Test
    public void testCheckBalance()
    {
        //Given
        final String accountNumber = "01001";
        final String expected = "2738.59";

        String result = accountService.checkBalance(accountNumber);

        assertEquals(expected, result);
    }
}
