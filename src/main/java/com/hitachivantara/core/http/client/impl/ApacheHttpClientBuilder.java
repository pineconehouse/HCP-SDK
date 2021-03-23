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

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.amituofo.common.util.StringUtils;
import com.hitachivantara.core.http.Protocol;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.core.http.client.HttpClient;
import com.hitachivantara.core.http.client.HttpClientBuilder;

public class ApacheHttpClientBuilder implements HttpClientBuilder {
	protected final ClientConfiguration configuration;
	private final PoolingHttpClientConnectionManager connectionManager;
	private final org.apache.http.impl.client.HttpClientBuilder httpClientBuilder;

	public ApacheHttpClientBuilder(ClientConfiguration configuration) {
		this.configuration = configuration;

		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
		if (configuration.getProtocol() == Protocol.HTTPS) {
			if (configuration.getSslSocketFactory() != null) {
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
						configuration.getSslSocketFactory(),
						configuration.getSupportedProtocols(),
						configuration.getSupportedCipherSuites(),
						configuration.getHostnameVerifier());
				registryBuilder.register(Protocol.HTTPS.toString(), sslsf);
			} else {
				registryBuilder.register(Protocol.HTTPS.toString(), SSLConnectionSocketFactory.getSocketFactory());
			}
		} else if (configuration.getProtocol() == Protocol.HTTP) {
			registryBuilder.register(Protocol.HTTP.toString(), new PlainConnectionSocketFactory());
		}

		Registry<ConnectionSocketFactory> registry = registryBuilder.build();

		connectionManager = new PoolingHttpClientConnectionManager(registry, configuration.getDnsResolver());
		connectionManager.setMaxTotal(configuration.getMaxConnections());
		connectionManager.setDefaultMaxPerRoute(configuration.getDefaultMaxConnectionsPerRoute());
		// connectionManager = new PoolingHttpClientConnectionManager();
		// connectionManager.setMaxTotal(20);
		// connectionManager.setDefaultMaxPerRoute(20);

		this.httpClientBuilder = HttpClients.custom().setConnectionManager(connectionManager);

		// remove Accept-Encoding: gzip,deflate
		this.httpClientBuilder.disableContentCompression();

		if (configuration.getDnsResolver() != null) {
			this.httpClientBuilder.setDnsResolver(configuration.getDnsResolver());
		}

		int configCount = 0;
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
		if (configuration.getConnectTimeout() > 0) {
			requestConfigBuilder.setConnectTimeout(configuration.getConnectTimeout());
			configCount++;
		}
		if (configuration.getRequestTimeout() > 0) {
			requestConfigBuilder.setConnectionRequestTimeout(configuration.getRequestTimeout());
			configCount++;
		}

		String proxyHost = configuration.getProxyHost();
		int proxyPort = configuration.getProxyPort();
		if (proxyHost != null && proxyPort > 0) {
			HttpHost proxy = new HttpHost(proxyHost, proxyPort, configuration.getProtocol().name());
			requestConfigBuilder.setProxy(proxy);

			String userName = configuration.getProxyUsername();
			String password = configuration.getProxyPassword();
			if (StringUtils.isNotEmpty(userName)) {
				CredentialsProvider cp = new BasicCredentialsProvider();
				cp.setCredentials(new AuthScope(proxyHost, proxyPort), new UsernamePasswordCredentials(userName, password));
				this.httpClientBuilder.setDefaultCredentialsProvider(cp);
			}

			configCount++;
		}

		if (configCount > 0) {
			RequestConfig defaultRequestConfig = requestConfigBuilder.build();
			this.httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig);
		}

	}

	public HttpClient build() {
		// CloseableHttpClient httpClient = HttpClients.custom().setDnsResolver(dnsResolver).setConnectionManager(connectionManager).build();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		return new ApacheHttpClient(this.configuration, httpClient);
	}

	public ClientConfiguration getClientConfiguration() {
		return configuration;
	}

}
