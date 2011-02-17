/**
 * 
 */
package com.vimukti.accounter.web.client.core;

/**
 * @author vimukti5
 * 
 *         Parent Class for ClientVatItem and ClientVatGroup
 * 
 */
@SuppressWarnings("serial")
public class ClientTAXItemGroup implements IAccounterCore {

	long id;

	String stringID;

	/**
	 * Name of the Tax Group which is unique for every TaxGroup
	 */
	String name;

	/**
	 * Description about the VAT item (About its VAT codes, rates, VAT agncies
	 * etc).
	 */
	String description;

	boolean isActive;
	boolean isSalesType;
	boolean isPercentage;
	boolean isDefault;

	transient boolean isImported;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#getClientClassSimpleName
	 * ()
	 */
	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#getDisplayName()
	 */
	@Override
	public String getDisplayName() {

		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getObjectType()
	 */
	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.TAX_ITEM_GROUP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getStringID()
	 */
	@Override
	public String getStringID() {

		return this.stringID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#setStringID(java
	 * .lang.String)
	 */
	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	/**
	 * @return the isSalesType
	 */
	public boolean isSalesType() {
		return isSalesType;
	}

	/**
	 * @param isSalesType
	 *            the isSalesType to set
	 */
	public void setSalesType(boolean isSalesType) {
		this.isSalesType = isSalesType;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the isPercentage
	 */
	public boolean isPercentage() {
		return isPercentage;
	}

	/**
	 * @param isPercentage
	 *            the isPercentage to set
	 */
	public void setPercentage(boolean isPercentage) {
		this.isPercentage = isPercentage;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

}
