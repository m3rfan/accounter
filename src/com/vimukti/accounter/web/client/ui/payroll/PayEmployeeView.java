package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientEmail;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientPayEmployee;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionPayEmployee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.BankAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.EmployeesAndGroupsCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class PayEmployeeView extends
		AbstractTransactionBaseView<ClientPayEmployee> {

	private EmployeesAndGroupsCombo employeeCombo;
	private BankAccountCombo bankAccountCombo;
	private PayEmployeeTable table;
	private List<DynamicForm> listforms;
	public double totalOrginalAmt = 0.0D, totalDueAmt = 0.0D,
			totalPayment = 0.0D;

	public PayEmployeeView() {
		super(ClientTransaction.TYPE_PAY_EMPLOYEE);
		this.getElement().setId("pay-employee-view");
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
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();
		StyledPanel mainPanel = new StyledPanel("main-panel");

		Label lab1 = new Label(messages.payEmployee());
		lab1.setStyleName("label-title");
		DynamicForm dateNoForm = new DynamicForm("dateNoForm");

		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		transactionNumber.setTitle(messages.no());
		dateNoForm.setStyleName("datenumber-panel");

		if (!isTemplate) {
			dateNoForm.add(transactionDateItem, transactionNumber);
		}
		employeeCombo = new EmployeesAndGroupsCombo(messages.employeeGroup(),
				"employee-group");
		employeeCombo.setRequired(true);
		employeeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPayStructureDestination>() {

					@Override
					public void selectedComboBoxItem(
							ClientPayStructureDestination selectItem) {
						employeeSelected(selectItem);
					}
				});

		bankAccountCombo = new BankAccountCombo(messages.payFrom());
		bankAccountCombo.setRequired(true);

		table = new PayEmployeeTable(isInViewMode(), this) {

			@Override
			protected void updateTotalPayment(ClientTransactionPayEmployee obj) {
			}

			@Override
			protected void updateFootervalues(ClientTransactionPayEmployee row,
					boolean canEdit) {
				PayEmployeeView.this.updateFooterValues();
			}

			@Override
			protected void resetTotlas() {
				PayEmployeeView.this.resetTotals();
			}

			@Override
			protected boolean isInViewMode() {
				return PayEmployeeView.this.isInViewMode();
			}

			@Override
			protected void deleteTotalPayment(ClientTransactionPayEmployee obj) {
				PayEmployeeView.this.deleteTotalPayment(obj);
			}

			@Override
			protected void adjustPaymentValue(
					ClientTransactionPayEmployee selectedObject) {
				PayEmployeeView.this.adjustPaymentValue(selectedObject);
			}

			@Override
			protected void adjustAmountAndEndingBalance() {
				PayEmployeeView.this.adjustAmountAndEndingBalance();
			}

		};
		DynamicForm mainForm = new DynamicForm("main-form");
		mainForm.add(employeeCombo, bankAccountCombo);
		mainPanel.add(dateNoForm);
		mainPanel.add(mainForm);
		mainPanel.add(table);

		listforms.add(dateNoForm);
		listforms.add(mainForm);

		this.add(mainPanel);
	}

	protected void employeeSelected(ClientPayStructureDestination selectItem) {
		table.clear();
		if (selectItem != null) {
			Accounter.createPayrollService().getTransactionPayEmployeeList(
					selectItem,
					new AsyncCallback<List<ClientTransactionPayEmployee>>() {

						@Override
						public void onSuccess(
								List<ClientTransactionPayEmployee> result) {
							setRecordsToTable(result);
						}

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}
					});
		}
	}

	protected void setRecordsToTable(List<ClientTransactionPayEmployee> result) {
		if (table != null && result != null) {
			table.setRecords(result);
		}
	}

	protected void adjustAmountAndEndingBalance() {
		// TODO Auto-generated method stub

	}

	protected void adjustPaymentValue(
			ClientTransactionPayEmployee selectedObject) {
		// TODO Auto-generated method stub

	}

	protected void deleteTotalPayment(ClientTransactionPayEmployee obj) {
		ClientTransaction transactionObject = getTransactionObject();
		transactionObject.setTotal(transactionObject.getTotal()
				- obj.getPayment());
	}

	protected void resetTotals() {
		// TODO Auto-generated method stub

	}

	protected void updateFooterValues() {
		totalOrginalAmt = 0.0D;
		totalDueAmt = 0.0D;
	}

	@Override
	protected void initTransactionViewData() {
		if (transaction != null) {
			long employee = data.getEmployee();
			if (employee == 0) {
				employee = data.getEmployeeGroup();
			}
			employeeCombo.setEmpGroup(employee, data.getEmployee() != 0);
			table.setAllRows(transaction.getPayRunComponents());
			bankAccountCombo.setComboItem(getCompany().getAccount(
					transaction.getPayAccount()));
			transactionNumber.setValue(transaction.getNumber());
			transactionDateItem.setValue(transaction.getDate());
		} else {
			transaction = new ClientPayEmployee();
		}
	}

	@Override
	public void updateNonEditableItems() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void refreshTransactionGrid() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAmountsFromGUI() {
		this.table.updateAmountsFromGUI();
	}

	@Override
	protected void classSelected(ClientAccounterClass clientAccounterClass) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.payEmployee();
	}

	@Override
	public List<DynamicForm> getForms() {
		return listforms;
	}

	@Override
	public void setFocus() {
		employeeCombo.setFocus();
	}

	@Override
	public void saveAndUpdateView() {
		super.saveAndUpdateView();
		transaction.setPayRunComponents(table.getSelectedRecords());
		if (employeeCombo.getSelectedValue() instanceof ClientEmployee) {
			transaction.setEmployee(employeeCombo.getSelectedValue().getID());
		} else {
			transaction.setEmployeeGroup(employeeCombo.getSelectedValue()
					.getID());
		}
		transaction.setPayAccount(bankAccountCombo.getSelectedValue().getID());
		transaction.setDate(getTransactionDate().getDate());
		transaction.setNumber(transactionNumber.getValue());
		transaction.setCurrency(getCompany().getPrimaryCurrency().getID());
		saveOrUpdate(transaction);
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (employeeCombo.getSelectedValue() == null
				|| employeeCombo.getSelectedValue().getName().trim().isEmpty()) {
			result.addError(employeeCombo,
					messages.pleaseSelect(messages.employeeGroup()));
		}
		if (bankAccountCombo.getSelectedValue() == null
				|| bankAccountCombo.getSelectedValue().getName().trim()
						.isEmpty()) {
			result.addError(bankAccountCombo,
					messages.pleaseSelect(messages.Account()));
		}
		if (table.getSelectedRecords().isEmpty()) {
			result.addError(table, messages.pleaseSelectAtLeastOneRecord());
		}
		return result;
	}

}