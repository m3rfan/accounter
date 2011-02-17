package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientCustomerGroup;
import com.vimukti.accounter.web.client.ui.customers.CustomerGroupListDialog;

public class CustomerGroupCombo extends CustomCombo<ClientCustomerGroup> {

	public CustomerGroupCombo(String title) {
		super(title);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newCustomerGroup();
	}

	@Override
	protected String getDisplayName(ClientCustomerGroup object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onAddNew() {
		CustomerGroupListDialog customerGroupDialog = new CustomerGroupListDialog(
				"", "");
		customerGroupDialog.hide();
		customerGroupDialog.addCallBack(createAddNewCallBack());
		customerGroupDialog.showAddEditGroupDialog(null);
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.CUSTOMER_GROUP;
	}

	@Override
	protected String getColumnData(ClientCustomerGroup object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}
}
