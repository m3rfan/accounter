package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

@SuppressWarnings("unchecked")
public class ManageCountryRegionDialog extends GroupDialog {
	protected GroupDialogButtonsHandler groupDialogButtonHandler;

	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);

	public ManageCountryRegionDialog(String title, String description) {
		super(title, description);
		initialise();
		center();
	}// end of constructor

	public void initialise() {

		getGrid().addColumn(ListGrid.COLUMN_TYPE_TEXT,
				companyConstants.countryRegion());
		// ListGridField country_or_RegionField=new ListGridField("country",);
		// country_or_RegionField.setWidth(220);
		// addField(country_or_RegionField);
		setSize("40%", "40%");

		groupDialogButtonHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {
				// TODO Auto-generated method stub

			}

			public void onFirstButtonClick() {
				// TODO Auto-generated method stub
				new CountryRegionDialog(FinanceApplication.getCompanyMessages()
						.addCountry(), FinanceApplication.getCompanyMessages()
						.enterNameOfCountry()).show();

			}

			public void onSecondButtonClick() {
				// TODO Auto-generated method stub
				new CountryRegionDialog(FinanceApplication.getCompanyMessages()
						.editCountry(), FinanceApplication.getCompanyMessages()
						.enterNameOfCountry()).show();

			}

			public void onThirdButtonClick() {
				// TODO Auto-generated method stub

			}
		};
		addGroupButtonsHandler(groupDialogButtonHandler);
	}// end of initialise

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] setColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List getRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteCallBack() {
	}

	public void addCallBack() {
	}

	public void editCallBack() {
	}

}// end of ManageCountryRegionDialog
