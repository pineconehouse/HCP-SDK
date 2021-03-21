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
package com.hitachivantara.core.http.conn.impl;

import java.io.IOException;

import com.amituofo.common.kit.io.FinalReadListener;
import com.amituofo.common.kit.io.NoticeInputStream;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.core.http.client.StatusLine;
import com.hitachivantara.core.http.conn.HttpConnection;
import com.hitachivantara.core.http.content.HttpEntity;
import com.hitachivantara.core.http.content.InputStreamEntity;
import com.hitachivantara.core.http.model.Header;

public class DefaultHttpResponse implements HttpResponse {
	private final HttpConnection connection;
	private final StatusLine statusline;
	private HttpEntity content;
	private Header[] headers = null;

	public DefaultHttpResponse(HttpConnection connection) throws IOException {
		this.connection = connection;
		this.statusline = new StatusLine(connection.getResponseCode(), connection.getResponseMessage());
		this.headers = connection.getAllHeaders();
	}

	@Override
	public HttpEntity getEntity() {
		if (this.content == null) {
			try {
				NoticeInputStream nis = new NoticeInputStream(connection.getInputStream(), new FinalReadListener() {

					@Override
					public void allReaded() {
						DefaultHttpConnectionManager.releaseService.releaseConnection(DefaultHttpResponse.this.connection);
					}

					@Override
					public void closed() {
						DefaultHttpConnectionManager.releaseService.releaseConnection(DefaultHttpResponse.this.connection);
					}
				});

				this.content = new InputStreamEntity(nis);
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}

		return this.content;
	}

	@Override
	public Header[] getAllHeaders() {
		return headers;
	}

	@Override
	public Header getHeader(String name) {
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
		connection.close();
	}

	// @Override
	// public Header getFirstHeader(String keyname) {
	// // TODO Auto-generated method stub
	// return null;
	// }

}
