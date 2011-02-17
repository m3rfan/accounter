package com.vimukti.accounter.core;


public class AccountTransactionByTaxCode {

	Account account;
	
	double amount;

	transient boolean isImported;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
	
}
