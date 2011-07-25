/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vimukti5
 * 
 */
@SuppressWarnings("serial")
public class ClientVATReturn extends ClientTransaction {

	/**
	 * The Start date of this VAT Return
	 */
	long VATperiodStartDate;
	/**
	 * The End date of this VAT Return
	 */
	long VATperiodEndDate;

	/**
	 * The VAT Agency to which we are going to create this VAT Return
	 * 
	 */
	long taxAgency;

	List<ClientBox> boxes = new ArrayList<ClientBox>();

	String journalEntry;

	double balance;

	/**
	 * @return the vATperiodStartDate
	 */
	public long getVATperiodStartDate() {
		return VATperiodStartDate;
	}

	/**
	 * @param tperiodStartDate
	 *            the vATperiodStartDate to set
	 */
	public void setVATperiodStartDate(long tperiodStartDate) {
		VATperiodStartDate = tperiodStartDate;
	}

	/**
	 * @return the vatAgency
	 */
	public long getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param taxAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(long taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * @return the journalEntry
	 */
	public String getJournalEntry() {
		return journalEntry;
	}

	/**
	 * @param journalEntry
	 *            the journalEntry to set
	 */
	public void setJournalEntry(String journalEntry) {
		this.journalEntry = journalEntry;
	}

	/**
	 * @return the boxes
	 */
	public List<ClientBox> getBoxes() {
		return boxes;
	}

	public long getTAXAgency() {
		return taxAgency;
	}

	public void setTAXAgency(long tAXAgency) {
		taxAgency = tAXAgency;
	}

	public long getVATperiodEndDate() {
		return VATperiodEndDate;
	}

	public void setVATperiodEndDate(long vATperiodEndDate) {
		VATperiodEndDate = vATperiodEndDate;
	}

	/**
	 * @param boxes
	 *            the boxes to set
	 */
	public void setBoxes(List<ClientBox> boxes) {
		this.boxes = boxes;
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#getClientClassSimpleName
	 * ()
	 */
	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return null;
	}

	/*
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getName()
	 */
	@Override
	public String getName() {
		return null;
	}

	/*
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getObjectType()
	 */
	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.VATRETURN;
	}

	/*
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getID()
	 */
	@Override
	public long getID() {
		return this.id;
	}

	/*
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#setID(java
	 * .lang.String)
	 */
	@Override
	public void setID(long id) {
		this.id = id;

	}

}
