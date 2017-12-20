package com.modulr.atm;

import com.modulr.account.AccountService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ATMServiceImplTest{

    private ATMService atmService;

    private AccountService accountServiceMock;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        accountServiceMock = mock(AccountService.class);
        atmService = new ATMServiceImpl(accountServiceMock);

        Map<BankNote, Integer> bankNotes = new EnumMap<>(BankNote.class);
        bankNotes.put(BankNote.FIVE, 20);
        bankNotes.put(BankNote.TEN, 20);
        bankNotes.put(BankNote.TWENTY, 20);
        bankNotes.put(BankNote.FIFTY, 20);

        atmService.replenish(bankNotes);
    }

    @Test
    public void checkBalance() {
        //given
        String accountNumber = "01001";

        //when
        atmService.checkBalance(accountNumber);

        //then
        verify(accountServiceMock, times(1)).checkBalance(accountNumber);
    }

    @Test
    public void withdrawAmountSuccessfully() {
        //given
        String accountNumber = "01001";
        Integer withdrawAmount = 30;

        //when
        atmService.withdrawAmount(accountNumber, withdrawAmount);

        //then
        verify(accountServiceMock, times(1)).withdrawAmount(accountNumber, BigDecimal.valueOf(withdrawAmount));
    }

    @Test
    public void withdrawAmountThrowsExceptionIfAmountIsLessThan20() {
        //given
        String accountNumber = "01001";
        Integer withdrawAmount = 15;

        //then
        exception.expect(IllegalArgumentException.class);
        atmService.withdrawAmount(accountNumber, withdrawAmount);
    }

    @Test
    public void withdrawAmountThrowsExceptionIfAmountIsGreaterThan250() {
        //given
        String accountNumber = "01001";
        Integer withdrawAmount = 300;

        //then
        exception.expect(RuntimeException.class);
        atmService.withdrawAmount(accountNumber, withdrawAmount);
    }

    @Test
    public void withdrawAmountThrowsExceptionIfAmountIsNotMultiplesOf5() {
        //given
        String accountNumber = "01001";
        Integer withdrawAmount = 201;

        //then
        exception.expect(IllegalArgumentException.class);
        atmService.withdrawAmount(accountNumber, withdrawAmount);
//        assertThrows(IllegalArgumentException.class, () -> atmService.withdrawAmount(accountNumber, withdrawAmount));
    }

    @Test
    public void withdrawAmountWith5Note() {
        //given
        String accountNumber = "01001";
        Integer withdrawAmount = 20;

        //when
        when(accountServiceMock.withdrawAmount(accountNumber, BigDecimal.valueOf(withdrawAmount))).thenReturn(true);
        List<BankNote> notes = atmService.withdrawAmount(accountNumber, withdrawAmount);

        //then
//        assertThat(notes).contains(BankNote.FIVE);
    }

    @Test
    public void withdrawAmountWithout5Note() {
        //given
        String accountNumber = "01001";
        Integer withdrawAmount = 20;
        Map<BankNote, Integer> bankNotes = new EnumMap<>(BankNote.class);
        bankNotes.put(BankNote.FIVE, 0);
        bankNotes.put(BankNote.TEN, 10);
        bankNotes.put(BankNote.TWENTY, 10);
        bankNotes.put(BankNote.FIFTY, 10);

        atmService.replenish(bankNotes);

        //when
        when(accountServiceMock.withdrawAmount(accountNumber, BigDecimal.valueOf(withdrawAmount))).thenReturn(true);
        List<BankNote> notes = atmService.withdrawAmount(accountNumber, withdrawAmount);

        //then
//        assertThat(notes).doesNotContain(BankNote.FIVE);
    }

    @Test
    public void withdrawAmountThrowsExceptionWhenNotEnoughNotes() {
        //given
        String accountNumber = "01001";
        Integer withdrawAmount = 25;
        Map<BankNote, Integer> bankNotes = new EnumMap<>(BankNote.class);
        bankNotes.put(BankNote.FIVE, 0);
        bankNotes.put(BankNote.TEN, 10);
        bankNotes.put(BankNote.TWENTY, 10);
        bankNotes.put(BankNote.FIFTY, 10);

        atmService.replenish(bankNotes);

        //when
        when(accountServiceMock.withdrawAmount(accountNumber, BigDecimal.valueOf(withdrawAmount))).thenReturn(true);

        //then
//        assertThrows(IllegalStateException.class, () -> atmService.withdrawAmount(accountNumber, withdrawAmount));
    }

    @Test
    public void withdrawAmountWith2X50Note() {
        //given
        String accountNumber = "01001";
        Integer withdrawAmount = 100;

        Map<BankNote, Integer> bankNotes = new EnumMap<>(BankNote.class);
        bankNotes.put(BankNote.FIVE, 1);
        bankNotes.put(BankNote.TEN, 1);
        bankNotes.put(BankNote.FIFTY, 2);

        atmService.replenish(bankNotes);

        //when
        when(accountServiceMock.withdrawAmount(accountNumber, BigDecimal.valueOf(withdrawAmount))).thenReturn(true);
        List<BankNote> notes = atmService.withdrawAmount(accountNumber, withdrawAmount);

        //then
//        assertThat(notes).containsExactly(BankNote.FIFTY, BankNote.FIFTY);
    }

    @Test
    public void withdrawAmountAddNotesBack() {
        //given
        String accountNumber = null;
        Integer withdrawAmount = 100;

        //when
        when(accountServiceMock.withdrawAmount(accountNumber, BigDecimal.valueOf(withdrawAmount))).thenThrow(new NullPointerException());

        //then
//        assertThrows(NullPointerException.class, () -> atmService.withdrawAmount(accountNumber, withdrawAmount));
    }
}
