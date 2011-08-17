package com.vimukti.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.VList;
import com.vimukti.accounter.web.client.ui.Accounter;

public class AccountComboTable extends CellTable<ClientAccount> {

	private int cols = 3;

	public AccountComboTable(VList<ClientAccount> vList) {
		ListDataProvider<ClientAccount> list = new VListDataProvider<ClientAccount>(
				vList);
		list.addDataDisplay(this);

		initColumns();
	}

	private void initColumns() {
		for (int i = 0; i < cols; i++) {
			this.addColumn(createColumn(i));
		}
	}

	private Column<ClientAccount, String> createColumn(final int col) {
		Column<ClientAccount, String> column = new Column<ClientAccount, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientAccount object) {
				if (object.equals("emptyRow"))
					return "    ";
				else if (object.equals("addNewCaption")) {
					if (cols > 1)
						return (col == 1) ? getDefaultAddNewCaption() : "  ";
					else
						return getDefaultAddNewCaption();
				}
				return getColumnData(object, 0, col);
			}

		};
		return column;
	}

	private String getColumnData(ClientAccount object, int row, int col) {

		switch (col) {
		case 0:
			if (ClientCompanyPreferences.get().getUseAccountNumbers() == true) {
				return object.getNumber();
			} else {
				return null;
			}
		case 1:
			return object.getName();
		case 2:
			return Utility.getAccountTypeString(object.getType());
		default:
			break;
		}
		return null;
	}

	public String getDefaultAddNewCaption() {

		return Accounter.constants().newAccount();
	}
}
