package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaySalesTax;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.company.CompanyMessages;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class VATPaymentView extends
		AbstractTransactionBaseView<ClientPaySalesTax> {
	private AmountField vatBalance, amount, endingBalanceText;
	private TextItem referenceNo, checkNoText, memo, vatAgency;
	private AddressForm addressForm;
	private AccountCombo payFromAccCombo;
	private CheckboxItem toBePrinted;

	private DynamicForm vatAgencyForm;
	private DynamicForm paymentInformationForm;
	private DynamicForm accountInformationForm;
	private DynamicForm paymentMethodForm;
	private ClientAccount selectedPayFromAccount;
	private List<ClientAccount> payFromAccounts;

	private Double initialEndingBalance = 0D;
	private Double endingBalance = 0D;
	private Double totalAmount = 0D;

	private ArrayList<DynamicForm> listforms;

	private CompanyMessages companyConstants = GWT
			.create(CompanyMessages.class);

	public VATPaymentView() {
		super(ClientTransaction.TYPE_PAY_SALES_TAX, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initTransactionViewData() {
		initTransactionNumber();
		getPayFromAccounts();

	}

	private void getPayFromAccounts() {
		payFromAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : FinanceApplication.getCompany()
				.getActiveAccounts()) {
			if (account.getType() == ClientAccount.TYPE_CASH
					|| account.getType() == ClientAccount.TYPE_BANK
					|| account.getType() == ClientAccount.TYPE_CREDIT_CARD
					|| account.getType() == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
					|| account.getType() == ClientAccount.TYPE_LONG_TERM_LIABILITY) {

				payFromAccounts.add(account);
			}
		}
		payFromAccCombo.initCombo(payFromAccounts);
	}

	protected void createControls() {
		setTitle(UIUtils.title(FinanceApplication.getFinanceUIConstants()
				.VATPayment()));
		Label lab = new Label(FinanceApplication.getFinanceUIConstants()
				.VATPayment());
		// lab.setAutoHeight();
		// lab.setWrap(false);

		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		// dateNoForm.setWidth("*");
		// dateNoForm.setLayoutAlign(Alignment.RIGHT);
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);

		vatAgency = new TextItem(companyConstants.vatAgency());
		// vatAgency.setWidth("*");
		vatAgency.setDisabled(true);
		// vatAgency.setShowDisabled(false);

		addressForm = new AddressForm(null);
		vatAgencyForm = new DynamicForm();
		vatAgencyForm = UIUtils.form(companyConstants.vatAgency());
		vatAgencyForm.setWidth("100%");
		// vatAgencyForm.setAutoHeight();
		vatAgencyForm.setFields(vatAgency);

		amount = new AmountField(companyConstants.amount());
		// amount.setWidth("*");
		amount.setRequired(true);
		amount.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		// amount.setTextAlign(Alignment.RIGHT);

		vatBalance = new AmountField(companyConstants.vatBalance());
		// vatBalance.setWidth("*");
		// vatBalance.setTextAlign(Alignment.RIGHT);
		vatBalance.setDisabled(true);
		// vatBalance.setShowDisabled(false);
		memo = new TextItem(companyConstants.memo());
		// memo.setWidth("*");
		// memo.setTextAlign(Alignment.LEFT);

		referenceNo = new TextItem(companyConstants.referenceNo());
		// referenceNo.setWidth("*");
		// referenceNo.setTextAlign(Alignment.RIGHT);

		paymentInformationForm = new DynamicForm();
		paymentInformationForm.setIsGroup(true);
		paymentInformationForm.setWidth("100%");
		// paymentInformationForm.setAutoHeight();
		paymentInformationForm.setGroupTitle(companyConstants
				.paymentInformation());
		paymentInformationForm.setFields(vatBalance, amount, memo, referenceNo);

		endingBalanceText = new AmountField(companyConstants.endingBalance());
		// endingBalanceText.setWidth("*");
		endingBalanceText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		// endingBalanceText.setAlign(Alignment.RIGHT);
		// endingBalanceText.setTextAlign(Alignment.RIGHT);
		endingBalanceText.setDisabled(true);
		// endingBalanceText.setShowDisabled(false);

		payFromAccCombo = new PayFromAccountsCombo(companyConstants.payFrom());
		payFromAccCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		payFromAccCombo.setRequired(true);
		// payFromAccCombo.setWidth("*");

		payFromAccCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedPayFromAccount = selectItem;
						initialEndingBalance = !DecimalUtil.isEquals(
								selectedPayFromAccount.getTotalBalance(), 0) ? selectedPayFromAccount
								.getTotalBalance()
								: 0D;
						calculateEndingBalance();

						endingBalanceText.setValue(DataUtils
								.getAmountAsString(endingBalance));

					}

				});
		accountInformationForm = new DynamicForm();
		accountInformationForm.setIsGroup(true);
		accountInformationForm.setWidth("100%");
		// accountInformationForm.setAutoHeight();
		accountInformationForm.setGroupTitle(companyConstants
				.accountInformation());
		accountInformationForm.setFields(payFromAccCombo, endingBalanceText);

		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setRequired(true);
		// paymentMethodCombo.setWidth("*");
		paymentMethodCombo.setDefaultValue(FinanceApplication
				.getFinanceUIConstants().check());

		toBePrinted = new CheckboxItem(FinanceApplication
				.getFinanceUIConstants().toBePrinted());
		// toBePrinted.setDefaultValue(true);
		toBePrinted.addChangedHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {

				boolean isChecked = (Boolean) toBePrinted.getValue();

				if (isChecked)
					// checkNoText.enable();
					checkNoText.setDisabled(false);
				else
					// checkNoText.disable();
					checkNoText.setDisabled(true);
			}

		});
		// printCheck.setShowDisabled(false);

		checkNoText = new TextItem(FinanceApplication.getFinanceUIConstants()
				.checkNo());
		// checkNoText.setWidth("*");

		paymentMethodForm = new DynamicForm();
		paymentMethodForm.setIsGroup(true);
		paymentMethodForm.setWidth("100%");
		paymentMethodForm.setGroupTitle(companyConstants.paymentMethod());
		paymentMethodForm.setFields(paymentMethodCombo, toBePrinted,
				checkNoText);

		VerticalPanel lLayout = new VerticalPanel();
		lLayout.setWidth("100%");
		// lLayout.setAutoHeight();
		lLayout.add(vatAgencyForm);
		lLayout.add(addressForm);
		lLayout.add(paymentInformationForm);
		VerticalPanel rLayout = new VerticalPanel();
		rLayout.setWidth("100%");
		// rLayout.setAutoHeight();
		rLayout.add(accountInformationForm);
		rLayout.add(paymentMethodForm);

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setWidth("100%");
		hPanel.add(lLayout);
		hPanel.add(rLayout);
		// HorizontalPanel.setMembersMargin(20);

		VerticalPanel mainLayout = new VerticalPanel();
		mainLayout.setWidth("100%");
		// mainLayout.setMembersMargin(10);
		// mainLayout.setMargin(10);
		mainLayout.add(lab);
		mainLayout.add(dateNoForm);
		mainLayout.add(hPanel);
		canvas.add(mainLayout);

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vatAgencyForm);
		listforms.add(paymentInformationForm);
		listforms.add(accountInformationForm);
		listforms.add(paymentMethodForm);

	}

	private void calculateEndingBalance() {

		if (selectedPayFromAccount != null) {

			if (selectedPayFromAccount.isIncrease())
				endingBalance = initialEndingBalance + totalAmount;
			else
				endingBalance = initialEndingBalance - totalAmount;

		}
	}

	@Override
	public void saveAndUpdateView() throws Exception {

		ClientPaySalesTax paySalesTax = getPaySalesTax();
		createObject(paySalesTax);
	}

	private ClientPaySalesTax getPaySalesTax() {

		ClientPaySalesTax paySalesTax = new ClientPaySalesTax();

		paySalesTax.setType(ClientTransaction.TYPE_PAY_SALES_TAX);

		paySalesTax.setNumber(transactionNumber.getValue().toString());

		paySalesTax
				.setDate(((ClientFinanceDate) transactionDateItem.getValue())
						.getTime());

		paySalesTax.setTotal(amount.getAmount());

		paySalesTax.setMemo(memo.getValue().toString());

		paySalesTax.setReference(referenceNo.getValue().toString());

		paySalesTax.setPayFrom(selectedPayFromAccount.getStringID());

		paySalesTax.setEndingBalance(endingBalance);

		paySalesTax.setPaymentMethod(paymentMethod);

		return paySalesTax;

	}

	@Override
	protected void paymentMethodSelected(String paymentmethod) {
		super.paymentMethodSelected(paymentmethod);
		if (paymentmethod.equalsIgnoreCase(FinanceApplication
				.getFinanceUIConstants().check())) {
			toBePrinted.setDisabled(false);
			toBePrinted.setValue(true);
			checkNoText.setDisabled(false);
		} else {
			toBePrinted.setValue(false);
			toBePrinted.setDisabled(true);
			checkNoText.setDisabled(true);
		}
	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNonEditableItems() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException {

		if (!accountInformationForm.validate())
			throw new InvalidTransactionEntryException(FinanceApplication
					.getFinanceUIConstants().boldFieldsShouldFill());
		if (!paymentMethodForm.validate())
			throw new InvalidTransactionEntryException(FinanceApplication
					.getFinanceUIConstants().blankTransIsNotAllowed());

		return true;
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
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
		// TODO Auto-generated method stub
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.payFromAccCombo.addComboItem((ClientAccount) core);

			break;
		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.payFromAccCombo.removeComboItem((ClientAccount) core);
			break;
		case AccounterCommand.UPDATION_SUCCESS:
			break;
		}
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

}
