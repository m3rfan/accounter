package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class AdjustTAXAction extends Action {
	protected AdjustTAXView view;
	private ClientTAXAgency vatAgency;

	public AdjustTAXAction(String text) {
		super(text);
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			this.catagory = Accounter.constants().company();
		else
			this.catagory = Accounter.constants().vat();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vatAdjustment();
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				if (isDependent) {
					view = new AdjustTAXView(vatAgency);
					MainFinanceWindow.getViewManager().showView(view, null,
							isDependent, AdjustTAXAction.this);
				} else {
					view = new AdjustTAXView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, AdjustTAXAction.this);
				}

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	public void setVatAgency(ClientTAXAgency selectedVatAgency) {
		this.vatAgency = selectedVatAgency;
	}

	@Override
	public String getHistoryToken() {
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			return "vatAdjustment";
		else
			return "taxAdjustment";
	}

}
