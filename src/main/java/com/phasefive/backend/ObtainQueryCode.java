package com.phasefive.backend;

public final class ObtainQueryCode {
	
	private static int queryCodeID;
	
    private ObtainQueryCode() {
    }
    
    public static int getQueryCode() {
		return queryCodeID;
	}

	public static void setQueryCode(int queryCode) {
		ObtainQueryCode.queryCodeID = queryCode;
	}

}
