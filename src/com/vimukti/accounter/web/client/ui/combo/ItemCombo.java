package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;

public class ItemCombo extends CustomCombo<ClientItem> {
	// Type For Checking Request is from Customer View Or Vendor View
	private int type;

	// private final int TYPE_CUSTOMER = 1;
	// private final int TYPE_VENDOR = 2;

	public ItemCombo(String title, int type) {
		super(title);
		this.type = type;
		initCombo(FinanceApplication.getCompany().getActiveItems());
	}

	public ItemCombo(String title, int type, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 3);
		this.type = type;
		initCombo(FinanceApplication.getCompany().getActiveItems());
	}

	public ItemCombo(String title, int type, boolean isAddNewRequired,
			boolean isService) {
		super(title, isAddNewRequired, 3);
		this.type = type;
		initCombo(isService);

	}

	public void initCombo(boolean isService) {
		List<ClientItem> items = FinanceApplication.getCompany()
				.getActiveItems();
		List<ClientItem> serviceitems = new ArrayList<ClientItem>();
		List<ClientItem> productitems = new ArrayList<ClientItem>();
		for (ClientItem item : items) {
			if (item.getType() == ClientItem.TYPE_SERVICE)
				serviceitems.add(item);
			else
				productitems.add(item);
		}
		if (isService)
			initCombo(serviceitems);
		else
			initCombo(productitems);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newItem();
	}

	@Override
	protected String getDisplayName(ClientItem object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		Action action;
		if (type == 1) {
			action = CustomersActionFactory.getNewItemAction();
		} else {
			action = CompanyActionFactory.getNewItemAction();
		}
		action.setActionSource(this);
		action.run(null, true);
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.ITEM;
	}

	@Override
	protected String getColumnData(ClientItem object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		case 1:
			return object.getType() == ClientItem.TYPE_SERVICE ? FinanceApplication.getCustomersMessages().service()
					: FinanceApplication.getCustomersMessages().PRoduct();
		case 2:
			return DataUtils.getAmountAsString(object.getSalesPrice());
		default:
			break;
		}
		return null;
	}

}
