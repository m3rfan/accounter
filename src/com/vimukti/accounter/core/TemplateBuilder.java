package com.vimukti.accounter.core;

public abstract class TemplateBuilder {
	protected String headerHtml;
	protected String footerHtml;
	protected String bodyHtml;
	protected Company company;
	protected String imgUrl;
	protected String style1;
	private static String CmpName;

	public TemplateBuilder(String footerImageUrl, String stylefile) {
		this.imgUrl = footerImageUrl;
		this.style1 = stylefile;
		this.company = Company.getCompany();
		init();
	}

	public TemplateBuilder() {
		this.company = Company.getCompany();
		if (this.company.getTradingName() != null
				&& !this.company.getTradingName().equals("")) {
			CmpName = company.getTradingName();
		}
	}

	protected void init() {
		initHeader();
		initBody();
		initFooter();
	}

	public abstract void initBody();

	public abstract void initFooter();

	public abstract void initHeader();

	public static void setCmpName(String cmpName) {
		CmpName = cmpName;
	}

	public static String getCmpName() {
		return CmpName;
	}
}
