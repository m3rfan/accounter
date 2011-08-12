package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author vimukti1
 * 
 */
public class CashPurchaseView extends
		AbstractVendorTransactionView<ClientCashPurchase> {

	protected DynamicForm vendorForm;
	protected DynamicForm termsForm;
	public List<String> selectedComboList;
	private ArrayList<DynamicForm> listforms;
	protected Label titlelabel;
	private TextAreaItem billToAreaItem;
	com.vimukti.accounter.web.client.externalization.AccounterConstants accounterConstants = Accounter
			.constants();

	protected CashPurchaseView() {
		super(ClientTransaction.TYPE_CASH_PURCHASE, VENDOR_TRANSACTION_GRID);
	}

	protected CashPurchaseView(int type) {
		super(type, VENDOR_TRANSACTION_GRID);
	}

	@Override
	protected void createControls() {

		// setTitle(UIUtils.title(vendorConstants.cashPurchase()));

		titlelabel = new Label(Accounter.constants().cashPurchase());
		titlelabel.setStyleName(Accounter.constants().labelTitle());
		// titlelabel.setHeight("50px");
		listforms = new ArrayList<DynamicForm>();

		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						if (date != null) {
							deliveryDateItem.setEnteredDate(date);
							setTransactionDate(date);
						}
					}
				});
		transactionDateItem.setWidth(100);

		transactionNumber = createTransactionNumberItem();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		datepanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();

		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(titlelabel);
		labeldateNoLayout.setHorizontalAlignment(ALIGN_RIGHT);
		labeldateNoLayout.setCellHorizontalAlignment(datepanel, ALIGN_RIGHT);
		labeldateNoLayout.add(datepanel);

		if (this.isEdit)
			// --the form need to be disabled here
			dateNoForm.setDisabled(true);

		forms.add(dateNoForm);
		formItems.add(transactionDateItem);
		formItems.add(transactionNumber);

		vendorCombo = createVendorComboItem(UIUtils
				.getVendorString(Accounter.constants().supplierName(),
						Accounter.constants().vendorName()));
		vendorCombo.setHelpInformation(true);
		// vendorCombo.setWidth(100);
		contactCombo = createContactComboItem();
		contactCombo.setHelpInformation(true);
		// contactCombo.setWidth(100);
		billToAreaItem = new TextAreaItem(Accounter.constants().billTo());
		billToAreaItem.setHelpInformation(true);
		billToAreaItem.setWidth(100);
		billToAreaItem.setDisabled(true);
		phoneSelect = new TextItem(Accounter.constants().phone());
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		if (isEdit)
			phoneSelect.setDisabled(true);

		vendorForm = UIUtils.form(UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor()));

		vendorForm.setWidth("100%");
		vendorForm.setFields(vendorCombo, contactCombo, phoneSelect,
				billToAreaItem);
		vendorForm.getCellFormatter().setWidth(0, 0, "160px");
		vendorForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		forms.add(vendorForm);
		formItems.add(contactCombo);
		formItems.add(billToCombo);

		payFromCombo = createPayFromCombo(Accounter.constants().payFrom());
		// payFromCombo.setWidth(100);
		payFromCombo.setPopupWidth("500px");
		checkNo = createCheckNumberItem(getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? Accounter
				.constants().chequeNo() : Accounter.constants().checkNo());
		checkNo.setDisabled(true);
		checkNo.setWidth(100);
		deliveryDateItem = createTransactionDeliveryDateItem();
		// deliveryDateItem.setWidth(100);

		paymentMethodCombo = createPaymentMethodSelectItem();
		// paymentMethodCombo.setWidth(100);
		paymentMethodCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						paymentMethodSelected(paymentMethodCombo
								.getSelectedValue());
						if (paymentMethodCombo.getSelectedValue().equals(
								Accounter.constants().check())
								|| paymentMethodCombo.getSelectedValue()
										.equals(Accounter.constants().cheque())) {
							checkNo.setDisabled(false);
						} else {
							checkNo.setDisabled(true);
						}

						if (paymentMethod.equals(Accounter.constants().check())
								&& isEdit && payFromAccount != null) {
							ClientCashPurchase cashPurchase = (ClientCashPurchase) transaction;
							checkNo.setValue(cashPurchase.getCheckNumber());
						}
					}
				});

		termsForm = new DynamicForm();
		termsForm.setWidth("100%");
		termsForm.setFields(paymentMethodCombo, payFromCombo, checkNo,
				deliveryDateItem);
		termsForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "203px");

		forms.add(termsForm);
		formItems.add(checkNo);
		formItems.add(deliveryDateItem);

		Label lab2 = new Label(Accounter.constants().itemsAndExpenses());
		menuButton = createAddNewButton();

		netAmount = new AmountLabel(Accounter.constants().netAmount());
		netAmount.setDefaultValue("£0.00");
		netAmount.setDisabled(true);
		transactionTotalNonEditableText = createTransactionTotalNonEditableItem();

		vatTotalNonEditableText = createVATTotalNonEditableItem();

		vatinclusiveCheck = getVATInclusiveCheckBox();
		vendorTransactionGrid = getGrid();
		vendorTransactionGrid.setTransactionView(this);
		vendorTransactionGrid.setCanEdit(true);
		vendorTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		vendorTransactionGrid.isEnable = false;
		vendorTransactionGrid.init();
		vendorTransactionGrid.setDisabled(isEdit);
		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		forms.add(memoForm);
		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);
		DynamicForm totalForm = new DynamicForm();
		totalForm.setNumCols(2);
		totalForm.setWidth("100%");
		totalForm.setStyleName("invoice-total");
		totalForm.setFields(netAmount, vatTotalNonEditableText,
				transactionTotalNonEditableText);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(vendorForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");
		rightVLay.add(termsForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);

		if (this instanceof CashExpenseView) {
			topHLay.setCellWidth(leftVLay, "0");
			topHLay.setCellWidth(rightVLay, "100%");
		} else {
			topHLay.setCellWidth(leftVLay, "50%");
			topHLay.setCellWidth(rightVLay, "41%");
		}
		HorizontalPanel bottomLayout = new HorizontalPanel();
		bottomLayout.setWidth("100%");

		HorizontalPanel panel = new HorizontalPanel();
		panel.setHorizontalAlignment(ALIGN_RIGHT);
		panel.add(createAddNewButton());
		panel.getElement().getStyle().setMarginTop(8, Unit.PX);

		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			VerticalPanel vpanel = new VerticalPanel();
			vpanel.setWidth("100%");
			vpanel.setHorizontalAlignment(ALIGN_RIGHT);
			vpanel.add(panel);
			vpanel.add(totalForm);

			bottomLayout.add(memoForm);
			bottomLayout.add(totalForm);
			bottomLayout.setCellWidth(totalForm, "30%");

			bottompanel.add(vpanel);
			bottompanel.add(bottomLayout);

			// VerticalPanel vPanel = new VerticalPanel();
			// vPanel.add(menuButton);
			// vPanel.add(memoForm);
			// vPanel.setWidth("100%");
			//
			// bottomLayout.add(vPanel);
			// bottomLayout.add(vatCheckform);
			// bottomLayout.setCellHorizontalAlignment(vatCheckform,
			// HasHorizontalAlignment.ALIGN_RIGHT);
			// bottomLayout.add(totalForm);
			// bottomLayout.setCellHorizontalAlignment(totalForm,
			// HasHorizontalAlignment.ALIGN_RIGHT);
		} else {
			memoForm.setStyleName("align-form");
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setWidth("100%");
			vPanel.add(panel);
			vPanel.setCellHorizontalAlignment(panel, ALIGN_RIGHT);
			vPanel.add(memoForm);

			bottompanel.add(vPanel);
		}

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(titlelabel);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		// mainVLay.add(lab2);

		mainVLay.add(vendorTransactionGrid);

		mainVLay.add(bottompanel);

		// setOverflow(Overflow.SCROLL);
		this.add(mainVLay);
		// addChild(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(termsForm);
		listforms.add(memoForm);
		listforms.add(vatCheckform);
		listforms.add(totalForm);

		if (UIUtils.isMSIEBrowser())
			resetFormView();

		initViewType();
	}

	@Override
	protected void accountSelected(ClientAccount account) {

		if (account == null) {
			payFromCombo.setValue("");
			return;
		}
		this.payFromAccount = account;
		payFromCombo.setComboItem(payFromAccount);
		if (account != null
				&& getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? paymentMethod
				.equalsIgnoreCase(Accounter.constants().cheque())
				: paymentMethod.equalsIgnoreCase(Accounter.constants().check())
						&& isEdit) {
			ClientCashPurchase cashPurchase = (ClientCashPurchase) transaction;
			checkNo.setValue(cashPurchase.getCheckNumber());
			// setCheckNumber();
		} else if (account == null) {
			checkNo.setValue("");
		}
	}

	protected void setCheckNumber() {

		rpcUtilService.getNextCheckNumber(payFromAccount.getID(),
				new AccounterAsyncCallback<Long>() {

					public void onException(AccounterException t) {
						// //UIUtils.logError(
						// "Failed to get the next check number!!", t);
						checkNo.setValue(Accounter.constants().toBePrinted());
						return;
					}

					public void onResultSuccess(Long result) {
						if (result == null)
							onFailure(null);

						checkNumber = String.valueOf(result);
						checkNo.setValue(result.toString());
					}

				});

	}

	@Override
	protected void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientCashPurchase());
		} else {

			contactSelected(transaction.getContact());
			vendorSelected(getCompany().getVendor(transaction.getVendor()));
			phoneSelect.setValue(transaction.getPhone());
			this.billingAddress = transaction.getVendorAddress();
			if (billingAddress != null) {

				billToAreaItem.setValue(getValidAddress(billingAddress));

			} else
				billToAreaItem.setValue("");
			// paymentMethodSelected(cashPurchaseToBeEdited.getPaymentMethod()
			// !=
			// null ? cashPurchaseToBeEdited
			// .getPaymentMethod()
			// : "");
			paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
			accountSelected(getCompany().getAccount(transaction.getPayFrom()));
			// transactionDateItem.setEnteredDate(cashPurchaseToBeEdited.get)
			initMemoAndReference();
			checkNo.setDisabled(true);
			if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				netAmount.setAmount(transaction.getNetAmount());
				vatTotalNonEditableText.setAmount(transaction.getTotal()
						- transaction.getNetAmount());
			}
			transactionTotalNonEditableText.setAmount(transaction.getTotal());

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());
			}
			vendorTransactionGrid.setCanEdit(false);
		}
		super.initTransactionViewData();
		initTransactionNumber();
		initPayFromAccounts();

	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		if (this.getVendor() != null && this.getVendor() != vendor) {
			ClientCashPurchase ent = (ClientCashPurchase) this.transaction;

			if (ent != null && ent.getVendor() == vendor.getID()) {
				this.vendorTransactionGrid.removeAllRecords();
				this.vendorTransactionGrid
						.setRecords(ent.getTransactionItems());
			} else if (ent != null && ent.getVendor() != vendor.getID()) {
				this.vendorTransactionGrid.removeAllRecords();
				this.vendorTransactionGrid.updateTotals();
			}
		}
		super.vendorSelected(vendor);
		if (vendor == null) {
			return;
		}
		if (!(this instanceof CashExpenseView)) {
			if (vendor.getPhoneNo() != null)
				phoneSelect.setValue(vendor.getPhoneNo());
			else
				phoneSelect.setValue("");
		}
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {
			billToAreaItem.setValue(getValidAddress(billingAddress));
		} else
			billToAreaItem.setValue("");
		if (vendor == null)
			return;
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			super.setVendorTaxcodeToAccount();

	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		// super.paymentMethodSelected(paymentMethod);
		// setDisableStateForCheckNo(paymentMethod);
		// paymentMethodCombo.setValue(paymentMethod);
	}

	private void setDisableStateForCheckNo(String paymentMethod) {

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? paymentMethod
				.equalsIgnoreCase(Accounter.constants().cheque())
				: paymentMethod.equalsIgnoreCase(Accounter.constants().check())) {
			checkNo.setDisabled(false);
		} else {
			checkNo.setValue("");
			checkNo.setDisabled(true);

		}
		if (isEdit) {
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? paymentMethod
					.equalsIgnoreCase(Accounter.constants().cheque())
					: paymentMethod.equalsIgnoreCase(Accounter.constants()
							.check())) {
				checkNo.setDisabled(false);
			} else {
				checkNo.setDisabled(true);
			}
		}
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			transaction.setNetAmount(netAmount.getAmount());
			// if (vatinclusiveCheck != null)
			// cashPurchase.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
			// .getValue());
		}
		super.saveAndUpdateView();

		createAlterObject();
	}

	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type
		transaction.setType(ClientTransaction.TYPE_CASH_PURCHASE);

		if (this.getVendor() != null) {
			// Setting Vendor
			transaction.setVendor(this.getVendor().getID());
		}

		// Setting Contact
		if (contact != null)
			transaction.setContact(this.contact);

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress((billingAddress));

		// Setting Phone
		// if (phoneNo != null)
		transaction.setPhone(phoneSelect.getValue().toString());

		// Setting Payment Methods
		transaction.setPaymentMethod(paymentMethodCombo.getValue().toString());

		// Setting Pay From Account
		transaction.setPayFrom(payFromAccount.getID());

		// Setting Check number
		transaction.setCheckNumber(checkNo.getValue().toString());
		// transaction
		// .setCheckNumber(getCheckNoValue() ==
		// ClientWriteCheck.IS_TO_BE_PRINTED ? "0"
		// : getCheckNoValue() + "");

		// Setting Delivery date
		if (deliveryDateItem.getEnteredDate() != null)
			transaction.setDeliveryDate(deliveryDateItem.getEnteredDate()
					.getDate());

		// Setting Total
		transaction.setTotal(vendorTransactionGrid.getTotal());

		// Setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// cashPurchase.setReference(getRefText());
	}

	public void createAlterObject() {

		saveOrUpdate((ClientCashPurchase) transaction);

	}

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setDisabled(true);
		setMemoTextAreaItem(((ClientCashPurchase) transaction).getMemo());
		// setRefText(((ClientCashPurchase) transactionObject).getReference());
	}

	@Override
	public void updateNonEditableItems() {
		netAmount.setAmount(vendorTransactionGrid.getGrandTotal());
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			vatTotalNonEditableText.setAmount(vendorTransactionGrid.getTotal()
					- vendorTransactionGrid.getGrandTotal());
		}
		transactionTotalNonEditableText.setAmount(vendorTransactionGrid
				.getTotal());
	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = super.validate();

		if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateTransactionDate());
		}

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateDate());
		}

		result.add(vendorForm.validate());
		result.add(termsForm.validate());

		if (!AccounterValidator.isValidDueOrDelivaryDates(
				deliveryDateItem.getEnteredDate(), this.transactionDate)) {
			result.addError(deliveryDateItem, Accounter.constants().the()
					+ " "
					+ Accounter.constants().deliveryDate()
					+ " "
					+ " "
					+ Accounter.constants()
							.cannotbeearlierthantransactiondate());
		}

		if (AccounterValidator.isBlankTransaction(vendorTransactionGrid)) {
			result.addError(vendorTransactionGrid,
					accounterConstants.blankTransaction());
		}
		result.add(vendorTransactionGrid.validateGrid());
		return result;

	}

	public static CashPurchaseView getInstance() {
		return new CashPurchaseView();
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vendorCombo.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	protected void onLoad() {
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR) {

				vendorCombo.addComboItem((ClientVendor) core);
			}
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				payFromCombo.addComboItem((ClientAccount) core);

			break;
		case AccounterCommand.UPDATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR) {
				vendorCombo.updateComboItem((ClientVendor) core);
			}

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				payFromCombo.updateComboItem((ClientAccount) core);
			break;
		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				vendorCombo.removeComboItem((ClientVendor) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				payFromCombo.removeComboItem((ClientAccount) core);
			break;
		}
	}

	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);

	}

	protected void enableFormItems() {
		isEdit = false;
		vendorCombo.setDisabled(isEdit);
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		paymentMethodCombo.setDisabled(isEdit);
		if (paymentMethod.equals(Accounter.constants().check())
				|| paymentMethod.equals(Accounter.constants().cheque())) {
			checkNo.setDisabled(isEdit);
		} else {
			checkNo.setDisabled(!isEdit);
		}
		deliveryDateItem.setDisabled(isEdit);
		vendorTransactionGrid.setDisabled(isEdit);
		vendorTransactionGrid.setCanEdit(true);
		memoTextAreaItem.setDisabled(isEdit);
		super.onEdit();
	}

	protected void initViewType() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected Double getTransactionTotal() {
		return this.transactionTotalNonEditableText.getAmount();
	}

	private void resetFormView() {
		vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		vendorForm.setWidth("75%");
		// refText.setWidth("200px");

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().cashPurchases();
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		// TODO Auto-generated method stub

	}
}
