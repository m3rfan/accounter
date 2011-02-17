package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.Accounter.AccounterType;

public class AccountRegisterOtherListGrid extends BaseListGrid<AccountRegister> {

	public double balance = 0.0;
	public double totalBalance = 0.0;

	public AccountRegisterOtherListGrid(boolean isMultiSelectionEnable) {
		super(false, true);
	}

	@Override
	protected Object getColumnValue(AccountRegister accRegister, int col) {
		switch (col) {
		case 0:
			return accRegister.getDate();
		case 1:
			return Utility.getTransactionName((accRegister.getType()));
		case 2:
			return accRegister.getNumber();
		case 3:
			if (DecimalUtil.isGreaterThan(accRegister.getAmount(), 0.0))
				return DataUtils.getAmountAsString(accRegister.getAmount());
			else
				return DataUtils.getAmountAsString(0.00);
		case 4:
			if (DecimalUtil.isLessThan(accRegister.getAmount(), 0.0))
				return DataUtils
						.getAmountAsString(-1 * accRegister.getAmount());
			else
				return DataUtils.getAmountAsString(0.00);
		case 5:
			return accRegister.getAccount();
		case 6:
			return accRegister.getMemo();
		case 7:
			return DataUtils.getAmountAsString(getBalanceValue(accRegister));

		case 8:
			if (!accRegister.isVoided())
				return FinanceApplication.getFinanceImages().notvoid();
			// return "/images/not-void.png";
			else
				return FinanceApplication.getFinanceImages().voided();
			// return "/images/voided.png";

		}
		return null;
	}

	/*
	 * The balce value calculated using the formaula Balance = Balance - Reduce
	 * + Increase
	 */
	private double getBalanceValue(AccountRegister accountRegister) {
		/* Here 'd' value might be "positive" or "negative" */
		double d = accountRegister.getAmount();

		if (DecimalUtil.isLessThan(d, 0.0)) {
			d = -1 * d;
			balance = balance - d;
		} else {
			balance = balance + d;
		}
		totalBalance += balance;
		return balance;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { FinanceApplication.getCustomersMessages().date(),
				FinanceApplication.getCustomersMessages().type(),
				FinanceApplication.getCustomersMessages().documentNo(),
				FinanceApplication.getCustomersMessages().increase(),
				FinanceApplication.getCustomersMessages().reduce(),
				FinanceApplication.getCustomersMessages().account(),
				FinanceApplication.getCustomersMessages().memo(),
				FinanceApplication.getCustomersMessages().currentBalance(),
				FinanceApplication.getCustomersMessages().isVoided() };
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	public void onDoubleClick(AccountRegister obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validateGrid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void executeDelete(AccountRegister object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0)
			return 70;
		else if (index == 1)
			return 100;
		else if (index == 2)
			return 95;
		else if (index == 3 || index == 4 || index == 7)
			return 115;
		else if (index == 8)
			return 70;
		// if (index == 7)
		// return 50;
		else if(index==5)
			return 125; 

		return super.getCellWidth(index);
		// return -1;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	protected void onClick(AccountRegister obj, int row, int col) {
		// if (col == 8 && !obj.isVoided()) {
		// showWarningDialog(obj);
		// }

	}

	private void showWarningDialog(final AccountRegister obj) {
		Accounter.showWarning("Do you want to Void the Transaction",
				AccounterType.WARNING, new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() throws InvalidEntryException {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean onNoClick() throws InvalidEntryException {
						return true;
					}

					@Override
					public boolean onYesClick() throws InvalidEntryException {
						voidTransaction(obj);
						return true;
					}

				});

	}

	protected void voidTransaction(final AccountRegister obj) {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					obj.setVoided(true);
					updateData(obj);

				}

			}
		};

		AccounterCoreType coretype = UIUtils
				.getAccounterCoreType(obj.getType());
		if (coretype != null) {
			rpcDoSerivce.voidTransaction(coretype, obj.getTransactionId(),
					callback);
		}
	}

	public AccounterCoreType getType() {
		return null;
	}

}
