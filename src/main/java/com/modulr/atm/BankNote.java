package com.modulr.atm;

/**
 * This enum defines currency denominations 5, 10, 20 and 50
 * The denomination has defined in descending order.
 * @author MohammedHabibulRaham
 *
 */
public enum BankNote
{
	FIFTY(50), TWENTY(20), TEN(10), FIVE(5);
	
	private int value;
	
	BankNote(final int value)
	{
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}