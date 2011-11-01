package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemAccountsRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemItemsRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

public class NewCreditCardChargeCommond extends NewAbstractTransactionCommand {

	private static final String DELIVERY_DATE = "deliveryDate";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new VendorRequirement(VENDOR, getMessages().pleaseEnterName(
				Global.get().Vendor()), getMessages().vendorName(
				Global.get().Vendor()), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(Global.get().Vendor());
			}

			@Override
			protected List<ClientVendor> getLists(Context context) {
				return context.getClientCompany().getVendors();
			}

			@Override
			protected boolean filter(ClientVendor e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new TransactionItemItemsRequirement(ITEMS, getMessages()
				.pleaseEnterName(getConstants().item()), getConstants().item(),
				false, true, true) {

			@Override
			protected List<ClientItem> getLists(Context context) {
				return getClientCompany().getItems();
			}
		});

		list.add(new TransactionItemAccountsRequirement(ACCOUNTS, getMessages()
				.pleaseEnterNameOrNumber(Global.get().Account()), Global.get()
				.Account(), false, true) {

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						if (account.getType() != ClientAccount.TYPE_CASH
								&& account.getType() != ClientAccount.TYPE_BANK
								&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != ClientAccount.TYPE_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& account.getType() != ClientAccount.TYPE_EQUITY
								&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY) {
							return true;
						} else {
							return false;
						}
					}
				}, getClientCompany().getAccounts());
			}
		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().date()), getConstants().date(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().number()), getConstants().number(), true, false));

		list.add(new ContactRequirement(CONTACT, getMessages().pleaseEnter(
				getConstants().contactName()), getConstants().contacts(), true,
				true, null) {

			@Override
			protected List<ClientContact> getLists(Context context) {
				return new ArrayList<ClientContact>(
						((ClientVendor) NewCreditCardChargeCommond.this.get(
								VENDOR).getValue()).getContacts());
			}

			@Override
			protected String getContactHolderName() {
				return ((ClientVendor) get(CUSTOMER).getValue())
						.getDisplayName();
			}
		});
		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getConstants().phoneNumber()), getConstants().phone(), true,
				true));

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getConstants().paymentMethod()),
				getConstants().paymentMethod(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getConstants().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getConstants().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPaymentMethods();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().paymentMethod());
			}
		});
		list.add(new AccountRequirement(PAY_FROM, getMessages()
				.pleaseSelectPayFromAccount(getConstants().bankAccount()),
				getConstants().bankAccount(), false, false, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().payFrom());
			}

			@Override
			protected List<ClientAccount> getLists(Context context) {

				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						if (e.getType() == ClientAccount.TYPE_BANK
								|| e.getType() == ClientAccount.TYPE_OTHER_ASSET) {
							return true;
						}
						return false;
					}
				}, getClientCompany().getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Accounts());
			}

			@Override
			protected boolean filter(ClientAccount e, String name) {
				return false;
			}
		});

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getConstants().deliveryDate()), getConstants().deliveryDate(),
				true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnterName(
				getConstants().taxCode()), getConstants().taxCode(), false,
				true, null) {

			@Override
			protected List<ClientTAXCode> getLists(Context context) {
				return context.getClientCompany().getTaxCodes();
			}

			@Override
			protected boolean filter(ClientTAXCode e, String name) {
				return e.getName().toLowerCase().startsWith(name);
			}
		});
	}

	// @Override
	// public Result run(Context context) {
	// setDefaultValues();
	// String process = (String) context.getAttribute(PROCESS_ATTR);
	// Result result = context.makeResult();
	// if (process != null) {
	// if (process.equals(ADDRESS_PROCESS)) {
	// result = addressProcess(context);
	// if (result != null) {
	// return result;
	// }
	// } else if (process.equals(TRANSACTION_ITEM_PROCESS)) {
	// result = transactionItemProcess(context);
	// if (result != null) {
	// return result;
	// }
	// } else if (process.equals(TRANSACTION_ACCOUNT_ITEM_PROCESS)) {
	// result = transactionAccountProcess(context);
	// if (result != null) {
	// return result;
	// }
	// }
	// }
	// Result makeResult = context.makeResult();
	// makeResult.add(getMessages().readyToCreate(
	// getConstants().creditCardCharge()));
	// ResultList list = new ResultList("values");
	// makeResult.add(list);
	// ResultList actions = new ResultList(ACTIONS);
	// setTransactionType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);
	// result = createSupplierRequirement(context, list, SUPPLIER, Global
	// .get().Vendor());
	// if (result != null) {
	// return result;
	// }
	//
	// result = itemsAndAccountsRequirement(context, makeResult, actions,
	// new ListFilter<ClientAccount>() {
	//
	// @Override
	// public boolean filter(ClientAccount account) {
	// if (account.getType() != ClientAccount.TYPE_CASH
	// && account.getType() != ClientAccount.TYPE_BANK
	// && account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
	// && account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
	// && account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
	// && account.getType() != ClientAccount.TYPE_INCOME
	// && account.getType() != ClientAccount.TYPE_OTHER_INCOME
	// && account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
	// && account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
	// && account.getType() != ClientAccount.TYPE_OTHER_ASSET
	// && account.getType() != ClientAccount.TYPE_EQUITY
	// && account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	// }, false);
	// if (result != null) {
	// return result;
	// }
	//
	// result = accountRequirement(context, list, "payFrom", getConstants()
	// .Account(), new ListFilter<ClientAccount>() {
	//
	// @Override
	// public boolean filter(ClientAccount account) {
	// return account.getIsActive()
	// && Arrays.asList(ClientAccount.TYPE_BANK,
	// ClientAccount.TYPE_OTHER_CURRENT_ASSET)
	// .contains(account.getType());
	// }
	// });
	// if (result != null) {
	// return result;
	// }
	// makeResult.add(actions);
	// ClientCompanyPreferences preferences = getClientCompany()
	// .getPreferences();
	// if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
	// result = taxCodeRequirement(context, list);
	// if (result != null) {
	// return result;
	// }
	// }
	//
	// result = createOptionalResult(context, list, actions, makeResult);
	// if (result != null) {
	// return result;
	// }
	// completeProcess(context);
	// markDone();
	// result = new Result();
	// result.add(getMessages().createSuccessfully(
	// getConstants().creditCardCharge()));
	// return result;
	// }

	private void setDefaultValues() {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue("1");
		get(PHONE).setDefaultValue("");
		ClientContact contact = new ClientContact();
		contact.setName(null);
		get(CONTACT).setDefaultValue(contact);
		get(MEMO).setDefaultValue("");
		get("deliveryDate").setDefaultValue(new ClientFinanceDate());
		get(PAYMENT_METHOD).setDefaultValue(getConstants().creditCard());
	}

	private void completeProcess(Context context) {

		ClientCreditCardCharge creditCardCharge = new ClientCreditCardCharge();

		ClientVendor supplier = get(VENDOR).getValue();
		creditCardCharge.setVendor(supplier.getID());

		ClientContact contact = get(CONTACT).getValue();
		creditCardCharge.setContact(contact);

		ClientFinanceDate date = get(DATE).getValue();
		creditCardCharge.setDate(date.getDate());

		creditCardCharge.setType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);

		String number = get(NUMBER).getValue();
		creditCardCharge.setNumber(number);

		String paymentMethod = get(PAYMENT_METHOD).getValue();
		creditCardCharge.setPaymentMethod(paymentMethod);

		String phone = get(PHONE).getValue();
		creditCardCharge.setPhone(phone);

		ClientAccount account = get("payFrom").getValue();
		creditCardCharge.setPayFrom(account.getID());

		ClientFinanceDate deliveryDate = get("deliveryDate").getValue();
		creditCardCharge.setDeliveryDate(deliveryDate);
		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		items.addAll(accounts);
		creditCardCharge.setTransactionItems(items);
		ClientCompanyPreferences preferences = context.getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}
		String memo = get(MEMO).getValue();
		creditCardCharge.setMemo(memo);
		updateTotals(context, creditCardCharge, false);
		create(creditCardCharge, context);
	}

	@Override
	protected Result onCompleteProcess(Context context) {

		ClientCreditCardCharge creditCardCharge = new ClientCreditCardCharge();

		ClientVendor supplier = get(VENDOR).getValue();
		creditCardCharge.setVendor(supplier.getID());

		ClientContact contact = get(CONTACT).getValue();
		creditCardCharge.setContact(contact);

		ClientFinanceDate date = get(DATE).getValue();
		creditCardCharge.setDate(date.getDate());

		creditCardCharge.setType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);

		String number = get(NUMBER).getValue();
		creditCardCharge.setNumber(number);

		String paymentMethod = get(PAYMENT_METHOD).getValue();
		creditCardCharge.setPaymentMethod(paymentMethod);

		String phone = get(PHONE).getValue();
		creditCardCharge.setPhone(phone);

		ClientAccount account = get("payFrom").getValue();
		creditCardCharge.setPayFrom(account.getID());

		ClientFinanceDate deliveryDate = get("deliveryDate").getValue();
		creditCardCharge.setDeliveryDate(deliveryDate);
		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		items.addAll(accounts);
		creditCardCharge.setTransactionItems(items);
		ClientCompanyPreferences preferences = context.getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}
		String memo = get(MEMO).getValue();
		creditCardCharge.setMemo(memo);
		updateTotals(context, creditCardCharge, false);
		create(creditCardCharge, context);

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getConstants().creditCardCharge());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().creditCardCharge());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue("1");
		get(PHONE).setDefaultValue("");
		ClientContact contact = new ClientContact();
		contact.setName(null);
		get(CONTACT).setDefaultValue(contact);
		get(MEMO).setDefaultValue("");
		get(DELIVERY_DATE).setDefaultValue(new ClientFinanceDate());
		get(PAYMENT_METHOD).setDefaultValue(getConstants().creditCard());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(
				getConstants().creditCardCharge());
	}

	// private Result createOptionalResult(Context context, ResultList list,
	// ResultList actions, Result makeResult) {
	//
	// if (context.getAttribute(INPUT_ATTR) == null) {
	// context.setAttribute(INPUT_ATTR, "optional");
	// }
	//
	// Object selection = context.getSelection(ACTIONS);
	// if (selection != null) {
	// ActionNames actionName = (ActionNames) selection;
	// switch (actionName) {
	// case FINISH:
	// context.removeAttribute(INPUT_ATTR);
	// markDone();
	// return null;
	// default:
	// break;
	// }
	// }
	//
	// selection = context.getSelection("values");
	// Result result = dateOptionalRequirement(context, list, DATE,
	// getConstants().date(),
	// getMessages().pleaseEnter(getConstants().date()), selection);
	// if (result != null) {
	// return result;
	// }
	//
	// result = numberOptionalRequirement(
	// context,
	// list,
	// selection,
	// NUMBER,
	// getConstants().creditCard() + getConstants().number(),
	// getMessages().pleaseEnter(
	// getConstants().creditCardCharge()
	// + getConstants().number()));
	// if (result != null) {
	// return result;
	// }
	// Requirement vendorReq = get(SUPPLIER);
	// ClientVendor vendor = vendorReq.getValue();
	// result = contactRequirement(context, list, selection, vendor);
	// if (result != null) {
	// return result;
	// }
	//
	// result = numberOptionalRequirement(context, list, selection, PHONE,
	// getConstants().phoneNumber(),
	// getMessages().pleaseEnter(getConstants().phoneNumber()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = dateOptionalRequirement(context, list, "deliveryDate",
	// getConstants().deliveryDate(),
	// getMessages().pleaseEnter(getConstants().deliveryDate()),
	// selection);
	// if (result != null) {
	// return result;
	// }
	//
	// result = stringOptionalRequirement(context, list, selection, MEMO,
	// getConstants().addMemo(),
	// getMessages().pleaseEnter(getConstants().memo()));
	// if (result != null) {
	// return result;
	// }
	//
	// Record finish = new Record(ActionNames.FINISH);
	// finish.add("",
	// getMessages().finishToCreate(getConstants().creditCardCharge()));
	// actions.add(finish);
	// return makeResult;
	// }
}
