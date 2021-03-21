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

import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.core.http.conn.HttpConnection;
import com.hitachivantara.core.http.conn.HttpConnectionFactory;

public class DefaultHttpConnectionFactory implements HttpConnectionFactory {
	public final static DefaultHttpConnectionFactory instance = new DefaultHttpConnectionFactory();
	
	private ClientConfiguration config;

	public DefaultHttpConnectionFactory() {
		this.config = new ClientConfiguration();
	}

	public DefaultHttpConnectionFactory(ClientConfiguration configuration) {
		this.config = configuration;
	}

	@Override
	public void setClientConfiguration(ClientConfiguration config) {
		this.config = config;
	}

	@Override
	public HttpConnection create() {
		return new HttpURLConnection(config);
	}

}
