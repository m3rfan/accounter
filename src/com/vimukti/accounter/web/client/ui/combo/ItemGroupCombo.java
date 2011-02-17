package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.ui.ItemGroupListDialog;

public class ItemGroupCombo extends CustomCombo<ClientItemGroup> {

	public ItemGroupCombo(String title) {
		super(title);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newItemGroup();
	}

	@Override
	protected String getDisplayName(ClientItemGroup object) {
		return object != null ? object.getName() != null ? object.getName()
				: "" : "";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onAddNew() {
		ItemGroupListDialog itemGroupDialog = new ItemGroupListDialog("", "");
		itemGroupDialog.removeFromParent();
		itemGroupDialog.showAddEditGroupDialog(null);
		itemGroupDialog.addCallBack(createAddNewCallBack());

	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.ITEM_GROUP;
	}

	@Override
	protected String getColumnData(ClientItemGroup object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}
}
