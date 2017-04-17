package com.phasefive.backend;

import java.io.PrintWriter;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import com.phasefive.springboot.Phase5RESTController;

public class JSONResponse extends SQLConnect {

	private JSONArray responseArray;
	private ResultSetToJSONConverter converter;
	private SQLConnect sc;



	public JSONArray getResponseArray() {
		return responseArray;
	}

	protected void setResponseArray(JSONArray responseArray) {
		this.responseArray = responseArray;
	}

	protected ResultSetToJSONConverter getConverter() {
		return converter;
	}

	protected void setConverter(ResultSetToJSONConverter converter) {
		this.converter = converter;
	}

	protected SQLConnect getSc() {
		return sc;
	}

	protected void setSc(SQLConnect sc) {
		this.sc = sc;
	}

	public JSONResponse() {
		super();
	}

	public void obtainJSONResponse() throws JSONException, SQLException {
		setUpJSONResponseFields();
		createJSONResponseArray();

		this.sendResponseToServlet(Phase5RESTController.out);
	}

	protected void setUpJSONResponseFields() {
		responseArray = new JSONArray();
		converter = new ResultSetToJSONConverter();
		sc = new SQLConnect();
	}

	protected void createJSONResponseArray() {
		try {
			putJSONArrayIntoResponseArray();
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
	}

	protected void putJSONArrayIntoResponseArray() throws JSONException, SQLException {
		JSONArray SQLData = converter.convert(SQLConnect.getMonarchDBResults());
		responseArray.put(0, SQLData);
	}


	protected void sendResponseToServlet(PrintWriter responseOutput) {
		responseOutput.println(responseArray);
	}

}
