package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AbstractBaseDialog;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

/**
 * 
 * @author Mandeep Singh
 * 
 */
public class CompanySetupDialog extends AbstractBaseDialog<ClientCompany> {

	Label companyPrefLabel, step1Label, step2Label, step3Label, typeLabel,
			descLabel;
	TextItem companyNameText, legalNameText, street1Text, street2Text,
			cityText, stateText, zipText, phoneText, faxText, emailText,
			websiteText, taxIDText;
	SelectItem countrySelect, accountType;
	DialogGrid typeGrid;
	Button helpButt, backButt, nextButt, finButt, canButt, addBusinessTypeButt;
	DynamicForm companyForm;

	VerticalPanel step1Canvas = null, step2Canvas = null, step3Canvas = null;

	ClientUser user;

	final private int STEP1 = 1, STEP2 = 2, STEP3 = 3;

	int currentStep;

	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);

	@SuppressWarnings("unused")
	private static final String[] typeRecords = new String[] {
			FinanceApplication.getCompanyMessages().basic(),
			FinanceApplication.getCompanyMessages().healthcareProfessional(),
			FinanceApplication.getCompanyMessages().homeBasedSales(),
			FinanceApplication.getCompanyMessages().insuranceAgency(),
			FinanceApplication.getCompanyMessages().ITServices(),
			FinanceApplication.getCompanyMessages().lawFirm(),
			FinanceApplication.getCompanyMessages().manufacturing(),
			FinanceApplication.getCompanyMessages()
					.manufacturingRepresentative(),
			FinanceApplication.getCompanyMessages().notForProfitOrganization(),
			FinanceApplication.getCompanyMessages().personalCareSalon(),
			FinanceApplication.getCompanyMessages().personalInstructor(),
			FinanceApplication.getCompanyMessages().petCare(),
			FinanceApplication.getCompanyMessages().photographer(),
			FinanceApplication.getCompanyMessages().propertyManagement(),
			FinanceApplication.getCompanyMessages().realEstateAgent(),
			FinanceApplication.getCompanyMessages().restaurant(),
			FinanceApplication.getCompanyMessages().retail(),
			FinanceApplication.getCompanyMessages().shippingAndPostalService(),
			FinanceApplication.getCompanyMessages().webBasedSales() };

	public CompanySetupDialog(AbstractBaseView<ClientCompany> parent) {
		super(parent);
		currentStep = STEP1;
		prepareCanvases();

		// setShowEdges(false);//Strange!
		// setShowHeader(false);

		add(step1Canvas);
		setSize("80%", "90%");
		show();
	}

	private void prepareCanvases() {
		createStep1Canvas();
		createStep2Canvas();
	}

	private ClientCompany getCompanyObject() {

		ClientCompany company = new ClientCompany();

		if (companyNameText.getValue() != null) {
			company.setName(companyNameText.getValue().toString());
		}
		if (legalNameText.getValue() != null) {

			company.setTradingName(legalNameText.getValue().toString());
		}
		if (accountType.getValue().toString().equalsIgnoreCase(
				FinanceApplication.getCompanyMessages().UK())) {
			company.setAccountingType(1);
		} else if (accountType.getValue().toString().equalsIgnoreCase(
				FinanceApplication.getCompanyMessages().US())) {

			company.setAccountingType(0);
		}

		if (taxIDText.getValue() != null) {
			company.setTaxId(taxIDText.getValue().toString());
		}

		ClientAddress a = new ClientAddress();
		if (street1Text.getValue() != null && street2Text.getValue() != null) {
			a.setStreet(street1Text.getValue().toString() + " "
					+ street2Text.getValue().toString());
		}

		if (cityText.getValue() != null) {
			a.setCity(cityText.getValue().toString());
		}
		if (stateText.getValue() != null) {
			a.setStateOrProvinence(stateText.getValue().toString());
		}
		if (zipText.getValue() != null) {
			a.setZipOrPostalCode(zipText.getValue().toString());
		}

		// company.setCompanyAddress(a);

		ClientContact c = new ClientContact();
		if (phoneText.getValue() != null) {
			c.setBusinessPhone(phoneText.getValue().toString());
		}
		if (emailText.getValue() != null) {
			c.setEmail(emailText.getValue().toString());
		}

		company.setContact(c);

		// company.setUser(FinanceApplication.getUser());

		company.setIndustry(ClientCompany.TYPE_BASIC);

		return company;
	}

	private void addEventHandlers() {
		helpButt.addClickHandler(UIUtils.todoClick());

		backButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				createPrevStepCanvas();
			}

		});

		nextButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (companyForm.validate())
					createNextStepCanvas();
			}
		});

		canButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// FIXME--need some action
				// cancelClick();
			}
		});

		finButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (validateForm()) {
					createCompany();
				}
			}
		});
	}

	protected boolean validateForm() {
		return companyForm.validate();
	}

	private void createCompany() {
		// UIUtils.log("Creating company for ["
		// + FinanceApplication.getUser().getEmail() + "]");
		final AsyncCallback<String> createCompanyCallBack = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(String result) {
				if (result != null) {
					company.setStringID(result);
					FinanceApplication.setCompany(company);
					hide();
				} else {
				}
			}
		};

		final ClientCompany company = getCompanyObject();
		FinanceApplication.createCRUDService().create(company,
				createCompanyCallBack);
	}

	@SuppressWarnings("unused")
	private void getCompany() {
		final IAccounterGETServiceAsync getService = (IAccounterGETServiceAsync) GWT
				.create(IAccounterGETService.class);

		((ServiceDefTarget) getService)
				.setServiceEntryPoint(FinanceApplication.GET_SERVICE_ENTRY_POINT);

		final AsyncCallback<ClientCompany> getCompanyCallBack = new AsyncCallback<ClientCompany>() {
			public void onFailure(Throwable caught) {
				UIUtils.say(FinanceApplication.getCompanyMessages()
						.getCompanyFailed());

			}

			public void onSuccess(ClientCompany company) {
				if (company != null) {

					FinanceApplication.setCompany(company);
				} else {
				}
			}
		};

		getService.getObjectByName(AccounterCoreType.COMPANY, companyNameText
				.getValue().toString(), getCompanyCallBack);
	}

	private void createPrevStepCanvas() {
		switch (currentStep) {
		case STEP3:
			currentStep = STEP2;
			// TODO: really?
			break;
		case STEP2:
			currentStep = STEP1;

			remove(step2Canvas);
			remove(step1Canvas);
			break;
		}
	}

	private void createNextStepCanvas() {
		switch (currentStep) {
		case STEP1:
			currentStep = STEP2;
			remove(step1Canvas);
			remove(step2Canvas);
			break;
		case STEP2:
			currentStep = STEP3;
			// TODO: really?
			break;
		}
	}

	private void createStep1Canvas() {
		setTitle(companyConstants.companySetup());

		companyPrefLabel = new Label();
		// companyPrefLabel.setAutoFit(true);
		// companyPrefLabel.setIcon("");
		companyPrefLabel.setText(FinanceApplication.getCompanyMessages()
				.companyAndPreferences());
		// companyPrefLabel.setWrap(false);

		step1Label = new Label(companyConstants.companyDetails());
		step2Label = new Label(companyConstants.typeofBusiness());
		step3Label = new Label(companyConstants.companyFile());

		// step1Label.setAutoFit(true);
		// step2Label.setAutoFit(true);
		// step3Label.setAutoFit(true);

		// step1Label.setWrap(false);
		// step2Label.setWrap(false);
		// step3Label.setWrap(false);

		typeLabel = new Label();
		descLabel = new Label();

		// typeLabel.setAutoFit(true);
		// typeLabel.setWrap(false);
		// typeLabel.setContents(
		// "<div style='font-size: 20px;'>Choose a Business type</div>");
		typeLabel.setText(FinanceApplication.getCompanyMessages()
				.addCompanyDetails());

		// descLabel.setAutoFit(true);
		// descLabel.setContents(
		// "To preconfigure your Office Accounting company with a chart-of-accounts, other data, and preferences that are typical for your type of business, select a template from the list. You can review and modify the settings that are preconfigured after you create the company. If you don't want your company preconfigured to a business type, select 'Basic' at the bottom of the list."
		// );
		descLabel.setText(FinanceApplication.getCompanyMessages()
				.typeCompanyInfo());

		companyNameText = new TextItem(companyConstants.companyName());
		companyNameText.setRequired(true);
		// companyNameText.setColSpan(3);

		accountType = new SelectItem(FinanceApplication.getCompanyMessages()
				.accountType());
		accountType.setValueMap(FinanceApplication.getCompanyMessages().UK(),
				FinanceApplication.getCompanyMessages().US());
		accountType.setDefaultValue(FinanceApplication.getCompanyMessages()
				.US());

		legalNameText = new TextItem(companyConstants.legalName());
		legalNameText.setRequired(true);
		legalNameText.setColSpan(3);

		street1Text = new TextItem(companyConstants.streetAddress1());
		street1Text.setColSpan(3);

		street2Text = new TextItem(companyConstants.streetaddress2());
		street2Text.setColSpan(3);
		// street2Text.setShowTitle(false);

		cityText = new TextItem(companyConstants.city());
		cityText.setColSpan(3);

		stateText = new TextItem(companyConstants.stateProvince());
		stateText.setColSpan(1);

		zipText = new TextItem(companyConstants.zipPostalCode());
		zipText.setColSpan(1);
		zipText.setValidators(DataUtils.zipValidator());

		countrySelect = new SelectItem(companyConstants.countryRegion());
		countrySelect.setValueMap(FinanceApplication.getCompanyMessages()
				.india(), FinanceApplication.getCompanyMessages().UK(),
				FinanceApplication.getCompanyMessages().US());
		countrySelect.setColSpan(3);

		phoneText = new TextItem(companyConstants.phone());
		phoneText.setColSpan(1);
		phoneText.setValidators(DataUtils.phoneValidator());

		faxText = new TextItem(companyConstants.fax());
		faxText.setColSpan(1);
		faxText.setValidators(DataUtils.faxValidator());

		emailText = new TextItem(companyConstants.email());
		emailText.setColSpan(3);
		emailText.setValidators(DataUtils.emailValidator());

		websiteText = new TextItem(companyConstants.webSite());
		websiteText.setColSpan(3);
		websiteText.setValidators(DataUtils.webValidator());

		taxIDText = new TextItem(companyConstants.federalTaxId());
		taxIDText.setColSpan(1);

		companyForm = new DynamicForm();
		// companyForm.setSize("80%", "*");
		// companyForm.setWrapItemTitles(false);
		companyForm.setNumCols(4);
		companyForm.setFields(companyNameText, accountType, legalNameText,
				street1Text, street2Text, cityText, stateText, zipText,
				countrySelect, phoneText, faxText, emailText, websiteText,
				taxIDText);

		helpButt = new Button(companyConstants.help());
		helpButt.setAccessKey('H');
		backButt = new Button(companyConstants.back());
		backButt.setAccessKey('B');
		nextButt = new Button(companyConstants.next());
		nextButt.setAccessKey('N');
		finButt = new Button(companyConstants.finish());
		finButt.setAccessKey('F');
		canButt = new Button(companyConstants.cancel());
		canButt.setAccessKey('C');

		backButt.setEnabled(true);
		nextButt.setEnabled(false);
		finButt.setEnabled(true);

		VerticalPanel leftVLay = new VerticalPanel();
		// leftVLay.setSize("20%", "*");
		leftVLay.add(step1Label);
		leftVLay.add(step2Label);
		// leftVLay.add(step3Label);

		VerticalPanel rightVLay = new VerticalPanel();
		// rightVLay.setSize("80%", "*");
		rightVLay.add(typeLabel);
		rightVLay.add(descLabel);
		rightVLay.add(companyForm);

		HorizontalPanel divHLay = new HorizontalPanel();
		// divHLay.setSize("100%", "*");
		divHLay.add(leftVLay);
		divHLay.add(rightVLay);

		HorizontalPanel helpHLay = new HorizontalPanel();
		// helpHLay.setSize("50%", "*");
		helpHLay.add(helpButt);

		HorizontalPanel buttHLay2 = new HorizontalPanel();
		// buttHLay2.setMembersMargin(10);
		// buttHLay2.setSize("50%", "*");
		buttHLay2.add(backButt);
		buttHLay2.add(nextButt);
		buttHLay2.add(finButt);
		buttHLay2.add(canButt);

		HorizontalPanel buttHLay1 = new HorizontalPanel();
		// buttHLay1.setBackgroundColor("#ff00ff");
		// buttHLay1.setMargin(10);
		// buttHLay1.setSize("100%", "*");
		// buttHLay1.setWidth("100%");
		// buttHLay1.setAutoHeight();
		// buttHLay1.setAlign(Alignment.RIGHT);
		// buttHLay1.setAlign(VerticalAlignment.CENTER);
		// buttHLay1.setMembersMargin(100);
		buttHLay1.add(helpHLay);
		buttHLay1.add(buttHLay2);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(companyPrefLabel);
		mainVLay.add(divHLay);
		mainVLay.add(buttHLay1);

		addEventHandlers();
		step1Canvas = new VerticalPanel();
		step1Canvas.setSize("100%", "100%");
		step1Canvas.add(mainVLay);
	}

	private void createStep2Canvas() {
		setTitle(companyConstants.companySetup());

		companyPrefLabel = new Label();
		// companyPrefLabel.setAutoFit(true);
		// companyPrefLabel.setIcon("");
		companyPrefLabel.setText(FinanceApplication.getCompanyMessages()
				.companyAndPreferences());
		// companyPrefLabel.setWrap(false);

		step1Label = new Label(companyConstants.companyDetails());
		step2Label = new Label(companyConstants.typeofBusiness());
		step3Label = new Label(companyConstants.companyFile());

		// step1Label.setAutoFit(true);
		// step2Label.setAutoFit(true);
		// step3Label.setAutoFit(true);

		// step1Label.setWrap(false);
		// step2Label.setWrap(false);
		// step3Label.setWrap(false);

		typeLabel = new Label();
		descLabel = new Label();

		// typeLabel.setAutoFit(true);
		// typeLabel.setWrap(false);
		// typeLabel.setContents(
		// "<div style='font-size: 20px;'>Choose a Business type</div>");
		typeLabel.setText(FinanceApplication.getCompanyMessages()
				.chooseBusinessType());

		descLabel.setText(FinanceApplication.getCompanyMessages()
				.preConfigureAccountingCompany());

		typeGrid = new DialogGrid(false);
		// typeGrid.setAutoFitData(Autofit.BOTH);
		// typeGrid.setOverflow(Overflow.SCROLL);
		typeGrid.setSize("100%", "100%");
		// typeGrid.setShowAllRecords(true);
		typeGrid.addColumn(ListGrid.COLUMN_TYPE_LABEL, companyConstants
				.businessTypes());

		// businessTypeField.setAlign(Alignment.CENTER);
		// typeGrid.setCanResizeFields(true);
		// typeGrid.addRecords(typeRecords);
		// typeGrid.setData(createListGridRecords(typeRecords));
		// typeGrid.setDisabled(true);
		typeGrid.selectRecord(0);

		addBusinessTypeButt = new Button(companyConstants.add());
		addBusinessTypeButt.addClickHandler(UIUtils.todoClick());

		helpButt = new Button(companyConstants.help());
		helpButt.setAccessKey('H');
		backButt = new Button(companyConstants.back());
		backButt.setAccessKey('B');
		backButt.setEnabled(false);
		nextButt = new Button(companyConstants.next());
		nextButt.setAccessKey('N');
		nextButt.setEnabled(true);
		finButt = new Button(companyConstants.finish());
		finButt.setAccessKey('F');
		canButt = new Button(companyConstants.cancel());
		canButt.setAccessKey('C');

		VerticalPanel leftVLay = new VerticalPanel();
		// leftVLay.setSize("20%", "*");
		leftVLay.add(step1Label);
		leftVLay.add(step2Label);
		// leftVLay.add(step3Label);

		HorizontalPanel businessTypeHLay = new HorizontalPanel();
		businessTypeHLay.setSize("90%", "100%");
		// businessTypeHLay.setMembersMargin(10);
		businessTypeHLay.add(typeGrid);
		businessTypeHLay.add(addBusinessTypeButt);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setSize("80%", "100%");
		rightVLay.add(typeLabel);
		rightVLay.add(descLabel);
		rightVLay.add(businessTypeHLay);

		HorizontalPanel divHLay = new HorizontalPanel();
		divHLay.setSize("100%", "80%");
		divHLay.add(leftVLay);
		divHLay.add(rightVLay);

		HorizontalPanel helpHLay = new HorizontalPanel();
		// helpHLay.setSize("50%", "*");
		helpHLay.add(helpButt);

		HorizontalPanel buttHLay2 = new HorizontalPanel();
		// buttHLay2.setMembersMargin(10);
		// buttHLay2.setSize("50%", "*");
		buttHLay2.add(backButt);
		buttHLay2.add(nextButt);
		buttHLay2.add(finButt);
		buttHLay2.add(canButt);

		HorizontalPanel buttHLay1 = new HorizontalPanel();
		// buttHLay1.setSize("100%", "*");
		// buttHLay1.setAlign(Alignment.RIGHT);
		// buttHLay1.setAlign(VerticalAlignment.CENTER);
		// buttHLay1.setMembersMargin(100);
		buttHLay1.add(helpHLay);
		buttHLay1.add(buttHLay2);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(companyPrefLabel);
		mainVLay.add(divHLay);
		mainVLay.add(buttHLay1);

		addEventHandlers();
		step2Canvas = new VerticalPanel();
		// step2Canvas.setSize("100%", "100%");
		step2Canvas.add(mainVLay);
	}

	@SuppressWarnings("unused")
	private void createStep3Canvas() {
		setTitle(FinanceApplication.getCompanyMessages().companySetup());

		companyPrefLabel = new Label();
		// companyPrefLabel.setAutoFit(true);
		// companyPrefLabel.setIcon("");
		companyPrefLabel.setText(FinanceApplication.getCompanyMessages()
				.companyAndPreferences());
		// companyPrefLabel.setWrap(false);

		step1Label = new Label(companyConstants.companyDetails());
		step2Label = new Label(companyConstants.typeofBusiness());
		step3Label = new Label(companyConstants.companyFile());

		// step1Label.setAutoFit(true);
		// step2Label.setAutoFit(true);
		// step3Label.setAutoFit(true);

		// step1Label.setWrap(false);
		// step2Label.setWrap(false);
		// step3Label.setWrap(false);

		typeLabel = new Label();
		descLabel = new Label();

		// typeLabel.setAutoFit(true);
		// typeLabel.setWrap(false);
		typeLabel.setText(FinanceApplication.getCompanyMessages()
				.chooseBusinessType());
		typeLabel.setText(FinanceApplication.getCompanyMessages()
				.addCompanyDetails());

		// descLabel.setAutoFit(true);
		// descLabel.setContents(
		// "To preconfigure your Office Accounting company with a chart-of-accounts, other data, and preferences that are typical for your type of business, select a template from the list. You can review and modify the settings that are preconfigured after you create the company. If you don't want your company preconfigured to a business type, select 'Basic' at the bottom of the list."
		// );
		descLabel.setText(FinanceApplication.getCompanyMessages()
				.typeCompanyInfo());

		companyNameText = new TextItem(companyConstants.companyName());
		companyNameText.setColSpan(3);

		legalNameText = new TextItem(companyConstants.legalName());
		// legalNameText.setColSpan(3);

		accountType = new SelectItem("Account Type");
		accountType.setValueMap("UK", "US");

		street1Text = new TextItem(companyConstants.streetAddress1());
		street1Text.setColSpan(3);

		street2Text = new TextItem(companyConstants.streetaddress2());
		street2Text.setColSpan(3);
		// street2Text.setShowTitle(false);

		cityText = new TextItem(companyConstants.city());
		cityText.setColSpan(3);

		stateText = new TextItem(companyConstants.stateProvince());
		stateText.setColSpan(1);

		zipText = new TextItem(companyConstants.zipPostalCode());
		zipText.setColSpan(1);

		countrySelect = new SelectItem(companyConstants.countryRegion());
		countrySelect.setValueMap(FinanceApplication.getCompanyMessages()
				.india(), FinanceApplication.getCompanyMessages().UK(),
				FinanceApplication.getCompanyMessages().US());
		countrySelect.setColSpan(3);

		phoneText = new TextItem(companyConstants.phone());
		phoneText.setColSpan(1);

		faxText = new TextItem(companyConstants.fax());
		faxText.setColSpan(1);

		emailText = new TextItem(companyConstants.email());
		emailText.setColSpan(3);

		websiteText = new TextItem(companyConstants.webSite());
		websiteText.setColSpan(3);

		taxIDText = new TextItem(companyConstants.federalTaxId());
		taxIDText.setColSpan(1);

		companyForm = new DynamicForm();
		// companyForm.setSize("80%", "*");
		// companyForm.setWrapItemTitles(false);
		companyForm.setNumCols(4);
		companyForm.setFields(companyNameText, legalNameText, street1Text,
				street2Text, cityText, stateText, zipText, countrySelect,
				phoneText, faxText, emailText, websiteText, taxIDText);

		helpButt = new Button(companyConstants.help());
		backButt = new Button(companyConstants.back());
		backButt.setEnabled(false);
		nextButt = new Button(companyConstants.next());
		nextButt.setEnabled(true);
		finButt = new Button(companyConstants.finish());
		canButt = new Button(companyConstants.cancel());

		VerticalPanel leftVLay = new VerticalPanel();
		// leftVLay.setSize("20%", "*");
		leftVLay.add(step1Label);
		leftVLay.add(step2Label);
		leftVLay.add(step3Label);

		VerticalPanel rightVLay = new VerticalPanel();
		// rightVLay.setSize("80%", "*");
		rightVLay.add(typeLabel);
		rightVLay.add(descLabel);
		rightVLay.add(companyForm);

		HorizontalPanel divHLay = new HorizontalPanel();
		// divHLay.setSize("100%", "*");
		divHLay.add(leftVLay);
		divHLay.add(rightVLay);

		HorizontalPanel helpHLay = new HorizontalPanel();
		// helpHLay.setSize("50%", "*");
		helpHLay.add(helpButt);

		HorizontalPanel buttHLay2 = new HorizontalPanel();
		// buttHLay2.setMembersMargin(10);
		// buttHLay2.setSize("50%", "*");
		buttHLay2.add(backButt);
		buttHLay2.add(nextButt);
		buttHLay2.add(finButt);
		buttHLay2.add(canButt);

		HorizontalPanel buttHLay1 = new HorizontalPanel();
		// buttHLay1.setSize("100%", "*");
		// buttHLay1.setAlign(Alignment.RIGHT);
		// buttHLay1.setMembersMargin(100);
		buttHLay1.add(helpHLay);
		buttHLay1.add(buttHLay2);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(companyPrefLabel);
		mainVLay.add(divHLay);
		mainVLay.add(buttHLay1);

		addEventHandlers();

		if (step3Canvas == null)
			step3Canvas = new VerticalPanel();
		step3Canvas.setSize("100%", "100%");
		step3Canvas.add(mainVLay);

		if (step2Canvas != null)
			remove(step2Canvas);

		add(step3Canvas);

		setSize("80%", "90%");
		show();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	// private ListGridRecord[] createListGridRecords(String[] records) {
	// ListGridRecord[] result = new ListGridRecord[records.length];
	// for (int recordIndex = 0; recordIndex < records.length; ++recordIndex) {
	//
	// result[recordIndex] = new ListGridRecord();
	// result[recordIndex].setAttribute("business_type",
	// records[recordIndex]);
	// }
	// return result;
	// }

}
