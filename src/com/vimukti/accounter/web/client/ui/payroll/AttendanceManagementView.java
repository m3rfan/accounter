package com.vimukti.accounter.web.client.ui.payroll;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAttendanceManagement;
import com.vimukti.accounter.web.client.core.ClientAttendanceManagementItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class AttendanceManagementView extends
		BaseView<ClientAttendanceManagement> {

	AttendanceManagementTable table;
	private AddNewButton itemTableButton;

	public AttendanceManagementView() {
		this.getElement().setId("AttendanceManagementView");
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	private void createControls() {
		Label lab1 = new Label(messages.attendanceManagement());
		lab1.setStyleName("label-title");

		table = new AttendanceManagementTable();
		table.setEnabled(!isInViewMode());

		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab1);
		mainVLay.add(table);
		mainVLay.add(itemTableButton);

		this.add(mainVLay);
	}

	protected void addItem() {
		ClientAttendanceManagementItem transactionItem = new ClientAttendanceManagementItem();
		table.add(transactionItem);

	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientAttendanceManagement());
		} else {
			initViewData(getData());
		}
		super.initData();
	}

	private void initViewData(ClientAttendanceManagement data) {

		table.setAllRows(data.getItems());
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.attendanceManagement();
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveAndUpdateView() {
		updateData();
	}

	private void updateData() {
		for (ClientAttendanceManagementItem row : table.getAllRows()) {
			row.setNumber(row.getCurrBal());
		}
		data.setItems(table.getAllRows());
	}

}
