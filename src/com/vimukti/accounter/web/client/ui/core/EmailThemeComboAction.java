package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.customers.EmailThemeComboDialog;

public class EmailThemeComboAction extends Action {

	public EmailThemeComboAction() {
		super();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	public void run() {

		runAsync(data, isDependent);

	}

	private void runAsync(Object data, Boolean isDependent) {
		EmailThemeComboDialog comboDialog = new EmailThemeComboDialog(Accounter
				.messages().selectThemes(), "", (ClientTransaction) data);
		comboDialog.show();
		comboDialog.center();
	}

	@Override
	public String getHistoryToken() {
		return "BrandingThemeCombo";
	}

	@Override
	public String getHelpToken() {
		return "new-branding-theme";
	}

	@Override
	public String getText() {
		return messages.brandingThemeCombo();
	}

}