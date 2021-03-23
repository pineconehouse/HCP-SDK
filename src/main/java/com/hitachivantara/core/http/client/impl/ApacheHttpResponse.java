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

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.hitachivantara.core.http.client.StatusLine;
import com.hitachivantara.core.http.content.ApacheAbortableInputStream;
import com.hitachivantara.core.http.content.HttpEntity;
import com.hitachivantara.core.http.content.InputStreamEntity;
import com.hitachivantara.core.http.model.Header;

public class ApacheHttpResponse implements com.hitachivantara.core.http.CloseableHttpResponse {
	private final HttpRequestBase httprequest;
	private final CloseableHttpResponse httpresponse;
	private final StatusLine statusline;
	private HttpEntity content;
	private Header[] headers = null;

	public ApacheHttpResponse(HttpRequestBase httprequest, CloseableHttpResponse response) throws IOException {
		this.httprequest = httprequest;
		this.httpresponse = response;
		this.statusline = new StatusLine(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
	}

	@Override
	public HttpEntity getEntity() {
		if (this.content == null) {
			org.apache.http.HttpEntity entity = httpresponse.getEntity();
			try {
				if (entity != null && entity.getContent() != null) {
					int x = entity.getContent().available();
					long y = entity.getContentLength();
					ApacheAbortableInputStream aais = new ApacheAbortableInputStream(httprequest, entity.getContent(), entity.getContentLength());
					this.content = new InputStreamEntity(aais);
				}
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return this.content;
	}

	@Override
	public Header[] getAllHeaders() {
		if (this.headers == null) {
			org.apache.http.Header[] conheaders = httpresponse.getAllHeaders();
			headers = new Header[conheaders.length];
			int i = 0;
			for (int j = 0; j < conheaders.length; j++) {
				headers[i++] = new Header(conheaders[j].getName(), conheaders[j].getValue());
			}
		}

		return headers;
	}

	@Override
	public Header getHeader(String name) {
		Header[] headers = getAllHeaders();
		for (Header header : headers) {
			if (name.equalsIgnoreCase(header.getName())) {
				return header;
			}
		}

		return null;
	}

	@Override
	public StatusLine getStatusLine() {
		return statusline;
	}

	@Override
	public void close() throws IOException {
		httpresponse.close();
	}

	// @Override
	// public Header getFirstHeader(String keyname) {
	// // TODO Auto-generated method stub
	// return null;
	// }

}
