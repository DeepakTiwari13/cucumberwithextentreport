package com.citrus.base;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import com.cucumber.listener.Reporter;

public class Services {

public HashMap<String,String> api=null;	

	URL url = null;
	HttpURLConnection conn = null;
	int responseCode;
	JSONObject myResponse = null;
	BufferedReader rd = null;
	StringBuilder sb;
	String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36";

	public String getMethod(String endpointtoLoad) {

		StringBuilder result = new StringBuilder();
		try {
			url = new URL(endpointtoLoad);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				// System.out.println("Printing data " + line);
				result.append(line);
			}
			rd.close();
			conn.disconnect();
			Reporter.addStepLog("Response in GET method :" + result);
			return result.toString();
		} catch (Throwable t) {
			t.fillInStackTrace();
			Reporter.addStepLog(" Exception in getresponseHeader :" + t.fillInStackTrace());
		}
		return null;
	}

	public String postMethod(String endpointtoLoad, String requestBody) {

		try {
			url = new URL(endpointtoLoad);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(1000);
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			HttpURLConnection.setFollowRedirects(true);
			conn.setRequestProperty("User-Agent", USER_AGENT);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-length", String.valueOf(requestBody.length()));
			conn.setRequestMethod("POST");
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(requestBody);
			wr.flush();
			wr.close();
			responseCode = conn.getResponseCode();
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = rd.readLine()) != null) {
				// System.out.println("Printing data " + inputLine);
				response.append(inputLine);
			}
			rd.close();
			conn.disconnect();
			Reporter.addStepLog("Response in POST method :" + response);
			return response.toString();
		} catch (Throwable t) {
			t.fillInStackTrace();
			Reporter.addStepLog("Exception in POST method :" + t.fillInStackTrace());
		}
		return null;
	}

	public int putMethod(String endpointtoLoad, String type) throws IOException {

		URL url = new URL(endpointtoLoad);
		conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("PUT");
		conn.setRequestProperty("Content-Type", type);
		conn.setRequestProperty("Accept", type);
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		String Resourcecontent = "{\"userId\": 1," + "\"id\": 11," + "\"title\": \"Delta\"}";
		out.write(Resourcecontent);
		out.flush();
		out.close();
		responseCode = conn.getResponseCode();
		conn.disconnect();
		return responseCode;
	}

	public String getResponseHeader(String endpointtoLoad) {

		try {
			url = new URL(endpointtoLoad);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			String hf = conn.getHeaderField("Content-Type");
			Reporter.addStepLog("Present header field " + hf);
			conn.disconnect();
			return hf;
		} catch (Throwable t) {
			t.fillInStackTrace();
			Reporter.addStepLog(" Exception in getresponseHeader :" + t.fillInStackTrace());

		}
		return null;
	}

	public String appendString(List<String> l) {

		sb = new StringBuilder();
		for (int i = 0; i < l.size(); i++) {
			sb.append(l.get(i).concat("&"));
		}
		sb.deleteCharAt(sb.lastIndexOf("&"));
		Reporter.addStepLog(" Request body :" + sb.toString());
		return sb.toString();
	}
	
	public HashMap<String,String> parseResponse(String s) {
		api = new HashMap<String,String>();

		String[] respFields = s.split("&");
		for (String f : respFields) {
			int index = f.indexOf('=');
			api.put((String) f.subSequence(0, index), f.substring(index + 1));
		}
		return api;
	
}
}
