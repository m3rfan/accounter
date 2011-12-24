package com.vimukti.accounter.mobile.commands.preferences;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.commands.AbstractCommand;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.OperationContext;
import com.vimukti.accounter.web.server.managers.CompanyManager;

public abstract class AbstractCompanyPreferencesCommand extends
		AbstractCommand {

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void savePreferences(Context context,
			ClientCompanyPreferences preferences) {
		try {
			CompanyManager companyManager = new FinanceTool()
					.getCompanyManager();
			ClientCompany clientCompany = companyManager.getClientCompany(
					context.getEmailId(), getCompanyId());

			OperationContext opContext = new OperationContext(getCompanyId(),
					clientCompany, context.getEmailId());

			clientCompany.setPreferences(preferences);
			companyManager.updateCompany(opContext);

		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}

}
