package com.ishraq.janna.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ServiceHandler {

	static String response = null;
	public final static int GET = 1;
	public final static int POST = 2;
	String datafromServer = null;

	public ServiceHandler() {

	}

	/*
	 * Making service call
	 * 
	 * @url - url to make request
	 * 
	 * @method - http request method
	 */
	public String makeServiceCall(String url, int method) {
		return this.makeServiceCall(url, method, null);
	}

	/*
	 * Making service call
	 * 
	 * @url - url to make request
	 * 
	 * @method - http request method
	 * 
	 * @params - http request params
	 */
	public String makeServiceCall(String url, int method,
			List<NameValuePair> params) {
		try {

			HttpURLConnection urlConnection = (HttpURLConnection) new URL(url)
					.openConnection();
			urlConnection.setUseCaches(true);
			urlConnection.setRequestProperty("Accept", "application/json");
			// urlConnection.addRequestProperty("Cache-Control","only-if-cached");
			// //return nothing

			urlConnection.addRequestProperty("Cache-Control",
					"max-stale=" + 60); // 60 * 60 * 24 * 28 :4-weeks
			// urlConnection.setConnectTimeout(1000);
			// urlConnection.setReadTimeout(1000 );
			InputStream is = new BufferedInputStream(
					urlConnection.getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			datafromServer = sb.toString();

			// http client
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;

			// Checking http request method type
			if (method == POST) {
				HttpPost httpPost = new HttpPost(url);
				// adding post params
				if (params != null) {
					httpPost.setEntity(new UrlEncodedFormEntity(params));
				}

				httpResponse = httpClient.execute(httpPost);

			} else if (method == GET) {
				// appending params to url
				if (params != null) {
					String paramString = URLEncodedUtils
							.format(params, "utf-8");
					url += "?" + paramString;
				}
				HttpGet httpGet = new HttpGet(url);

				httpResponse = httpClient.execute(httpGet);

			}
			httpEntity = httpResponse.getEntity();
			response = datafromServer;// EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;

	}
}
