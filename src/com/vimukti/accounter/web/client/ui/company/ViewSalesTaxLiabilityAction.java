package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class ViewSalesTaxLiabilityAction extends Action {

	public ViewSalesTaxLiabilityAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public ViewSalesTaxLiabilityAction(String text, String iconString) {
		super(text, iconString);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ParentCanvas<?> getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		// TODO Auto-generated method stub

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().reports();
	}
@Override
public String getImageUrl() {
	// TODO Auto-generated method stub
	return "/images/reports.png";
}
}
