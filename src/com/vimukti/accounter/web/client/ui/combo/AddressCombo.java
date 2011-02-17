package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientAddress;

public class AddressCombo extends CustomCombo<ClientAddress> {

	public AddressCombo(String title) {
		super(title);
	}

	// AddressCombo dont have "AddNew" option
	@Override
	public String getDefaultAddNewCaption() {
		return null;
	}

	@Override
	protected String getDisplayName(ClientAddress object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		// TODO Auto-generated method stub

	}

	@Override
	public SelectItemType getSelectItemType() {

		return SelectItemType.ADDRESS;
	}

	@Override
	protected String getColumnData(ClientAddress object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
