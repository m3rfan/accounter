package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.grids.JobListGrid;

public class JobListView extends BaseListView<ClientJob> implements
		IPrintableView {

	@Override
	public void updateInGrid(ClientJob objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getJobs(this);
	}

	@Override
	public void onSuccess(PaginationList<ClientJob> result) {
		super.onSuccess(result);

	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return false;
	}

	@Override
	protected void initGrid() {
		viewSelect.setVisible(false);
		grid = new JobListGrid();
		grid.init();

	}

	@Override
	protected String getListViewHeading() {
		return messages.jobList();
	}

	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getNewJobAction(null);
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.addNew(messages.job());

	}

	@Override
	protected String getViewTitle() {
		return messages.jobList();
	}

}