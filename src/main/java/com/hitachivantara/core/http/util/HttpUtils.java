/*                                                                             
 * Copyright (C) 2019 Rison Han                                     
 *                                                                             
 * Licensed under the Apache License, Version 2.0 (the "License");           
 * you may not use this file except in compliance with the License.            
 * You may obtain a copy of the License at                                     
 *                                                                             
 *      http://www.apache.org/licenses/LICENSE-2.0                             
 *                                                                             
 * Unless required by applicable law or agreed to in writing, software         
 * distributed under the License is distributed on an "AS IS" BASIS,         
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and         
 * limitations under the License.                                              
 */
package com.hitachivantara.core.http.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.amituofo.common.ex.ParseException;
import com.amituofo.common.kit.value.NameValuePair;
import com.amituofo.common.kit.value.Value;
import com.amituofo.common.util.StreamUtils;
import com.amituofo.common.util.URLUtils;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.core.http.client.methods.HttpRequestBase;
import com.hitachivantara.core.http.model.Header;
import com.hitachivantara.core.http.model.HttpParameter;

public class HttpUtils {
	// private static SimpleHttpClient httpClient;
	//
	// static {
	// ClientConfiguration configuration = new ClientConfiguration();
	// configuration.setProtocol(null);
	// // configuration.setMaxConnections(10);
	// configurateClient(configuration, true);
	// }
	//
	// public static void configurateClient(ClientConfiguration configuration, boolean singleHttpClient) {
	// HttpClientBuilder builder = singleHttpClient ? new SingleHttpClientBuilder(configuration) : new DefaultHttpClientBuilder(configuration);
	// httpClient = new SimpleHttpClient(builder);
	// }

	// public static HttpResponse post(String url, RequestHeaders header, RequestParams param, RequestForm form, HttpEntity entity) throws
	// Exception {
	// return httpClient.post(url, header, param, form, entity);
	// }
	//
	// public static HttpResponse put(String url, RequestHeaders header, RequestParams param, HttpEntity entity) throws Exception {
	// return httpClient.put(url, header, param, entity);
	// }
	//
	// public static HttpResponse get(String url, RequestHeaders header, RequestParams param) throws Exception {
	// return httpClient.get(url, header, param);
	// }
	//
	// public static HttpResponse head(String url, RequestHeaders header, RequestParams param) throws Exception {
	// return httpClient.head(url, header, param);
	// }
	//
	// public static HttpResponse delete(String url, RequestHeaders header, RequestParams param) throws Exception {
	// return httpClient.delete(url, header, param);
	// }

