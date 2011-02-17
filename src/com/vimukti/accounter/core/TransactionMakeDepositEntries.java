package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.bizantra.server.utils.SecureUtils;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;

public class TransactionMakeDepositEntries implements IAccounterServerCore,
		Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7558189028423742152L;
	public static final int TYPE_FINANCIAL_ACCOUNT = 3;
	public static final int TYPE_VENDOR = 2;
	public static final int TYPE_CUSTOMER = 1;

	long id;

	public String stringID;

	/**
	 * In this variable we store the type of the entry that was being created
	 * for this Class.
	 */
	int type;

	/**
	 * This reference to Transaction class is maintained to know what
	 * MakeDeposit results this TransactionMakeDepositEntries.
	 */
	private Transaction transaction;

	// private PaymentMethod paymentMethod;
	//
	/**
	 * This Account reference is used to indicate the Bank account to where the
	 * Make Deposit total has to be stored.
	 */
	private Account account;
	//
	// private Vendor vendor;
	//
	// private Customer customer;
	//
	// private String reference;

	/**
	 * The amount with which this object to be created.
	 */
	private double amount;

	/**
	 * The amount which has to be paid still is maintained in this variable.
	 */
	private double balance;

	transient boolean isImported;

	public TransactionMakeDepositEntries() {

	}

	public TransactionMakeDepositEntries(Account account,
			Transaction transaction, double amount) {
		this.transaction = transaction;
		this.account = account;
		this.amount = amount;
		this.balance = amount;

	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	public void updateAmount(double amount) {
		this.amount += amount;
		this.balance += amount;

	}

	@Override
	public String getStringID() {
		// TODO Auto-generated method stub
		return this.stringID;
	}

	@Override
	public void setImported(boolean isImported) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

	/**
	 * To convert the transactionMakeDeposits from the server side object to
	 * client side object.
	 * 
	 * @param transactionMakeDepositEntries
	 * @return
	 */
	public static List<ClientTransactionMakeDeposit> prepareClientTransactionMakeDeposits(
			List<TransactionMakeDepositEntries> transactionMakeDepositEntries) {
		List<ClientTransactionMakeDeposit> list = new ArrayList<ClientTransactionMakeDeposit>();
		if (transactionMakeDepositEntries != null
				&& transactionMakeDepositEntries.size() > 0) {

			for (TransactionMakeDepositEntries transactionMakeDepositEntry : transactionMakeDepositEntries) {

				Payee payee = transactionMakeDepositEntry.getTransaction()
						.getInvolvedPayee();

				ClientTransactionMakeDeposit clientTransactionMakeDeposit = new ClientTransactionMakeDeposit();
				clientTransactionMakeDeposit
						.setDepositedTransaction(transactionMakeDepositEntry
								.getTransaction().getStringID());
				clientTransactionMakeDeposit
						.setDate(transactionMakeDepositEntry.getTransaction()
								.getDate().getTime());
				clientTransactionMakeDeposit
						.setNumber(transactionMakeDepositEntry.getTransaction()
								.getNumber());
				clientTransactionMakeDeposit
						.setAmount(transactionMakeDepositEntry.getAmount());

				clientTransactionMakeDeposit
						.setCashAccount(transactionMakeDepositEntry
								.getAccount().getStringID());
				clientTransactionMakeDeposit
						.setPaymentMethod(transactionMakeDepositEntry
								.getTransaction().getPaymentMethod());
				clientTransactionMakeDeposit
						.setReference(transactionMakeDepositEntry
								.getTransaction().getReference());

				if (payee != null) {
					if (payee.getType() == TYPE_CUSTOMER) {
						clientTransactionMakeDeposit.setType(TYPE_CUSTOMER);
						clientTransactionMakeDeposit.setCustomer(payee
								.getStringID());
					} else if (payee.getType() == TYPE_VENDOR) {
						clientTransactionMakeDeposit.setType(TYPE_VENDOR);
						clientTransactionMakeDeposit.setVendor(payee
								.getStringID());
					}

				} else {
					clientTransactionMakeDeposit
							.setType(TYPE_FINANCIAL_ACCOUNT);
					clientTransactionMakeDeposit
							.setAccount(transactionMakeDepositEntry
									.getAccount().getStringID());
				}
				list.add(clientTransactionMakeDeposit);
			}
		}

		return list;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		this.stringID = this.stringID == null || this.stringID != null
    && this.stringID.isEmpty() ? SecureUtils.createID()
    : this.stringID;
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

}
