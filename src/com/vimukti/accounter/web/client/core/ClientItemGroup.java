package com.vimukti.accounter.web.client.core;

import java.util.List;

@SuppressWarnings("serial")
public class ClientItemGroup implements IAccounterCore {

	int version;

	String name;

	long id;

	boolean isDefault;

	List<ClientItem> items;

	public List<ClientItem> getItems() {
		return items;
	}

	public void setItems(List<ClientItem> items) {
		this.items = items;
	}

	public ClientItemGroup() {
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param nmame
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ITEM_GROUP;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientItemGroup";
	}

}
