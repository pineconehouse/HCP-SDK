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
package com.hitachivantara.core.http.client.impl;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import com.amituofo.common.kit.value.NameValuePair;
import com.hitachivantara.core.http.HttpRequest;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.core.http.client.HttpClientBase;
import com.hitachivantara.core.http.ex.HttpException;
import com.hitachivantara.core.http.model.HttpHeader;

public class ApacheHttpClient extends HttpClientBase {
	private CloseableHttpClient httpClient;

	public ApacheHttpClient(ClientConfiguration config, CloseableHttpClient httpClient) {
		super(config);
		this.httpClient = httpClient;
	}

	@Override
	protected HttpResponse request(HttpRequest request) throws HttpException {

		try {
			HttpRequestBase httprequest = toApacheHttpRequest(request);
			
			// CloseableHttpResponse httpresponse = httpClient.execute(target, httprequest);
			CloseableHttpResponse httpresponse = httpClient.execute(httprequest);

			ApacheHttpResponse response = new ApacheHttpResponse(httprequest, httpresponse);

			return response;

		} catch (Exception e) {
			throw new HttpException(e);
		}
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	private HttpRequestBase toApacheHttpRequest(HttpRequest request) throws UnsupportedOperationException, IOException {
		HttpRequestBase httprequest = null;
		URI uri = request.getURI();
		switch (request.getMethod()) {
			case PUT:
				// HttpPut put;
				httprequest = new HttpPut(uri);
				if (request.getEntity() != null) {
					((HttpPut) httprequest).setEntity(new InputStreamEntity(request.getEntity().getContent(), request.getEntity().getContentLength()));
				}
				break;
			case GET:
				// HttpGet get;
				httprequest = new HttpGet(uri);
				break;
			case HEAD:
				// HttpHead head;
				httprequest = new HttpHead(uri);
				break;
			case DELETE:
				// HttpDelete delete;
				httprequest = new HttpDelete(uri);
				break;
			case POST:
				// HttpPost post;
				httprequest = new HttpPost(uri);
				if (request.getEntity() != null) {
					((HttpPost) httprequest).setEntity(new InputStreamEntity(request.getEntity().getContent(), request.getEntity().getContentLength()));
				}
				break;
		}

		HttpHeader requestHeaders = request.getHttpHeader();
		if (requestHeaders != null && requestHeaders.size() > 0) {
			Collection<NameValuePair<String>> pair = requestHeaders.values();
			for (NameValuePair<String> entry : pair) {
				String name = entry.getName();
				String value = entry.getStringValue();
				httprequest.addHeader(name, value);
			}
		}
		return httprequest;
	}

	@Override
	public void close() throws IOException {
		httpClient.close();
	}

}
