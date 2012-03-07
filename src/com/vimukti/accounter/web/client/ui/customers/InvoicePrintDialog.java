package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

public class InvoicePrintDialog extends BaseDialog {
	private String description;

	public InvoicePrintDialog(String title, String desc, String description) {
		super(title, desc);
		this.description = description;
		createControls();
	}

	private void createControls() {

		Label label = new Label(description);

		label.setWidth("100%");
		label.addStyleName("invoice_print_dialog");

		StyledPanel comboPanel = new StyledPanel("comboPanel");
		comboPanel.add(label);
		cancelBtn.setVisible(false);

		setBodyLayout(comboPanel);
	}

	@Override
	protected boolean onOK() {
		return true;
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
