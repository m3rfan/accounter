package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.server.FinanceTool;

public class TransactionDetailbyTaxItemReportCommand extends
		NewAbstractReportCommand<TransactionDetailByTaxItem> {
	private String taxItemName;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<TransactionDetailByTaxItem>() {

			@Override
			protected String onSelection(TransactionDetailByTaxItem selection,
					String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<TransactionDetailByTaxItem> records = getRecords();
				Map<String, List<TransactionDetailByTaxItem>> recordGroups = new HashMap<String, List<TransactionDetailByTaxItem>>();
				for (TransactionDetailByTaxItem transactionDetailByTaxItem : records) {
					String taxItemName = transactionDetailByTaxItem
							.getTaxItemName();
					List<TransactionDetailByTaxItem> group = recordGroups
							.get(taxItemName);
					if (group == null) {
						group = new ArrayList<TransactionDetailByTaxItem>();
						recordGroups.put(taxItemName, group);
					}
					group.add(transactionDetailByTaxItem);
				}

				Set<String> keySet = recordGroups.keySet();
				List<String> taxItems = new ArrayList<String>(keySet);
				Collections.sort(taxItems);

				for (String tax : taxItems) {
					List<TransactionDetailByTaxItem> group = recordGroups
							.get(tax);
					double salesTaxTotal = 0.0;
					double taxableAmount = 0.0;
					addSelection(tax);
					ResultList resultList = new ResultList(tax);
					for (TransactionDetailByTaxItem rec : group) {
						resultList.setTitle(rec.getTaxAgencyName() + "-" + tax);
						resultList.add(createReportRecord(rec));
						salesTaxTotal += rec.getSalesTaxAmount();
						taxableAmount += rec.getTaxableAmount();
					}
					makeResult.add(resultList);
					makeResult.add("Sales Tax Total: " + salesTaxTotal);
					makeResult.add("Taxable amount Total: " + taxableAmount);
				}
			}
		});
	}

	protected Record createReportRecord(TransactionDetailByTaxItem record) {
		Record tdRecord = new Record(record);
		tdRecord.add(Utility.getTransactionName(record.getTransactionType()));
		tdRecord.add(getMessages().taxRate(), record.getRate());
		tdRecord.add(getMessages().date(), record.getDate());
		tdRecord.add(getMessages().number(), record.getNumber());
		tdRecord.add(getMessages().taxItemName(), record.getTaxItemName());
		tdRecord.add(getMessages().memo(), record.getMemo());
		tdRecord.add(getMessages().salesTax(), record.getSalesTaxAmount());
		tdRecord.add(getMessages().taxable(), record.getTaxableAmount());
		return tdRecord;
	}

	protected List<TransactionDetailByTaxItem> getRecords() {
		ArrayList<TransactionDetailByTaxItem> transactionDetailByTaxItems = new ArrayList<TransactionDetailByTaxItem>();
		try {
			if (taxItemName == null || taxItemName.isEmpty()) {
				transactionDetailByTaxItems = new FinanceTool()
						.getReportManager().getTransactionDetailByTaxItem(
								getStartDate(), getEndDate(), getCompanyId());
			} else if (taxItemName != null) {
				transactionDetailByTaxItems = new FinanceTool()
						.getReportManager().getTransactionDetailByTaxItem(
								taxItemName, getStartDate(), getEndDate(),
								getCompanyId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionDetailByTaxItems;
	}

	protected String addCommandOnRecordClick(
			TransactionDetailByTaxItem selection) {
		return "update transaction " + selection.getTransactionId();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		dateRangeChanged(getMessages().financialYearToDate());
		if (string != null && !string.isEmpty()) {
			String[] split = string.split(",");
			context.setString(split[0]);
			taxItemName = split[1];
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().transactionDetailByTaxItem());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().transactionDetailByTaxItem());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().transactionDetailByTaxItem());
	}

}
