package com.modulr.atm;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BankNoteHelper
{
	//ATM bank notes
	private Map<BankNote, Integer> bankNotes;
	
	BankNoteHelper()
	{
		this.bankNotes = new EnumMap<>(BankNote.class);
	}

	/**
	 * Replenish bank notes into ATM
	 * @param bankNotes - holds bankNotes
	 */
	public void replenish(Map<BankNote, Integer> bankNotes)
	{
		this.bankNotes.clear();
		this.bankNotes.putAll(bankNotes);
	}
	
	/**
	 * Iterate through the list and subtract the bank note
	 * @param notes - list of note to be subtracted
	 */
	public void subtractBankNotes(List<BankNote> notes)
	{
		notes.forEach(note -> bankNotes.put(note, bankNotes.get(note) - 1));
	}
	
	/**
	 * Iterate through the list and add the bank note back
	 * @param notes - list of bank note to be added
	 */
	public void addBankNotes(List<BankNote> notes)
	{
		notes.forEach(note -> bankNotes.put(note, bankNotes.get(note) + 1));
	}
	
	/**
	 * This method returns a copy of the ATM bank notes
	 * 
	 * Note: creates a new EnumMap every time we call this method
	 * so that any updates to this map does affect actual ATM bank notes
	 * 
	 * @return - a copy of the repository
	 */
	public Map<BankNote, Integer> getBankNotes()
	{
		return new EnumMap<>(bankNotes);
	}
}
