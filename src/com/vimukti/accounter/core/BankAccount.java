package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.core.IAccounterCore;

public class BankAccount extends Account {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Bank bank;

	private int bankAccountType;

	private String bankAccountNumber;

	private FinanceDate lastReconcilationDate;

	/**
	 * Creates new Instance
	 */
	public BankAccount() {
	}

	public BankAccount(int type, String number, String name, boolean isActive,
			Account parent, int cashFlowCategory, double openingBalance,
			boolean isConsiderAsCashAccount, String comment, Bank bank,
			int bankAccountType, String bankAccountNumber, double creditLimit,
			String cardOrLoanNumber, boolean isIncrease,
			boolean isOpeningBalanceEditable, Account openingBalanceAccount,
			String flow, boolean isDefaultAccount, FinanceDate asOf) {

		super(type, number, name, isActive, parent, cashFlowCategory,
				openingBalance, isConsiderAsCashAccount, comment, creditLimit,
				cardOrLoanNumber, isIncrease, isOpeningBalanceEditable,
				openingBalanceAccount, flow, isDefaultAccount, asOf);

		this.bank = bank;
		this.bankAccountType = bankAccountType;
		this.bankAccountNumber = bankAccountNumber;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public int getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(int bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public FinanceDate getLastReconcilationDate() {
		return lastReconcilationDate;
	}

	public void setLastReconcilationDate(FinanceDate lastReconcilationDate) {
		this.lastReconcilationDate = lastReconcilationDate;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.BANK_ACCOUNT;
	}
}
