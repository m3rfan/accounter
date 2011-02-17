package com.vimukti.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

@SuppressWarnings("serial")
public class FixedAssetList implements IsSerializable, Serializable {

	/**
	 * This will hold a secure 40 digit random number.
	 */
	private String stringID;

	/**
	 * Unique Item ID, for which the
	 */
	private String name;

	private String assetNumber;

	/**
	 * Asset Account
	 * 
	 * @see
	 */
	private String assetAccount;

	/**
	 * Date of Purchase
	 */
	private ClientFinanceDate purchaseDate;

	/**
	 * Purchase Price
	 */
	private double purchasePrice;

	private double bookValue;

	/**
	 * @return the stringID
	 */
	public String getStringID() {
		return stringID;
	}

	/**
	 * @param stringID
	 *            the stringID to set
	 */
	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the assetNumber
	 */
	public String getAssetNumber() {
		return assetNumber;
	}

	/**
	 * @param assetNumber
	 *            the assetNumber to set
	 */
	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}

	/**
	 * @return the assetAccount
	 */
	public String getAssetAccount() {
		return assetAccount;
	}

	/**
	 * @param assetAccount
	 *            the assetAccount to set
	 */
	public void setAssetAccount(String assetAccount) {
		this.assetAccount = assetAccount;
	}

	/**
	 * @return the purchaseDate
	 */
	public ClientFinanceDate getPurchaseDate() {
		return purchaseDate;
	}

	/**
	 * @param purchaseDate
	 *            the purchaseDate to set
	 */
	public void setPurchaseDate(ClientFinanceDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	/**
	 * @return the purchasePrice
	 */
	public double getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * @param purchasePrice
	 *            the purchasePrice to set
	 */
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	/**
	 * @return the bookValue
	 */
	public double getBookValue() {
		return bookValue;
	}

	/**
	 * @param bookValue
	 *            the bookValue to set
	 */
	public void setBookValue(double bookValue) {
		this.bookValue = bookValue;
	}

}
