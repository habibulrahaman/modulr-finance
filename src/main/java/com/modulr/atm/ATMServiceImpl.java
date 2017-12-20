package com.modulr.atm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modulr.account.AccountService;

public class ATMServiceImpl implements ATMService
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private AccountService accountService;
	
	private BankNoteHelper bankNoteHelper;
	
	public ATMServiceImpl(AccountService accountService)
	{
		this.accountService = accountService;
		bankNoteHelper = new BankNoteHelper();
	}

	@Override
	public void replenish(Map<BankNote, Integer> bankNotes)
	{
		logger.info("replenish bank notes");
		bankNoteHelper.replenish(bankNotes);
	}

	@Override
	public String checkBalance(String accountNumber)
	{
		logger.info("check balanace for acount number {} ", accountNumber);
		return accountService.checkBalance(accountNumber);
	}

	@Override
	public List<BankNote> withdrawAmount(String accountNumber, Integer amount)
	{	
		logger.info("withdrawing {} amount from account number {} ", amount, accountNumber);
		validateAmount(amount);
		
		//disburse Bank Notes
		List<BankNote> notes = disburseBankNotes(amount);
		
		try 
		{	
			//update account service
			boolean updated = accountService.withdrawAmount(accountNumber, BigDecimal.valueOf(amount));
			
			if(updated)
			{
				//sort the list in order of enum
				notes.sort(Comparator.naturalOrder());
				return notes;
			}
				
		} catch (Exception exception) 
		{
			//The withdrawAmount operation failed on accountService
			//for this reason the notes are added back to disbursement tray
			bankNoteHelper.addBankNotes(notes);
			
			throw exception;
		}
		
		return Collections.<BankNote>emptyList();
	}

	/**
	 * Disburse bank notes for the given amount
	 * @param amount - the requested amount
	 * @return - List of bank notes to be returned
	 */
	private List<BankNote> disburseBankNotes(Integer amount)
	{
		List<BankNote> notes = calculateNotesWithAtLeastOne5Note(bankNoteHelper.getBankNotes(), amount);
		if(!notes.isEmpty())
		{
			logger.info("disbursing Bank Notes with 5 Note.");
			bankNoteHelper.subtractBankNotes(notes);
			return notes;
		}
		
		notes = calculateNotes(bankNoteHelper.getBankNotes(), new ArrayList<>(), amount);
		if(!notes.isEmpty())
		{
			logger.warn("disbursing Bank Notes without 5 Note.");
			bankNoteHelper.subtractBankNotes(notes);
			return notes;
		}
		
		logger.error("Not enough bank notes, wait for replenish");
		throw new IllegalStateException("Not enough bank notes");
	}

	/**
	 * This method disburse at least one 5 note and then calculates other notes
	 * @param bankNotes - The copy of ATM bank notes
	 * @param amount - the requested amount
	 * @return - List of bank notes to be returned
	 */
	private List<BankNote> calculateNotesWithAtLeastOne5Note(Map<BankNote, Integer> bankNotes, Integer amount)
	{	
		List<BankNote> notes = new ArrayList<>();
		
		BankNote five = BankNote.FIVE;
		
		//Add the five to the list and
		//reduce one five note from the copy of atm bank notes
		updateMapAndList(bankNotes, notes, five);
		
		//If the list is empty means five note is note available
		if(notes.isEmpty())
		{
			return Collections.<BankNote>emptyList();
		}
		
		return calculateNotesWithOutRecursive(bankNotes, notes, amount - five.getValue());
	}

	/**
	 * This method calculate other notes to be added into the List
	 * @param bankNotes - The copy of ATM bank notes
	 * @param notes - holds the list of note to be returned
	 * @param amount - the request amount
	 * @return - the list of note to be returned
	 */
	private List<BankNote> calculateNotes(Map<BankNote, Integer> bankNotes, List<BankNote> notes, Integer amount)
	{
		//The key will be in descending order as this is defined natural order inside Enum.
		for(BankNote note : bankNotes.keySet()) 
		{
			if(amount >= note.getValue() && bankNotes.get(note) > 0)
			{
				//Add the note to the list and
				//reduce one note from the copy of atm bank notes
				updateMapAndList(bankNotes, notes, note);
				
				//recursive
				return calculateNotes(bankNotes, notes, amount - note.getValue());
			}
		}
		
		return amount == 0 ? notes : Collections.<BankNote>emptyList();
	}
	
	private List<BankNote> calculateNotesWithOutRecursive(Map<BankNote, Integer> bankNotes, List<BankNote> notes, Integer amount)
	{
		//The key will be in descending order as this is defined natural order for the BankNote Enum.
		for(BankNote note : bankNotes.keySet()) 
		{
			if(amount >= note.getValue() && bankNotes.get(note) > 0)
			{
				//Add the note to the list and
				//reduce one note from the copy of atm bank notes
				updateMapAndList(bankNotes, notes, note);
				
				amount = amount - note.getValue();
			}
		}
		
		return amount == 0 ? notes : Collections.<BankNote>emptyList();
	}
	
	/**
	 * This method checks if the note is available and then
	 * 1. subtract the count of notes by one
	 * 2. add the note to the into the list
	 * @param bankNotes - Copy of the ATM bank notes
	 * @param notes - List of Notes
	 * @param note - Bank Note
	 */
	private void updateMapAndList(Map<BankNote, Integer> bankNotes, List<BankNote> notes, BankNote note)
	{
		//check if the note is available
		//and has at least one note is available of the specified note
		if(bankNotes.containsKey(note) && (bankNotes.get(note) - 1) >= 0)
		{
			bankNotes.put(note, bankNotes.get(note) - 1);
			notes.add(note);
		} else {
			logger.error("Not Enough {} bank note", note.getValue());
		}
	}

	/**
	 * Validates withdrawal amount between 20 and 250 inclusive, and in multiples of 5
	 * @param amount - The requested amount
	 */
	private void validateAmount(Integer amount)
	{
		final int MIN_AMOUNT = 20;
		
		final int MAX_AMOUNT = 250;
		
		final int DIVISOR = 5;
		
		if(amount < MIN_AMOUNT) 
		{
			throw new IllegalArgumentException("Cannot withdraw amount less than 20");
		}
		
		if(amount > MAX_AMOUNT) 
		{
			throw new IllegalArgumentException("Cannot withdraw amount greater than 250");
		}
		
		if(amount % DIVISOR != 0)
		{
			throw new IllegalArgumentException("The amount should be multiples of 5");
		}
	}
}
