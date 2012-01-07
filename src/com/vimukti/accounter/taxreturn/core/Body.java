package com.vimukti.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class Body {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public IXMLElement toXML() {
		XMLElement bodyElement;
		if (value != null) {
			bodyElement = new XMLElement("Body", value);
		} else {
			bodyElement = new XMLElement("Body");
		}
		return bodyElement;
	}
}