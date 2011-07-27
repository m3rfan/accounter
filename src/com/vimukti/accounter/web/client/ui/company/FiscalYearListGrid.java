package com.vimukti.accounter.web.client.ui.company;

import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class FiscalYearListGrid extends ListGrid<ClientFiscalYear> {

	private ManageFiscalYearDialog manageFiscalYear;

	public FiscalYearListGrid(ManageFiscalYearDialog manageFiscalYear,
			boolean isMultiSelectionEnable) {
		super(false);
		this.manageFiscalYear = manageFiscalYear;
		this.isEnable = false;
		init();
	}

	@Override
	protected int getColumnType(int index) {
		return ListGrid.COLUMN_TYPE_TEXT;
	}

	@Override
	protected Object getColumnValue(ClientFiscalYear obj, int index) {
		switch (index) {
		case 0:
			return UIUtils.getDateByCompanyType(obj.getStartDate());
		case 1:
			return UIUtils.getDateByCompanyType(obj.getEndDate());
		case 2:
			if (obj.getStatus() == 1) {
				return Accounter.getCompanyMessages().open();
			} else {
				return Accounter.getCompanyMessages().close();
			}

		default:
			return null;
		}
	}

	@Override
	protected String[] getSelectValues(ClientFiscalYear obj, int index) {
		// NOTHING TO DO
		return null;
	}

	@Override
	protected boolean isEditable(ClientFiscalYear obj, int row, int index) {
		// NOTHING TO DO
		return false;
	}

	@Override
	protected void onClick(ClientFiscalYear obj, int row, int index) {
		manageFiscalYear.showButtonsVisibility();
	}

	@Override
	public void onDoubleClick(ClientFiscalYear obj) {
		// NOTHING TO DO
	}

	@Override
	protected void onValueChange(ClientFiscalYear obj, int index, Object value) {
		// NOTHING TO DO
	}

	@Override
	protected int sort(ClientFiscalYear obj1, ClientFiscalYear obj2, int index) {
		// NOTHING TO DO
		return 0;
	}

	@Override
	public boolean validateGrid() {
		// NOTHING TO DO
		return false;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 100;
		case 1:
			return 100;
		case 2:
			return 100;
		default:
		}
		return -2;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.getCompanyMessages().startDate(),
				Accounter.getCompanyMessages().endDate(),
				Accounter.getCompanyMessages().status() };
	}

	/*
	 * @Override protected int[] setColTypes() { return new int[] {
	 * ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_DATE,
	 * ListGrid.COLUMN_TYPE_TEXT }; }
	 */
	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
	}

	public void sortList() {
		this.headerCellClicked(1);
	};
}
