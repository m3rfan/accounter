package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class NewCreditCardExpenseCommand extends NewAbstractTransactionCommand {

	private static final String DELIVERY_DATE = "deliveryDate";
	ClientCreditCardCharge creditCardCharge;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new VendorRequirement(VENDOR, getMessages().pleaseEnterName(
				Global.get().Vendor()), getMessages().payeeName(
				Global.get().Vendor()), false, true,
				new ChangeListner<Vendor>() {

					@Override
					public void onSelection(Vendor value) {
						get(PHONE).setValue(value.getPhoneNo());
						get(CONTACT).setValue(null);
						for (Contact contact : value.getContacts()) {
							if (contact.isPrimary()) {
								get(CONTACT).setValue(contact);
								break;
							}
						}

						try {
							double mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							NewCreditCardExpenseCommand.this.get(
									CURRENCY_FACTOR).setValue(
									mostRecentTransactionCurrencyFactor);
						} catch (AccounterException e) {
							e.printStackTrace();
						}

					}
				}) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(Global.get().Vendor());
			}

			@Override
			protected List<Vendor> getLists(Context context) {
				return getVendors();
			}

			@Override
			protected boolean filter(Vendor e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), getMessages()
				.currencyFactor()) {
			@Override
			protected ClientCurrency getSelectedCurrency() {
				Vendor vendor = (Vendor) NewCreditCardExpenseCommand.this.get(
						VENDOR).getValue();
				return getCurrency(vendor.getCurrency().getID());
			}

		});

		list.add(new TransactionItemTableRequirement(ITEMS,
				"Please Enter Item Name or number", getMessages().items(),
				true, true) {

			@Override
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				List<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.isIBuyThisItem()) {
						if (item.isActive())
							items.add(item);
					}
				}
				return items;
			}

			@Override
			public boolean isSales() {
				return false;
			}

			@Override
			protected Payee getPayee() {
				return (Vendor) NewCreditCardExpenseCommand.this.get(VENDOR)
						.getValue();
			}

			@Override
			protected double getCurrencyFactor() {
				return NewCreditCardExpenseCommand.this.getCurrencyFactor();
			}

		});

		list.add(new TransactionAccountTableRequirement(ACCOUNTS, getMessages()
				.pleaseEnterNameOrNumber(getMessages().Account()),
				getMessages().Account(), true, true) {

			@Override
			protected List<Account> getAccounts(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account account) {
							if (account.getIsActive()
									&& account.getType() != Account.TYPE_CASH
									&& account.getType() != Account.TYPE_BANK
									&& account.getType() != Account.TYPE_INVENTORY_ASSET
									&& account.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
									&& account.getType() != Account.TYPE_ACCOUNT_PAYABLE
									&& account.getType() != Account.TYPE_INCOME
									&& account.getType() != Account.TYPE_OTHER_INCOME
									&& account.getType() != Account.TYPE_OTHER_CURRENT_ASSET
									&& account.getType() != Account.TYPE_OTHER_CURRENT_LIABILITY
									&& account.getType() != Account.TYPE_OTHER_ASSET
									&& account.getType() != Account.TYPE_EQUITY
									&& account.getType() != Account.TYPE_LONG_TERM_LIABILITY) {
								return true;
							} else {
								return false;
							}
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			public boolean isSales() {
				return false;
			}

			@Override
			protected Payee getPayee() {
				return (Vendor) NewCreditCardExpenseCommand.this.get(VENDOR)
						.getValue();
			}

			@Override
			protected boolean isTrackTaxPaidAccount() {
				return false;
			}

		});
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().date()), getMessages().date(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, false));

		list.add(new ContactRequirement(CONTACT, getMessages().pleaseEnter(
				getMessages().contactName()), getMessages().contacts(), true,
				true, null) {

			@Override
			protected Payee getPayee() {
				return get(VENDOR).getValue();
			}
		});

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phone(), true, true));

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getMessages().paymentMethod()), getMessages()
				.paymentMethod(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages()
						.pleaseSelect(getMessages().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPaymentMethods();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().paymentMethod());
			}
		});
		list.add(new AccountRequirement(PAY_FROM, getMessages()
				.pleaseSelectPayFromAccount(), getMessages().bankAccount(),
				false, false, null) {
			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().payFrom());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (obj.getIsActive()
							&& obj.getType() == ClientAccount.TYPE_CREDIT_CARD) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Accounts());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return false;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(new UserCommand("createAccount", "Credit card"));
			}
		});

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getMessages().deliveryDate()), getMessages().deliveryDate(),
				true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnterName(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				new ChangeListner<TAXCode>() {

					@Override
					public void onSelection(TAXCode value) {
						setTaxCodeToItems(value);
					}
				}) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& getPreferences().isTrackPaidTax()
						&& !getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				return new ArrayList<TAXCode>(context.getCompany()
						.getTaxCodes());
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().toLowerCase().startsWith(name);
			}

		});

		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context.getPreferences();
				if (preferences.isTrackTax()
						&& getPreferences().isTrackPaidTax()
						&& !preferences.isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return "Include VAT with Amount enabled";
			}

			@Override
			protected String getFalseString() {
				return "Include VAT with Amount disabled";
			}
		});
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}
		Vendor supplier = get(VENDOR).getValue();
		creditCardCharge.setVendor(supplier.getID());

		Contact contact = get(CONTACT).getValue();
		creditCardCharge.setContact(toClientContact(contact));

		ClientFinanceDate date = get(DATE).getValue();
		creditCardCharge.setDate(date.getDate());

		creditCardCharge.setType(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);

		String number = get(NUMBER).getValue();
		creditCardCharge.setNumber(number);

		String paymentMethod = get(PAYMENT_METHOD).getValue();
		creditCardCharge.setPaymentMethod(paymentMethod);

		String phone = get(PHONE).getValue();
		creditCardCharge.setPhone(phone);

		Account account = get(PAY_FROM).getValue();
		creditCardCharge.setPayFrom(account.getID());

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		creditCardCharge.setDeliveryDate(deliveryDate.getDate());
		items.addAll(accounts);
		creditCardCharge.setTransactionItems(items);
		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && getPreferences().isTrackPaidTax()
				&& !preferences.isTaxPerDetailLine()) {
			creditCardCharge.setAmountsIncludeVAT(isVatInclusive);
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : accounts) {
				item.setTaxCode(taxCode.getID());
			}
		}

		String memo = get(MEMO).getValue();
		creditCardCharge.setMemo(memo);
		creditCardCharge.setCurrency(supplier.getCurrency().getID());
		creditCardCharge.setCurrencyFactor((Double) get(CURRENCY_FACTOR)
				.getValue());
		updateTotals(context, creditCardCharge, false);
		create(creditCardCharge, context);

		return null;
	}

	protected void setTaxCodeToItems(TAXCode value) {
		List<ClientTransactionItem> items = this.get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		List<ClientTransactionItem> allrecords = new ArrayList<ClientTransactionItem>();
		allrecords.addAll(items);
		allrecords.addAll(accounts);
		for (ClientTransactionItem clientTransactionItem : allrecords) {
			clientTransactionItem.setTaxCode(value.getID());
		}
	}

	private String getNextTransactionNumber(Context context) {
		String nextTransactionNumber = new FinanceTool()
				.getNextTransactionNumber(
						ClientTransaction.TYPE_CREDIT_CARD_EXPENSE, context
								.getCompany().getID());
		return nextTransactionNumber;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		String string = context.getString();
		if (isUpdate) {
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().cashExpense()));
				return "expensesList ," + getMessages().creditCard();
			}
			creditCardCharge = getTransaction(string,
					AccounterCoreType.CREDITCARDCHARGE, context);

			if (creditCardCharge == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().cashExpense()));
				return "expensesList " + string + " ,"
						+ getMessages().creditCard();
			}
			setValues(context);
		} else {
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			creditCardCharge = new ClientCreditCardCharge();
		}
		setTransaction(creditCardCharge);
		return null;
	}

	private void setValues(Context context) {
		get(CURRENCY_FACTOR).setValue(creditCardCharge.getCurrencyFactor());
		List<ClientTransactionItem> items = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> accounts = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> transactionItemsList = creditCardCharge
				.getTransactionItems();
		for (ClientTransactionItem clientTransactionItem : transactionItemsList) {
			if (clientTransactionItem.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				accounts.add(clientTransactionItem);
			} else {
				items.add(clientTransactionItem);
			}
		}
		get(ACCOUNTS).setValue(accounts);
		get(ITEMS).setValue(items);
		get(VENDOR).setValue(
				CommandUtils.getServerObjectById(creditCardCharge.getVendor(),
						AccounterCoreType.VENDOR));
		get(CONTACT).setValue(toServerContact(creditCardCharge.getContact()));
		get(DATE).setValue(creditCardCharge.getDate());
		get(NUMBER).setValue(creditCardCharge.getNumber());
		get(PAYMENT_METHOD).setValue(
				CommandUtils.getPaymentMethod(
						creditCardCharge.getPaymentMethodForCommands(),
						getMessages()));
		get(PHONE).setValue(creditCardCharge.getPhone());
		get("payFrom").setValue(
				CommandUtils.getServerObjectById(creditCardCharge.getPayFrom(),
						AccounterCoreType.ACCOUNT));
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && getPreferences().isTrackPaidTax()
				&& !preferences.isTaxPerDetailLine()) {
			get(TAXCODE).setValue(
					getTaxCodeForTransactionItems(
							creditCardCharge.getTransactionItems(), context));
		}
		get("deliveryDate").setValue(
				new ClientFinanceDate(creditCardCharge.getDeliveryDate()));
		get(IS_VAT_INCLUSIVE).setValue(creditCardCharge.isAmountsIncludeVAT());
		get(MEMO).setValue(creditCardCharge.getMemo());
	}

	@Override
	protected String getWelcomeMessage() {
		return creditCardCharge.getID() == 0 ? getMessages().creating(
				getMessages().creditCardExpense())
				: "Update Credit Card Expense command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return creditCardCharge.getID() == 0 ? getMessages().readyToCreate(
				getMessages().creditCardExpense()) : getMessages()
				.readyToUpdate(getMessages().creditCardExpense());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(getNextTransactionNumber(context));
		get(PHONE).setDefaultValue(" ");
		Contact contact = new Contact();
		contact.setName(null);
		get(CONTACT).setDefaultValue(contact);
		get(MEMO).setDefaultValue(" ");
		get("deliveryDate").setDefaultValue(new ClientFinanceDate());
		get(PAYMENT_METHOD).setDefaultValue(getMessages().creditCard());
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
	}

	@Override
	public String getSuccessMessage() {
		return creditCardCharge.getID() == 0 ? getMessages()
				.createSuccessfully(getMessages().creditCardExpense())
				: getMessages().updateSuccessfully(
						getMessages().creditCardExpense());
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			addFirstMessage(context, getMessages()
					.transactiontotalcannotbe0orlessthan0());
		}
		super.beforeFinishing(context, makeResult);
	}

	@Override
	protected Payee getPayee() {
		return (Vendor) NewCreditCardExpenseCommand.this.get(VENDOR).getValue();
	}
}
