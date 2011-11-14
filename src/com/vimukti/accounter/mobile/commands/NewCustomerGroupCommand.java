package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class NewCustomerGroupCommand extends NewAbstractTransactionCommand {
	private static final String CUSTPMERGROUP_NAME = "CustomerGroup Name";

	private ClientCustomerGroup customerGroup;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				return "Customers";
			}
			ClientCustomerGroup customerGroupByName = CommandUtils
					.getCustomerGroupByName(context.getCompany(), string);
			if (customerGroupByName == null) {
				return "Customers " + string;
			}
			customerGroup = customerGroupByName;
			get(CUSTPMERGROUP_NAME).setValue(customerGroup.getName());
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(CUSTPMERGROUP_NAME).setValue(string);
			}
			customerGroup = new ClientCustomerGroup();
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		if (customerGroup.getID() == 0) {
			return "Creating new Customer Group... ";
		} else {
			return "Updating Custoerm Group " + customerGroup.getDisplayName();
		}
	}

	@Override
	protected String getDetailsMessage() {
		if (customerGroup.getID() == 0) {
			return getMessages().readyToCreate(
					getMessages().payeeGroup(Global.get().customer()));
		} else {
			return getMessages().readyToUpdate(
					getMessages().payeeGroup(Global.get().customer()));
		}
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		if (customerGroup.getID() == 0) {
			return getMessages().createSuccessfully(
					getMessages().payeeGroup(Global.get().customer()));
		} else {
			return getMessages().updateSuccessfully(
					getMessages().payeeGroup(Global.get().customer()));
		}
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(CUSTPMERGROUP_NAME,
				"Please Enter Customer Group Name", getMessages().payeeGroup(
						Global.get().Customer()), false, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		customerGroup.setName((String) get(CUSTPMERGROUP_NAME).getValue());
		create(customerGroup, context);
		return null;
	}
	// @Override
	// public String getId() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// protected void addRequirements(List<Requirement> list) {
	// list.add(new Requirement(CUSTPMERGROUP_NAME, false, true));
	//
	// }
	//
	// @Override
	// public Result run(Context context) {
	//
	// Object attribute = context.getAttribute(INPUT_ATTR);
	// if (attribute == null) {
	// context.setAttribute(INPUT_ATTR, "optional");
	// }
	// Result makeResult = context.makeResult();
	// makeResult.add(getMessages().readyToCreate(
	// getMessages().payeeGroup(Global.get().Customer())));
	// ResultList list = new ResultList("values");
	// makeResult.add(list);
	// ResultList actions = new ResultList(ACTIONS);
	// makeResult.add(actions);
	//
	// Result result = nameRequirement(
	// context,
	// list,
	// CUSTPMERGROUP_NAME,
	// getMessages().payeeGroup(Global.get().Customer()),
	// getMessages().pleaseEnter(
	// getMessages().payeeGroup(Global.get().Customer())));
	// if (result != null) {
	// return result;
	// }
	// markDone();
	// return createcustomerGroupObject(context);
	// }
	//
	// /**
	// *
	// * @param context
	// * @return
	// */
	// private Result createcustomerGroupObject(Context context) {
	// ClientCustomerGroup customerGroup = new ClientCustomerGroup();
	// customerGroup.setName((String) get(CUSTPMERGROUP_NAME).getValue());
	// create(customerGroup, context);
	// markDone();
	// Result result = new Result();
	// result.add(getMessages().createSuccessfully(
	// getMessages().payeeGroup(Global.get().Customer())));
	//
	// return result;
	// }

}
