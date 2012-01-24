package com.vimukti.accounter.core;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Set;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.reports.BudgetOverviewServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.APAgingDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.APAgingSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ARAgingDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ARAgingSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.AbstractFinaneReport;
import com.vimukti.accounter.web.client.ui.serverreports.AmountsDueToVendorServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.BalanceSheetServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.CashFlowStatementServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.CustomerTransactionHistoryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.DepreciationSheduleServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ECSalesListDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ECSalesListServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ExpenseServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.MISC1099TransactionDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.MostProfitableCustomerServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.PriorVATReturnsServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ProfitAndLossServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseByItemDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseByItemSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseByVendorDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseByVendorSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseClosedOrderServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseOpenOrderServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.RealisedExchangeLossesAndGainsServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReconcilationDetailsByAccountServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReconcilationsServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.ReverseChargeListDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReverseChargeListServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByCustomerDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByCustomerSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByItemDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByItemSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByLocationDetailsServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByLocationsummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesClosedOrderServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesOpenOrderServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesTaxLiabilityServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.StatementServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.TAXItemDetailServerReportView;
import com.vimukti.accounter.web.client.ui.serverreports.TAXItemExceptionDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.TransactionDetailByAccountServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.TransactionDetailByTaxItemServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.TrialBalanceServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.VAT100ServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.VATDetailServerReportView;
import com.vimukti.accounter.web.client.ui.serverreports.VATExceptionServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.VATItemDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.VATItemSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.VATUncategorisedAmountsServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.VendorTransactionHistoryServerReport;
import com.vimukti.accounter.web.server.AccounterReportServiceImpl;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.managers.SalesManager;

public class ReportsGenerator {

	public final static int REPORT_TYPE_PROFITANDLOSS = 111;
	public final static int REPORT_TYPE_BALANCESHEET = 112;
	public final static int REPORT_TYPE_TRIALBALANCE = 113;
	public final static int REPORT_TYPE_TRANSACTIONDETAILBYTAXITEM = 114;
	// public final static int REPORT_TYPE_TRANSACTIONDETAILBYTAXCODE = 114;
	public final static int REPORT_TYPE_TRANSACTIONDETAILBYACCOUNT = 115;
	public final static int REPORT_TYPE_EXPENSE = 116;
	public final static int REPORT_TYPE_AR_AGEINGSUMMARY = 117;
	public final static int REPORT_TYPE_AR_AGEINGDETAIL = 118;
	public final static int REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY = 119;
	public final static int REPORT_TYPE_SALESBYCUSTOMERSUMMARY = 121;
	public final static int REPORT_TYPE_SALESBYCUSTOMERDETAIL = 122;
	public final static int REPORT_TYPE_SALESBYITEMSUMMARY = 123;
	public final static int REPORT_TYPE_SALESBYITEMDETAIL = 124;
	public final static int REPORT_TYPE_SALESORDER_OPEN = 125;
	public final static int REPORT_TYPE_SALESORDER_CLOSE = 126;
	public final static int REPORT_TYPE_AP_AGEINGSUMMARY = 127;
	public final static int REPORT_TYPE_AP_AGEINGDETAIL = 128;
	public final static int REPORT_TYPE_VENDORTRANSACTIONHISTORY = 129;
	public final static int REPORT_TYPE_PURCHASEBYVENDORSUMMARY = 130;
	public final static int REPORT_TYPE_PURCHASEBYVENDORDETAIL = 131;
	public final static int REPORT_TYPE_PURCHASEBYITEMSUMMARY = 132;
	public final static int REPORT_TYPE_PURCHASEBYITEMDETAIL = 133;
	public final static int REPORT_TYPE_PURCHASEORDER_OPEN = 134;
	public final static int REPORT_TYPE_PURCHASEORDER_CLOSE = 135;
	public final static int REPORT_TYPE_PRIORVATRETURNS = 136;
	public final static int REPORT_TYPE_VAT100 = 137;
	public final static int REPORT_TYPE_VATDETAIL = 138;
	public final static int REPORT_TYPE_VATITEMDETAIL = 139;
	public final static int REPORT_TYPE_UNCATEGORISEDVATAMOUNT = 140;
	public final static int REPORT_TYPE_VATITEMSUMMARY = 141;
	public final static int REPORT_TYPE_ECSCALESLIST = 142;
	public final static int REPORT_TYPE_ECSCALESLISTDETAIL = 143;
	public final static int REPORT_TYPE_SALESTAXLIABILITY = 144;
	public final static int REPORT_TYPE_REVERSECHARGELIST = 145;
	public final static int REPORT_TYPE_REVERSECHARGELISTDETAIL = 146;
	public final static int REPORT_TYPE_MOSTPROFITABLECUSTOMER = 147;
	public final static int REPORT_TYPE_CASHFLOWSTATEMENT = 148;
	public final static int REPORT_TYPE_AMOUNTSDUETOVENDOR = 149;
	public final static int REPORT_TYPE_CUSTOMERSTATEMENT = 150;
	public final static int REPORT_TYPE_SALESBYLOCATIONDETAIL = 151;
	public final static int REPORT_TYPE_SALESBYLOCATIONDETAILFORLOCATION = 152;
	public final static int REPORT_TYPE_PROFITANDLOSSBYLOCATION = 153;
	public final static int REPORT_TYPE_BUDGET = 154;
	private static final int REPORT_TYPE_1099TRANSACTIONDETAIL = 158;
	public final static int REPORT_TYPE_SALESBYCLASSDETAIL = 159;
	public final static int REPORT_TYPE_SALESBYCLASSDETAILFORCLASS = 160;
	public final static int REPORT_TYPE_PROFITANDLOSSBYCLASS = 161;
	public final static int REPORT_TYPE_GENERAL_LEDGER_REPORT = 162;
	public final static int REPORT_TYPE_TAX_ITEM_DETAIL = 165;
	public final static int REPORT_TYPE_VAT_EXCEPTION_DETAIL = 166;
	public final static int REPORT_TYPE_VENDORSTATEMENT = 167;
	public final static int REPORT_TYPE_DEPRECIATIONSHEDULE = 168;
	public final static int REPORT_TYPE_RECONCILATIONS = 169;
	public final static int REPORT_TYPE_RECONCILATION_ACCOUNTSLIST = 170;
	public final static int REPORT_TYPE_TAX_EXCEPTION_DETAIL = 171;
	public final static int REALISED_EXCHANGE_LOSSES_AND_GAINS = 172;
	public final static int UNREALISED_EXCHANGE_LOSSES_AND_GAINS = 173;

