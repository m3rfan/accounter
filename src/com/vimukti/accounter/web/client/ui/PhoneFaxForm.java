package com.vimukti.accounter.web.client.ui;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.vimukti.accounter.web.client.core.ClientFax;
import com.vimukti.accounter.web.client.core.ClientPhone;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author venki.p
 * 
 */
public class PhoneFaxForm extends DynamicForm {
	SelectItem businessPhoneSelect;
	private TextItem businessPhoneText, businessFaxText;
	private LinkedHashMap<Integer, ClientPhone> allPhones;
	private SelectItem businessFaxSelect;
	private LinkedHashMap<Integer, ClientFax> allFaxes;
	private ClientPhone toBeShownPhone = null;
	private ClientFax toBeShownFax = null;

	public PhoneFaxForm(Set<ClientPhone> phones, Set<ClientFax> faxes) {

		allPhones = new LinkedHashMap<Integer, ClientPhone>();
		allFaxes = new LinkedHashMap<Integer, ClientFax>();
		setPhonesAndFaxes(phones, faxes);
		setIsGroup(true);
		setGroupTitle(FinanceApplication.getFinanceUIConstants()
				.phoneAndFaxNumbers());
		setNumCols(3);
		businessPhoneSelect = new SelectItem("Phone");
		businessPhoneSelect.setWidth(85);
		businessPhoneSelect.getMainWidget().removeStyleName("gwt-ListBox");
		businessPhoneSelect.setValueMap(new ClientPhone().getPhoneTypes());
		businessPhoneSelect.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				ClientPhone p = allPhones
						.get(UIUtils.getPhoneType(businessPhoneSelect
								.getValue().toString()));
				if (p != null)
					businessPhoneText.setValue(p.getNumber());
				else
					businessPhoneText.setValue("");
			}
		});
		businessPhoneText = new TextItem(FinanceApplication
				.getFinanceUIConstants().phoneText());
		businessPhoneText.setShowTitle(false);
		businessPhoneText.setWidth(100);
		businessPhoneText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event != null) {
					try {
						String ph = businessPhoneText.getValue().toString();
						if (!ph.equals("") && !UIUtils.isValidPhone(ph)) {
							Accounter
									.showError(AccounterErrorType.INCORRECTINFORMATION);
							businessPhoneText.setValue("");
						} else {
							ClientPhone phone = new ClientPhone();
							phone.setType(UIUtils
									.getPhoneType(businessPhoneSelect
											.getValue().toString()));
							phone.setNumber(ph);
							allPhones.put(UIUtils
									.getPhoneType(businessPhoneSelect
											.getDisplayValue().toString()),
									phone);
						}
					} catch (Exception e) {
					}
				}
			}
		});
		if (toBeShownPhone != null) {
			businessPhoneSelect.setValue(toBeShownPhone.getPhoneTypes().get(
					toBeShownPhone.getType() + ""));
			businessPhoneText.setValue(toBeShownPhone.getNumber());
		} else
			businessPhoneSelect.setDefaultToFirstOption(true);
		businessFaxSelect = new SelectItem("Fax");
		businessFaxSelect.setWidth(85);
		businessFaxSelect.getMainWidget().removeStyleName("gwt-ListBox");
		businessFaxSelect.setValueMap(new ClientFax().getFaxTypes());
		businessFaxSelect.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				ClientFax f = allFaxes.get(UIUtils.getFaxType(businessFaxSelect
						.getValue().toString()));
				if (f != null)
					businessFaxText.setValue(f.getNumber());
				else
					businessFaxText.setValue("");
			}
		});
		businessFaxText = new TextItem(FinanceApplication
				.getFinanceUIConstants().businessFax());
		businessFaxText.setWidth(100);
		businessFaxText.setShowTitle(false);

		businessFaxText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event != null) {
					String fx = businessFaxText.getValue().toString();
					if (!fx.equals("") && !UIUtils.isValidFax(fx)) {
						Accounter
								.showError(AccounterErrorType.INCORRECTINFORMATION);
						businessFaxText.setValue("");
					} else {
						ClientFax fax = new ClientFax();
						fax.setType(UIUtils.getFaxType(businessFaxSelect
								.getDisplayValue().toString()));
						fax.setNumber(fx);
						allFaxes.put(UIUtils.getFaxType(businessFaxSelect
								.getDisplayValue().toString()), fax);
					}
				}

			}
		});
		if (toBeShownFax != null) {
			businessFaxSelect.setValue(toBeShownFax.getFaxTypes().get(
					toBeShownFax.getType() + ""));
			businessFaxText.setValue(toBeShownFax.getNumber() + "");
		} else
			businessFaxSelect.setDefaultToFirstOption(true);
		setFields(businessPhoneSelect, businessPhoneText, businessFaxSelect,
				businessFaxText);

		setWidth("100%");
		setHeight("100px");
	}

	private void setPhonesAndFaxes(Set<ClientPhone> phones, Set<ClientFax> faxes) {

		if (phones != null) {
			for (ClientPhone pho : phones) {
				if (pho.getIsSelected()) {
					toBeShownPhone = pho;
				}
				allPhones.put(pho.getType(), pho);

				// System.out.println("Existing Phones  Type " + pho.getType()
				// + " number is " + pho.getNumber() + " Is Selected"
				// + pho.getIsSelected());
			}
		}
		if (faxes != null) {
			for (ClientFax fax : faxes) {
				if (fax.getIsSelected()) {
					toBeShownFax = fax;
				}
				allFaxes.put(fax.getType(), fax);

				// System.out.println("Existing Faxes  Type " + fax.getType()
				// + " number is " + fax.getNumber() + " Is Selected"
				// + fax.getIsSelected());
			}
		}

	}

	public Set<ClientPhone> getAllPhones() {
		ClientPhone selectedPhoneFromSelect = allPhones.get(UIUtils
				.getPhoneType(businessPhoneSelect.getValue().toString()));
		if (selectedPhoneFromSelect != null) {
			selectedPhoneFromSelect.setIsSelected(true);
			allPhones.put(UIUtils.getPhoneType(businessPhoneSelect.getValue()
					.toString()), selectedPhoneFromSelect);

		}
		Collection<ClientPhone> pho = allPhones.values();
		Set<ClientPhone> toBeSetPhones = new HashSet<ClientPhone>();
		for (ClientPhone p : pho) {
			toBeSetPhones.add(p);
			// System.out.println("Sending Phones  Type " + p.getType()
			// + " number is " + p.getNumber() + " Is Selected"
			// + p.getIsSelected());
		}
		return toBeSetPhones;

	}

	public Set<ClientFax> getAllFaxes() {
		ClientFax selectedFaxFromSelect = allFaxes.get(UIUtils
				.getFaxType(businessFaxSelect.getValue().toString()));
		if (selectedFaxFromSelect != null) {
			selectedFaxFromSelect.setIsSelected(true);
			allFaxes
					.put(selectedFaxFromSelect.getType(), selectedFaxFromSelect);
		}
		Collection<ClientFax> faxs = allFaxes.values();
		Set<ClientFax> toBeSetFaxes = new HashSet<ClientFax>();
		for (ClientFax f : faxs) {
			toBeSetFaxes.add(f);
			// System.out.println("Sending Fax  Type " + f.getType()
			// + " number is " + f.getNumber() + " Is Selected"
			// + f.getIsSelected());
		}
		return toBeSetFaxes;

	}
}
