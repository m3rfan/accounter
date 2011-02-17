package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.bizantra.server.utils.SecureUtils;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * For every {@link IssuePayment} entry there will be one or more
 * TransactionIssuePayment entries. These are populated whenever any transaction
 * involves payment method as 'Check' and has to be printed.
 * 
 * @author Chandan
 * 
 */
public class TransactionIssuePayment implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3092025774192829205L;

	/**
	 * A Unique long id Generated By Hibernate.
	 */
	long id;

	/**
	 * This will hold a secure 40 digit random number.
	 */
	public String stringID;

	/**
	 * Transaction Issue payment Date.
	 */
	FinanceDate date;

	String number;

	/**
	 * This is used to indicate the name of the payee
	 */
	String name;

	String memo;

	/**
	 * The amount that how much we have to pay in this TransationIssuePayment
	 */
	double amount = 0D;

	/**
	 * The transaction reference holds the Issue Payment reference which holds
	 * the TransactionIssuePayment
	 */
	Transaction transaction;

	/**
	 * Reference to a write check to know that this TransactionIssuePayment is
	 * caused by WriteCheck
	 */
	WriteCheck writeCheck;

	/**
	 * Reference to a CustomerRefund to know that this TransactionIssuePayment
	 * is caused by CustomerRefund
	 */
	CustomerRefund customerRefund;

	/**
	 * Reference to a PayBill to know that this TransactionIssuePayment is
	 * caused by PayBill
	 */
	PayBill payBill;

	/**
	 * Reference to a PayBill to know that this TransactionIssuePayment is
	 * caused by PayBill
	 */
	CreditCardCharge creditCardCharge;

	/**
	 * Reference to a CashPurchase to know that this TransactionIssuePayment is
	 * caused by CashPurchase
	 */
	CashPurchase cashPurchase;

	/**
	 * Reference to a PaySalesTax to know that this TransactionIssuePayment is
	 * caused by PaySalesTax
	 */
	PaySalesTax paySalesTax;
	/**
	 * Reference to a ReceiveVAT to know that this TransactionIssuePayment is
	 * caused by ReceiveVAT
	 */
	ReceiveVAT receiveVAT;
	/**
	 * Reference to a PayVAT to know that this TransactionIssuePayment is caused
	 * by PayVAT
	 */
	PayVAT payVAT;
	/**
	 * Reference to a CustomerPrepayment to know that this
	 * TransactionIssuePayment is caused by CustomerPrepayment
	 */
	private CustomerPrePayment customerPrepayment;

	int version;

	transient boolean isImported;

	transient private boolean isOnSaveProccessed;

	public TransactionIssuePayment() {

	}

	/**
	 * @return the payBill
	 */
	public PayBill getPayBill() {
		return payBill;
	}

	/**
	 * @param payBill
	 *            the payBill to set
	 */
	public void setPayBill(PayBill payBill) {
		this.payBill = payBill;
	}

	/**
	 * @return the creditCardCharge
	 */
	public CreditCardCharge getCreditCardCharge() {
		return creditCardCharge;
	}

	/**
	 * @param creditCardCharge
	 *            the creditCardCharge to set
	 */
	public void setCreditCardCharge(CreditCardCharge creditCardCharge) {
		this.creditCardCharge = creditCardCharge;
	}

	/**
	 * @return the cashPurchase
	 */
	public CashPurchase getCashPurchase() {
		return cashPurchase;
	}

	/**
	 * @param cashPurchase
	 *            the cashPurchase to set
	 */
	public void setCashPurchase(CashPurchase cashPurchase) {
		this.cashPurchase = cashPurchase;
	}

	/**
	 * @return the paySalesTax
	 */
	public PaySalesTax getPaySalesTax() {
		return paySalesTax;
	}

	/**
	 * @param paySalesTax
	 *            the paySalesTax to set
	 */
	public void setPaySalesTax(PaySalesTax paySalesTax) {
		this.paySalesTax = paySalesTax;
	}

	/**
	 * @return the payVAT
	 */

	public PayVAT getPayVAT() {
		return payVAT;
	}

	/**
	 * @param payVAT
	 *            the payVAT to set
	 */
	public void setPayVAT(PayVAT payVAT) {
		this.payVAT = payVAT;
	}

	/**
	 * @return the receiveVAT
	 */
	public ReceiveVAT getReceiveVAT() {
		return receiveVAT;
	}

	/**
	 * @param receiveVAT
	 *            the receiveVAT to set
	 */
	public void setReceiveVAT(ReceiveVAT receiveVAT) {
		this.receiveVAT = receiveVAT;
	}

	/**
	 * @return the CustomerPrePayment
	 */
	public CustomerPrePayment getCustomerPrepayment() {
		return customerPrepayment;
	}

	/**
	 * @param CustomerPrePayment
	 *            the CustomerPrePayment to set
	 */
	public void setCustomerPrepayment(CustomerPrePayment customerPrepayment) {
		this.customerPrepayment = customerPrepayment;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	public int getVersion() {
		return version;
	}

	/**
	 * @return the date
	 */
	public FinanceDate getDate() {
		return date;
	}

	/**
	 * @return the writeCheck
	 */
	public WriteCheck getWriteCheck() {
		return writeCheck;
	}

	/**
	 * @param writeCheck
	 *            the writeCheck to set
	 */
	public void setWriteCheck(WriteCheck writeCheck) {
		this.writeCheck = writeCheck;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @return the customerRefund
	 */
	public CustomerRefund getCustomerRefund() {
		return customerRefund;
	}

	/**
	 * @param customerRefund
	 *            the customerRefund to set
	 */
	public void setCustomerRefund(CustomerRefund customerRefund) {
		this.customerRefund = customerRefund;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// ChangeTracker.put(this);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isImported) {
			return false;
		}
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		this.stringID = this.stringID == null || this.stringID != null
				&& this.stringID.isEmpty() ? SecureUtils.createID()
				: this.stringID;

		if (this.id == 0l) {

			if (this.writeCheck != null) {

				// Update the Status of Write Check as Issued
				this.writeCheck
						.setStatus(Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED);
				session.saveOrUpdate(this.writeCheck);

			} else if (this.getCustomerRefund() != null) {

				// Update the Status of Customer Refund as Issued
				this.customerRefund
						.setStatus(Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED);
				session.saveOrUpdate(this.customerRefund);
			} else if (this.getPayBill() != null) {

				// Update the Status of Customer Refund as Issued
				this.getPayBill().setStatus(
						Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED);
				session.saveOrUpdate(this.getPayBill());
			} else if (this.getPaySalesTax() != null) {

				// Update the Status of Customer Refund as Issued
				this.getPaySalesTax().setStatus(
						Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED);
				session.saveOrUpdate(this.getPaySalesTax());
			} else if (this.getCreditCardCharge() != null) {

				// Update the Status of Customer Refund as Issued
				this.getCreditCardCharge().setStatus(
						Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED);
				session.saveOrUpdate(this.getCreditCardCharge());
			} else if (this.getCashPurchase() != null) {

				// Update the Status of Customer Refund as Issued
				this.getCashPurchase().setStatus(
						Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED);
				session.saveOrUpdate(this.getCashPurchase());

			} else if (this.getPayVAT() != null) {

				// Update the Status of Pay Vat vat as Issued
				this.getPayVAT().setStatus(
						Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED);
				session.saveOrUpdate(this.getPayVAT());

			} else if (this.getReceiveVAT() != null) {

				// Update the Status of Receive Vat as Issued
				this.getReceiveVAT().setStatus(
						Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED);
				session.saveOrUpdate(this.getReceiveVAT());

			} else if (this.getCustomerPrepayment() != null) {
				// Update the Status of CustomerPrepayment as Issued
				this.getCustomerPrepayment().setStatus(
						Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED);
				session.saveOrUpdate(this.getCustomerPrepayment());
			}

		}
		// ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		// ChangeTracker.put(this);
		return false;
	}

	@Override
	public String getStringID() {
		// TODO Auto-generated method stub
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	@Override
	public void setImported(boolean isImported) {
		this.isImported = isImported;
	}

	public boolean equals(TransactionIssuePayment obj) {
		if ((this.transaction != null && obj.transaction != null) ? (this.transaction
				.equals(obj.transaction))
				: true && (this.writeCheck != null && obj.writeCheck != null) ? (this.writeCheck
						.equals(obj.writeCheck))
						: true && (this.customerRefund != null && obj.customerRefund != null) ? (this.customerRefund
								.equals(obj.customerRefund))
								: true && (this.payBill != null && obj.payBill != null) ? (this.payBill
										.equals(obj.payBill))
										: true && (this.creditCardCharge != null && obj.creditCardCharge != null) ? (this.creditCardCharge
												.equals(obj.creditCardCharge))
												: true && (this.cashPurchase != null && obj.cashPurchase != null) ? (this.cashPurchase
														.equals(obj.cashPurchase))
														: true && (this.paySalesTax != null && obj.paySalesTax != null) ? (this.paySalesTax
																.equals(obj.paySalesTax))
																: true && (!DecimalUtil
																		.isEquals(
																				this.amount,
																				0) && !DecimalUtil
																		.isEquals(
																				obj.amount,
																				0)) ? DecimalUtil
																		.isEquals(
																				this.amount,
																				obj.amount)
																		: true) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}

}
