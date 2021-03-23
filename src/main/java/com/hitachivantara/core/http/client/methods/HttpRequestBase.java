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
package com.hitachivantara.core.http.client.methods;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;

import com.amituofo.common.kit.value.NameValuePair;
import com.amituofo.common.kit.value.Value;
import com.hitachivantara.core.http.HttpRequest;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.core.http.content.HttpEntity;
import com.hitachivantara.core.http.model.Header;
import com.hitachivantara.core.http.model.HttpHeader;

public class HttpRequestBase implements HttpRequest {
	private final Method method;
	private final URI uri;

	private HttpHeader headers = new HttpHeader();
	// private Parameters params = new Parameters();
	private HttpEntity entity = null;

	public HttpRequestBase(Method method, URL url) throws URISyntaxException {
		this.method = method;
		this.uri = url.toURI();
	}

	public HttpRequestBase(Method method, URI uri) {
		this.method = method;
		this.uri = uri;
	}

	public HttpRequestBase(Method method, String url) throws MalformedURLException {
		this.method = method;
		this.uri = URI.create(url);
	}

	@Override
	public void addHeaders(HttpHeader headers) {
		if (headers != null) {
			this.headers.putAll(headers.values());
		}
	}

	@Override
	public void setHeader(Header header) {
		headers.put(header.getName(), header.getStringValue());
	}

	@Override
	public void setHeader(String name, String value) {
		headers.put(name, value);
	}

	@Override
	public String getHeaderValue(String name) {
		Value<String> rv = headers.get(name);
		if (rv != null) {
			return rv.getValue();
		}

		return null;
	}

	@Override
	public HttpHeader getHttpHeader() {
		return headers;
	}

	@Override
	public Header[] getAllHeaders() {
		int i = 0;
		Header[] all = new Header[headers.size()];
		Collection<NameValuePair<String>> pairs = headers.values();
		for (NameValuePair<String> pair : pairs) {
			String name = pair.getName();
			String value = pair.getStringValue();
			all[i++] = new Header(name, value);
		}
		return all;
	}

	@Override
	public HttpEntity getEntity() {
		return entity;
	}

	@Override
	public void setEntity(HttpEntity entity) {
		this.entity = entity;
	}

	@Override
	public URI getURI() {
		return uri;
	}

	@Override
	public Method getMethod() {
		return method;
	}

}
