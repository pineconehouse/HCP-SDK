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

import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.core.http.client.HttpClient;
import com.hitachivantara.core.http.client.HttpClientBuilder;

public class SimpleUrlHttpClientBuilder implements HttpClientBuilder {
//	protected HttpClientConnectionManager cm = null;
	protected ClientConfiguration configuration;
//	protected HttpClientBuilder httpClientBuilder;
//	ApacheHttpClientBuilder ab;

	public SimpleUrlHttpClientBuilder(ClientConfiguration configuration) {
		this.configuration = configuration;
		
//		try {
//			SSLContextBuilder builder = new SSLContextBuilder();
//			if (!configuration.isSslVerify()) {
//				// Trush all
//				builder.loadTrustMaterial(null, new TrustStrategy() {
//					public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
//						return true;
//					}
//				});
//			}
//
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(), configuration.getSupportedProtocols(), null, NoopHostnameVerifier.INSTANCE);
//			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register(Protocol.HTTP.toString(), new PlainConnectionSocketFactory())
//					.register(Protocol.HTTPS.toString(), sslsf).build();
//			this.cm = new PoolingHttpClientConnectionManager(registry, configuration.getDnsResolver()); // TODO Problem?
////			this.cm.setMaxTotal(configuration.getMaxConnections());
////			this.cm.setMaxTotal(configuration.getMaxConnections());
//
////			this.cm = new PoolingHttpClientConnectionManager();
////			this.cm.closeExpiredConnections();;
//
//			this.httpClientBuilder = HttpClients.custom()
//					.setSSLSocketFactory(sslsf)
//					.setConnectionManager(cm)
//					.setConnectionManagerShared(true)
//					.setDnsResolver(configuration.getDnsResolver());
//			
//
//			String proxyHost = configuration.getProxyHost();
//			int proxyPort = configuration.getProxyPort();
//			if (proxyHost != null && proxyPort > 0) {
//				HttpHost proxy = new HttpHost(proxyHost, proxyPort);
//				RequestConfig defaultRequestConfig = RequestConfig.custom().setProxy(proxy).build();
//				this.httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig);
//
//				String userName = configuration.getProxyUsername();
//				String password = configuration.getProxyPassword();
//				if (StringUtils.isNotEmpty(userName)) {
//					CredentialsProvider cp = new BasicCredentialsProvider();
//					cp.setCredentials(new AuthScope(proxyHost, proxyPort), new UsernamePasswordCredentials(userName, password));
//					this.httpClientBuilder.setDefaultCredentialsProvider(cp);
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public HttpClient build() {
		HttpClient httpClient = new SimpleUrlHttpClient(configuration);
		return httpClient;
	}

	public ClientConfiguration getClientConfiguration() {
		return configuration;
	}

}
