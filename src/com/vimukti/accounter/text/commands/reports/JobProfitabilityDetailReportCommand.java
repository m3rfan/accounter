package com.vimukti.accounter.text.commands.reports;

import com.vimukti.accounter.text.commands.AbstractReportCommand;

public class JobProfitabilityDetailReportCommand extends AbstractReportCommand {

	@Override
	public int getReportType() {
		return ReportTypeConstants.REPORT_TYPE_JOB_PROFITABILITY_BY_JOBID;
	}

}
