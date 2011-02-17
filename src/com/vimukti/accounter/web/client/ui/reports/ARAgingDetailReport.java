package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;

/**
 * Modified By Ravi Kiran.G
 * 
 */
public class ARAgingDetailReport extends AbstractReportView<AgedDebtors> {
	private String sectionName = "";
	private List<String> types = new ArrayList<String>();
	private List<String> sectiontypes = new ArrayList<String>();
	private String curentParent;
	private int precategory = 1001;

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				// COLUMN_TYPE_DATE,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { FinanceApplication.getReportsMessages().name(),
				FinanceApplication.getReportsMessages().date(),
				FinanceApplication.getReportsMessages().type(),
				FinanceApplication.getReportsMessages().num(),
				FinanceApplication.getReportsMessages().ageing(),
				FinanceApplication.getReportsMessages().amount()
		// FinanceApplication.getReportsMessages().reference(),
		// FinanceApplication.getReportsMessages().Void(),
		// FinanceApplication.getReportsMessages().dueDate(),
		// FinanceApplication.getReportsMessages().total()
		};
	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages().ARAgeingDetails();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_AS_OF;
	}

	@Override
	protected int getColumnWidth(int index) {
		if (index == 2)
			return 160;
		else if (index == 1)
			return 70;
		else if (index == 3)
			return 70;
		else if (index == 0)
			return 200;
		else if (index == 4)
			return 100;
		else if (index == 5)
			return 150;
		else
			return -1;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		DummyDebitor byCustomerDetail = (DummyDebitor) this.data;

		if (byCustomerDetail == null) {
			FinanceApplication.createReportService().getAgedDebtors(
					start.getTime(), new ClientFinanceDate().getTime(), this);
		} else if (byCustomerDetail.getDebitorName() != null) {
			FinanceApplication.createReportService().getAgedDebtors(
					byCustomerDetail.getDebitorName(), start.getTime(),
					new ClientFinanceDate().getTime(), this);
		}
		sectiontypes.clear();
	}

	@Override
	public void OnRecordClick(AgedDebtors record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getType(), record
				.getTransactionId());
	}

	@Override
	public Object getColumnData(AgedDebtors record, int columnIndex) {
		switch (columnIndex) {
		case 2:
			return Utility.getTransactionName(record.getType());
		case 1:
			return UIUtils.getDateByCompanyType(record.getDate());
		case 3:
			return record.getNumber();
		case 0:
			return record.getName();
			// return record.getReference();
			// case 4:
			// return UIUtils.getDateByCompanyType(record.getDueDate());
			// return record.getIsVoid() ?
			// FinanceApplication.getReportsMessages()
			// .yes() : FinanceApplication.getReportsMessages().no();
		case 4:
			return record.getAgeing();
		case 5:
			return record.getTotal();
		}
		return null;
	}

	@Override
	public void processRecord(AgedDebtors record) {

		// if (sectionDepth == 0) {
		// // addSection(new String[] { "", "" }, new String[] { "", "", "",
		// // "",
		// // FinanceApplication.getReportsMessages().total() },
		// // new int[] { 5 });
		// } else
		if (precategory == 1001 || precategory == record.getCategory()) {
			if (addCategoryTypes(record)) {
				return;
			}
		} else if (precategory != 1001 && record.getCategory() != precategory) {
			precategory = record.getCategory();
			endSection();
		} else {
			return;
		}

		// if (record.getCategory() == 1) {
		// precategory = record.getCategory();
		// addCurrent(record);
		// }
		// if (sectionDepth == 0) {
		// addSection("", FinanceApplication.getReportsMessages().total(),
		// new int[] { 6,7 });
		// } else if (sectionDepth == 1) {
		// this.sectionName = record.getName();
		// addSection(sectionName, "", new int[] { 6,7 });
		// } else if (sectionDepth == 2) {
		// // No need to do anything, just allow adding this record
		// if (!sectionName.equals(record.getName())) {
		// endSection();
		// endSection();
		// } else {
		// return;
		// }
		// }
		// // Go on recursive calling if we reached this place
		processRecord(record);
	}

	//
	// @Override
	// protected void endAllSections() {
	// for (int i = this.sections.size() - 1; i >= 0; i--) {
	// if (i == 1) {
	// sections.get(0).isaddFooter = true;
	// endSection();
	// } else
	// endSection();
	// }
	// }

	private boolean addCategoryTypes(AgedDebtors record) {

		if (record.getCategory() == 1) {
			precategory = record.getCategory();
			return addOneTothirty(record);
		} else if (record.getCategory() == 2) {
			precategory = record.getCategory();
			return addThirtyToSixty(record);
		} else if (record.getCategory() == 3) {
			precategory = record.getCategory();
			return addSixtyTo90(record);
		} else if (record.getCategory() == 4) {
			precategory = record.getCategory();
			return addGreaterThan90(record);
		} else if (record.getCategory() == 5) {
			precategory = record.getCategory();
			return addTotalBalance(record);
		}
		return true;
	}

	private void addCurrent(AgedDebtors record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.current())) {
			addTypeSection(FinanceApplication.getReportsMessages().current(),
					"");
		}
	}

	private boolean addOneTothirty(AgedDebtors record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.days30())) {
			addTypeSection(FinanceApplication.getReportsMessages().days30(), "");
			return false;
		}
		return true;
	}

	private boolean addThirtyToSixty(AgedDebtors record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.days60())) {
			addTypeSection(FinanceApplication.getReportsMessages().days60(), "");
			return false;
		}
		return true;

	}

	private boolean addSixtyTo90(AgedDebtors record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.days90())) {
			addTypeSection(FinanceApplication.getReportsMessages().days90(), "");
			return false;
		}
		return true;

	}

	private boolean addGreaterThan90(AgedDebtors record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.older())) {
			addTypeSection(FinanceApplication.getReportsMessages().older(), "");
			return false;
		}
		return true;

	}

	private boolean addTotalBalance(AgedDebtors record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.totalBalance())) {
			addTypeSection(FinanceApplication.getReportsMessages()
					.totalBalance(), "");
			return false;
		}
		return true;
	}

	/**
	 * add Type Section
	 * 
	 * @param title
	 */
	public void addTypeSection(String title, String bottomTitle) {
		if (!sectiontypes.contains(title)) {
			addSection(new String[] { title }, new String[] { "", "", "", "",
					FinanceApplication.getReportsMessages().total() },
					new int[] { 5 });
			types.add(title);
			sectiontypes.add(title);
		}
	}

	public void closeAllSection() {
		for (int i = types.size() - 1; i > 0; i--) {
			closeSection(i);
		}
	}

	public void closeOtherSections() {
		for (int i = types.size() - 1; i > 0; i--) {
			closePrevSection(types.get(i));
		}
	}

	public boolean closePrevSection(String title) {
		if (curentParent != null && curentParent != "")
			if (!title.equals(curentParent)) {
				if (!sectiontypes.contains(curentParent)) {
					types.remove(types.size() - 1);
					if (types.size() > 0) {
						curentParent = types.get(types.size() - 1);
					}
					endSection();
					return true;
				}
			}
		return false;
	}

	public void closeSection(int index) {
		types.remove(index);
		curentParent = "";
		endSection();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {

		if (UIUtils.isMSIEBrowser()) {
			printDataForIEBrowser();
		} else
			printDataForOtherBrowser();
	}

	private void printDataForOtherBrowser() {
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();

		gridhtml = gridhtml.replaceAll(headerhtml, "");
		gridhtml = gridhtml.replaceAll(grid.getFooter(), "");

		headerhtml = headerhtml.replaceAll("td", "th");
		headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
				headerhtml.indexOf("</tbody>"));

		String firsRow = "<tr class=\"ReportGridRow\">"
				+ grid.rowFormatter.getElement(0).getInnerHTML() + "</tr>";
		headerhtml = headerhtml + firsRow;

		gridhtml = gridhtml.replace(firsRow, headerhtml);
		gridhtml = gridhtml.replaceAll("<tbody>", "");
		gridhtml = gridhtml.replaceAll("</tbody>", "");

		String dateRangeHtml = null;

		UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	private void printDataForIEBrowser() {
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();
		String footerHtml = grid.getFooter();

		gridhtml = gridhtml.replaceAll("\r\n", "");
		headerhtml = headerhtml.replaceAll("\r\n", "");
		footerHtml = footerHtml.replaceAll("\r\n", "");

		gridhtml = gridhtml.replaceAll(headerhtml, "");
		gridhtml = gridhtml.replaceAll(footerHtml, "");

		headerhtml = headerhtml.replaceAll("TD", "TH");
		headerhtml = headerhtml.substring(headerhtml.indexOf("<TR "),
				headerhtml.indexOf("</TBODY>"));

		String firsRow = "<TR class=ReportGridRow>"
				+ grid.rowFormatter.getElement(0).getInnerHTML() + "</TR>";
		firsRow = firsRow.replaceAll("\r\n", "");
		headerhtml = headerhtml + firsRow;

		gridhtml = gridhtml.replace(firsRow, headerhtml);
		gridhtml = gridhtml.replaceAll("<TBODY>", "");
		gridhtml = gridhtml.replaceAll("</TBODY>", "");

		String dateRangeHtml = null;

		UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientFinanceDate getEndDate(AgedDebtors obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(AgedDebtors obj) {
		return obj.getStartDate();
	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((BaseReport) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((BaseReport) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((BaseReport) object).getEndDate();
	}

	@Override
	public AgedDebtors getObject(AgedDebtors parent, AgedDebtors child) {
		// TODO Auto-generated method stub
		return super.getObject(parent, child);
	}

	@Override
	public int sort(AgedDebtors obj1, AgedDebtors obj2, int col) {

		int ret = UIUtils.compareInt(obj1.getCategory(), obj2.getCategory());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 2:
			return UIUtils.compareInt(obj1.getType(), obj2.getType());

		case 1:
			return UIUtils.compareTo(obj1.getDate(), obj2.getDate());

		case 3:
			int num1 = UIUtils.isInteger(obj1.getNumber())?Integer.parseInt(obj1.getNumber()):0;
			int num2 = UIUtils.isInteger(obj2.getNumber())?Integer.parseInt(obj2.getNumber()):0;
			if (num1!=0 && num2!=0)
				return UIUtils.compareInt(num1, num2);
			else
			return UIUtils.compareTo(obj1.getNumber(), obj2.getNumber());


		case 0:
			return obj1.getName().toLowerCase().compareTo(
					obj2.getName().toLowerCase());

			// case 4:
			// return obj1.getDueDate().compareTo(obj2.getDueDate());

		case 4:
			return UIUtils.compareDouble(obj1.getAgeing(), obj2.getAgeing());

		case 5:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		sectionDepth = 0;
		sectionName = "";
		precategory = 1001;
		types.clear();
		sectiontypes.clear();
		curentParent = "";
		super.resetVariables();
	}

	@Override
	protected String getDefaultDateRange() {
		return FinanceApplication.getReportsMessages().all();
	}
}
