/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vimukti16
 * 
 */
@SuppressWarnings("serial")
public class ClientFixedAsset implements IAccounterCore {

	public static final int METHOD_STRAIGHT_LINE = 1;
	public static final int METHOD_REDUCING_BALANCE = 2;

	public static final int STATUS_PENDING = 1;
	public static final int STATUS_REGISTERED = 2;
	public static final int STATUS_SOLD_OR_DISPOSED = 3;

	/**
	 * This is automatically generated by Hibernate which is Unique accross the
	 * Finance.
	 * 
	 */
	long id;

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
	 */
	private String assetAccount;

	String linkedAccumulatedDepreciationAccount;

	/**
	 * Date of Purchase
	 */
	private long purchaseDate;

	/**
	 * Purchase Price
	 */
	private double purchasePrice;

	/**
	 * Fixed Asset Description
	 */
	private String description;

	/**
	 * Asset Type
	 */
	private String assetType;

	private double depreciationRate;

	private int depreciationMethod;

	/**
	 * Depreciation Expense Account
	 * 
	 */
	private String depreciationExpenseAccount;

	private double accumulatedDepreciationAmount = 0.0;

	private int status;

	private double bookValue;

	private boolean isSoldOrDisposed;
	private List<ClientFixedAssetNote> fixedAssetNotes = new ArrayList<ClientFixedAssetNote>();
	private List<ClientFixedAssetHistory> fixedAssetsHistory = new ArrayList<ClientFixedAssetHistory>();

	/**
	 * Selling OR Disposing Fixed Asset
	 * 
	 */

	private long soldOrDisposedDate;

	private ClientAccount accountForSale;

	private double salePrice = 0.0;

	private boolean noDepreciation;

	private long depreciationTillDate;

	private String notes;

	private String lossOrGainOnDisposalAccount;

	private String totalCapitalGain;
	private double totalCapitalGainAmount;

	private double lossOrGain;

	// private String sellingOrDisposingFixedAsset;

	public ClientFixedAsset() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLinkedAccumulatedDepreciationAccount() {
		return linkedAccumulatedDepreciationAccount;
	}

	public void setLinkedAccumulatedDepreciationAccount(
			String linkedAccumulatedDepreciationAccount) {
		this.linkedAccumulatedDepreciationAccount = linkedAccumulatedDepreciationAccount;
	}

	public String getStringID() {
		return stringID;
	}

	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

	public String getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}

	public String getAssetAccount() {
		return assetAccount;
	}

	public void setAssetAccount(String assetAccount) {
		this.assetAccount = assetAccount;
	}

	public long getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(long purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public double getDepreciationRate() {
		return depreciationRate;
	}

	public void setDepreciationRate(double depreciationRate) {
		this.depreciationRate = depreciationRate;
	}

	public int getDepreciationMethod() {
		return depreciationMethod;
	}

	public void setDepreciationMethod(int depreciationMethod) {
		this.depreciationMethod = depreciationMethod;
	}

	public String getDepreciationExpenseAccount() {
		return depreciationExpenseAccount;
	}

	public void setDepreciationExpenseAccount(String depreciationExpenseAccount) {
		this.depreciationExpenseAccount = depreciationExpenseAccount;
	}

	public double getAccumulatedDepreciationAmount() {
		return accumulatedDepreciationAmount;
	}

	public void setAccumulatedDepreciationAmount(
			double accumulatedDepreciationAmount) {
		this.accumulatedDepreciationAmount = accumulatedDepreciationAmount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getBookValue() {
		return bookValue;
	}

	public void setBookValue(double bookValue) {
		this.bookValue = bookValue;
	}

	public boolean isSoldOrDisposed() {
		return isSoldOrDisposed;
	}

	public void setSoldOrDisposed(boolean isSoldOrDisposed) {
		this.isSoldOrDisposed = isSoldOrDisposed;
	}

	// public String getSellingOrDisposingFixedAsset() {
	// return sellingOrDisposingFixedAsset;
	// }
	//
	// public void setSellingOrDisposingFixedAsset(
	// String sellingOrDisposingFixedAsset) {
	// this.sellingOrDisposingFixedAsset = sellingOrDisposingFixedAsset;
	// }

	@Override
	public String getClientClassSimpleName() {
		return "ClientFixedAsset";
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.FIXEDASSET;
	}

	public List<ClientFixedAssetNote> getFixedAssetNotes() {
		return fixedAssetNotes;
	}

	public void setFixedAssetNotes(List<ClientFixedAssetNote> fixedAssetNotes) {
		this.fixedAssetNotes = fixedAssetNotes;
	}

	public List<ClientFixedAssetHistory> getFixedAssetsHistory() {
		return fixedAssetsHistory;
	}

	public void setFixedAssetsHistory(
			List<ClientFixedAssetHistory> fixedAssetsHistory) {
		this.fixedAssetsHistory = fixedAssetsHistory;
	}

	public void setSoldOrDisposedDate(long soldOrDisposedDate) {
		this.soldOrDisposedDate = soldOrDisposedDate;
	}

	public long getSoldOrDisposedDate() {
		return soldOrDisposedDate;
	}

	public void setAccountForSale(ClientAccount accountForSale) {
		this.accountForSale = accountForSale;
	}

	public ClientAccount getAccountForSale() {
		return accountForSale;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setNoDepreciation(boolean noDepreciation) {
		this.noDepreciation = noDepreciation;
	}

	public boolean isNoDepreciation() {
		return noDepreciation;
	}

	public void setDepreciationTillDate(long depreciationTillDate) {
		this.depreciationTillDate = depreciationTillDate;
	}

	public long getDepreciationTillDate() {
		return depreciationTillDate;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param lossOrGainOnDisposalAccount
	 *            the lossOrGainOnDisposalAccount to set
	 */
	public void setLossOrGainOnDisposalAccount(
			String lossOrGainOnDisposalAccount) {
		this.lossOrGainOnDisposalAccount = lossOrGainOnDisposalAccount;
	}

	/**
	 * @return the lossOrGainOnDisposalAccount
	 */
	public String getLossOrGainOnDisposalAccount() {
		return lossOrGainOnDisposalAccount;
	}

	/**
	 * @param totalCapitalGain
	 *            the totalCapitalGain to set
	 */
	public void setTotalCapitalGain(String totalCapitalGain) {
		this.totalCapitalGain = totalCapitalGain;
	}

	/**
	 * @return the totalCapitalGain
	 */
	public String getTotalCapitalGain() {
		return totalCapitalGain;
	}

	/**
	 * @param lossOrGain
	 *            the lossOrGain to set
	 */
	public void setLossOrGain(double lossOrGain) {
		this.lossOrGain = lossOrGain;
	}

	/**
	 * @return the lossOrGain
	 */
	public double getLossOrGain() {
		return lossOrGain;
	}

	/**
	 * @param totalCapitalGainAmount
	 *            the totalCapitalGainAmount to set
	 */
	public void setTotalCapitalGainAmount(double totalCapitalGainAmount) {
		this.totalCapitalGainAmount = totalCapitalGainAmount;
	}

	/**
	 * @return the totalCapitalGainAmount
	 */
	public double getTotalCapitalGainAmount() {
		return totalCapitalGainAmount;
	}

}
