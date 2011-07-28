package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class AutomaticSequenceAction extends Action {

	public AutomaticSequenceAction(String text) {
		super(text);
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

//	@Override
//	public ParentCanvas getView() {
//		return null;
//	}

	@Override
	public void run(Object data, Boolean isDependent) {
		try {
			AutomaticSequenceDialog automaticSequenceDialog = new AutomaticSequenceDialog(
					Accounter.constants()
							.automaticSequencing(), "");
			automaticSequenceDialog.show();
			automaticSequenceDialog.center();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
