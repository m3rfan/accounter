package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.server.FinanceTool;

public abstract class NewAbstractTransactionCommand extends NewAbstractCommand {
	protected static final String CUSTOMER = "customer";
	protected static final String ITEMS = "items";
	protected static final String DATE = "date";
	protected static final String NUMBER = "number";
	protected static final String CONTACT = "contact";
	protected static final String BILL_TO = "billto";
	protected static final String MEMO = "memo";
	protected static final String TAXCODE = "taxCode";
	protected static final String VENDOR = "vendor";
	protected static final String PHONE = "phone";
	protected static final String ACCOUNTS = "acounts";
	protected static final String PAYMENT_TERMS = "paymentTerms";
	protected static final String DUE_DATE = "duedate";
	protected static final String DELIVERY_DATE = "deliveryDate";
	protected static final String CHEQUE_NO = "chequeNo";
	protected static final String PAYMENT_METHOD = "paymentMethod";
	// protected static final String CURRENCY = "currency";
	// protected static final String CURRENCY_FACTOR = "currencyFactor";
	protected static final String PAY_FROM = "payFrom";
	protected static final String ORDER_NO = "orderNo";
	protected static final String ESTIMATEANDSALESORDER = "estimateAndSalesOrder";
	protected static final String ESTIMATE = "estimate";
	protected static final String DISPATCH_DATE = "dispathDate";
	protected static final String RECIEVED_DATE = "deliveryDate";
	protected static final String AMOUNT = "amount";
	protected static final String TO_BE_PRINTED = "toBePrinted";
	protected static final String FILTER_BY_DUE_ON_BEFORE = "filterByDueOnBefore";
	protected static final String BILLS_DUE = "BillsDue";
	protected static final String IS_VAT_INCLUSIVE = "isVatInclusive";

	public double updateTotals(Context context, ClientTransaction transaction,
			boolean isSales) {
		List<ClientTransactionItem> allrecords = transaction
				.getTransactionItems();
		double[] result = getTransactionTotal(context,
				transaction.isAmountsIncludeVAT(), allrecords, isSales);
		double grandTotal = result[0] + result[1];
		transaction.setTotal(grandTotal);
		transaction.setNetAmount(result[0]);
		return result[1];
	}

	public double[] getTransactionTotal(Context context,
			boolean isAmountsIncludeVAT,
			List<ClientTransactionItem> allrecords, boolean isSales) {
		double lineTotal = 0.0;
		double totalTax = 0.0;

		for (ClientTransactionItem record : allrecords) {

			int type = record.getType();

			if (type == 0)
				continue;
			// totaldiscount += record.getDiscount();

			Double lineTotalAmt = record.getLineTotal();
			lineTotal += lineTotalAmt;

			if (record != null && record.isTaxable()) {
				// ClientTAXItem taxItem = getCompany().getTAXItem(
				// citem.getTaxCode());
				// if (taxItem != null) {
				// totalVat += taxItem.getTaxRate() / 100 * lineTotalAmt;
				// }
				// taxableLineTotal += lineTotalAmt;

				double taxAmount = getVATAmount(context, isAmountsIncludeVAT,
						record.getTaxCode(), record, isSales);
				if (isAmountsIncludeVAT) {
					lineTotal -= taxAmount;
				}
				record.setVATfraction(taxAmount);
				totalTax += record.getVATfraction();

			}

			// super.update(record);
			// totalVat += citem.getVATfraction();
		}

		double[] result = new double[2];
		result[0] = lineTotal;
		result[1] = totalTax;
		return result;
	}

	public double getVATAmount(Context context, boolean isAmountsIncludeVAT,
			long TAXCodeID, ClientTransactionItem record, boolean isSales) {

		double vatRate = 0.0;
		try {
			if (TAXCodeID != 0) {
				// Checking the selected object is VATItem or VATGroup.
				// If it is VATItem,the we should get 'VATRate',otherwise
				// 'GroupRate
				ClientTAXCode taxCode = (ClientTAXCode) CommandUtils
						.getClientObjectById(TAXCodeID,
								AccounterCoreType.TAX_CODE, getCompanyId());

				if (!taxCode.getName().equals("EGS")
						&& !taxCode.getName().equals("EGZ")
						&& !taxCode.getName().equals("RC")) {
					ClientTAXItemGroup vatItemGroup = (ClientTAXItem) CommandUtils
							.getClientObjectById(
									isSales ? taxCode.getTAXItemGrpForSales()
											: taxCode
													.getTAXItemGrpForPurchases(),
									AccounterCoreType.TAXITEM, getCompanyId());
					if (vatItemGroup != null) {
						vatRate = ((ClientTAXItem) vatItemGroup).getTaxRate();
					}
					if (vatItemGroup == null) {
						vatItemGroup = (ClientTAXGroup) CommandUtils
								.getClientObjectById(
										isSales ? taxCode
												.getTAXItemGrpForSales()
												: taxCode
														.getTAXItemGrpForPurchases(),
										AccounterCoreType.TAX_GROUP,
										getCompanyId());
						if (vatItemGroup != null) {
							vatRate = ((ClientTAXGroup) vatItemGroup)
									.getGroupRate();
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Double vat = 0.0;
		if (isAmountsIncludeVAT) {
			vat = ((ClientTransactionItem) record).getLineTotal()
					- (100 * (((ClientTransactionItem) record).getLineTotal() / (100 + vatRate)));
		} else {
			vat = ((ClientTransactionItem) record).getLineTotal() * vatRate
					/ 100;
		}
		vat = UIUtils.getRoundValue(vat);
		return vat.doubleValue();
	}

	public ArrayList<ReceivePaymentTransactionList> getTransactionReceivePayments(
			long companyId, long customerId, long paymentDate)
			throws AccounterException {
		List<ReceivePaymentTransactionList> receivePaymentTransactionList = null;
		receivePaymentTransactionList = new FinanceTool()
				.getTransactionReceivePayments(customerId, paymentDate,
						companyId);
		return new ArrayList<ReceivePaymentTransactionList>(
				receivePaymentTransactionList);
	}
}
