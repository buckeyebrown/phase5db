package com.phasefive.backend;

public final class ObtainQueryCode {
	
	private static int queryCodeID;
	private static String queryTextID;
    private ObtainQueryCode() {
    }
    
    public static int getQueryCode() {
		return queryCodeID;
	}

	public static void setQueryCode(int queryCode) {
		ObtainQueryCode.queryCodeID = queryCode;
	}

	public static String getQueryText() {
		return queryTextID;
	}

	public static void setQueryText(String queryText) {
		ObtainQueryCode.queryTextID = queryText;
	}
}
