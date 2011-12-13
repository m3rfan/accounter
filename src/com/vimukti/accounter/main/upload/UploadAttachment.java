package com.vimukti.accounter.main.upload;

public class UploadAttachment {
	public static final int CREATE = 1;
	public static final int DELETE = 2;
	public int processType;
	public String attachmentId;

	public UploadAttachment(String attachmentId, int processType) {
		this.processType = processType;
		this.attachmentId = attachmentId;
	}
}