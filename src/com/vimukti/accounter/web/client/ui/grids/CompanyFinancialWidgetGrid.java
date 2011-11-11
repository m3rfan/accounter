package com.vimukti.accounter.web.client.ui.grids;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.KeyFinancialIndicator;

public class CompanyFinancialWidgetGrid extends ListGrid<KeyFinancialIndicator> {

	String[] keys = { Accounter.constants().grossProfit(),
			Accounter.constants().netProfit(),
			Accounter.messages().bankAccounts(Global.get().Accounts()) };

	Map<Integer, Integer> colsMap = new HashMap<Integer, Integer>();
	private Double rowTotal = 0.0;

	public CompanyFinancialWidgetGrid() {
		super(false);
	}

	@Override
	protected int getColumnType(int index) {
		if (index == 0)
			return COLUMN_TYPE_HTML;
		else
			return COLUMN_TYPE_DECIMAL_TEXT;

	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void addData(KeyFinancialIndicator obj) {
		super.addData(obj);
		if (keys[1].equals(obj.getKeyIndicator()))
			this.rowFormatter.getElement(currentRow).addClassName("net-profit");
		if (Arrays.asList(keys).contains(obj.getKeyIndicator()))
			this.rowFormatter.getElement(currentRow).addClassName(
					"financialDataTitle");
	}

	@Override
	protected Object getColumnValue(KeyFinancialIndicator account, int index) {
		if (account.getKeyIndicator().equals("empty"))
			return " ";
		switch (index) {
		case 0:
			/* Initializing rowtotal to 0 when next row is started */
			rowTotal = 0.0;
			return account.getKeyIndicator();
		case 7:
			return DataUtils.amountAsStringWithCurrency(rowTotal,

			getCompany().getPrimaryCurrency());
		}
		Double amt = account.getIndicators().get(colsMap.get(index));
		if (amt == null) {
			return UIUtils.getCurrencySymbol() + " 0.00";
		} else {
			// if (Accounter.getCompany()
			// .isCurrentInFiscalYear(colsMap.get(index))) {
			// rowTotal += amt;
			// }
		}
		return DataUtils.amountAsStringWithCurrency(amt, getCompany().getPrimaryCurrency());
	}

	@Override
	protected String[] getSelectValues(KeyFinancialIndicator obj, int index) {
		return null;
	}

	@Override
	protected boolean isEditable(KeyFinancialIndicator obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(KeyFinancialIndicator obj, int row, int index) {

	}

	@Override
	public void onDoubleClick(KeyFinancialIndicator obj) {

	}

	@Override
	protected void onValueChange(KeyFinancialIndicator obj, int index,
			Object value) {
	}

	@Override
	protected int sort(KeyFinancialIndicator obj1, KeyFinancialIndicator obj2,
			int index) {
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0) {
			return 100;
		}
		return -1;
	}

	@Override
	protected String[] getColumns() {
		String[] colArray = new String[8];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = "";
				break;
			case 1:
				colArray[index] = Accounter.constants().currentMonth();
				colsMap.put(1, getKeyValue(0));
				break;
			case 2:
				colArray[index] = getMonthAsString(getCurrentMonth() - 1);
				colsMap.put(2, getKeyValue(1));
				break;
			case 3:
				colArray[index] = getMonthAsString(getCurrentMonth() - 2);
				colsMap.put(3, getKeyValue(2));
				break;
			case 4:
				colArray[index] = getMonthAsString(getCurrentMonth() - 3);
				colsMap.put(4, getKeyValue(3));
				break;
			case 5:
				colArray[index] = getMonthAsString(getCurrentMonth() - 4);
				colsMap.put(5, getKeyValue(4));
				break;
			case 6:
				colArray[index] = getMonthAsString(getCurrentMonth() - 5);
				colsMap.put(6, getKeyValue(5));
				break;
			case 7:
				colArray[index] = Accounter.constants().yearToDate();
				break;
			default:
				break;
			}
		}
		return colArray;
	}

	private int getCurrentMonth() {
		return new ClientFinanceDate().getMonth();
	}

	private String getMonthAsString(int month) {
		switch (month) {
		case 0:
			return Accounter.constants().jan();
		case 1:
			return Accounter.constants().feb();
		case 2:
			return Accounter.constants().mar();
		case 3:
			return Accounter.constants().apr();
		case 4:
			return Accounter.constants().may();
		case 5:
			return Accounter.constants().jun();
		case 6:
			return Accounter.constants().jul();
		case 7:
		case -5:
			return Accounter.constants().aug();
		case 8:
		case -4:
			return Accounter.constants().sept();
		case 9:
		case -3:
			return Accounter.constants().oct();
		case 10:
		case -2:
			return Accounter.constants().nov();
		case 11:
		case -1:
			return Accounter.constants().dec();

		}
		return "";

	}

	private int getKeyValue(int column) {
		int currentMonth = getCurrentMonth();
		int currentYear = new ClientFinanceDate().getYear() + 1900;
		if (currentMonth - column >= 0)
			return (currentYear * 100) + (currentMonth - column);
		if (currentMonth - column < 0)
			return ((currentYear - 1) * 100) + (12 + (currentMonth - column));

		return 0;
	}
}
