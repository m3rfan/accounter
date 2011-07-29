package com.vimukti.accounter.web.client.ui.fixedassets;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class HistoryListAction extends Action {
	private HistoryListView view;

	public HistoryListAction(String text) {
		super(text);
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	
//	@Override
//	public ParentCanvas getView() {
//		return this.view;
//	}

	@Override
	public void run(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				try {
					/*
					 * From "ClientFixedAsset", in listview, we'll get the list
					 * of "FixedAssetHistory" objects
					 */
					view = new HistoryListView((ClientFixedAsset) data);
					MainFinanceWindow.getViewManager().showView(view, null,
							isDependent, HistoryListAction.this);

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Vendor View..", t);
			}
		});
	}

	@Override
	public String getHistoryToken() {
		return "historyList";
	}

}
