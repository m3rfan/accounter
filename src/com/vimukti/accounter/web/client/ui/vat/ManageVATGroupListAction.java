package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ManageVATGroupListAction extends Action {
	private ManageVATGroupListView view;

	/**
	 * @param text
	 */
	public ManageVATGroupListAction(String text) {
		super(text);
		this.catagory = Accounter.constants().vat();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.Action#getBigImage()
	 */
	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.Action#getSmallImage()
	 */
	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().manageSalesTaxGroup();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.Action#getView()
	 */

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.Action#run(java.lang.Object,
	 * java.lang.Boolean)
	 */
	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new ManageVATGroupListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ManageVATGroupListAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Manage_Sales_Tax_Group.png";
	// }

	@Override
	public String getHistoryToken() {
		return "manageVATGroupList";
	}

}
