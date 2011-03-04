package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ClientPhone implements IAccounterCore {

	int version;

	public static final int BUSINESS_PHONE_NUMBER = 1;
	public static final int MOBILE_PHONE_NUMBER = 2;
	public static final int HOME_PHONE_NUMBER = 3;
	public static final int ASSISTANT_PHONE_NUMBER = 4;
	public static final int OTHER_PHONE_NUMBER = 5;

	int type;
	String number = "";
	boolean isSelected = false;

	/**
	 * @return the id
	 */

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the isSelected
	 */
	public boolean getIsSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected
	 *            the isSelected to set
	 */
	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public List<String> getPhoneTypes() {
		List<String> valueMap = new ArrayList<String>();
		valueMap.add("Company");
		valueMap.add("Mobile");
		valueMap.add("Home");
		valueMap.add("Assistant");
		valueMap.add("Other");
		return valueMap;

	}

	@Override
	public String getName() {
		return getName();
	}

	@Override
	public String getDisplayName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.PHONE;
	}

	@Override
	public String getStringID() {
		return null;
	}

	@Override
	public void setStringID(String stringID) {
		// this.stringID = stringID;

	}

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

}
