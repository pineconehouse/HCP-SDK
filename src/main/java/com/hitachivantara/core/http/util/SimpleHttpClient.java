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

import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.core.http.client.impl.ApacheHttpClientBuilder;
import com.hitachivantara.core.http.ex.HttpException;
import com.hitachivantara.core.http.model.HttpHeader;
import com.hitachivantara.core.http.model.HttpParameter;

public class SimpleHttpClient extends HttpClientAgency {
	public SimpleHttpClient() {
		this(null);
	}

	public SimpleHttpClient(ClientConfiguration configuration) {
		super(new ApacheHttpClientBuilder(configuration == null ? new ClientConfiguration() : configuration));
	}

	public String getString(String url, HttpHeader header, HttpParameter param) throws HttpException, IOException  {
		HttpResponse response = get(url, header, param);
		return HttpUtils.responseEntityToString(response);
	}

}