	// private static int companyType;
	private final ClientCompanyPreferences preferences = Global.get()
			.preferences();

	private final int reportType;
	private final FinanceDate startDate;
	private final FinanceDate endDate;
	private final String navigateObjectName;
	private static String status;
	private int boxNo;
	private long vendorId;
	private static Company company;
	private String dateRangeHtml;
	private int generationType;

	public static final int GENERATIONTYPEPDF = 1001;
	public static final int GENERATIONTYPECSV = 1002;

	public ReportsGenerator(int reportType, long starDate, long endDate,
			String navigateObjectName, int generationType, Company company,
			String dateRangeHtml) {
		// ReportsGenerator.companyType = companyType;
		this.reportType = reportType;
		this.startDate = new FinanceDate(starDate);
		this.endDate = new FinanceDate(endDate);
		this.navigateObjectName = navigateObjectName;
		this.dateRangeHtml = dateRangeHtml;
		this.company = company;
		this.generationType = generationType;
	}

	public ReportsGenerator(int reportType, long starDate, long endDate,
			String navigateObjectName, int generationType, String status,
			Company company) {
		// ReportsGenerator.companyType = companyType;
		this.reportType = reportType;
		this.startDate = new FinanceDate(starDate);
		this.endDate = new FinanceDate(endDate);
		this.navigateObjectName = navigateObjectName;
		this.status = status;
		this.company = company;
		this.generationType = generationType;
	}

	public ReportsGenerator(int reportType, long startDate, long endDate,
			String navigateObjectName, int generationtypecsv, long vendorId,
			int boxNo, Company company) {
		// ReportsGenerator.companyType = companyType;
		this.reportType = reportType;
		this.startDate = new FinanceDate(startDate);
		this.endDate = new FinanceDate(endDate);
		this.navigateObjectName = navigateObjectName;
		this.vendorId = vendorId;
		this.boxNo = boxNo;
		this.company = company;
		this.generationType = generationType;
	}

	public String generate(FinanceTool financeTool, int generationType)
			throws IOException {
		ReportGridTemplate.generationType = generationType;

		ReportGridTemplate<?> gridTemplate = getReportGridTemplate(financeTool,
				generationType);
		return gridTemplate.getBody(Global.get().messages());
	}

	public ReportGridTemplate<?> getReportGridTemplate(
			final FinanceTool finaTool, int generationType1) throws IOException {

		AccounterReportServiceImpl reportsSerivce = new AccounterReportServiceImpl() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected FinanceTool getFinanceTool() {
				return finaTool;
			}

		};

