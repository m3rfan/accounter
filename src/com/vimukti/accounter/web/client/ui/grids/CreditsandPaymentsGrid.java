package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.customers.CustomerCreditsAndPaymentsDialiog;
import com.vimukti.accounter.web.client.ui.customers.CustomersMessages;

public class CreditsandPaymentsGrid extends
		AbstractTransactionGrid<ClientCreditsAndPayments> {

	private CustomerCreditsAndPaymentsDialiog dialog;
	private boolean isCanEdit;
	private CustomersMessages customerConstants = GWT
			.create(CustomersMessages.class);
	List<Integer> selectedValues = new ArrayList<Integer>();
	private List<ClientCreditsAndPayments> actualRecords, copyRecords;
	private int columns[] = { ListGrid.COLUMN_TYPE_TEXT,
			ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
			ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX,
			ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX };
	@SuppressWarnings("unused")
	private ClientTransactionReceivePayment traReceivePayment;
	public LinkedHashMap<ClientTransactionReceivePayment, List<ClientCreditsAndPayments>> creditsMap = new LinkedHashMap<ClientTransactionReceivePayment, List<ClientCreditsAndPayments>>();

	public CreditsandPaymentsGrid(boolean isMultiSelectionEnable,
			CustomerCreditsAndPaymentsDialiog creditdialog,
			ClientTransactionReceivePayment trareceivePayment) {
		super(isMultiSelectionEnable);
		this.dialog = creditdialog;
		this.traReceivePayment = trareceivePayment;
	}

	@Override
	public void setCanEdit(boolean enabled) {
		this.isCanEdit = enabled;
		super.setCanEdit(enabled);
	}

	@Override
	public void onHeaderCheckBoxClick(boolean isChecked) {
		if (isChecked) {
			selectAllValues();
		} else
			resetAllValues();
		super.onHeaderCheckBoxClick(isChecked);
	}

	/*
	 * This method invoked when headercheckbox is checked.It updates the
	 * non-editable fileds and record's vaFlues
	 */
	private void selectAllValues() {
		for (ClientCreditsAndPayments cpRecord : this.getRecords()) {
			if (!isSelected(cpRecord)) {
				((CheckBox) this.body.getWidget(indexOf(cpRecord), 0))
						.setValue(true);
				updateValue(cpRecord);
			}
		}
	}

	/*
	 * This method invoked when header checkbox is uncheked.And resets
	 * allrecords values to its original values as-well-as no-editable fields
	 */

	public void resetAllValues() {
		for (ClientCreditsAndPayments creditsAndPayments : getRecords()) {
			if (isSelected(creditsAndPayments)) {
				resetValidValue(creditsAndPayments);
			}
		}
	}

	private void resetValidValue(ClientCreditsAndPayments creditsAndPayments) {
		double balance = creditsAndPayments.getBalance();
		double amountToUse = creditsAndPayments.getAmtTouse();
		if (DecimalUtil.isGreaterThan(balance, 0) && DecimalUtil.isGreaterThan(amountToUse, 0)) {
			ClientCreditsAndPayments editRecord = creditsAndPayments;
			// ClientCreditsAndPayments originalRecord = actualRecords
			// .get(indexOf(creditsAndPayments));
			// editRecord.setAmtTouse(originalRecord.getRemaoningBalance());
			resetValue(editRecord);
		} else
			resetValue(creditsAndPayments);
	}

	@Override
	protected void onSelectionChanged(ClientCreditsAndPayments core, int row,
			boolean isChecked) {
		if (isChecked)
			updateValue(core);
		else if (!isChecked)
			resetValidValue(core);
		super.onSelectionChanged(core, row, isChecked);
	}

	private void updateValue(ClientCreditsAndPayments creditsAndpayments) {
		double bal = creditsAndpayments.getBalance();
		creditsAndpayments.setBalance(0.0d);
		creditsAndpayments.setAmtTouse(bal);
		creditsAndpayments.setRemaoningBalance(0.0);
		updateData(creditsAndpayments);
		updateAmountValues();
	}

	/*
	 * This method invoked when the record is unselected.It resets the
	 * non-editable fields as-well-as records values
	 */
	public void resetValue(ClientCreditsAndPayments creditPayments) {
		creditPayments
				.setBalance(creditPayments.isRecordChanged() ? creditPayments
						.getRemaoningBalance()
						+ creditPayments.getAmtTouse() : creditPayments
						.getAmtTouse());
		creditPayments.setAmtTouse(0.0D);
		updateData(creditPayments);
		updateAmountValues();
	}

	private void updateAmountValues() {
		dialog.totalBalances = 0.0D;
		dialog.totalAmountToUse = 0.0D;
		for (ClientCreditsAndPayments creditsAndPayments : getRecords()) {
			dialog.totalBalances += creditsAndPayments.getBalance();
			dialog.totalAmountToUse += creditsAndPayments.getAmtTouse();
		}
		dialog.updateFields();

	}

	@Override
	protected void onClick(ClientCreditsAndPayments obj, int row, int index) {
		if (index != 4)
			selectRow(row);
		super.onClick(obj, row, index);
	}

	@Override
	protected int getColumnType(int index) {
		return columns[index];
	}

	@Override
	public void selectRow(int row) {
		if (isCanEdit) {
			CheckBox box = (CheckBox) this.getWidget(row, 0);
			if (box.getValue()) {
				return;
			} else {
				if (currentCol == 4 && box.getValue())
					startEditing(row);
			}
		}
		super.selectRow(row);
	}

	@Override
	protected boolean isEditable(ClientCreditsAndPayments obj, int row,
			int index) {
		if (isCanEdit) {
			CheckBox box = (CheckBox) this.getWidget(row, 0);
			if (box.getValue()) {
				if (Arrays.asList(0, 1, 2, 3).contains(index))
					return false;
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	@Override
	protected String[] getColumns() {

		return new String[] { customerConstants.date(),
				customerConstants.memo(), customerConstants.creditAmount(),
				customerConstants.balance(), customerConstants.amountToUse() };
	}

	@Override
	public void editComplete(ClientCreditsAndPayments item, Object value,
			int col) {

		switch (col) {
		case 4:
			ClientCreditsAndPayments editingRecord = item;

			try {
				Double amtTouse = Double.parseDouble(DataUtils
						.getReformatedAmount(value.toString())
						+ "");
				// ClientCreditsAndPayments actualRecord = actualRecords
				// .get(indexOf(item));

				// Double originalBalance = actualRecord.getActualAmt();
				Double balance = item.getRemaoningBalance()
						+ (item.getAmtTouse() - amtTouse);

				if (DecimalUtil.isLessThan(amtTouse, 0) || DecimalUtil.isLessThan(balance, 0)) {
					// || balance != 0 ? ((amtTouse
					// .compareTo(item.getActualAmt()) > 0) ? true : false)
					// : false) {
					Accounter
							.showError(AccounterErrorType.RECEIVEPAYMET_APPLIED_CREDITS_AMOUNT);
					setText(indexOf(item), 4, DataUtils.getAmountAsString(item
							.getAmtTouse()));
				} else {
					if (DecimalUtil.isLessThan(amtTouse, item.getRemaoningBalance())
							&& !DecimalUtil.isGreaterThan(amtTouse, item.getActualAmt())
							&& DecimalUtil.isGreaterThan(amtTouse, balance)) {
						Accounter
								.showError(AccounterErrorType.RECEIVEPAYMET_APPLIED_CREDITS_AMOUNT);
						setText(indexOf(item), 4, DataUtils
								.getAmountAsString(item.getAmtTouse()));
					} else {
						editingRecord.setAmtTouse(amtTouse);
						editingRecord.setBalance(balance);
						editingRecord.setRemaoningBalance(balance);
						editingRecord.setRecordChanged(true);

						updateData(editingRecord);
						updateAmountValues();
					}

				}
			} catch (Exception e) {
				Accounter.showError("Invalid Amount");
			}

			break;

		default:
			break;
		}

		super.editComplete(item, value, col);
	}

	/*
	 * This method invoked when record values need to set to its original values
	 */
	public void initialCreditsAndPayments(
			List<ClientCreditsAndPayments> actualRecs) {
		actualRecords = new ArrayList<ClientCreditsAndPayments>();
		for (ClientCreditsAndPayments rec : actualRecs) {
			ClientCreditsAndPayments r = new ClientCreditsAndPayments();
			r.setAmtTouse(rec.getAmtTouse());
			r.setBalance(rec.getBalance());
			r.setCreditAmount(rec.getCreditAmount());
			r.setMemo(rec.getMemo() != null ? rec.getMemo() : "");
			r.setPayee(rec.getPayee());
			r.setTransactionCreditsAndPayments(rec
					.getTransactionCreditsAndPayments());
			r.setID(rec.getID());
			r.setTransaction(rec.getTransaction());
			r.setActualAmt(rec.getActualAmt());
			this.actualRecords.add(r);
		}
	}

	public void createCopyCreditandPayments(
			List<ClientCreditsAndPayments> actualRecs) {
		copyRecords = new ArrayList<ClientCreditsAndPayments>();
		for (ClientCreditsAndPayments rec : actualRecs) {
			ClientCreditsAndPayments r = new ClientCreditsAndPayments();
			r.setAmtTouse(rec.getAmtTouse());
			r.setBalance(rec.getBalance());
			r.setCreditAmount(rec.getCreditAmount());
			r.setMemo(rec.getMemo() != null ? rec.getMemo() : "");
			r.setPayee(rec.getPayee());
			r.setTransactionCreditsAndPayments(rec
					.getTransactionCreditsAndPayments());
			r.setID(rec.getID());
			r.setTransaction(rec.getTransaction());
			r.setActualAmt(rec.getActualAmt());
			r.setRecordChanged(rec.isRecordChanged());
			this.copyRecords.add(r);
		}

	}

	public List<ClientCreditsAndPayments> getCopyCreditAndPayments() {
		return this.copyRecords;
	}

	public List<ClientCreditsAndPayments> getUpdatedRecords() {
		List<ClientCreditsAndPayments> updatedPayments = new ArrayList<ClientCreditsAndPayments>();
		for (ClientCreditsAndPayments record : this.getRecords()) {
			if (isSelected(record))
				record.setRecordChanged(true);
			else
				record.setRecordChanged(false);

			updatedPayments.add(record);
		}
		return updatedPayments;
	}

	@Override
	protected int getCellWidth(int index) {
		return -1;
	}

	@Override
	protected Object getColumnValue(
			ClientCreditsAndPayments creditsAndPayments, int index) {
		switch (index) {
		case 0:
			return UIUtils.dateFormat(creditsAndPayments.getTransaction()
					.getDate());
		case 1:
			return creditsAndPayments.getMemo();
		case 2:
			return DataUtils.getAmountAsString(creditsAndPayments
					.getCreditAmount());
		case 3:
			return DataUtils.getAmountAsString(creditsAndPayments.getBalance());
		case 4:
			return DataUtils
					.getAmountAsString(creditsAndPayments.getAmtTouse());
		default:
			break;
		}
		return null;
	}

	@Override
	public boolean validateGrid() {
		if (this.getRecords().size() > 0
				&& this.getSelectedRecords().size() == 0)
			return false;
		return true;
	}

	public boolean isSelected(ClientCreditsAndPayments transactionList) {
		return ((CheckBox) getWidget(indexOf(transactionList), 0)).getValue();
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientCreditsAndPayments obj,
			int colIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClientTransactionItem> getallTransactions(
			ClientTransaction object) throws InvalidEntryException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ClientCreditsAndPayments> getActualRecords() {
		return this.actualRecords;
	}

	public void setCheckboxValue(ClientCreditsAndPayments record) {
		((CheckBox) getWidget(indexOf(record), 0)).setValue(record
				.isRecordChanged());
	}

	public void setCreditsAndPaymentsMap(
			ClientTransactionReceivePayment record,
			List<ClientCreditsAndPayments> actualRecords2) {
		creditsMap.put(record, actualRecords2);
	}

	@Override
	public void setTaxCode(long taxCode) {
		// TODO Auto-generated method stub
	}

}
