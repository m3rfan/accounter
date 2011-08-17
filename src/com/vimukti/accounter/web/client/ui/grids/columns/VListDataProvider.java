package com.vimukti.accounter.web.client.ui.grids.columns;

import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.core.ListListener;
import com.vimukti.accounter.web.client.core.VList;

public class VListDataProvider<T> extends ListDataProvider<T> implements ListListener<T> {

	public VListDataProvider(VList<T> vList) {
		vList.addListener(this);
		this.getList().addAll(vList);
	}

	@Override
	public void onAdd(T e) {
		this.getList().add(e);
	}

	@Override
	public void onRemove(T e) {
		this.getList().remove(e);
	}

}
