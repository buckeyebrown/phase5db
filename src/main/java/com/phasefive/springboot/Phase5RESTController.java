package com.phasefive.springboot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

import com.phasefive.backend.JSONResponse;
import com.phasefive.backend.ObtainQueryCode;
import com.phasefive.backend.SQLConnect;

import java.io.PrintWriter;

import java.sql.SQLException;
import java.io.IOException;
import org.json.JSONException;

@RestController
public class Phase5RESTController {
	public static PrintWriter out;
	private SQLConnect sc;
	private JSONResponse jsonResponse;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		return mav;
	}

	@RequestMapping(value = "/MainServ", method = RequestMethod.GET)
	public void mainServ(@RequestParam(value = "queryCode", defaultValue = "0") String queryValue, HttpServletResponse response)
			throws SQLException, IOException {

		response.setContentType("application/json; charset=UTF-8");
		out = response.getWriter();

		try {
			ObtainQueryCode.setQueryCode(Integer.parseInt(queryValue));
			sc = new SQLConnect();
			jsonResponse = new JSONResponse();
			jsonResponse.obtainJSONResponse();
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
}
