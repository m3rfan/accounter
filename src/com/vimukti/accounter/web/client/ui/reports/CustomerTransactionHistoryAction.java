package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Mandeep Singh
 */

public class CustomerTransactionHistoryAction extends Action {

	protected CustomerTransactionHistoryReport report;

	public CustomerTransactionHistoryAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getReportsMessages().report();
	}

	public CustomerTransactionHistoryAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getReportsMessages().report();
	}

	public void runAsync(final Object data, final Boolean dependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					report = new CustomerTransactionHistoryReport();
					MainFinanceWindow.getViewManager().showView(report, data,
							dependent, CustomerTransactionHistoryAction.this);
				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Report...", t);
			}
		});

	}

	@Override
	public void run(Object data, Boolean dependent) {
		runAsync(data, dependent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.report;
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().reports();
	}

	@Override
	public String getImageUrl() {
		return "/images/reports.png";
	}

}
