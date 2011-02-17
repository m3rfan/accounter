package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.AddEditSalesTaxCodeView;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class AddEditSalesTaxCodeAction extends Action {

	protected AddEditSalesTaxCodeView view;
	private String title;

	public AddEditSalesTaxCodeAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getCompanyMessages().company();
		title = FinanceApplication.getCompanyMessages().taxCode();
	}

	public AddEditSalesTaxCodeAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getCompanyMessages().company();
		title = FinanceApplication.getCompanyMessages().taxCode();
	}

	@Override
	public ParentCanvas<?> getView() {
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {

		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load "+title+" Code Dialog",
				// t);

			}

			public void onCreated() {
				try {
					view = new AddEditSalesTaxCodeView(title);
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, AddEditSalesTaxCodeAction.this);
				} catch (Throwable e) {
					onCreateFailed(e);

				}

			}
		});

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

}
