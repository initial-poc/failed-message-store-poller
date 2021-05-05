package com.infogain.gcp.poc.util;

import java.util.HashMap;
import java.util.Map;

public enum RecordStatus {
	CREATED(0, "Record is in progess") ,FAILED(4,"Record is failed");
	private final int statusCode;
	private final String statusMessage;

	private RecordStatus(final int statusCode, final String statusMessage) {
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	private static final Map<Integer, RecordStatus> lookUp = new HashMap<>();

	static {
		for (RecordStatus d : RecordStatus.values()) {
			lookUp.put(d.getStatusCode(), d);
		}
	}

	public static RecordStatus getStatusMessage(final int statusCode) {
		return lookUp.get(statusCode);
	}

}
