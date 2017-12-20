package com.modulr.atm;

import java.util.List;
import java.util.Map;

public interface ATMService
{
	/**
	 * Sets up with currency notes of denominations 5, 10, 20 and 50 
	 */
	void replenish(Map<BankNote, Integer> bankNotes);
	
	/**
	 * Check account balance for the given account number
	 * @param accountNumber - account number
	 * @return - account balance amount in string representation
	 */
	String checkBalance(String accountNumber);
	
	/**
	 * Withdraw amount from the account
	 * @param accountNumber - The account number
	 * @param amount - the specified withdraw amount
	 * @return - true if the withdraw operation is successful or else false
	 */
	List<BankNote> withdrawAmount(String accountNumber, Integer amount);
}
