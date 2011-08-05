package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class AddMeasurementView extends BaseView {

	private TextItem nameItem, description;
	private SelectCombo defaultItem;
	private AddUnitsGrid addUnitsGrid;
	private DynamicForm addMeasurmentForm, defaultForm;
	private AccounterConstants settingsMessages = Accounter.constants();
	private ClientMeasurement measurment;
	private List defaultList;
	private AddUnitsListGridData addUnitsListGridData;

	public AddMeasurementView() {
		// init();
	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(ViewManager manager) {
		super.init(manager);
		createControls();

	}

	private void createControls() {
		VerticalPanel panel = new VerticalPanel();
		defaultList = new ArrayList();
		panel.setSpacing(10);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		addMeasurmentForm = new DynamicForm();
		defaultForm = new DynamicForm();
		initGrid();
		nameItem = new TextItem(settingsMessages.measurementName());
		nameItem.setRequired(true);
		description = new TextItem(settingsMessages.measurementDescription());
		AccounterButton addUnitButton = new AccounterButton();
		addUnitButton.setText(settingsMessages.getAddUnitButton());
		addUnitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ClientUnit unitData = new ClientUnit();
				addUnitsGrid.setDisabled(false);
				addUnitsGrid.addData(unitData);
			}
		});
		defaultItem = new SelectCombo(settingsMessages.getdefaultUnit());
		defaultItem.setDisabled(false);
		defaultItem.setTitleStyleName(settingsMessages.getdefaultUnit());
		// AccounterButton saveButton = new AccounterButton();
		// saveButton.setText(settingsMessages.getAddMesurementSaveButton());
		// saveButton.addClickHandler(new ClickHandler() {
		// @Override
		// public void onClick(ClickEvent event) {
		//
		// }
		// });
		addMeasurmentForm.setFields(nameItem, description);
		// AccounterButton cancelButton = new AccounterButton();
		// cancelButton.setText(settingsMessages.getCancelButton());
		panel.add(addMeasurmentForm);
		panel.add(addUnitsGrid);
		horizontalPanel.setCellHorizontalAlignment(addUnitButton, ALIGN_RIGHT);
		panel.add(addUnitButton);
		defaultForm.setFields(defaultItem);
		defaultItem
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (defaultItem.getSelectedValue() != null)
							defaultItem.setComboItem(defaultItem
									.getSelectedValue());
					}
				});
		defaultItem.setRequired(true);
		defaultItem.setDisabled(isEdit);
		panel.add(defaultForm);
		// horizontalPanel.add(saveButton);
		// horizontalPanel.setSpacing(20);
		// horizontalPanel.add(cancelButton);
		// panel.add(horizontalPanel);
		this.add(panel);
	}

	private void initGrid() {
		addUnitsGrid = new AddUnitsGrid(false);
		addUnitsGrid.setDisabled(true);
		addUnitsGrid.setCanEdit(true);
		addUnitsGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		addUnitsGrid.init();
		addUnitsGrid.setView(this);
	}

	public String getName() {
		return nameItem.getValueField();
	}

	public String getDescription() {
		return description.getValueField();
	}

	public void setDefaultComboValue(ClientUnit unitData) {
		defaultList.add(unitData);
		if (((ClientUnit) (defaultList.get(0))).getType().equals(
				unitData.getType())) {
			defaultItem.setComboItem(unitData.getType());
			return;
		}
		defaultItem.addComboItem(unitData.getType());
	}

	private void addSelectedItemsToList() {
		measurment = new ClientMeasurement();
		measurment.setName(nameItem.getValue().toString());
		measurment.setDesctiption(description.getValue().toString());
		for (Object iterable_element : defaultList) {
			ClientUnit unit = (ClientUnit) iterable_element;
			measurment.addUnit(unit.getType(), unit.getFactor());
		}
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (nameItem.getValue().toString() == null
				|| nameItem.getValue().toString().isEmpty()) {
			result.addError(nameItem, Accounter.constants()
					.pleaseEnteraValidMeasurementName());
		}
		if (addUnitsGrid.getRecords().isEmpty()) {
			result.addError(addUnitsGrid, Accounter.constants()
					.unitsMustnotbeNull());
		}
		return result;
	}

	@Override
	public void saveAndUpdateView() {
		addSelectedItemsToList();
		super.saveAndUpdateView();
		MainFinanceWindow.getViewManager().createObject(measurment, this);
	}
}
