package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;

public class CustomerCreditMemoView extends
		AbstractCustomerTransactionView<ClientCustomerCreditMemo> {

	private ArrayList<DynamicForm> listforms;
	private TextAreaItem billToTextArea;

	public CustomerCreditMemoView() {
		super(ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO,
				CUSTOMER_TRANSACTION_GRID);
	}

	@Override
	protected void initTransactionViewData() {

		super.initTransactionViewData();

	}

	@Override
	protected void createControls() {
		setTitle(customerConstants.customerCreditNote());
		Label lab1 = new Label(customerConstants.customerCreditNote());
		lab1.setStyleName(FinanceApplication.getCustomersMessages()
				.lableTitle());

		listforms = new ArrayList<DynamicForm>();

		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						if (date != null) {
							try {
								ClientFinanceDate newDate = transactionDateItem
										.getValue();
								if (newDate != null)
									setTransactionDate(newDate);
							} catch (Exception e) {
								Accounter
										.showError("Invalid Transaction date!");
								setTransactionDate(new ClientFinanceDate());
								transactionDateItem
										.setEnteredDate(getTransactionDate());
							}

						}
						updateNonEditableItems();
					}
				});

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(FinanceApplication.getCustomersMessages()
				.creditNo());

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(customerConstants
				.customerName());

		contactCombo = createContactComboItem();
		// billToCombo = createBillToComboItem();
		billToTextArea = new TextAreaItem();
		billToTextArea.setWidth(100);
		billToTextArea.setTitle(FinanceApplication.getCustomersMessages()
				.creditTo());
		billToTextArea.setDisabled(true);

		custForm = UIUtils.form(customerConstants.customer());
		custForm.setFields(customerCombo, contactCombo, billToTextArea);
		custForm.setWidth("100%");
		custForm.setStyleName("align-form");
		forms.add(custForm);

		phoneSelect = new SelectItem();
		phoneSelect.setTitle(customerConstants.phone());
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(isEdit);
		phoneSelect.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				phoneNo = phoneSelect.getValue().toString();
			}
		});
		formItems.add(phoneSelect);
		salesPersonCombo = createSalesPersonComboItem();

		DynamicForm phoneForm = UIUtils.form(customerConstants.phoneNumber());
		phoneForm.setWidth("100%");
		// phoneForm.setFields(phoneSelect, salesPersonCombo);
		// phoneForm.setFields(salesPersonCombo);
		phoneForm.setStyleName("align-form");
		forms.add(phoneForm);

		@SuppressWarnings("unused")
		Label lab2 = new Label(customerConstants.productAndService());

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setTitle(FinanceApplication.getCustomersMessages()
				.reasonForIssue());

		taxCodeSelect = createTaxCodeSelectItem();

		priceLevelSelect = createPriceLevelSelectItem();

		refText = createRefereceText();

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		prodAndServiceForm1.setNumCols(2);
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setFields(memoTextAreaItem, refText);
		forms.add(prodAndServiceForm1);

		salesTaxTextNonEditable = createSalesTaxNonEditableItem();

		vatTotalNonEditableText = createVATTotalNonEditableItem();

		transactionTotalNonEditableText = createTransactionTotalNonEditableItem();
		netAmount = createNetAmountField();
		vatinclusiveCheck = getVATInclusiveCheckBox();

		customerTransactionGrid = getGrid();
		customerTransactionGrid.setTransactionView(this);
		customerTransactionGrid.init();
		customerTransactionGrid.setCanEdit(true);
		customerTransactionGrid.setDisabled(isEdit);
		customerTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);

		final TextItem disabletextbox = new TextItem();
		disabletextbox.setVisible(false);

		DynamicForm prodAndServiceForm2 = new DynamicForm();
		prodAndServiceForm2.setWidth("70%");
		prodAndServiceForm2.setNumCols(4);
		if (FinanceApplication.getCompany().getAccountingType() == 1) {

			prodAndServiceForm2.setFields(priceLevelSelect, netAmount,
					disabletextbox, vatTotalNonEditableText, disabletextbox,
					transactionTotalNonEditableText);
		} else {
			prodAndServiceForm2.setFields(taxCodeSelect,
					salesTaxTextNonEditable, priceLevelSelect,
					transactionTotalNonEditableText);
		}
		forms.add(prodAndServiceForm2);

		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");
		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(prodAndServiceForm2);
		prodAndServiceHLay.setCellHorizontalAlignment(prodAndServiceForm2,
				ALIGN_RIGHT);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(custForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");
		rightVLay.add(phoneForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setSize("100%", "100%");
		topHLay.setSpacing(20);

		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "50%");
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");

		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		mainVLay.add(createAddNewButton());
		mainVLay.add(customerTransactionGrid);
		mainVLay.add(prodAndServiceHLay);

		if (UIUtils.isMSIEBrowser()) {
			resetFormView();
		} else {
			memoTextAreaItem.setWidth(130);
			refText.setWidth(130);
		}

		canvas.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(phoneForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(prodAndServiceForm2);

	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {

		this.priceLevel = priceLevel;
		if (priceLevel != null && priceLevelSelect != null) {

			priceLevelSelect.setComboItem(FinanceApplication.getCompany()
					.getPriceLevel(priceLevel.getStringID()));

		}

		if (transactionObject == null && customerTransactionGrid != null) {
			customerTransactionGrid.priceLevelSelected(priceLevel);
			customerTransactionGrid.updatePriceLevel();
		}
		updateNonEditableItems();

	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson person) {
		this.salesPerson = person;
		if (salesPerson != null && salesPersonCombo != null) {

			salesPersonCombo.setComboItem(FinanceApplication.getCompany()
					.getSalesPerson(salesPerson.getStringID()));

		}

	}

	@Override
	public void saveAndUpdateView() throws Exception {
		try {
			transactionObject = getCreditMemoObject();
			super.saveAndUpdateView();
			if (transactionObject.getStringID() == null)
				createObject((ClientCustomerCreditMemo) transactionObject);
			else
				alterObject((ClientCustomerCreditMemo) transactionObject);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private ClientTransaction getCreditMemoObject() {
		try {

			ClientCustomerCreditMemo creditMemo = transactionObject != null ? (ClientCustomerCreditMemo) transactionObject
					: new ClientCustomerCreditMemo();

			creditMemo.setCustomer(customer.getStringID());
			if (contact != null)
				creditMemo.setContact(contact);
			if (salesPerson != null)
				creditMemo.setSalesPerson(salesPerson.getStringID());
			if (phoneNo != null)
				creditMemo.setPhone(phoneNo);
			if (billingAddress != null)
				creditMemo.setBillingAddress(billingAddress);
			if (priceLevel != null)
				creditMemo.setPriceLevel(priceLevel.getStringID());
			creditMemo.setMemo(getMemoTextAreaItem());
			creditMemo.setReference(getRefText());

			if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				creditMemo.setNetAmount(netAmount.getAmount());
				creditMemo.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
						.getValue());
			} else
				creditMemo.setSalesTax(this.salesTax);

			creditMemo.setTotal(transactionTotalNonEditableText.getAmount());
			transactionObject = creditMemo;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionObject;
	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		initTransactionViewData();
		ClientCustomerCreditMemo creditToBeEdited = (ClientCustomerCreditMemo) transactionObject;

		this.customer = FinanceApplication.getCompany().getCustomer(
				creditToBeEdited.getCustomer());
		this.transactionObject = creditToBeEdited;
		this.billingAddress = creditToBeEdited.getBillingAddress();
		this.contact = creditToBeEdited.getContact();
		this.phoneNo = creditToBeEdited.getPhone();
		phoneSelect.setValue(this.phoneNo);
		this.salesPerson = FinanceApplication.getCompany().getSalesPerson(
				creditToBeEdited.getSalesPerson());
		this.priceLevel = FinanceApplication.getCompany().getPriceLevel(
				creditToBeEdited.getPriceLevel());
		this.transactionItems = creditToBeEdited.getTransactionItems();

		initTransactionNumber();
		if (customer != null)
			customerCombo.setComboItem(customer);
		// billToaddressSelected(this.billingAddress);
		contactSelected(this.contact);
		priceLevelSelected(this.priceLevel);
		salesPersonSelected(this.salesPerson);
		memoTextAreaItem.setValue(creditToBeEdited.getMemo());
		refText.setValue(creditToBeEdited.getReference());
		if (billingAddress != null) {
			billToTextArea.setValue(getValidAddress(billingAddress));

		} else
			billToTextArea.setValue("");

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			netAmount.setAmount(creditToBeEdited.getNetAmount());
			vatTotalNonEditableText.setAmount(creditToBeEdited.getTotal()
					- creditToBeEdited.getNetAmount());
		} else {
			this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
			if (taxCode != null) {
				this.taxCodeSelect
						.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
			}
			salesTaxTextNonEditable.setAmount(creditToBeEdited.getSalesTax());
		}
		transactionTotalNonEditableText.setAmount(creditToBeEdited.getTotal());
		customerTransactionGrid.setCanEdit(false);
	}

	@Override
	protected void initMemoAndReference() {
		if (this.transactionObject != null) {

			ClientCustomerCreditMemo creditMemo = (ClientCustomerCreditMemo) transactionObject;

			if (creditMemo != null) {

				memoTextAreaItem.setValue(creditMemo.getMemo());
				if (creditMemo.getReference() != null)
					refText.setValue(creditMemo.getReference());

			}

		}

	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		if (transactionObject != null) {
			Double salesTaxAmout = ((ClientCustomerCreditMemo) transactionObject)
					.getSalesTax();
			if (salesTaxAmout != null) {
				salesTaxTextNonEditable.setAmount(salesTaxAmout);
			}

		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transactionObject != null) {
			Double transactionTotal = ((ClientCustomerCreditMemo) transactionObject)
					.getTotal();
			if (transactionTotal != null) {
				transactionTotalNonEditableText.setAmount(transactionTotal);
			}

		}

	}

	@Override
	public void setTransactionDate(ClientFinanceDate transactionDate) {
		super.setTransactionDate(transactionDate);
		if (this.transactionDateItem != null
				&& this.transactionDateItem.getValue() != null)
			updateNonEditableItems();
	}

	@Override
	public void updateNonEditableItems() {
		if (customerTransactionGrid == null)
			return;
		if (FinanceApplication.getCompany().getAccountingType() == 0) {
			Double taxableLineTotal = customerTransactionGrid
					.getTaxableLineTotal();

			if (taxableLineTotal == null)
				return;
			Double salesTax = taxCode != null ? Utility.getCalculatedSalesTax(
					transactionDateItem.getEnteredDate(), taxableLineTotal,
					FinanceApplication.getCompany().getTAXItemGroup(
							taxCode.getTAXItemGrpForSales())) : 0;

			setSalesTax(salesTax);

			setTransactionTotal(customerTransactionGrid.getTotal()
					+ this.salesTax);

			// salesTax = Utility.getCalculatedSalesTax(transactionDateItem
			// .getEnteredDate(), taxableLineTotal, taxItemGroup);
			// setSalesTax(salesTax);
			//
			// this.salesTaxTextNonEditable.setAmount(salesTax != null ?
			// salesTax
			// : 0.0D);
			//
			// this.transactionTotalNonEditableText
			// .setAmount(customerTransactionGrid.getTotal()
			// + this.salesTax);
		} else {
			if (customerTransactionGrid.getGrandTotal() != null
					&& customerTransactionGrid.getTotalValue() != null) {
				netAmount.setAmount(customerTransactionGrid.getGrandTotal());
				vatTotalNonEditableText.setAmount(customerTransactionGrid
						.getTotalValue()
						- customerTransactionGrid.getGrandTotal());
				setTransactionTotal(customerTransactionGrid.getTotalValue());
			}
		}

		// TODO this.paymentsNonEditableText.setValue(transactionGrid.);

		// this.balanceDueNonEditableText.setValue(""+UIUtils.getCurrencySymbol()
		// +"0.00");

	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {
		return super.validate();

	}

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (this.customer != null && this.customer != customer) {
			ClientCustomerCreditMemo ent = (ClientCustomerCreditMemo) this.transactionObject;

			if (ent != null && ent.getCustomer().equals(customer.getStringID())) {
				this.customerTransactionGrid.removeAllRecords();
				this.customerTransactionGrid.setRecords(ent
						.getTransactionItems());
			} else if (ent != null
					&& !ent.getCustomer().equals(customer.getStringID())) {
				this.customerTransactionGrid.removeAllRecords();
				this.customerTransactionGrid.updateTotals();
			} else if (ent == null)
				this.customerTransactionGrid.removeAllRecords();
		}
		super.customerSelected(customer);
		this.customer = customer;
		if (customer != null) {
			customerCombo.setComboItem(customer);
		}
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			super.setCustomerTaxCodetoAccount();
		this.addressListOfCustomer = customer.getAddress();
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {
			billToTextArea.setValue(getValidAddress(billingAddress));

		} else
			billToTextArea.setValue("");
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.customerCombo.setFocus();
	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.addComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				this.salesPersonCombo.addComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.addComboItem((ClientPriceLevel) core);
			break;

		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.updateComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				this.salesPersonCombo.updateComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.updateComboItem((ClientPriceLevel) core);
			break;

		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.removeComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				this.salesPersonCombo.removeComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.removeComboItem((ClientPriceLevel) core);

			break;
		}

	}

	public AbstractTransactionGrid<ClientTransactionItem> getGridForPrinting() {
		return customerTransactionGrid;
	}

	public void onEdit() {
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				Accounter.showError(((InvalidOperationException) (caught))
						.getDetailedMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.stringID,
				editCallBack);

	}

	protected void enableFormItems() {
		isEdit = false;
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		customerCombo.setDisabled(isEdit);
		salesPersonCombo.setDisabled(isEdit);
		priceLevelSelect.setDisabled(isEdit);
		taxCodeSelect.setDisabled(isEdit);
		customerTransactionGrid.setDisabled(isEdit);
		customerTransactionGrid.setCanEdit(true);
		super.onEdit();

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {

		UIUtils.downloadAttachment(
				((ClientCustomerCreditMemo) getCreditMemoObject())
						.getStringID(),
				ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);

	}

	public void resetFormView() {
		custForm.getCellFormatter().setWidth(0, 1, "200px");
		custForm.setWidth("75%");
		refText.setWidth("200px");
		priceLevelSelect.setWidth("150px");
	}

	private String getValidAddress(ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion();
		}
		return toToSet;
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {

			taxCodeSelect.setComboItem(FinanceApplication.getCompany()
					.getTAXCode(taxCode.getStringID()));
			customerTransactionGrid.setTaxCode(taxCode.getStringID());
		} else
			taxCodeSelect.setValue("");
		// updateNonEditableItems();

	}
}
