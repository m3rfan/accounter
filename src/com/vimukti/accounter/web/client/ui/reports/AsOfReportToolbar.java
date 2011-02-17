package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.forms.ComboBoxItem;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class AsOfReportToolbar extends ReportToolbar {

	private ComboBoxItem reportBasisItem;
	private ComboBoxItem dateRangeItem;
	private DateItem customDate;
	private Button updateButton;

	public AsOfReportToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.getReportsMessages().all();
	}

	private void createControls() {
		String[] reportBasisArray = {
				FinanceApplication.getReportsMessages().cash(),
				FinanceApplication.getReportsMessages().accrual() };
		String[] dateRangeArray = {
				FinanceApplication.getReportsMessages().all(),
				FinanceApplication.getReportsMessages().thisWeek(),
				FinanceApplication.getReportsMessages().thisMonth(),
				FinanceApplication.getReportsMessages().lastWeek(),
				FinanceApplication.getReportsMessages().lastMonth(),
				FinanceApplication.getReportsMessages().thisFinancialYear(),
				FinanceApplication.getReportsMessages().lastFinancialYear(),
				FinanceApplication.getReportsMessages().thisFinancialQuarter(),
				FinanceApplication.getReportsMessages().lastFinancialQuarter(),
				FinanceApplication.getReportsMessages().financialYearToDate(),

				// FinanceApplication.getReportsMessages().today(),
				// FinanceApplication.getReportsMessages().endThisWeek(),
				// FinanceApplication.getReportsMessages().endThisWeekToDate(),
				// FinanceApplication.getReportsMessages().endThisMonth(),
				// FinanceApplication.getReportsMessages().endThisMonthToDate(),
				// FinanceApplication.getReportsMessages().endThisFiscalQuarter(),
				// FinanceApplication.getReportsMessages()
				// .endThisFiscalQuarterToDate(),
				// FinanceApplication.getReportsMessages()
				// .endThisCalanderQuarter(),
				// FinanceApplication.getReportsMessages()
				// .endThisCalanderQuarterToDate(),
				// FinanceApplication.getReportsMessages().endThisFiscalYear(),
				// FinanceApplication.getReportsMessages()
				// .endThisFiscalYearToDate(),
				// FinanceApplication.getReportsMessages().endThisCalanderYear(),
				// FinanceApplication.getReportsMessages()
				// .endThisCalanderYearToDate(),
				// FinanceApplication.getReportsMessages().endYesterday(),
				// FinanceApplication.getReportsMessages()
				// .endPreviousFiscalQuarter(),
				// FinanceApplication.getReportsMessages()
				// .endLastCalenderQuarter(),
				// FinanceApplication.getReportsMessages()
				// .previousFiscalYearSameDates(),
				// FinanceApplication.getReportsMessages().lastCalenderYear(),
				// FinanceApplication.getReportsMessages().previousCalenderYear(),
				FinanceApplication.getReportsMessages().custom() };

		LabelItem report = new LabelItem();
		report.setTitle("Report Basis - Accrual");

		// reportBasisItem = new ComboBoxItem();
		// reportBasisItem.setTitle(FinanceApplication.getReportsMessages()
		// .reportBasis());
		// reportBasisItem.setValueMap(reportBasisArray);
		// reportBasisItem.setDefaultValue(reportBasisArray[1]);
		// // report basic is not yet implemented, so disable the feature.
		// reportBasisItem.setDisabled(true);

		dateRangeItem = new ComboBoxItem();
		dateRangeItem.setTitle(FinanceApplication.getReportsMessages()
				.dateRange());
		dateRangeItem.setValueMap(dateRangeArray);
		// dateRangeItem.setDefaultValue(dateRangeArray[0]);
		dateRangeItem.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// if (!dateRangeItem.getValue().toString().equals(
				// FinanceApplication.getReportsMessages().custom())) {
				dateRangeChanged(dateRangeItem.getValue().toString());
				// customDate.setDisabled(true);
				// updateButton.setEnabled(false);
				// } else {
				// customDate.setDisabled(false);
				// updateButton.setEnabled(true);
				// }

			}
		});
		customDate = new DateItem();
		// customDate.setUseTextField(true);
		// customDate.setWidth(100);
		customDate.setShowTitle(false);
		// customDate.setDisabled(true);
		customDate.setColSpan(2);
		customDate.addValueChangeHandler(new ValueChangeHandler<String>() {

			@SuppressWarnings("deprecation")
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				ClientFinanceDate date = (customDate.getValue());

				if (date != null) {
					if (!date.after(startDate))
						Accounter.showError(FinanceApplication
								.getReportsMessages()
								.pleaseSelectDateAfterCompanyStartDate()
								+ UIUtils.getDateStringByDate(startDate
										.toString()));
					else
						changeDates(startDate, date);
				} else {
					Accounter.showError(FinanceApplication.getReportsMessages()
							.pleaseSelectDate());
				}

			}

		});

		ClientFinanceDate date = Utility.getLastandOpenedFiscalYearEndDate();
		if (date != null)
			customDate.setValue(date);
		else
			customDate.setValue(new ClientFinanceDate());

		updateButton = new Button("Update");
		// updateButton.setEnabled(false);
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				// setStartDate(fromItem.getDate());
				setEndDate(customDate.getDate());

				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						startDate, customDate.getDate());
				dateRangeItem.setDefaultValue(FinanceApplication
						.getReportsMessages().custom());
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.custom());

			}
		});

		if (UIUtils.isMSIEBrowser()) {
			dateRangeItem.setWidth("200px");
		}
		addItems(report, dateRangeItem, customDate);
		add(updateButton);
		this.setCellVerticalAlignment(updateButton,
				HasVerticalAlignment.ALIGN_MIDDLE);
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		customDate.setValue(endDate);
		itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL, startDate,
				endDate);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeItem.setDefaultValue(defaultDateRange);
		dateRangeChanged(defaultDateRange);

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		if (endDate != null) {
			customDate.setEnteredDate(endDate);
			setStartDate(startDate);
			setEndDate(endDate);
		}

	}
}
