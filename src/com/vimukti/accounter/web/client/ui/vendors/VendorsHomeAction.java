package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author kumar kasimala
 */

public class VendorsHomeAction extends Action {

	private VendorSectionHomeView view;

	public VendorsHomeAction(String text) {
		super(text);
		this.catagory = UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().supplier(), FinanceApplication
				.getVendorsMessages().vendor());
	}

	public VendorsHomeAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().supplier(), FinanceApplication
				.getVendorsMessages().vendor());
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// // TODO Auto-generated method stub
		// return null;
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		view = new VendorSectionHomeView();
		try {
			MainFinanceWindow.getViewManager()
					.showView(view, null, false, this);
		} catch (Exception e) {
			Accounter.showError(UIUtils.getVendorString(
					"failedToLoadSuppliersHome", "failedToLoadVendorsHome"));
		}

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().vendorHome();
	}

	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/vendor_home.png";
	}
}
