package com.vimukti.accounter.web.client.ui.core;

import java.util.List;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public abstract class BaseView<T extends IAccounterCore> extends
		AbstractBaseView<T> implements IEditableView {

	private ButtonBar buttonBar;

	private EditMode mode;
	// private boolean isInViewMode;

	protected SaveAndCloseButton saveAndCloseButton;

	protected SaveAndNewButtom saveAndNewButton;

	protected CancelButton cancelButton;

	protected ApproveButton approveButton;

	protected String quickAddText;

	public BaseView() {
		super();
	}

	protected abstract String getViewTitle();

	@Override
	public void init() {
		super.init();
		createView();
		createButtons(getButtonBar());
	}

	public static boolean checkIfNotNumber(String in) {
		try {
			Integer.parseInt(in);

		} catch (NumberFormatException ex) {
			return true;
		}
		return false;
	}

	private void createView() {

		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		setWidth("100%");
		setHeight("100%");

		buttonBar = new ButtonBar(this);
		buttonBar.setStyleName("button_bar");

		super.add(buttonBar);
		if (data != null)
			super.add(createHistoryView());
	}

	protected VerticalPanel createHistoryView() {
		return new VerticalPanel();
	}

	/**
	 * Return list of all DynamicForm items in this view
	 * 
	 * @return
	 */
	public abstract List<DynamicForm> getForms();

	@Override
	public void fitToSize(int height, int width) {
		// canvas.setHeight(height - 125 + "px");
		// canvas.setWidth(width - 15 + "px");
	}

	public ButtonBar getButtonBar() {
		return this.buttonBar;
	}

	/**
	 * This method will be called my all sub classes to add items to this view.
	 */
	public void add(Widget child) {
		int index = this.getWidgetIndex(buttonBar);
		// Insert widgets above button bar
		super.insert(child, index);
	}

	public void setData(T data) {
		super.setData(data);
		if (data == null || data.getID() == 0) {
			this.setMode(EditMode.CREATE);
		} else {
			this.setMode(EditMode.VIEW);
		}
	}

	protected void createButtons(ButtonBar buttonBar) {
		this.saveAndCloseButton = new SaveAndCloseButton(this);
		this.saveAndNewButton = new SaveAndNewButtom(this);
		this.cancelButton = new CancelButton(this);
		// if (this instanceof AbstractTransactionBaseView<?>) {
		// this.approveButton = new ApproveButton(
		// (AbstractTransactionBaseView<?>) this);
		// }
		if (!isInViewMode()) {
			buttonBar.add(saveAndCloseButton);
			buttonBar.add(saveAndNewButton);
			// if (this instanceof AbstractTransactionBaseView<?>) {
			// buttonBar.add(approveButton);
			// }
		}
		buttonBar.add(cancelButton);
	}

	@Override
	public boolean canEdit() {
		return getMode() == EditMode.VIEW;
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	public EditMode getMode() {
		return mode;
	}

	public void setMode(EditMode mode) {
		if (this.mode == mode) {
			return;
		}
		this.mode = mode;
		if (getManager() != null)
			getManager().updateButtons();
		if (!isInViewMode()) {
			showSaveButtons();
		}
	}

	public boolean isInViewMode() {
		return this.mode == EditMode.VIEW;
	}

	private void showSaveButtons() {
		// if (approveButton != null) {
		// this.buttonBar.insert(approveButton, 0);
		// }
		if (saveAndNewButton != null) {
			this.buttonBar.insert(saveAndNewButton, 0);
		}
		if (saveAndCloseButton != null) {
			this.buttonBar.insert(saveAndCloseButton, 0);
		}
	}

	public void prepareForQuickAdd(String text) {
		quickAddText = text;
	}

	@Override
	protected void changeButtonBarMode(boolean disable) {
		if (buttonBar != null) {
			buttonBar.setDisabled(disable);
		}
	}
	
	protected ClientCurrency getBaseCurrency() {
		return getCompany().getPrimaryCurrency();
	}

	protected ClientCurrency getCurrency(long currency) {
		return getCompany().getCurrency(currency);
	}

	/**
	 * Creates HistoryView
	 */

}
