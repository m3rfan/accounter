package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ClientFax implements IAccounterCore {

	public static final int TYPE_BUSINESS = 1;
	public static final int TYPE_HOME = 2;
	public static final int TYPE_OTHER = 3;

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

	public List<String> getFaxTypes() {
		List<String> valueMap = new ArrayList<String>();
		valueMap.add("Company");
		valueMap.add("Home");
		valueMap.add("Other");
		return valueMap;

	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.FAX;
	}

	@Override
	public long getID() {
		return 0;
	}

	@Override
	public void setID(long id) {
		// this.id=id;

	}

	@Override
	public String getClientClassSimpleName() {
		// its not using any whereb
		return null;
	}
}