		switch (reportType) {
		case REPORT_TYPE_DEPRECIATIONSHEDULE:
			DepreciationSheduleServerReport depreciationSheduleServerReport = new DepreciationSheduleServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(depreciationSheduleServerReport, finaTool);
			depreciationSheduleServerReport.resetVariables();
			try {
				depreciationSheduleServerReport.onResultSuccess(reportsSerivce
						.getDepreciationSheduleReport(startDate
								.toClientFinanceDate(), endDate
								.toClientFinanceDate(),
								FixedAsset.STATUS_REGISTERED, getCompany()
										.getId()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return depreciationSheduleServerReport.getGridTemplate();
		case REPORT_TYPE_RECONCILATIONS:
			ReconcilationDetailsByAccountServerReport reconcilationDetailsByAccountServerReport = new ReconcilationDetailsByAccountServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(reconcilationDetailsByAccountServerReport, finaTool);
			reconcilationDetailsByAccountServerReport.resetVariables();
			try {
				reconcilationDetailsByAccountServerReport
						.onResultSuccess(reportsSerivce
								.getReconciliationItemByBankAccountID(
										startDate.toClientFinanceDate(),
										endDate.toClientFinanceDate(),
										Long.parseLong(navigateObjectName),
										getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return reconcilationDetailsByAccountServerReport.getGridTemplate();
		case REPORT_TYPE_RECONCILATION_ACCOUNTSLIST:
			ReconcilationsServerReport reconcilationsReport = new ReconcilationsServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(reconcilationsReport, finaTool);
			// reconcilationsReport.resetVariables();
			try {
				reconcilationsReport.onResultSuccess(reportsSerivce
						.getAllReconciliations(startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate(), getCompany()
										.getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return reconcilationsReport.getGridTemplate();
		case REPORT_TYPE_PROFITANDLOSS:

			ProfitAndLossServerReport profitAndLossServerReport = new ProfitAndLossServerReport(
					startDate.getDate(), endDate.getDate(), generationType1) {

				@Override
				public ClientFinanceDate getCurrentFiscalYearEndDate() {
					return Utility_R.getCurrentFiscalYearEndDate(company);
				}

				@Override
				public ClientFinanceDate getCurrentFiscalYearStartDate() {
					return Utility_R.getCurrentFiscalYearStartDate(company);
				}

				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(profitAndLossServerReport, finaTool);
			profitAndLossServerReport.resetVariables();
			try {
				profitAndLossServerReport.onResultSuccess(reportsSerivce
						.getProfitAndLossReport(
								startDate.toClientFinanceDate(), endDate
										.toClientFinanceDate(), getCompany()
										.getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return profitAndLossServerReport.getGridTemplate();
		case REPORT_TYPE_SALESBYCLASSDETAILFORCLASS:
			return generateSalesByClassorLocationTemplate(reportsSerivce,
					false, generationType1, finaTool);

		case REPORT_TYPE_SALESBYLOCATIONDETAILFORLOCATION:
			return generateSalesByClassorLocationTemplate(reportsSerivce, true,
					generationType1, finaTool);
		case REPORT_TYPE_SALESBYCLASSDETAIL:
			return generateSalesByLocationorClassDetailReport(reportsSerivce,
					false, generationType1, finaTool);
		case REPORT_TYPE_SALESBYLOCATIONDETAIL:
			return generateSalesByLocationorClassDetailReport(reportsSerivce,
					true, generationType1, finaTool);
		case REPORT_TYPE_BALANCESHEET:
			BalanceSheetServerReport balanceSheetServerReport = new BalanceSheetServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(balanceSheetServerReport, finaTool);
			balanceSheetServerReport.resetVariables();
			try {
				balanceSheetServerReport.onResultSuccess(reportsSerivce
						.getBalanceSheetReport(startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate(), getCompany()
										.getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return balanceSheetServerReport.getGridTemplate();
		case REPORT_TYPE_TRIALBALANCE:
			TrialBalanceServerReport trialBalanceServerReport = new TrialBalanceServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(trialBalanceServerReport, finaTool);
			trialBalanceServerReport.resetVariables();
			try {
				trialBalanceServerReport.onResultSuccess(reportsSerivce
						.getTrialBalance(startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate(), getCompany()
										.getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return trialBalanceServerReport.getGridTemplate();
		case REPORT_TYPE_TRANSACTIONDETAILBYTAXITEM:
			TransactionDetailByTaxItemServerReport transactionDetailByTaxItemServerReport = new TransactionDetailByTaxItemServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(transactionDetailByTaxItemServerReport, finaTool);
			transactionDetailByTaxItemServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {
					transactionDetailByTaxItemServerReport
							.onResultSuccess(reportsSerivce
									.getTransactionDetailByTaxItem(
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate(),
											getCompany().getID()));
				} else {
					transactionDetailByTaxItemServerReport
							.onResultSuccess(reportsSerivce
									.getTransactionDetailByTaxItem(status,
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate(),
											getCompany().getID()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return transactionDetailByTaxItemServerReport.getGridTemplate();
		case REPORT_TYPE_TRANSACTIONDETAILBYACCOUNT:
		case REPORT_TYPE_GENERAL_LEDGER_REPORT:
			TransactionDetailByAccountServerReport transactionDetailByAccountServerReport = new TransactionDetailByAccountServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(transactionDetailByAccountServerReport, finaTool);
			transactionDetailByAccountServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {
					transactionDetailByAccountServerReport
							.onResultSuccess(reportsSerivce
									.getTransactionDetailByAccount(
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate(),
											getCompany().getID()));
				} else {
					transactionDetailByAccountServerReport
							.onResultSuccess(reportsSerivce
									.getTransactionDetailByAccount(status,
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate(),
											getCompany().getID()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return transactionDetailByAccountServerReport.getGridTemplate();
		case REPORT_TYPE_EXPENSE:
			ExpenseServerReport expenseServerReport = new ExpenseServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(expenseServerReport, finaTool);
			expenseServerReport.resetVariables();
			try {
				expenseServerReport.onResultSuccess(reportsSerivce
						.getExpenseReportByType(Integer.parseInt(status),
								startDate.toClientFinanceDate(), endDate
										.toClientFinanceDate(), getCompany()
										.getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return expenseServerReport.getGridTemplate();
		case REPORT_TYPE_AR_AGEINGSUMMARY:
			ARAgingSummaryServerReport arAgingSummaryServerReport = new ARAgingSummaryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(arAgingSummaryServerReport, finaTool);
			arAgingSummaryServerReport.resetVariables();
			try {
				arAgingSummaryServerReport.onResultSuccess(reportsSerivce
						.getDebitors(startDate.toClientFinanceDate(), endDate
								.toClientFinanceDate(), getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arAgingSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_AR_AGEINGDETAIL:
			ARAgingDetailServerReport arAgingDetailServerReport = new ARAgingDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(arAgingDetailServerReport, finaTool);
			arAgingDetailServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {
					arAgingDetailServerReport.onResultSuccess(reportsSerivce
							.getAgedDebtors(startDate.toClientFinanceDate(),
									endDate.toClientFinanceDate(), getCompany()
											.getID()));
				} else {
					arAgingDetailServerReport.onResultSuccess(reportsSerivce
							.getAgedDebtors(status, startDate
									.toClientFinanceDate(), endDate
									.toClientFinanceDate(), getCompany()
									.getID()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arAgingDetailServerReport.getGridTemplate();
		case REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY:
			CustomerTransactionHistoryServerReport customerTransactionHistoryServerReport = new CustomerTransactionHistoryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(customerTransactionHistoryServerReport, finaTool);
			customerTransactionHistoryServerReport.resetVariables();
			try {
				customerTransactionHistoryServerReport.onResultSuccess(finaTool
						.getCustomerManager().getCustomerTransactionHistory(
								startDate, endDate, getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return customerTransactionHistoryServerReport.getGridTemplate();
		case REPORT_TYPE_SALESBYCUSTOMERSUMMARY:
			SalesByCustomerSummaryServerReport salesByCustomerSummaryServerReport = new SalesByCustomerSummaryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(salesByCustomerSummaryServerReport, finaTool);
			salesByCustomerSummaryServerReport.resetVariables();
			try {
				salesByCustomerSummaryServerReport
						.onResultSuccess(reportsSerivce
								.getSalesByCustomerSummary(
										startDate.toClientFinanceDate(),
										endDate.toClientFinanceDate(),
										getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesByCustomerSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_SALESBYCUSTOMERDETAIL:
			SalesByCustomerDetailServerReport salesByCustomerDetailServerReport = new SalesByCustomerDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(salesByCustomerDetailServerReport, finaTool);
			salesByCustomerDetailServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {
					salesByCustomerDetailServerReport
							.onResultSuccess(reportsSerivce
									.getSalesByCustomerDetailReport(
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate(),
											getCompany().getID()));
				} else {
					salesByCustomerDetailServerReport
							.onResultSuccess(reportsSerivce
									.getSalesByCustomerDetailReport(status,
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate(),
											getCompany().getID()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesByCustomerDetailServerReport.getGridTemplate();
		case REPORT_TYPE_SALESBYITEMSUMMARY:
			SalesByItemSummaryServerReport salesByItemSummaryServerReport = new SalesByItemSummaryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(salesByItemSummaryServerReport, finaTool);
			salesByItemSummaryServerReport.resetVariables();
			try {
				salesByItemSummaryServerReport.onResultSuccess(reportsSerivce
						.getSalesByItemSummary(startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate(), getCompany()
										.getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesByItemSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_SALESBYITEMDETAIL:
			SalesByItemDetailServerReport salesByItemDetailServerReport = new SalesByItemDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(salesByItemDetailServerReport, finaTool);
			salesByItemDetailServerReport.resetVariables();
			try {
				SalesManager salesManager = finaTool.getSalesManager();
				if (status == null || status.isEmpty()) {
					salesByItemDetailServerReport.onResultSuccess(salesManager
							.getSalesByItemDetail(startDate, endDate,
									getCompany().getID()));
				} else {
					salesByItemDetailServerReport.onResultSuccess(salesManager
							.getSalesByItemDetail(status, startDate, endDate,
									getCompany().getID()));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesByItemDetailServerReport.getGridTemplate();
		case REPORT_TYPE_SALESORDER_OPEN:
			SalesOpenOrderServerReport salesOpenOrderServerReport = new SalesOpenOrderServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(salesOpenOrderServerReport, finaTool);
			salesOpenOrderServerReport.resetVariables();
			try {
				if (Integer.parseInt(status) == 1) {
					salesOpenOrderServerReport.onResultSuccess(reportsSerivce
							.getSalesOpenOrderReport(startDate
									.toClientFinanceDate(), endDate
									.toClientFinanceDate(), getCompany()
									.getID()));
				} else if (Integer.parseInt(status) == 2) {
					salesOpenOrderServerReport.onResultSuccess(reportsSerivce
							.getSalesCompletedOrderReport(startDate
									.toClientFinanceDate(), endDate
									.toClientFinanceDate(), getCompany()
									.getID()));
				} else if (Integer.parseInt(status) == 3) {
					salesOpenOrderServerReport.onResultSuccess(reportsSerivce
							.getSalesCancelledOrderReport(startDate
									.toClientFinanceDate(), endDate
									.toClientFinanceDate(), getCompany()
									.getID()));
				} else {
					salesOpenOrderServerReport.onResultSuccess(reportsSerivce
							.getSalesOrderReport(startDate
									.toClientFinanceDate(), endDate
									.toClientFinanceDate(), getCompany()
									.getID()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesOpenOrderServerReport.getGridTemplate();
		case REPORT_TYPE_SALESORDER_CLOSE:
			SalesClosedOrderServerReport salesClosedOrderServerReport = new SalesClosedOrderServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(salesClosedOrderServerReport, finaTool);
			salesClosedOrderServerReport.resetVariables();
			try {
				salesClosedOrderServerReport.onResultSuccess(finaTool
						.getSalesManager().getClosedSalesOrders(startDate,
								endDate, getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesClosedOrderServerReport.getGridTemplate();
		case REPORT_TYPE_AP_AGEINGSUMMARY:
			APAgingSummaryServerReport apAgingSummaryServerReport = new APAgingSummaryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(apAgingSummaryServerReport, finaTool);
			apAgingSummaryServerReport.resetVariables();
			try {
				apAgingSummaryServerReport.onResultSuccess(reportsSerivce
						.getCreditors(startDate.toClientFinanceDate(), endDate
								.toClientFinanceDate(), getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return apAgingSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_AP_AGEINGDETAIL:
			APAgingDetailServerReport apAgingDetailServerReport = new APAgingDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(apAgingDetailServerReport, finaTool);
			apAgingDetailServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {
					apAgingDetailServerReport.onResultSuccess(reportsSerivce
							.getAgedCreditors(startDate.toClientFinanceDate(),
									endDate.toClientFinanceDate(),
									company.getID()));
				} else {
					apAgingDetailServerReport.onResultSuccess(reportsSerivce
							.getAgedCreditors(status, startDate
									.toClientFinanceDate(), endDate
									.toClientFinanceDate(), getCompany()
									.getID()));
				}
				// apAgingDetailServerReport.onSuccess(finaTool.getAgedCreditors(
				// startDate, endDate,getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return apAgingDetailServerReport.getGridTemplate();
		case REPORT_TYPE_VENDORTRANSACTIONHISTORY:
			VendorTransactionHistoryServerReport vendorTransactionHistoryServerReport = new VendorTransactionHistoryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {

				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(vendorTransactionHistoryServerReport, finaTool);
			vendorTransactionHistoryServerReport.resetVariables();
			try {
				vendorTransactionHistoryServerReport
						.onResultSuccess(reportsSerivce
								.getVendorTransactionHistory(
										startDate.toClientFinanceDate(),
										endDate.toClientFinanceDate(),
										getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vendorTransactionHistoryServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEBYVENDORSUMMARY:
			PurchaseByVendorSummaryServerReport purchaseByVendorSummaryServerReport = new PurchaseByVendorSummaryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(purchaseByVendorSummaryServerReport, finaTool);
			purchaseByVendorSummaryServerReport.resetVariables();
			try {
				purchaseByVendorSummaryServerReport.onResultSuccess(finaTool
						.getVendorManager().getPurchasesByVendorSummary(
								startDate, endDate, getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseByVendorSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEBYVENDORDETAIL:
			PurchaseByVendorDetailServerReport purchaseByVendorDetailServerReport = new PurchaseByVendorDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(purchaseByVendorDetailServerReport, finaTool);
			purchaseByVendorDetailServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {

					purchaseByVendorDetailServerReport
							.onResultSuccess(reportsSerivce
									.getPurchasesByVendorDetail(
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate(),
											getCompany().getID()));
				} else {
					purchaseByVendorDetailServerReport.onResultSuccess(finaTool
							.getVendorManager().getPurchasesByVendorDetail(
									status, startDate, endDate,
									getCompany().getID()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseByVendorDetailServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEBYITEMSUMMARY:
			PurchaseByItemSummaryServerReport purchaseByItemSummaryServerReport = new PurchaseByItemSummaryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(purchaseByItemSummaryServerReport, finaTool);
			purchaseByItemSummaryServerReport.resetVariables();
			try {

				purchaseByItemSummaryServerReport.onResultSuccess(finaTool
						.getPurchageManager().getPurchasesByItemSummary(
								startDate, endDate, getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseByItemSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEBYITEMDETAIL:
			PurchaseByItemDetailServerReport purchaseByItemDetailServerReport = new PurchaseByItemDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(purchaseByItemDetailServerReport, finaTool);
			purchaseByItemDetailServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {
					purchaseByItemDetailServerReport.onResultSuccess(finaTool
							.getPurchageManager().getPurchasesByItemDetail(
									startDate, endDate, getCompany().getID()));
				} else {
					purchaseByItemDetailServerReport.onResultSuccess(finaTool
							.getPurchageManager().getPurchasesByItemDetail(
									status, startDate, endDate,
									getCompany().getID()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseByItemDetailServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEORDER_OPEN:
			PurchaseOpenOrderServerReport purchaseOpenOrderServerReport = new PurchaseOpenOrderServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(purchaseOpenOrderServerReport, finaTool);
			purchaseOpenOrderServerReport.resetVariables();
			try {
				if (Integer.parseInt(status) == 1) {
					purchaseOpenOrderServerReport
							.onResultSuccess(reportsSerivce
									.getPurchaseOpenOrderReport(
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate(),
											getCompany().getID()));
				} else if (Integer.parseInt(status) == 2) {
					purchaseOpenOrderServerReport
							.onResultSuccess(reportsSerivce
									.getPurchaseCompletedOrderReport(
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate(),
											getCompany().getID()));
				} else if (Integer.parseInt(status) == 3) {
					purchaseOpenOrderServerReport
							.onResultSuccess(reportsSerivce
									.getPurchaseCancelledOrderReport(
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate(),
											getCompany().getID()));
				} else {
					purchaseOpenOrderServerReport
							.onResultSuccess(reportsSerivce
									.getPurchaseOrderReport(
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate(),
											getCompany().getID()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseOpenOrderServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEORDER_CLOSE:
			PurchaseClosedOrderServerReport purchaseClosedOrderServerReport = new PurchaseClosedOrderServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(purchaseClosedOrderServerReport, finaTool);
			purchaseClosedOrderServerReport.resetVariables();
			try {
				purchaseClosedOrderServerReport.onResultSuccess(finaTool
						.getPurchageManager().getClosedPurchaseOrders(
								startDate, endDate, getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseClosedOrderServerReport.getGridTemplate();
		case REPORT_TYPE_PRIORVATRETURNS:
			PriorVATReturnsServerReport priorVATReturnsServerReport = new PriorVATReturnsServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(priorVATReturnsServerReport, finaTool);
			priorVATReturnsServerReport.resetVariables();
			try {
				priorVATReturnsServerReport.onResultSuccess(reportsSerivce
						.getPriorReturnVATSummary(Long.parseLong(status),
								endDate.toClientFinanceDate(), getCompany()
										.getID()));

			} catch (Exception e) {
				e.printStackTrace();
			}
			return priorVATReturnsServerReport.getGridTemplate();
		case REPORT_TYPE_VAT100:
			VAT100ServerReport vat100ServerReport = new VAT100ServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(vat100ServerReport, finaTool);
			vat100ServerReport.resetVariables();
			try {
				vat100ServerReport.onResultSuccess(reportsSerivce
						.getVAT100Report(Long.parseLong(status), startDate
								.toClientFinanceDate(), endDate
								.toClientFinanceDate(), getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vat100ServerReport.getGridTemplate();
		case REPORT_TYPE_VATDETAIL:
			VATDetailServerReportView vatDetailServerReportView = new VATDetailServerReportView(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(vatDetailServerReportView, finaTool);
			vatDetailServerReportView.resetVariables();
			try {
				vatDetailServerReportView.onResultSuccess(finaTool
						.getReportManager().getVATDetailReport(startDate,
								endDate, company.getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vatDetailServerReportView.getGridTemplate();
		case REPORT_TYPE_VATITEMDETAIL:
			VATItemDetailServerReport vatItemDetailServerReport = new VATItemDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(vatItemDetailServerReport, finaTool);
			vatItemDetailServerReport.resetVariables();
			try {
				vatItemDetailServerReport.onResultSuccess(finaTool
						.getReportManager().getVATItemDetailReport(status,
								startDate, endDate, getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vatItemDetailServerReport.getGridTemplate();
		case REPORT_TYPE_UNCATEGORISEDVATAMOUNT:
			VATUncategorisedAmountsServerReport vatUncategorisedAmountsServerReport = new VATUncategorisedAmountsServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(vatUncategorisedAmountsServerReport, finaTool);
			vatUncategorisedAmountsServerReport.resetVariables();
			try {
				vatUncategorisedAmountsServerReport.onResultSuccess(finaTool
						.getReportManager().getUncategorisedAmountsReport(
								startDate, endDate, getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vatUncategorisedAmountsServerReport.getGridTemplate();
		case REPORT_TYPE_VATITEMSUMMARY:
			VATItemSummaryServerReport vatItemSummaryServerReport = new VATItemSummaryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(vatItemSummaryServerReport, finaTool);
			vatItemSummaryServerReport.resetVariables();
			try {
				vatItemSummaryServerReport.onResultSuccess(finaTool
						.getReportManager().getVATItemSummaryReport(startDate,
								endDate, getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vatItemSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_ECSCALESLIST:
			ECSalesListServerReport ecSalesListServerReport = new ECSalesListServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String dateFormat(ClientFinanceDate date) {
					try {
						if (date == null)
							return "";
						SimpleDateFormat dateFormatter = new SimpleDateFormat(
								"dd/MM/yyyy");
						String format = dateFormatter.format(date
								.getDateAsObject());
						return format;

					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}

				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);

				}
			};
			updateReport(ecSalesListServerReport, finaTool);
			ecSalesListServerReport.resetVariables();
			try {
				ecSalesListServerReport.onResultSuccess(reportsSerivce
						.getECSalesListReport(startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate(), getCompany()
										.getID()));

			} catch (Exception e) {
				e.printStackTrace();
			}
			return ecSalesListServerReport.getGridTemplate();
		case REPORT_TYPE_ECSCALESLISTDETAIL:
			ECSalesListDetailServerReport ecSalesListDetailServerReport = new ECSalesListDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(ecSalesListDetailServerReport, finaTool);
			ecSalesListDetailServerReport.resetVariables();
			try {
				ecSalesListDetailServerReport.onResultSuccess(finaTool
						.getReportManager().getECSalesListDetailReport(status,
								startDate, endDate, company));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ecSalesListDetailServerReport.getGridTemplate();
		case REPORT_TYPE_SALESTAXLIABILITY:
			SalesTaxLiabilityServerReport salesTaxLiabilityServerReport = new SalesTaxLiabilityServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(salesTaxLiabilityServerReport, finaTool);
			salesTaxLiabilityServerReport.resetVariables();
			try {
				salesTaxLiabilityServerReport.onResultSuccess(finaTool
						.getReportManager().getSalesTaxLiabilityReport(
								startDate, endDate, getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesTaxLiabilityServerReport.getGridTemplate();
		case REPORT_TYPE_REVERSECHARGELIST:
			ReverseChargeListServerReport reverseChargeListServerReport = new ReverseChargeListServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(reverseChargeListServerReport, finaTool);
			reverseChargeListServerReport.resetVariables();
			try {
				reverseChargeListServerReport.onResultSuccess(finaTool
						.getReportManager().getReverseChargeListReport(
								startDate, endDate, getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return reverseChargeListServerReport.getGridTemplate();
		case REPORT_TYPE_REVERSECHARGELISTDETAIL:
			ReverseChargeListDetailServerReport reverseChargeListDetailServerReport = new ReverseChargeListDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(reverseChargeListDetailServerReport, finaTool);
			reverseChargeListDetailServerReport.resetVariables();
			try {
				reverseChargeListDetailServerReport.onResultSuccess(finaTool
						.getReportManager().getReverseChargeListDetailReport(
								status, startDate, endDate, company.getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return reverseChargeListDetailServerReport.getGridTemplate();
		case REPORT_TYPE_MOSTPROFITABLECUSTOMER:
			MostProfitableCustomerServerReport mostProfitableCustomerServerReport = new MostProfitableCustomerServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(mostProfitableCustomerServerReport, finaTool);
			mostProfitableCustomerServerReport.resetVariables();
			try {
				mostProfitableCustomerServerReport.onResultSuccess(finaTool
						.getCustomerManager().getMostProfitableCustomers(
								startDate, endDate, getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mostProfitableCustomerServerReport.getGridTemplate();
		case REPORT_TYPE_CASHFLOWSTATEMENT:
			CashFlowStatementServerReport cashFlowStatementServerReport = new CashFlowStatementServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(cashFlowStatementServerReport, finaTool);
			cashFlowStatementServerReport.resetVariables();
			try {
				cashFlowStatementServerReport.onResultSuccess(reportsSerivce
						.getCashFlowReport(startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate(), getCompany()
										.getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return cashFlowStatementServerReport.getGridTemplate();
		case REPORT_TYPE_AMOUNTSDUETOVENDOR:
			AmountsDueToVendorServerReport amountsDueToVendorServerReport = new AmountsDueToVendorServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(amountsDueToVendorServerReport, finaTool);
			amountsDueToVendorServerReport.resetVariables();
			try {
				amountsDueToVendorServerReport.onResultSuccess(finaTool
						.getVendorManager().getAmountsDueToVendor(startDate,
								endDate, getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return amountsDueToVendorServerReport.getGridTemplate();
		case REPORT_TYPE_CUSTOMERSTATEMENT:
			StatementServerReport statementReport = new StatementServerReport(
					false, this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(statementReport, finaTool);
			statementReport.resetVariables();
			try {
				statementReport.onResultSuccess(finaTool.getReportManager()
						.getPayeeStatementsList(false, Long.parseLong(status),
								startDate, endDate, company.getID()));

			} catch (Exception e) {
				e.printStackTrace();
			}
			return statementReport.getGridTemplate();

		case REPORT_TYPE_VENDORSTATEMENT:
			StatementServerReport statementReport1 = new StatementServerReport(
					true, this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(statementReport1, finaTool);
			statementReport1.resetVariables();
			try {
				statementReport1.onResultSuccess(finaTool.getReportManager()
						.getPayeeStatementsList(true, Long.parseLong(status),
								startDate, endDate, company.getID()));

			} catch (Exception e) {
				e.printStackTrace();
			}
			return statementReport1.getGridTemplate();
		case REPORT_TYPE_PROFITANDLOSSBYCLASS:
			return generateProfitandLossByLocationorClass(false,
					generationType1, finaTool);
		case REPORT_TYPE_PROFITANDLOSSBYLOCATION:
			return generateProfitandLossByLocationorClass(true,
					generationType1, finaTool);

		case REPORT_TYPE_1099TRANSACTIONDETAIL:
			MISC1099TransactionDetailServerReport misc1099TransactionDetailServerReport = new MISC1099TransactionDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(misc1099TransactionDetailServerReport, finaTool);
			misc1099TransactionDetailServerReport.resetVariables();
			try {
				misc1099TransactionDetailServerReport.onResultSuccess(finaTool
						.getVendorManager().getPaybillsByVendorAndBoxNumber(
								this.startDate, this.endDate, this.vendorId,
								this.boxNo, getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return misc1099TransactionDetailServerReport.getGridTemplate();
		case REPORT_TYPE_BUDGET:

			BudgetOverviewServerReport budgetServerReport = new BudgetOverviewServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {

				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}

			};

			updateReport(budgetServerReport, finaTool);
			budgetServerReport.resetVariables();
			try {
				budgetServerReport.onResultSuccess(reportsSerivce
						.getBudgetItemsList(Integer.parseInt(status),
								getCompany().getID()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return budgetServerReport.getGridTemplate();

		case REPORT_TYPE_TAX_ITEM_DETAIL:
			TAXItemDetailServerReportView taxItemDetailServerReportView = new TAXItemDetailServerReportView(
					startDate.getDate(), endDate.getDate(), generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(taxItemDetailServerReportView, finaTool);
			taxItemDetailServerReportView.resetVariables();
			taxItemDetailServerReportView.onResultSuccess(finaTool
					.getReportManager().getTAXItemDetailReport(
							getCompany().getId(), Long.parseLong(status),
							startDate.getDate(), endDate.getDate()));

			return taxItemDetailServerReportView.getGridTemplate();
		case REPORT_TYPE_VAT_EXCEPTION_DETAIL:
			VATExceptionServerReport report = new VATExceptionServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(report, finaTool);
			report.resetVariables();
			try {
				

				long taxReturnId = 0, taxAgency = 0;
				
				taxReturnId = Long.parseLong(navigateObjectName);
				taxAgency = Long.parseLong(dateRangeHtml);

				report.onResultSuccess(finaTool.getReportManager()
						.getVATExpectionsForPrint(company.getID(), taxAgency,
								taxReturnId));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return report.getGridTemplate();
		case REPORT_TYPE_TAX_EXCEPTION_DETAIL:
			TAXItemExceptionDetailServerReport taxReport = new TAXItemExceptionDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(taxReport, finaTool);
			taxReport.resetVariables();
			try {
				long taxReturnId = Long.parseLong(navigateObjectName);
				long taxAgencyId = Long.parseLong(dateRangeHtml);
				
				taxReport.onResultSuccess(finaTool.getReportManager()
						.getTAXExceptionsForPrint(company.getId(), taxReturnId,
								taxAgencyId));

			} catch (Exception e) {
				e.printStackTrace();
			}
			return taxReport.getGridTemplate();

		case REALISED_EXCHANGE_LOSSES_AND_GAINS:
			RealisedExchangeLossesAndGainsServerReport realisesExhanges = new RealisedExchangeLossesAndGainsServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {

					return getDateInDefaultType(date);
				}
			};
			updateReport(realisesExhanges, finaTool);
			realisesExhanges.resetVariables();
			try {
				realisesExhanges.onResultSuccess(finaTool.getReportManager()
						.getRealisedExchangeLossesOrGains(company.getID(),
								startDate.getDate(), endDate.getDate()));

			} catch (Exception e) {
				e.printStackTrace();
			}
			return realisesExhanges.getGridTemplate();
		default:
			break;
		}

		return null;
	}

	private Company getCompany() {
		return company;
	}

	private ReportGridTemplate<?> generateProfitandLossByLocationorClass(
			boolean isLocation, int generationType1, FinanceTool finaTool) {
		ProfitAndLossServerReport profitAndLossBylocationServerReport = new ProfitAndLossServerReport(
				startDate.getDate(), endDate.getDate(), generationType1) {

			@Override
			public ClientFinanceDate getCurrentFiscalYearEndDate() {
				return Utility_R.getCurrentFiscalYearEndDate(company);
			}

			@Override
			public ClientFinanceDate getCurrentFiscalYearStartDate() {
				return Utility_R.getCurrentFiscalYearStartDate(company);
			}

			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateByCompanyType(date);
			}
		};
		updateReport(profitAndLossBylocationServerReport, finaTool);
		profitAndLossBylocationServerReport.resetVariables();
		try {
			profitAndLossBylocationServerReport.onResultSuccess(finaTool
					.getReportManager().getProfitAndLossReport(startDate,
							endDate, getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return profitAndLossBylocationServerReport.getGridTemplate();
	}

	private ReportGridTemplate<?> generateSalesByLocationorClassDetailReport(
			AccounterReportServiceImpl reportsSerivce, boolean isLocation,
			int generationType1, FinanceTool finaTool) {
		SalesByLocationDetailsServerReport salesByLocationDetailsServerReport = new SalesByLocationDetailsServerReport(
				startDate.getDate(), endDate.getDate(), generationType1) {

			@Override
			public ClientFinanceDate getCurrentFiscalYearEndDate() {
				return Utility_R.getCurrentFiscalYearEndDate(company);
			}

			@Override
			public ClientFinanceDate getCurrentFiscalYearStartDate() {
				return Utility_R.getCurrentFiscalYearStartDate(company);
			}

			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateByCompanyType(date);
			}
		};
		updateReport(salesByLocationDetailsServerReport, finaTool);
		salesByLocationDetailsServerReport.resetVariables();
		try {
			salesByLocationDetailsServerReport
					.onResultSuccess(reportsSerivce
							.getSalesByLocationDetailsReport(isLocation,
									startDate.toClientFinanceDate(), endDate
											.toClientFinanceDate(),
									getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByLocationDetailsServerReport.getGridTemplate();
	}

	private ReportGridTemplate<?> generateSalesByClassorLocationTemplate(
			AccounterReportServiceImpl reportsSerivce, boolean isLocation,
			int generationType1, FinanceTool finaTool) {
		SalesByLocationsummaryServerReport salesByLocationsummaryServerReport = new SalesByLocationsummaryServerReport(
				startDate.getDate(), endDate.getDate(), generationType1) {

			@Override
			public ClientFinanceDate getCurrentFiscalYearEndDate() {
				return Utility_R.getCurrentFiscalYearEndDate(company);
			}

			@Override
			public ClientFinanceDate getCurrentFiscalYearStartDate() {
				return Utility_R.getCurrentFiscalYearStartDate(company);
			}

			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateByCompanyType(date);
			}
		};
		updateReport(salesByLocationsummaryServerReport, finaTool);
		salesByLocationsummaryServerReport.resetVariables();
		try {
			salesByLocationsummaryServerReport
					.onResultSuccess(reportsSerivce
							.getSalesByLocationSummaryReport(isLocation,
									startDate.toClientFinanceDate(), endDate
											.toClientFinanceDate(),
									getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByLocationsummaryServerReport.getGridTemplate();
	}

	private ReportGridTemplate<?> generateDepreciationSheduleForFixedAssetsTemplate(
			AccounterReportServiceImpl reportsSerivce, boolean isLocation,
			int generationType1, FinanceTool finaTool) {

		return null;
	}

	private void updateReport(AbstractFinaneReport<?> abstractFinaneReport,
			FinanceTool financeTool) {
		abstractFinaneReport.setNavigatedObjectName(navigateObjectName);
		// abstractFinaneReport.setFinaceTool(financeTool);
		abstractFinaneReport.setStartAndEndDates(
				startDate.toClientFinanceDate(), endDate.toClientFinanceDate());
		abstractFinaneReport.makeReportRequest(startDate.getDate(),
				endDate.getDate());
		abstractFinaneReport.setCurrentFiscalYearStartDate(Utility_R
				.getCurrentFiscalYearStartDate(company));
		abstractFinaneReport.setCurrentFiscalYearEndDate(Utility_R
				.getCurrentFiscalYearEndDate(company));
	}

	public static String getReportHtml() {
		return "";
	}

	public static String getReportNameByType(int reportType) {
		switch (reportType) {
		case REPORT_TYPE_PROFITANDLOSS:
			return "Profit And Loss Report";
		case REPORT_TYPE_DEPRECIATIONSHEDULE:
			return "Depreciation Shedule Report";
		case REPORT_TYPE_RECONCILATIONS:
			return "ReconcilationDetails By Account Report";
		case REPORT_TYPE_BALANCESHEET:
			return "Balance Sheet Report";
		case REPORT_TYPE_RECONCILATION_ACCOUNTSLIST:
			return "Reconcilation Reports";
		case REPORT_TYPE_TRIALBALANCE:
			return "Trial Balance Report";
		case REPORT_TYPE_TRANSACTIONDETAILBYTAXITEM:
			return "Transaction Detail By Tax Item Report";
		case REPORT_TYPE_TRANSACTIONDETAILBYACCOUNT:
			return "Transaction Detail By Account Report";
		case REPORT_TYPE_EXPENSE:
			return "Expense Report";
		case REPORT_TYPE_AR_AGEINGSUMMARY:
			return "AR Ageing Summary Report";
		case REPORT_TYPE_AR_AGEINGDETAIL:
			return "AR Ageing Detail Report";
		case REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY:
			return "Customer Transaction History Report";
		case REPORT_TYPE_SALESBYCUSTOMERSUMMARY:
			return "Sales By Customer Summary Report";
		case REPORT_TYPE_SALESBYCUSTOMERDETAIL:
			return "Sales By Customer Detail Report";
		case REPORT_TYPE_SALESBYITEMSUMMARY:
			return "Sales By Item Summary Report";
		case REPORT_TYPE_SALESBYITEMDETAIL:
			return "Sales By Item Detail Report";
		case REPORT_TYPE_SALESORDER_OPEN:
			return "Sales Open Order Report";
		case REPORT_TYPE_SALESORDER_CLOSE:
			return "Sales Closed Order Report";
		case REPORT_TYPE_AP_AGEINGSUMMARY:
			return "AP Ageing Summary Report";
		case REPORT_TYPE_AP_AGEINGDETAIL:
			return "AP Ageing Detail Report";
		case REPORT_TYPE_VENDORTRANSACTIONHISTORY:
			// return "Supplier Transaction History Report";
			return Global.get().messages()
					.payeeTransactionHistory(Global.get().Vendor());
		case REPORT_TYPE_PURCHASEBYVENDORSUMMARY:
			return "Purchase By Supplier Summary Report";
		case REPORT_TYPE_PURCHASEBYVENDORDETAIL:
			return "Purchase By Supplier Detail Report";
		case REPORT_TYPE_PURCHASEBYITEMSUMMARY:
			return "Purchase By Item Summary Report";
		case REPORT_TYPE_PURCHASEBYITEMDETAIL:
			return "Purchase By Item Detail Report";
		case REPORT_TYPE_PURCHASEORDER_OPEN:
			return "Purchase Open Order Report";
		case REPORT_TYPE_PURCHASEORDER_CLOSE:
			return "Purchase Closed Order Report";
		case REPORT_TYPE_PRIORVATRETURNS:
			return "Prior VAT Return Report";
		case REPORT_TYPE_VAT100:
			return "VAT 100 Report";
		case REPORT_TYPE_VATDETAIL:
			return "VAT Detail Report";
		case REPORT_TYPE_VATITEMDETAIL:
			return "VAT Item Detail Report";
		case REPORT_TYPE_UNCATEGORISEDVATAMOUNT:
			return "Uncategorised VAT Amount Report";
		case REPORT_TYPE_VATITEMSUMMARY:
			return "VAT Item Summary Report";
		case REPORT_TYPE_ECSCALESLIST:
			return "EC Sales List Report";
		case REPORT_TYPE_ECSCALESLISTDETAIL:
			return "EC Sales List Detail Report";
		case REPORT_TYPE_SALESTAXLIABILITY:
			return "Sales Tax Liability Report";
		case REPORT_TYPE_REVERSECHARGELIST:
			return "Reverse Charge List Report";
		case REPORT_TYPE_REVERSECHARGELISTDETAIL:
			return "Reverse Charge List Detail Report";
		case REPORT_TYPE_MOSTPROFITABLECUSTOMER:
			return "Most Profitable Customer Report";
		case REPORT_TYPE_CASHFLOWSTATEMENT:
			return "Cash Flow Statement Report";
		case REPORT_TYPE_AMOUNTSDUETOVENDOR:
			return "Amount Due To Vendor Report";
		case REPORT_TYPE_CUSTOMERSTATEMENT:
			return "Customer Statement";
		case REPORT_TYPE_SALESBYLOCATIONDETAIL:
			return "Sales By Location Details Report";
		case REPORT_TYPE_SALESBYLOCATIONDETAILFORLOCATION:
			return "Sales By Location Summary Report";
		case REPORT_TYPE_PROFITANDLOSSBYLOCATION:
			return "Profit and Loss by Location";
		case REPORT_TYPE_BUDGET:
			return "Budget Report";
		case REPORT_TYPE_1099TRANSACTIONDETAIL:
			return "1099 Transaction Detail By Vendor";
		case REPORT_TYPE_SALESBYCLASSDETAIL:
			return "Sales By Class Details Report";
		case REPORT_TYPE_SALESBYCLASSDETAILFORCLASS:
			return "Sales By Class Summary Report";
		case REPORT_TYPE_PROFITANDLOSSBYCLASS:
			return "Profit and Loss by Class";
		case REPORT_TYPE_GENERAL_LEDGER_REPORT:
			return "General Ledger Report";
		case REPORT_TYPE_TAX_ITEM_DETAIL:
			return "TAXItemDetail Report";
		case REPORT_TYPE_VAT_EXCEPTION_DETAIL:
			return "VAT Exception Detail Report";
		case REPORT_TYPE_VENDORSTATEMENT:
			return getVendorName();
		case REPORT_TYPE_TAX_EXCEPTION_DETAIL:
			return "TAX Item Exception Report";
		case REALISED_EXCHANGE_LOSSES_AND_GAINS:
			return "Realised Exchange Losses & Gains Report";
		default:
			break;
		}
		return "";
	}

	private static String getVendorName() {
		Set<Vendor> vendors = company.getVendors();
		for (Vendor vendor : vendors) {
			if (String.valueOf(vendor.getID()).equals(status)) {
				String name = vendor.getName();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				return name;
			}
		}
		return Global.get().Vendor() + " Statement";
	}

	public String getDateInDefaultType(ClientFinanceDate date) {
		if (date == null) {
			return "";
		}
		if (company == null) {
			date.toString();
		}

		SimpleDateFormat dateFormatter = new SimpleDateFormat(company
				.getPreferences().getDateFormat());
		String format = dateFormatter.format(date.getDateAsObject());
		return format;
	}

}
