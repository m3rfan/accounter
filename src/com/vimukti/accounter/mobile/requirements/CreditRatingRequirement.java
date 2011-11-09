package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.CreditRating;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;

public abstract class CreditRatingRequirement extends
		ListRequirement<CreditRating> {

	public CreditRatingRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<CreditRating> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().creditRating());
	}

	@Override
	protected Record createRecord(CreditRating value) {
		Record record = new Record(value);
		record.add("Name", value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(CreditRating value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().creditRating()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().creditRating());
	}

}
