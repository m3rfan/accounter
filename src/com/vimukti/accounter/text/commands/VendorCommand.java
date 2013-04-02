package com.vimukti.accounter.text.commands;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * Creating Vendor Command below fields
 * 
 * Name,VendorSince,OpeningBalance,Address,Web Address,Email,Phone,Fax
 * 
 * @author vimukti10
 * 
 */
public class VendorCommand extends CreateOrUpdateCommand {

	private String name;
	private FinanceDate vendorSince;
	private double openingBalance;
	private Address address;
	private String webAddress;
	private String email;
	private String phone;
	private String fax;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {

		name = data.nextString("");
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		// if next date is null,then set the default present date
		vendorSince = data.nextDate(new FinanceDate());
		if (!data.isDouble()) {
			respnse.addError("Invalid Double for Opening Balance");
			return false;
		}
		openingBalance = data.nextDouble(0);
		address = data.nextAddress(null);
		webAddress = data.nextString("");
		email = data.nextString("");
		phone = data.nextString("");
		fax = data.nextString("");

		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Criteria query = session.createCriteria(Customer.class);
		query.add(Restrictions.eq("company", getCompany()));
		query.add(Restrictions.eq("name", name));
		Vendor vendor = (Vendor) query.uniqueResult();
		if (vendor == null) {
			vendor = new Vendor();
		}
		vendor.setName(name);
		vendor.setPayeeSince(vendorSince);
		vendor.setOpeningBalance(openingBalance);
		if (address != null) {
			vendor.getAddress().add(address);
		}
		vendor.setWebPageAddress(webAddress);
		vendor.setEmail(email);
		vendor.setPhoneNo(phone);
		vendor.setFaxNo(fax);

		saveOrUpdate(vendor);

	}
}
