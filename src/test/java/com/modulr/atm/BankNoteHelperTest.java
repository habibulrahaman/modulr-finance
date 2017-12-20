package com.modulr.atm;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BankNoteHelperTest
{
	private BankNoteHelper helper;

	public BankNoteHelperTest()
	{
		System.out.println(getClass().getSimpleName() + " Constructor.");
	}

	
	@Before
	public void beforeEach()
	{
		System.out.println("BeforeEach");
		helper = new BankNoteHelper();
	}
	
	@Test
	public void replenishWithBankNotes()
	{
		//given
		Map<BankNote, Integer> bankNotes = new EnumMap<>(BankNote.class);
		bankNotes.put(BankNote.FIVE, 20);
		
		//when
		helper.replenish(bankNotes);
		Map<BankNote, Integer> result = helper.getBankNotes();
		
		//then
		assertEquals(1, result.size());
		
		System.out.println("replenishWithBankNotes Test");
	}
	
	@Test
	public void addBankNotesSucessfully()
	{
		//given
		Map<BankNote, Integer> bankNotes = new EnumMap<>(BankNote.class);
		bankNotes.put(BankNote.FIVE, 20);
		
		List<BankNote> notes = new ArrayList<>();
		notes.add(BankNote.FIVE);
		//when
		//replenish first
		helper.replenish(bankNotes);
		
		//Add notes
		helper.addBankNotes(notes);
		
		Map<BankNote, Integer> result = helper.getBankNotes();
		
		//then
		assertEquals(21, result.get(BankNote.FIVE).intValue());
		
		System.out.println("addBankNotesSucessfully Test");
	}
	
	@Test
	public void subtractBankNotesSucessfully()
	{
		//given
		Map<BankNote, Integer> bankNotes = new EnumMap<>(BankNote.class);
		bankNotes.put(BankNote.FIVE, 20);
		
		List<BankNote> notes = new ArrayList<>();
		notes.add(BankNote.FIVE);
		//when
		//replenish first
		helper.replenish(bankNotes);
		
		//subtract notes
		helper.subtractBankNotes(notes);
		
		Map<BankNote, Integer> result = helper.getBankNotes();
		
		//then
		assertEquals(19, result.get(BankNote.FIVE).intValue());
		
		System.out.println("subtractBankNotesSucessfully Test");
	}
}
