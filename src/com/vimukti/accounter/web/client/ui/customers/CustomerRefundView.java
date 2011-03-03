package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CustomerRefundView extends
		AbstractCustomerTransactionView<ClientCustomerRefund> {

	protected PayFromAccountsCombo payFromSelect;
	protected ClientAccount selectedAccount;
	protected AmountField amtText;
	private AmountField endBalText, custBalText;
	private TextItem checkNoText;
	private CheckboxItem printCheck;
	@SuppressWarnings("unused")
	private Double refundAmount;
	private Double endingBalance;
	private boolean isChecked = false;
	private Double customerBalanceAmount;
	private List<ClientAccount> payFromAccounts;
	private String checkNumber;
	private ArrayList<DynamicForm> listforms;
	protected DynamicForm payForm;
	private ClientCustomerRefund refund;

	public CustomerRefundView() {
		super(ClientTransaction.TYPE_CUSTOMER_REFUNDS,
				CUSTOMER_TRANSACTION_GRID);
	}

	@Override
	protected void initTransactionViewData() {

		initRPCService();

		initTransactionNumber();

		initCustomers();

		initPayFromAccounts();

	}

	private void initPayFromAccounts() {
		// payFromSelect.initCombo(payFromAccounts);
		// getPayFromAccounts();
		payFromSelect.setAccounts();

		if (transactionObject != null) {
			payFromSelect.setComboItem(FinanceApplication.getCompany()
					.getAccount(
							((ClientCustomerRefund) transactionObject)
									.getPayFrom()));
		}

		selectedAccount = payFromSelect.getSelectedValue();

	}

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (customer == null)
			return;
		this.customer = customer;
		if (customer != null && customerCombo != null) {
			customerCombo.setComboItem(FinanceApplication.getCompany()
					.getCustomer(customer.getStringID()));
		}
		addressListOfCustomer = customer.getAddress();
		super.initBillToCombo();
		setCustomerBalance(customer.getBalance());
		paymentMethodSelected(customer.getPaymentMethod());

	}

	@Override
	protected void paymentMethodSelected(String paymentMethod2) {
		super.paymentMethodSelected(paymentMethod2);
		paymentMethodCombo.setValue(paymentMethod2);

		if (paymentMethod2 == null)
			return;

		if (paymentMethod2.equals(UIUtils
				.getpaymentMethodCheckBy_CompanyType(FinanceApplication
						.getCustomersMessages().check()))) {
			printCheck.setDisabled(false);
			checkNoText.setDisabled(false);

		} else {
			printCheck.setDisabled(true);
			checkNoText.setDisabled(true);
		}
		printCheck.setValue(false);
		checkNoText.setValue("");
		isChecked = false;
	}

	@Override
	protected void createControls() {

		// Label lab1 = new Label(Utility.getTransactionName(transactionType)
		// + "(" + getTransactionStatus() + ")");
		Label lab1 = new Label(Utility.getTransactionName(transactionType));
		lab1.setStyleName(FinanceApplication.getCustomersMessages()
				.lableTitle());
		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(dateNoForm);
		labeldateNoLayout.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel totalLabel = new HorizontalPanel();
		totalLabel.setWidth("100%");
		totalLabel.add(lab1);
		totalLabel.add(labeldateNoLayout);
		totalLabel.setCellHorizontalAlignment(labeldateNoLayout, ALIGN_RIGHT);

		customerCombo = createCustomerComboItem(customerConstants.payTo());
		customerCombo.setWidth(100);
		billToCombo = createBillToComboItem();
		billToCombo.setTitle(customerConstants.address());

		custForm = new DynamicForm();

		custForm.setWidth("100%");
		// custForm.setFields(customerCombo, billToCombo);
		custForm.setWidth("100%");

		// custForm.setFields(customerCombo, billToCombo);
		// custForm.setWidth("100%");
		// custForm.getCellFormatter().setWidth(0, 0, "150");

		payFromSelect = new PayFromAccountsCombo(customerConstants.payFrom());
		payFromSelect.setHelpInformation(true);
		payFromSelect.setRequired(true);
		payFromSelect.setDisabled(isEdit);
		payFromSelect.setWidth("100%");
		payFromSelect.setPopupWidth("500px");
		payFromSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {

						selectedAccount = selectItem;

						setEndingBalance(selectedAccount.getTotalBalance());

					}

				});

		amtText = new AmountField(customerConstants.amount());
		amtText.setHelpInformation(true);
		amtText.setRequired(true);
		amtText.setWidth(100);
		amtText.setDisabled(isEdit);
		amtText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				try {

					amtText.setAmount(DataUtils.getAmountStringAsDouble(amtText
							.getValue().toString()));
					Double givenAmount = amtText.getAmount();
					if (DecimalUtil.isLessThan(givenAmount, 0)) {
						Accounter.showError(FinanceApplication
								.getCustomersMessages().noNegativeAmounts());
						setRefundAmount(0.00D);

					}

					else if (!DecimalUtil.isLessThan(givenAmount, 0)) {
						if (!AccounterValidator.isAmountTooLarge(givenAmount))
							refundAmountChanged(givenAmount);
						setRefundAmount(givenAmount);

					}

				} catch (Exception e) {
					if (e instanceof InvalidEntryException) {
						Accounter.showError(e.getMessage());
					}
					setRefundAmount(0.00D);
				}
			}
		});

		setRefundAmount(null);

		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setWidth(100);

		checkNoText = new TextItem(
				FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? customerConstants
						.chequeNo()
						: customerConstants.checkNo());
		checkNoText.setHelpInformation(true);
		checkNoText.setWidth(100);
		checkNoText.setDisabled(true);

		printCheck = new CheckboxItem(customerConstants.toBePrinted());
		printCheck.setDisabled(true);
		printCheck.setWidth(100);
		printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isChecked = (Boolean) event.getValue();

				if (isChecked) {
					checkNoText.setValue(FinanceApplication
							.getCustomersMessages().toBePrinted());
					checkNoText.setDisabled(true);
				} else {
					checkNoText.setValue("");
					checkNoText.setDisabled(false);
				}
			}
		});

		memoTextAreaItem = createMemoTextAreaItem();

		// refText = createRefereceText();
		// refText.setWidth(100);

		// payForm = new DynamicForm();
		// payForm.setWidth("100%");
		// payForm.setFields(payFromSelect, amtText, paymentMethodCombo,
		// printCheck, checkNoText);
		// forms.add(payForm);

		endBalText = new AmountField(customerConstants.endingBalance());
		endBalText.setHelpInformation(true);
		endBalText.setDisabled(true);
		setEndingBalance(null);

		custBalText = new AmountField(customerConstants.customerBalance());
		custBalText.setHelpInformation(true);
		custBalText.setDisabled(true);
		setCustomerBalance(null);

		// DynamicForm memoForm = new DynamicForm();
		// memoForm.setWidth("100%");
		// memoForm.setFields(memoTextAreaItem, refText);
		// memoForm.getCellFormatter().setWidth(0, 0, "150");
		custForm.setFields(customerCombo, billToCombo, payFromSelect, amtText,
				paymentMethodCombo, printCheck, checkNoText, memoTextAreaItem);
		custForm.setCellSpacing(5);
		custForm.setWidth("100%");

		DynamicForm balForm = new DynamicForm();
		balForm.setFields(endBalText, custBalText);
		forms.add(balForm);

		VerticalPanel leftPanel = new VerticalPanel();
		leftPanel.setWidth("100%");
		leftPanel.setSpacing(5);
		leftPanel.add(custForm);
		// leftPanel.add(payForm);
		// leftPanel.add(memoForm);

		VerticalPanel rightPanel = new VerticalPanel();
		rightPanel.setWidth("100%");
		rightPanel.add(balForm);
		rightPanel.setCellHorizontalAlignment(balForm,
				HasHorizontalAlignment.ALIGN_CENTER);

		HorizontalPanel hLay = new HorizontalPanel();
		hLay.setWidth("100%");
		hLay.setSpacing(10);
		hLay.add(leftPanel);
		hLay.setCellHorizontalAlignment(totalLabel, ALIGN_CENTER);
		hLay.add(rightPanel);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setWidth("100%");
		mainVLay.add(totalLabel);
		mainVLay.add(hLay);

		if (UIUtils.isMSIEBrowser()) {
			custForm.getCellFormatter().setWidth(0, 1, "300px");
			custForm.setWidth("75%");
		}
		canvas.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(payForm);
		listforms.add(balForm);

	}

	@Override
	public void saveAndUpdateView() throws Exception {

		try {
			if (transactionObject == null)
				refund = new ClientCustomerRefund();
			else
				refund = (ClientCustomerRefund) transactionObject;

			refund.setDate(transactionDateItem.getEnteredDate().getTime());

			refund.setNumber(transactionNumber.getValue().toString());

			refund.setPayTo(customer.getStringID());

			if (billingAddress != null)
				refund.setAddress(billingAddress);

			refund.setEndingBalance(endingBalance);

			refund.setCustomerBalance(customerBalanceAmount);
			if (selectedAccount != null)
				refund.setPayFrom(selectedAccount.getStringID());

			refund.setPaymentMethod(paymentMethod);
			if (checkNoText.getValue() != null
					&& !checkNoText.getValue().equals("")) {
				refund.setCheckNumber(getCheckValue());
			} else
				refund.setCheckNumber("");

			refund.setIsToBePrinted(isChecked);
			refund.setMemo(memoTextAreaItem.getValue().toString());

			// refund.setReference(refText.getValue().toString());

			refund.setType(ClientTransaction.TYPE_CUSTOMER_REFUNDS);

			refund.setTotal(amtText.getAmount());

			refund.setBalanceDue(amtText.getAmount());

			transactionObject = refund;

			super.saveAndUpdateView();

			if (transactionObject.getStringID() == null)
				createObject((ClientCustomerRefund) transactionObject);
			else
				alterObject((ClientCustomerRefund) transactionObject);

		} catch (Exception e) {
			Accounter.showError(FinanceApplication.getCustomersMessages()
					.error()
					+ e.getMessage());
			throw e;
		}

	}

	private String getCheckValue() {
		String value;
		if (!isEdit) {
			if (checkNoText.getValue().equals(
					FinanceApplication.getCustomersMessages().toBePrinted())) {
				value = String.valueOf(FinanceApplication.getVendorsMessages()
						.Tobeprinted());

			} else
				value = String.valueOf(checkNoText.getValue());
		} else {
			String checknumber;
			checknumber = this.checkNumber;
			if (checknumber.equals(FinanceApplication.getVendorsMessages()
					.Tobeprinted()))
				value = FinanceApplication.getCustomersMessages().toBePrinted();
			else
				value = String.valueOf(checknumber);
		}
		return value;
	}

	protected void refundAmountChanged(Double givenAmount) {
		if (selectedAccount != null) {
			endingBalance = selectedAccount.getTotalBalance();
			endingBalance -= givenAmount;
			setEndingBalance(endingBalance);
		}
		if (customer != null) {
			customerBalanceAmount = customer.getBalance();
			customerBalanceAmount += givenAmount;
			setCustomerBalance(customerBalanceAmount);
		}

	}

	protected void setRefundAmount(Double amountValue) {
		if (amountValue == null)
			amountValue = 0.00D;
		amtText.setAmount(amountValue);
		// this.refundAmount = amountValue;

	}

	protected void setEndingBalance(Double totalBalance) {

		if (totalBalance == null)
			totalBalance = 0.0D;

		// if (refundAmount != null)
		// totalBalance -= refundAmount;

		endBalText.setAmount(totalBalance);

		this.endingBalance = totalBalance;

	}

	@Override
	protected void initMemoAndReference() {
		if (transactionObject == null)
			return;

		String memo = ((ClientCustomerRefund) transactionObject).getMemo();

		if (memo != null) {
			memoTextAreaItem.setValue(memo);
		}

		// String refString = ((ClientCustomerRefund) transactionObject)
		// .getReference();
		//
		// if (refString != null) {
		// refText.setValue(refString);
		// }

	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {

		if (transactionObject == null)
			return;

		ClientCustomerRefund customerRefund = ((ClientCustomerRefund) transactionObject);

		setCustomerBalance(customerRefund.getCustomerBalance());
		setEndingBalance(customerRefund.getEndingBalance());

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		ClientCustomerRefund customerRefundTobeEdited = (ClientCustomerRefund) transactionObject;

		this.customer = FinanceApplication.getCompany().getCustomer(
				customerRefundTobeEdited.getPayTo());
		customerSelected(FinanceApplication.getCompany().getCustomer(
				customerRefundTobeEdited.getPayTo()));

		amtText.setAmount(customerRefundTobeEdited.getTotal());
		this.checkNumber = customerRefundTobeEdited.getCheckNumber();
		if (this.checkNumber != null)
			checkNoText.setValue(getCheckValue());
		printCheck.setValue(customerRefundTobeEdited.getIsToBePrinted());

		this.selectedAccount = FinanceApplication.getCompany().getAccount(
				customerRefundTobeEdited.getPayFrom());
		if (selectedAccount != null)
			payFromSelect.setComboItem(selectedAccount);
		paymentMethodCombo
				.setValue(customerRefundTobeEdited.getPaymentMethod());
		this.billingAddress = customerRefundTobeEdited.getAddress();
		if (billingAddress != null)
			billToaddressSelected(billingAddress);

		endBalText
				.setValue(DataUtils.getAmountAsString(customerRefundTobeEdited
						.getEndingBalance()));
		custBalText.setValue(DataUtils
				.getAmountAsString(customerRefundTobeEdited
						.getCustomerBalance()));
		memoTextAreaItem.setValue(customerRefundTobeEdited.getMemo());
		// refText.setValue(customerRefundTobeEdited.getReference());
		initTransactionNumber();
	}

	public void setCustomerBalance(Double amount) {
		if (amount == null)
			amount = 0.0D;
		//
		// if (refundAmount != null)
		// amount += refundAmount;

		custBalText.setAmount(amount);

		this.customerBalanceAmount = amount;
	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {

		if (custForm != null)
			AccounterValidator.validateForm(custForm);
		if (DecimalUtil.isEquals(amtText.getAmount(), 0))
			throw new InvalidTransactionEntryException(
					AccounterErrorType.INVALID_NEGATIVE_AMOUNT);
		return super.validate();
	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson person) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.addComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.payFromSelect.addComboItem((ClientAccount) core);

			break;

		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.updateComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.payFromSelect.updateComboItem((ClientAccount) core);

			break;

		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.removeComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.payFromSelect.removeComboItem((ClientAccount) core);

			break;
		}

	}

	@Override
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

		payFromSelect.setDisabled(isEdit);
		amtText.setDisabled(isEdit);

		paymentMethodCombo.setDisabled(isEdit);
		paymentMethodSelected(paymentMethodCombo.getValue().toString());
		checkNoText.setValue(((ClientCustomerRefund) transactionObject)
				.getCheckNumber());
		printCheck.setValue(((ClientCustomerRefund) transactionObject)
				.getIsToBePrinted());
		if (((ClientCustomerRefund) transactionObject).getIsToBePrinted()) {
			checkNoText.setDisabled(true);
		}
		if (((ClientCustomerRefund) transactionObject).getIsToBePrinted()) {
			checkNoText.setDisabled(true);
		}
		if (((ClientCustomerRefund) transactionObject).getIsToBePrinted()) {
			checkNoText.setDisabled(true);
		}
		super.onEdit();
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		// TODO Auto-generated method stub

	}

}
