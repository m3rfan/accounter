package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientReceivePayment extends ClientTransaction {

	public static final int TYPE_RECEIVEPAYMENT = 1;

	public static final int TYPE_CUSTOMER_PAYMENT = 2;

	String customer;

	ClientAddress address;

	private double amount = 0D;

	double customerBalance = 0D;

	String depositIn;

	double unUsedCredits = 0D;

	double unUsedPayments = 0D;

	double totalCashDiscount = 0D;

	double totalWriteOff = 0D;

	String accountsReceivable;

	double totalAppliedCredits = 0D;

	String creditsAndPayments;

	int receivePaymentType;

	boolean isToBePrinted;

	String checkNumber;

	double endingBalance;

	private double unusedAmount = 0D;

	// ClientTaxCode VATCode;
	//	
	// double VATFraction;
	//	
	//	
	//
	//
	// public ClientTaxCode getVATCode() {
	// return VATCode;
	// }
	//
	// public void setVATCode(ClientTaxCode code) {
	// VATCode = code;
	// }
	//
	// public double getVATFraction() {
	// return VATFraction;
	// }
	//
	// public void setVATFraction(double fraction) {
	// VATFraction = fraction;
	// }

	/**
	 * @return the version
	 */
	@Override
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return this.customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * @return the customerBalance
	 */
	public double getCustomerBalance() {
		return customerBalance;
	}

	/**
	 * @param customerBalance
	 *            the customerBalance to set
	 */
	public void setCustomerBalance(double customerBalance) {
		this.customerBalance = customerBalance;
	}

	/**
	 * @return the depositIn
	 */
	public String getDepositIn() {
		return this.depositIn;
	}

	/**
	 * @param depositIn
	 *            the depositIn to set
	 */
	public void setDepositIn(String depositIn) {
		this.depositIn = depositIn;
	}

	/**
	 * @return the unUsedCredits
	 */
	public double getUnUsedCredits() {
		return unUsedCredits;
	}

	/**
	 * @param unUsedCredits
	 *            the unUsedCredits to set
	 */
	public void setUnUsedCredits(double unUsedCredits) {
		this.unUsedCredits = unUsedCredits;
	}

	/**
	 * @return the unUsedPayments
	 */
	public double getUnUsedPayments() {
		return unUsedPayments;
	}

	/**
	 * @param unUsedPayments
	 *            the unUsedPayments to set
	 */
	public void setUnUsedPayments(double unUsedPayments) {
		this.unUsedPayments = unUsedPayments;
	}

	public double getTotalCashDiscount() {
		return totalCashDiscount;
	}

	public void setTotalCashDiscount(double totalCashDiscount) {
		this.totalCashDiscount = totalCashDiscount;
	}

	public double getTotalWriteOff() {
		return totalWriteOff;
	}

	public void setTotalWriteOff(double totalWriteOff) {
		this.totalWriteOff = totalWriteOff;
	}

	/**
	 * @return the accountsReceivable
	 */
	public String getAccountsReceivable() {
		return accountsReceivable;
	}

	/**
	 * @param accountsReceivable
	 *            the accountsReceivable to set
	 */
	public void setAccountsReceivable(String accountsReceivable) {
		this.accountsReceivable = accountsReceivable;
	}

	/**
	 * @return the totalAppliedCredits
	 */
	public double getTotalAppliedCredits() {
		return totalAppliedCredits;
	}

	/**
	 * @param totalAppliedCredits
	 *            the totalAppliedCredits to set
	 */
	public void setTotalAppliedCredits(double totalAppliedCredits) {
		this.totalAppliedCredits = totalAppliedCredits;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public String getStringID() {
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientReceivePayment";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.RECEIVEPAYMENT;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public int getReceivePaymentType() {
		return receivePaymentType;

	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public ClientAddress getAddress() {
		return this.address;
	}

	public void setReceivePaymentType(int typeReceivePayment) {

		this.receivePaymentType = typeReceivePayment;

	}

	public void setCustomer(ClientCustomer customer) {
		this.customer = customer.getStringID();

	}

	public ClientAddress setAddress(ClientAddress billingAddress) {
		return address;

	}

	public void setDepositIn(ClientAccount depositInAccount) {
		this.depositIn = depositInAccount.getStringID();
	}

	public void setToBePrinted(Boolean isToBePrinted) {
		this.isToBePrinted = isToBePrinted;

	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;

	}

	public void setEndingBalance(Double toBeSetEndingBalance) {
		this.endingBalance = amount;
	}

	public void setUnusedAmount(Double unusedAmount) {
		this.unusedAmount = unusedAmount;

	}

	public Double getUnusedAmount() {
		return unusedAmount;
	}

}
