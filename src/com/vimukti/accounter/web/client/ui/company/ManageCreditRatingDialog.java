//package com.vimukti.accounter.web.client.ui.company;
//
//import java.util.List;
//
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.vimukti.accounter.web.client.core.AccounterCoreType;
//import com.vimukti.accounter.web.client.core.ClientCreditRating;
//import com.vimukti.accounter.web.client.ui.FinanceApplication;
//import com.vimukti.accounter.web.client.ui.core.GroupDialog;
//import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
//
//public class ManageCreditRatingDialog extends GroupDialog<ClientCreditRating> {
//
//	protected GroupDialogButtonsHandler groupDialogButtonHandler;
//
//	public ManageCreditRatingDialog(String title, String description) {
//		super(title, description);
//		initialise();
//		center();
//	}
//
//	public void initialise() {
//		getGrid().setType(AccounterCoreType.CREDIT_RATING);
//		setSize("30%", "40%");
//		groupDialogButtonHandler = new GroupDialogButtonsHandler() {
//
//			public void onCloseButtonClick() {
//				// TODO Auto-generated method stub
//
//			}
//
//			public void onFirstButtonClick() {
//				// TODO Auto-generated method stub
//
//			}
//
//			public void onSecondButtonClick() {
//				// TODO Auto-generated method stub
//
//			}
//
//			public void onThirdButtonClick() {
//				// TODO Auto-generated method stub
//
//			}
//
//		};
//	}CREDIT_RATING
//
//	@Override
//	public Object getGridColumnValue(IsSerializable obj, int index) {
//		ClientCreditRating rec = (ClientCreditRating) obj;
//		switch (index) {
//		case 0:
//			if (rec.getName() != null)
//				return rec.getName();
//			else
//				return "";
//		default:
//			return "";
//		}
//	}
//
//	@Override
//	public String[] setColumns() {
//		return new String[] { constants.Name() };
//	}
//
//	@Override
//	protected List<ClientCreditRating> getRecords() {
//		return FinanceApplication.getCompany().getCreditRatings();
//	}
//
//	public void deleteCallBack() {
//	}
//
//	public void addCallBack() {
//	}
//
//	public void editCallBack() {
//	}
//
//}
