package com.vimukti.accounter.web.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.DemoCompany;

public class DemoCompanyManager {

	/*
	 * public static void main(String[] args) throws IOException { XStream
	 * xStream = new XStream(new DomDriver());
	 * 
	 * xStream = createAlias(xStream);
	 * 
	 * File file = getFile();
	 * 
	 * Object object = xStream.fromXML(new FileInputStream(file));
	 * 
	 * // return (DemoCompany) object; }
	 */

	private static XStream createAlias(XStream xStream) {

		xStream.alias("ClientCompany", DemoCompany.class);

		xStream.alias("preferences", ClientCompanyPreferences.class);

		xStream.alias("customers", List.class);
		xStream.alias("Customer", ClientCustomer.class);

		xStream.alias("banks", List.class);
		xStream.alias("ClientBank", ClientBank.class);

		xStream.alias("ClientAddress", ClientAddress.class);
		xStream.alias("ClientContact", ClientContact.class);

		xStream.alias("vendors", List.class);
		xStream.alias("Vendor", ClientVendor.class);

		xStream.alias("accounts", List.class);
		xStream.alias("BankAccount", ClientBankAccount.class);

		xStream.alias("items", List.class);
		xStream.alias("ClientItem", ClientItem.class);

		xStream.alias("quotes", List.class);
		xStream.alias("Quote", ClientEstimate.class);

		xStream.alias("transactionItems", List.class);
		xStream.alias("ClientTransactionItem", ClientTransactionItem.class);

		xStream.alias("invoices", List.class);
		xStream.alias("Invoice", ClientInvoice.class);

		xStream.alias("salesOrders", List.class);
		xStream.alias("ClientSalesOrder", ClientSalesOrder.class);

		xStream.alias("purchaseOrders", List.class);
		xStream.alias("ClientPurchaseOrder", ClientPurchaseOrder.class);

		xStream.alias("shippingTerms", List.class);
		xStream.alias("ClientShippingTerms", ClientShippingTerms.class);

		xStream.alias("shippingMethods", List.class);
		xStream.alias("ClientShippingMethod", ClientShippingMethod.class);

		xStream.alias("priceLevels", List.class);
		xStream.alias("ClientPriceLevel", ClientPurchaseOrder.class);

		xStream.alias("itemGroups", List.class);
		xStream.alias("ClientItemGroup", ClientPurchaseOrder.class);

		xStream.alias("taxGroups", List.class);
		xStream.alias("ClientTAXGroup", ClientPurchaseOrder.class);

		xStream.alias("paySalesTaxs", List.class);
		xStream.alias("ClientPaySalesTax", ClientPurchaseOrder.class);

		xStream.alias("creditRatings", List.class);
		xStream.alias("ClientCreditRating", ClientPurchaseOrder.class);

		xStream.alias("salesPersons", List.class);
		xStream.alias("ClientSalesPerson", ClientPurchaseOrder.class);

		xStream.alias("taxItemGroups", List.class);
		xStream.alias("ClientTAXItemGroup", ClientPurchaseOrder.class);

		return xStream;

	}

	private static File getFile() {
		return new File(ServerConfiguration.getDefaultCompanyDir()
				+ "/unitedkingdom.xml");
	}

	public DemoCompany getDemoCompany() throws IOException {
		XStream xStream = new XStream(new DomDriver());

		xStream = createAlias(xStream);

		File file = getFile();

		Object object = xStream.fromXML(new FileInputStream(file));

		return (DemoCompany) object;
	}

}
