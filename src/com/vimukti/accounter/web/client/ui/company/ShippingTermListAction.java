package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.ShippingTermListDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

/**
 * 
 * @author Raj Vimal
 */

public class ShippingTermListAction extends Action {

	public ShippingTermListAction(String text) {
		super(text);
	}

	public ShippingTermListAction(String text, String iconString) {
		super(text, iconString);
	}

	@Override
	public ParentCanvas<?> getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Shipping Terms", t);

			}

			public void onCreated() {
				try {

					ShippingTermListDialog dialog = new ShippingTermListDialog(
							FinanceApplication.getCompanyMessages()
									.manageShippingTermList(),
							FinanceApplication.getCompanyMessages()
									.toAddShippingTerm());
					ViewManager viewManager = ViewManager.getInstance();
					viewManager.setCurrentDialog(dialog);
					// dialog.addCallBack(getViewConfiguration().getCallback());
					dialog.show();

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
		return FinanceApplication.getFinanceMenuImages().shippingTermList();
	}

	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/Shipping_term_icon.png";
	}
}
