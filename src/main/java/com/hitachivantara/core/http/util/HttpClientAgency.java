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
import java.util.Collection;

import com.amituofo.common.define.Constants;
import com.amituofo.common.kit.value.NameValuePair;
import com.amituofo.common.util.URLUtils;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.core.http.client.CloseableHttpClient;
import com.hitachivantara.core.http.client.HttpClient;
import com.hitachivantara.core.http.client.HttpClientBuilder;
import com.hitachivantara.core.http.client.methods.HttpDelete;
import com.hitachivantara.core.http.client.methods.HttpGet;
import com.hitachivantara.core.http.client.methods.HttpHead;
import com.hitachivantara.core.http.client.methods.HttpPost;
import com.hitachivantara.core.http.client.methods.HttpPut;
import com.hitachivantara.core.http.client.methods.HttpRequestBase;
import com.hitachivantara.core.http.content.HttpEntity;
import com.hitachivantara.core.http.content.UrlEncodedFormEntity;
import com.hitachivantara.core.http.define.HeaderKey;
import com.hitachivantara.core.http.ex.HttpException;
import com.hitachivantara.core.http.model.HttpForm;
import com.hitachivantara.core.http.model.HttpHeader;
import com.hitachivantara.core.http.model.HttpParameter;

public class HttpClientAgency {
	private final HttpHeader commonHeader = new HttpHeader();
	private final HttpClient httpClient;
	public final static String USER_AGENT = "hitachivantara-sdk-java/0.5.492";

	public HttpClientAgency(HttpClientBuilder httpClientBuilder) {
		httpClient = httpClientBuilder.build();
		
		commonHeader.put(HeaderKey.USER_AGENT.getKeyname(), USER_AGENT);
	}

	public void addCommonHeader(String key, String value) {
		commonHeader.put(key, value);
	}

	public HttpResponse post(String url, HttpHeader header, HttpParameter param, HttpForm form, HttpEntity entity) throws HttpException, IOException {
		url = HttpUtils.catParams(url, param);
		HttpPost request = new HttpPost(url);
		HttpResponse response = request(request, header, form, entity);
		// request.releaseConnection();

		return response;
	}

	public HttpResponse put(String url, HttpHeader header, HttpParameter param, HttpEntity entity) throws HttpException, IOException {
		url = HttpUtils.catParams(url, param);
		HttpPut request = new HttpPut(url);
		HttpResponse response = request(request, header, entity);
		// request.releaseConnection();

		return response;
	}

	public HttpResponse get(String url, HttpHeader header, HttpParameter param) throws HttpException, IOException {
		url = HttpUtils.catParams(url, param);
		return request(new HttpGet(url), header);
	}

	public HttpResponse head(String url, HttpHeader header, HttpParameter param) throws HttpException, IOException {
		url = HttpUtils.catParams(url, param);
		HttpHead request = new HttpHead(url);
		HttpResponse response = request(request, header);
		// request.releaseConnection();

		return response;
	}

	public HttpResponse delete(String url, HttpHeader header, HttpParameter param) throws HttpException, IOException {
		HttpDelete request = new HttpDelete(url);
		HttpResponse response = request(request, header);
		// request.releaseConnection();

		return response;
	}

	public HttpResponse request(HttpRequestBase req, HttpHeader header, HttpEntity entity) throws HttpException, IOException {
		// Set input stream
		if (entity != null) {
			req.setEntity(entity);
		}

		return request(req, header);
	}

	public HttpResponse request(HttpRequestBase req, HttpHeader header, HttpForm form, HttpEntity entity) throws HttpException, IOException {
		// Set input stream
		if (entity != null) {
			req.setEntity(entity);
		} else {
			// Add form data
			if (form != null && form.size() != 0) {
				Collection<NameValuePair<String>> formparams = form.values();
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formparams);
				req.setEntity(urlEncodedFormEntity);
			}
		}
		return request(req, header);
	}

	public HttpResponse request(HttpRequestBase request, HttpHeader header) throws HttpException {
		try {
			// Set common header
			request.addHeaders(commonHeader);
			// Set header
			request.addHeaders(header);

			// v For Debug test
//			 HttpUtils.printHttpRequest(request);
			// ^ For Debug test
			// httpClient.execute(req, responseHandler)
			HttpResponse httpResponse = httpClient.execute(request);
			// vv For Debug test
			// HttpUtils.printHttpResponse(httpResponse, false);
			// ^^ For Debug test

			return httpResponse;
		} finally {
			// if (httpClient != null) {
			// // httpClient.close();
			// }
		}
	}

	// private HttpClient getHttpClient() {
	// HttpClient httpClient = httpClientBuilder.build();
	// return httpClient;
	// }
	
	public void close() throws IOException {
		((CloseableHttpClient)httpClient).close();
	}
}
