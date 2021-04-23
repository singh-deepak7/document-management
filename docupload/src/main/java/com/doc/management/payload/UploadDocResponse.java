package com.doc.management.payload;

public class UploadDocResponse {

	private String docName;
	private String docDownloadUri;
	private String docType;
	private long size;

	public UploadDocResponse(String docName, String docDownloadUri, String docType, long size) {
		this.docName = docName;
		this.docDownloadUri = docDownloadUri;
		this.docType = docType;
		this.size = size;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocDownloadUri() {
		return docDownloadUri;
	}

	public void setDocDownloadUri(String docDownloadUri) {
		this.docDownloadUri = docDownloadUri;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
