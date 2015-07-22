package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.TransactionReceivePayment;

public class ReceivePaymentMigrator extends TransactionMigrator<ReceivePayment> {
	@Override
	public JSONObject migrate(ReceivePayment obj, MigratorContext context)
			throws JSONException {

		JSONObject jsonObj = super.migrate(obj, context);
		jsonObj.put("payee", context.get("Customer", obj.getCustomer().getID()));
		// Deposit In Account
		Account depositIn = obj.getDepositIn();
		if (depositIn != null) {
			jsonObj.put("depositIn", context.get("Account", depositIn.getID()));
		}
		// Amount Received
		jsonObj.put("amountReceived", obj.getAmount());
		// TDS Amount
		jsonObj.put("tDSAmount", obj.getTdsTotal());

		List<TransactionReceivePayment> transactionReceivePayment = obj
				.getTransactionReceivePayment();
		JSONArray array = new JSONArray();
		for (TransactionReceivePayment item : transactionReceivePayment) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("dueDate", item.getDueDate().getAsDateObject()
					.getTime());
			jsonObject.put("invoice",
					context.get("Invoice", item.getInvoice().getID()));
			Account account = item.getDiscountAccount();
			if (account != null) {
				jsonObject.put("discountAccount",
						context.get("Account", account.getID()));
			}

			JSONObject jsonWritOffObject = new JSONObject();
			Account writeOffAccount = item.getWriteOffAccount();
			if (writeOffAccount != null) {
				jsonWritOffObject.put("writeoffAccount",
						context.get("Account", writeOffAccount.getID()));
				jsonWritOffObject.put("amount", item.getWriteOff());
				jsonObject.put("writeOff", jsonWritOffObject);
			}
			array.put(jsonObject);
		}
		jsonObj.put("paymentItems", array);

		// PaymentableTransaction
		jsonObj.put("paymentMethod", PicklistUtilMigrator
				.getPaymentMethodIdentifier(obj.getPaymentMethod()));
		try {
			Long chequeNumber = Long.valueOf(obj.getCheckNumber());
			jsonObj.put("chequeNumber", chequeNumber);
		} catch (Exception e) {
		}
		return jsonObj;
	}
}
