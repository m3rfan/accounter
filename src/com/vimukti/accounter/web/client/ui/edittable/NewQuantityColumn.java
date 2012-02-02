package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.WarehouseCombo;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.settings.UnitCombo;

public class NewQuantityColumn extends TextEditColumn<ClientTransactionItem> {

	PopupPanel popup = new PopupPanel(true);

	@Override
	public int getWidth() {
		return 80;
	}

	protected String getValue(ClientTransactionItem row) {
		ClientItem item = Accounter.getCompany().getItem(row.getItem());
		if (item == null) {
			ClientQuantity quantity = row.getQuantity();
			return quantity != null ? String.valueOf(quantity.getValue()) : "";
		} else {
			if (item.getType() == ClientItem.TYPE_INVENTORY_PART) {
				ClientUnit unit = Accounter.getCompany().getUnitById(
						row.getQuantity().getUnit());
				StringBuffer data = new StringBuffer();
				data.append(String.valueOf(row.getQuantity().getValue()));
				data.append(" ");
				if (unit != null) {
					data.append(unit.getType());
				}
				if (getPreferences().iswareHouseEnabled()) {
					data.append(" (W: ");
					ClientWarehouse warehouse = Accounter.getCompany()
							.getWarehouse(row.getWareHouse());
					if (warehouse != null)
						data.append(warehouse.getWarehouseCode());
					data.append(")");
				}

				return data.toString();
			} else {
				ClientQuantity value = row.getQuantity();
				if (value != null)
					return String.valueOf(value.getValue());
				else
					return "";
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public IsWidget getWidget(RenderContext<ClientTransactionItem> context) {
		final IsWidget widget = super.getWidget(context);
		final ClientTransactionItem row = context.getRow();
		// Checking the units and wareHouses is enabled or not.
		if (widget instanceof TextBox && getPreferences().iswareHouseEnabled()
				|| getPreferences().isUnitsEnabled()) {
			((TextBox) widget).addFocusListener(new FocusListener() {
				@Override
				public void onFocus(Widget sender) {
					ClientItem item = Accounter.getCompany().getItem(
							row.getItem());
					if (item != null
							&& item.getType() == ClientItem.TYPE_INVENTORY_PART) {
						showPopUp(row);
						((TextBox) widget).setFocus(false);
					}
				}

				@Override
				public void onLostFocus(Widget arg0) {
					// TODO Auto-generated method stub
				}
			});
		} else if (widget instanceof TextBox) {
			((TextBox) widget).addBlurHandler(new BlurHandler() {

				@Override
				public void onBlur(BlurEvent arg0) {
					ClientItem item = Accounter.getCompany().getItem(
							row.getItem());
					if (item != null
							&& item.getType() == ClientItem.TYPE_INVENTORY_PART) {
						ClientCompany company = Accounter.getCompany();
						ClientWarehouse warehouse = company
								.getWarehouse(company.getDefaultWarehouse());
						ClientUnit defaultUnit = company.getMeasurement(
								item.getMeasurement()).getDefaultUnit();
						String value = ((TextBox) widget).getValue();
						ClientQuantity quantity = row.getQuantity();
						quantity.setUnit(defaultUnit.getId());
						try {
							quantity.setValue(DataUtils
									.getAmountStringAsDouble(value));
						} catch (Exception e) {
							e.printStackTrace();
						}
						setQuantity(row, quantity);
						row.setWareHouse(warehouse.getID());
					}
				}
			});
		}
		return widget;
	}

	private void showPopUp(final ClientTransactionItem row) {
		if (popup.isShowing()) {
			return;
		}
		popup.clear();
		FlexTable table = new FlexTable();
		Label valueLabel = new Label(messages.value());
		Label unitLabel = new Label(messages.unit());
		Label wareHouseLabel = new Label(messages.wareHouse());

		ClientUnit unit = Accounter.getCompany().getUnitById(
				row.getQuantity().getUnit());
		ClientWarehouse wareHouse = Accounter.getCompany().getWarehouse(
				row.getWareHouse());
		ClientItem item = Accounter.getCompany().getItem(row.getItem());
		final TextBox valueBox = new TextBox();
		valueBox.setFocus(true);
		valueBox.setText(String.valueOf(row.getQuantity().getValue()));
		final UnitCombo unitBox = new UnitCombo("");
		if (item != null) {
			ClientMeasurement measurement = Accounter.getCompany()
					.getMeasurement(item.getMeasurement());
			unitBox.initCombo(measurement.getUnits());
		}
		unitBox.setSelected(unit.getName());
		unitBox.setShowTitle(false);
		final WarehouseCombo whCombo = new WarehouseCombo("") {
			@Override
			public void onAddNew() {
				// For hide pop up quantity dialog.
				if (popup.isShowing()) {
					popup.hide();
				}
				super.onAddNew();
			}
		};
		whCombo.setComboItem(wareHouse);
		whCombo.setShowTitle(false);
		whCombo.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientWarehouse>() {

			@Override
			public void selectedComboBoxItem(ClientWarehouse selectItem) {
				if (!popup.isShowing()) {
					if (selectItem != null) {
						row.setWareHouse(selectItem.getID());
						getTable().update(row);
					}
				}
			}
		});

		table.setWidget(0, 0, valueLabel);
		table.setWidget(1, 0, valueBox);
		if (getPreferences().isUnitsEnabled()) {
			table.setWidget(0, 1, unitLabel);
			table.setWidget(1, 1, unitBox.getMainWidget());
			table.getCellFormatter().addStyleName(1, 1, "quantity_unit_width");
		}
		if (getPreferences().iswareHouseEnabled()) {
			table.setWidget(0, 2, wareHouseLabel);
			table.setWidget(1, 2, whCombo.getMainWidget());
		}
		popup.add(table);
		popup.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {

				String value = valueBox.getValue();
				ClientUnit unit = unitBox.getSelectedValue();
				if (value.isEmpty()) {
					value = "1";
				}
				ClientQuantity quantity = row.getQuantity();
				try {
					quantity.setValue(DataUtils.getAmountStringAsDouble(value));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (unit != null)
					quantity.setUnit(unit.getId());
				setQuantity(row, quantity);
				if (whCombo.getSelectedValue() != null)
					row.setWareHouse(whCombo.getSelectedValue().getID());
				getTable().update(row);
			}
		});
		Widget widget = getTable().getWidget(row, this);
		popup.setAutoHideEnabled(true);
		popup.addStyleName("quantityPopup");
		popup.showRelativeTo(widget);
	}

	protected void setQuantity(ClientTransactionItem row,
			ClientQuantity quantity) {
		if (quantity != null) {
			row.setQuantity(quantity);
			double lt = quantity.getValue() * row.getUnitPrice();
			double disc = row.getDiscount();
			row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
					* disc / 100)) : lt);
			getTable().update(row);
		}
	}

	@Override
	protected String getColumnName() {
		return messages.quantity();
	}

	@Override
	protected void setValue(ClientTransactionItem row, String value) {
		try {
			if (value.isEmpty()) {
				value = "1";
			}
			ClientItem item = Accounter.getCompany().getItem(row.getItem());
			if (item != null
					&& item.getType() == ClientItem.TYPE_INVENTORY_PART) {
				return;
			}
			ClientQuantity quantity = row.getQuantity();
			if (quantity != null) {
				quantity.setValue(DataUtils.getAmountStringAsDouble(value));
			}
			setQuantity(row, quantity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
