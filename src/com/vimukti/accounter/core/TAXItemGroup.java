/**
 * 
 */
package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.bizantra.server.storage.HibernateUtil;
import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.InvalidOperationException;

/**
 * 
 * A Parent Class for VATGroup and VATItem. It contains all the information that
 * is common to these sub classes.
 * 
 * @author Chandan 
 */

public class TAXItemGroup implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8121892431883668109L;

	long id;

	String stringID;

	/**
	 * Name of the Tax Group which is unique for every TaxGroup
	 */
	String name;

	/**
	 * Description about the VAT item (About its VAT codes, rates, VAT agencies
	 * etc).
	 */
	String description;

	boolean isActive;
	boolean isSalesType;
	boolean isPercentage;

	transient boolean isImported;
	String createdBy;
	String lastModifier;
	FinanceDate createdDate;
	FinanceDate lastModifiedDate;
	boolean isDefault;

	private boolean isOnSaveProccessed;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.core.IAccounterServerCore#getStringID()
	 */
	@Override
	public String getStringID() {
		// TODO Auto-generated method stub
		return this.stringID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.core.IAccounterServerCore#setImported(boolean)
	 */
	@Override
	public void setImported(boolean isImported) {
		this.isImported = isImported;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.core.IAccounterServerCore#setStringID(java.lang
	 *      .String)
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		Session session = HibernateUtil.getCurrentSession();
		TAXItemGroup taxItemGroup = (TAXItemGroup) clientObject;
		// Query query =
		// session.createQuery("from VATItemGroup V where V.name =: name")
		// .setParameter("name", vatItemGroup.name);
		Query query = session.getNamedQuery("getTAXItemGroupWithSameName").setParameter("name", this.name)
				.setParameter("id", this.id);
		List list = query.list();
		if (list != null && list.size() > 0) {
			throw new InvalidOperationException(
					"A VATItem or VATGroup already exists with this name");
		}
		return true;
	}
}
