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
package com.hitachivantara.core.http.client;

import com.hitachivantara.core.http.HttpRequest;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.core.http.ex.HttpException;

public abstract class HttpClientBase implements CloseableHttpClient {
	protected final ClientConfiguration config;

	public HttpClientBase(ClientConfiguration config) {
		if (config != null) {
			this.config = config;
		} else {
			this.config = new ClientConfiguration();
		}
	}

	@Override
	public <T> T execute(HttpRequest request, ResponseHandler<T> handler) throws HttpException {
		HttpResponse response = execute(request);

		T result = handler.handle(response);

		return result;
	}

	@Override
	public HttpResponse execute(HttpRequest request) throws HttpException {
		HttpResponse response;
		try {
			response = request(request);
		} catch (HttpException e) {
			throw e;
		}

		return response;
	}

	protected abstract HttpResponse request(HttpRequest request) throws HttpException;

}
