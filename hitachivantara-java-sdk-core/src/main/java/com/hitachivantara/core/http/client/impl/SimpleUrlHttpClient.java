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

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;

import com.amituofo.common.kit.value.NameValuePair;
import com.amituofo.common.util.StringUtils;
import com.hitachivantara.core.http.DnsResolver;
import com.hitachivantara.core.http.HttpRequest;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.core.http.client.HttpClientBase;
import com.hitachivantara.core.http.content.HttpEntity;
import com.hitachivantara.core.http.define.HeaderKey;
import com.hitachivantara.core.http.ex.HttpException;
import com.hitachivantara.core.http.model.Header;
import com.hitachivantara.core.http.model.HttpHeader;

public class SimpleUrlHttpClient extends HttpClientBase {
	private java.net.HttpURLConnection connection;
	private Header[] headers;

	public SimpleUrlHttpClient(ClientConfiguration config) {
		super(config);
	}

	@Override
	protected HttpResponse request(HttpRequest request) throws HttpException {

		try {
			URL url = request.getURI().toURL();

			DnsResolver dnsr = config.getDnsResolver();
			if (dnsr != null) {
				InetAddress[] ip = dnsr.resolve(url.getHost());
				if (ip != null) {
					url = new URL(url.getProtocol(), ip[0].getHostAddress(), url.getPort(), url.getPath());
				}
			}

			// HttpUtils.printHttpRequest((HttpRequestBase)request, url);

			// Set proxy if necessary
			if (config.getProxy() != null) {
				connection = (java.net.HttpURLConnection) url.openConnection(config.getProxy());

				if (StringUtils.isNotEmpty(config.getProxyUsername())) {
					String headerkey = "Proxy-Authorization";
					String headerValue = "Basic " + Base64.encodeBase64String((config.getProxyUsername() + ":" + config.getProxyPassword()).getBytes());
					connection.setRequestProperty(headerkey, headerValue);
				}
			} else {
				connection = (java.net.HttpURLConnection) url.openConnection();
			}

			if ("https".equalsIgnoreCase(url.getProtocol())) {
				if (config.getSslSocketFactory() != null) {
					((HttpsURLConnection) connection).setSSLSocketFactory(config.getSslSocketFactory());
				}
				if (config.getHostnameVerifier() != null) {
					((HttpsURLConnection) connection).setHostnameVerifier(config.getHostnameVerifier());
				}
			}

			connection.setRequestMethod(request.getMethod().name()); // Set method
			// sets whether this URLConnection can be used for reading content from the server (default is true).
			connection.setDoInput(true);
			connection.setUseCaches(false); // disable caches
			connection.setInstanceFollowRedirects(true); // Auto redirect
			// connection.setUseCaches(config.isUseCaches()); //
			// connection.setInstanceFollowRedirects(config.isFollowRedirects());

			if (config.getRequestTimeout() != ClientConfiguration.DEFAULT_REQUEST_TIMEOUT) {
				connection.setConnectTimeout(config.getRequestTimeout());
			}

//			if (config.getReadTimeout() != ClientConfiguration.DEFAULT_READ_TIMEOUT) {
//				connection.setReadTimeout(config.getReadTimeout());
//			}

			HttpEntity httpContent = request.getEntity();
			if (httpContent != null) {
				// sets whether this URLConnection can be used for sending data to the server (default is false).
				connection.setDoOutput(true);

				if (httpContent.getContentLength() > 0) {
					request.setHeader(HeaderKey.CONTENT_LENGTH.createHeader(httpContent.getContentLength()));
				} else {
					// if (request.getMethod() == Method.PUT)
					// enable streaming of a HTTP request body without internal buffering, when the content length is not known in advance.
					connection.setChunkedStreamingMode(config.getChunkSize());
					// }
				}

				if (httpContent.getContentType() != null) {
					request.setHeader(HeaderKey.CONTENT_TYPE.createHeader(httpContent.getContentType()));
				}

				if (httpContent.getContentEncoding() != null) {
					request.setHeader(HeaderKey.CONTENT_ENCODING.createHeader(httpContent.getContentEncoding()));
				}
			}

			// System.out.println("-Request URI:-------------------------------------------------------------------------");
			// System.out.println(" " + request.getMethod() + " " + request.getURL());

			HttpHeader requestHeaders = request.getHttpHeader();
			if (requestHeaders != null && requestHeaders.size() > 0) {
				Collection<NameValuePair<String>> pair = requestHeaders.values();
				for (NameValuePair<String> entry : pair) {
					String name = entry.getName();
					String value = entry.getStringValue();

					connection.addRequestProperty(name, value);

					// System.out.println(">>" + name + "=" + value);
				}
			}

			if (httpContent != null) {
				OutputStream out = null;
				out = connection.getOutputStream();

				httpContent.writeTo(out);
				out.flush();
				out.close();
			}

			Map<String, List<String>> responseHeaders = connection.getHeaderFields();
			this.headers = new Header[responseHeaders.size()];
			int i = 0;
			for (Iterator<String> it = responseHeaders.keySet().iterator(); it.hasNext();) {
				String name = it.next();
				String value = null;
				List<String> listvalue = responseHeaders.get(name);
				if (listvalue != null && listvalue.size() > 0) {
					value = listvalue.get(0);
				}

				this.headers[i++] = new Header(name, value);
				// System.out.println("<<" + name + "=" + value);
			}

			HttpResponse response = null;//new DefaultHttpResponse(connection);

			return response;
		} catch (Exception e) {
			throw new HttpException(e);
		}
	}

	@Override
	public void close() {
		connection.disconnect();
	}

}
