package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientSalesOrder extends ClientTransaction {

	String customer;

	ClientContact contact;

	ClientAddress billingAddress;

	ClientAddress shippingAdress;

	String phone;

	String salesPerson;

	String paymentTerm;

	String shippingTerm;

	String shippingMethod;

	long dueDate;

	double discountTotal;

	String priceLevel;

	double salesTaxAmount;

	String estimate;

	String salesTaxItem;

	/**
	 * To give the user the feature to maintain his own number to know about
	 * this SalesOrder
	 */
	String customerOrderNumber;

	public ClientSalesOrder() {
		super();
		setType(ClientTransaction.TYPE_SALES_ORDER);
	}

	public ClientSalesOrder(ClientEstimate estimate) {
		super();
		setType(ClientTransaction.TYPE_SALES_ORDER);

		this.customer = estimate.customer;
		this.contact = estimate.contact;
		this.phone = estimate.getPhone();
		this.billingAddress = estimate.address;

		this.paymentTerm = estimate.paymentTerm;
		this.salesPerson = estimate.salesPerson;
		this.priceLevel = estimate.priceLevel;
		this.salesTaxAmount = estimate.getSalesTax();
		this.total = estimate.getTotal();
		// this.deliverydate = estimate.getDeliveryDate();

		this.transactionDate = estimate.getDate().getTime();
		this.transactionItems = estimate.getTransactionItems();

		this.memo = estimate.getMemo();
		this.reference = estimate.getReference();
	}

	/**
	 * @return the customerOrderNumber
	 */
	public String getCustomerOrderNumber() {
		return customerOrderNumber;
	}

	/**
	 * @param customerOrderNumber
	 *            the customerOrderNumber to set
	 */
	public void setCustomerOrderNumber(String customerOrderNumber) {
		this.customerOrderNumber = customerOrderNumber;
	}

	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * @return the contact
	 */
	public ClientContact getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	/**
	 * @return the billingAddress
	 */
	public ClientAddress getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @param billingAddress
	 *            the billingAddress to set
	 */
	public void setBillingAddress(ClientAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @return the shippingAdress
	 */
	public ClientAddress getShippingAdress() {
		return shippingAdress;
	}

	/**
	 * @param shippingAdress
	 *            the shippingAdress to set
	 */
	public void setShippingAdress(ClientAddress shippingAdress) {
		this.shippingAdress = shippingAdress;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the salesPerson
	 */
	public String getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @param salesPerson
	 *            the salesPerson to set
	 */
	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	/**
	 * @return the paymentTerm
	 */
	public String getPaymentTerm() {
		return paymentTerm;
	}

	/**
	 * @param paymentTerm
	 *            the paymentTerm to set
	 */
	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @return the shippingTerm
	 */
	public String getShippingTerm() {
		return shippingTerm;
	}

	/**
	 * @param shippingTerm
	 *            the shippingTerm to set
	 */
	public void setShippingTerm(String shippingTerm) {
		this.shippingTerm = shippingTerm;
	}

	/**
	 * @return the shippingMethod
	 */
	public String getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * @param shippingMethod
	 *            the shippingMethod to set
	 */
	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * @return the priceLevel
	 */
	public String getPriceLevel() {
		return priceLevel;
	}

	/**
	 * @param priceLevel
	 *            the priceLevel to set
	 */
	public void setPriceLevel(String priceLevel) {
		this.priceLevel = priceLevel;
	}

	/**
	 * @return the estimate
	 */
	public String getEstimate() {
		return estimate;
	}

	/**
	 * @param estimate
	 *            the estimate to set
	 */
	public void setEstimate(String estimate) {
		this.estimate = estimate;
	}

	/**
	 * @return the salesTaxItem
	 */
	public String getSalesTaxItem() {
		return salesTaxItem;
	}

	/**
	 * @param salesTaxItem
	 *            the salesTaxItem to set
	 */
	public void setSalesTaxItem(String salesTaxItem) {
		this.salesTaxItem = salesTaxItem;
	}

	/**
	 * @return the dueDate
	 */
	public long getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(long dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the discountTotal
	 */
	public double getDiscountTotal() {
		return discountTotal;
	}

	/**
	 * @param discountTotal
	 *            the discountTotal to set
	 */
	public void setDiscountTotal(double discountTotal) {
		this.discountTotal = discountTotal;
	}

	public double getSalesTaxAmount() {
		return salesTaxAmount;
	}

	/**
	 * @param salesTaxAmount
	 *            the salesTaxAmount to set
	 */
	public void setSalesTaxAmount(double salesTaxAmount) {
		this.salesTaxAmount = salesTaxAmount;
	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientSalesOrder";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.SALESORDER;
	}

	@Override
	public String getStringID() {

		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {

		this.stringID = stringID;
	}

}