	/**
	 * * 跳过证书验证
	 */
	public static final TrustManager[] DUMMY_TRUST_MGR = new TrustManager[] { new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;//new java.security.cert.X509Certificate[] {};
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
		
		
	} };

	/**
	 * 跳过主机验证
	 */
	public static final HostnameVerifier DUMMY_HOST_NAME_VERIFIER = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * Ignore verification
	 * 
	 * @param connection
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static void trustAll(HttpsURLConnection connection) throws NoSuchAlgorithmException, KeyManagementException {
		connection.setHostnameVerifier(DUMMY_HOST_NAME_VERIFIER);

		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, DUMMY_TRUST_MGR, new java.security.SecureRandom());
		SSLSocketFactory newFactory = sc.getSocketFactory();
		connection.setSSLSocketFactory(newFactory);
	}

	public static void printHttpResponse(HttpResponse response, boolean printContents) throws ParseException, IOException {
		// StringBuilder builder = new StringBuilder();
		// HttpEntity entity = httpResponse.getEntity();
		// builder.append("status:" + httpResponse.getStatusLine() + "\n");
		// builder.append("headers:" + "\n");
		// HeaderIterator iterator = httpResponse.headerIterator();
		// while (iterator.hasNext()) {
		// builder.append("\t" + iterator.next() + "\n");
		// }
		// if (entity != null) {
		// String responseString = EntityUtils.toString(entity);
		// builder.append("response length:" + responseString.length() + "\n");
		// builder.append("response content:" + responseString.replace("\r\n", "") + "\n");
		// }
		// System.out.println(builder.toString());

		try {
			if (printContents && response.getEntity() != null && response.getEntity().getContentLength() != 0) {
				System.out.println("-Response Body:-----------------------------------------------------------------------");
				try {
					StreamUtils.inputStreamToConsole(response.getEntity().getContent(), true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("-Response Status:---------------------------------------------------------------------");
			// print response status to console
			System.out.println("  Code : " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());

			System.out.println("-Response Header:---------------------------------------------------------------------");
			Header[] headers = response.getAllHeaders();
			for (Header header : headers) {
				System.out.println("  " + header.getName() + "=" + header.getValue());
			}
			System.out.println("--------------------------------------------------------------------------------------");

			// if (assertStatusCode == 0) {
			// Assert.assertTrue(response.getStatusLine().getStatusCode() == 200 ||
			// response.getStatusLine().getStatusCode() == 201);
			// } else {
			// Assert.assertTrue(response.getStatusLine().getStatusCode() == assertStatusCode);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void printHttpRequest(org.apache.http.client.methods.HttpRequestBase req) {
		System.out.println("-Request URI:-------------------------------------------------------------------------");
		System.out.println("  " + req.getMethod() + " " + req.getURI());
		System.out.println("-Request Header:----------------------------------------------------------------------");
		org.apache.http.Header[] headers = req.getAllHeaders();
		for (org.apache.http.Header header : headers) {
			System.out.println("  " + header.getName() + "=" + header.getValue());
		}
	}

	public static void printHttpRequest(HttpRequestBase req) {
		System.out.println("-Request URI:-------------------------------------------------------------------------");
		System.out.println("  " + req.getMethod() + " " + req.getURI());
		System.out.println("-Request Header:----------------------------------------------------------------------");
		Header[] headers = req.getAllHeaders();
		for (Header header : headers) {
			System.out.println("  " + header.getName() + "=" + header.getValue());
		}
	}

	public static void printHttpRequest(HttpRequestBase req, URL newurl) {
		System.out.println("-Request URI:-------------------------------------------------------------------------");
		System.out.println("  " + req.getMethod() + " " + newurl);
		System.out.println("-Request Header:----------------------------------------------------------------------");
		Header[] headers = req.getAllHeaders();
		for (Header header : headers) {
			System.out.println("  " + header.getName() + "=" + header.getValue());
		}
	}

	public static void close(HttpResponse response) throws IOException {
		EntityUtils.consume(response.getEntity());
		response.close();
	}

	public static String responseEntityToString(HttpResponse response) throws IOException {
		String content;
		try {
			InputStream in = response.getEntity().getContent();
			content = StreamUtils.inputStreamToString(in, false);
		} finally {
			close(response);
		}

		return content;
	}
	
//	private static SimpleConfiguration responseToJson(HttpResponse response) {
//		InputStream in = null;
//		HashMap<String, Object> qresult = null;
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			in = response.getEntity().getContent();
//
//			qresult = mapper.readValue(in, new TypeReference<HashMap<String, Object>>() {
//			});
//			// System.out.println(qresult);
//
//			return new SimpleConfiguration(qresult);
//		} catch (Exception e) {
//			throw new InvalidResponseException("Error occurred when parsing response.", e);
//		} finally {
//			HCPRestResponseHandler.close(response);
//		}
//	}
	

	public static String catParams(String url, HttpParameter params) {
		if (params == null || params.isEmpty()) {
			return url;
		}

		url = url.trim();
		StringBuilder builder = new StringBuilder(url);
		if (url.charAt(url.length() - 1) != '?') {
			if (url.contains("?")) {
				builder.append("&");
			} else {
				builder.append("?");
			}
		}

		builder.append(paramsToString(params));

		return builder.toString();
	}

	public static String paramsToString(HttpParameter params) {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		int i = 0;
		Collection<NameValuePair<String>> paramValues = params.values();
		for (NameValuePair<String> paramValue : paramValues) {
			// String value = params.get(key);
			// if (value == null) { // 过滤空的key
			// continue;
			// }

			if (i != 0) {
				builder.append(URLUtils.QP_SEP_A);
			}

			builder.append(paramValue.getName());
			Value<String> value = paramValue.getValue();
			if (value != null) {
				String strVal = value.buildStringValue();
				if (strVal != null) {
					builder.append(URLUtils.NAME_VALUE_SEPARATOR);
					builder.append(strVal);
				}
			}

			i++;
		}

		return builder.toString();
	}

}
