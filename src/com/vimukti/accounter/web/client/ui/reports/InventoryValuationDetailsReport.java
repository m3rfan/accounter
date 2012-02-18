package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.core.ReportsGenerator;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.InventoryValutionDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.InventoryValuationDetailsServerReport;

public class InventoryValuationDetailsReport extends
		AbstractReportView<InventoryValutionDetail> {
	private final long id;

	public InventoryValuationDetailsReport(long id) {
		this.serverReport = new InventoryValuationDetailsServerReport(this);
		this.id = id;

	}

	@Override
	public void print() {
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())),
				REPORT_TYPE_INVENTORY_VALUTION_DETAIL, "", "", id);
	}

	@Override
	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())),
				REPORT_TYPE_INVENTORY_VALUTION_DETAIL, "", "", id);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(InventoryValutionDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransType(),
				record.getTransactionId());
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getInventoryValutionDetail(id, start,
				end, this);
	}

}
