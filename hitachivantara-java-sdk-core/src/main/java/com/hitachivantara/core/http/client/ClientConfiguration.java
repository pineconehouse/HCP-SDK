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

import java.net.InetSocketAddress;
import java.net.Proxy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import com.amituofo.common.util.StringUtils;
import com.hitachivantara.core.http.DnsResolver;
import com.hitachivantara.core.http.Protocol;
import com.hitachivantara.core.http.util.HttpUtils;

public class ClientConfiguration {
	public static final int DEFAULT_CHUNK_SIZE = 1024 * 128;

	/**
	 * 
	 */
	public static final int DEFAULT_CONNECT_TIMEOUT = -1;

	/**
	 * 
	 */
	public static final int DEFAULT_REQUEST_TIMEOUT = -1;

	/**
	 * 
	 */
	// public static final int DEFAULT_READ_TIMEOUT = -1;

	/**
	 * 
	 */
	// public static final int DEFAULT_MAX_ERROR_RETRY = 3;

	/**
	 * 
	 */
	public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 100;

	/**
	 * 
	 */
	public static final int DEFAULT_MAX_CONNECTIONS = 100;

	public final static String[] DEFAULT_SUPPORTED_PROTOCOLS = new String[] { "TLSv1" };

	public final static String[] DEFAULT_SUPPORTED_CIPHERSUITES = new String[] { "TLS_RSA_WITH_AES_128_CBC_SHA" };

	public final static int DEFAULT_NETWORKADDRESS_CACHE_TTL = 60;

	public final static int DEFAULT_NETWORKADDRESS_CACHE_NEGATIVE_TTL = 5;

	/**
	 * The default configuration is to use HTTP for all requests.
	 */
	private Protocol protocol = Protocol.HTTP;
	/**
	 * 
	 */
	private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
	/**
	 * specific for configuring the connection manager. It returns the timeout in milliseconds used when requesting a connection from the
	 * connection manager.
	 */
	private int requestTimeout = DEFAULT_REQUEST_TIMEOUT;

	/**
	 * 
	 */
	// private int readTimeout = DEFAULT_READ_TIMEOUT;

	/** Optionally specifies the proxy host to connect through. */
	private String proxyHost = null;

	/** Optionally specifies the port on the proxy host to connect through. */
	private int proxyPort = -1;

	private Proxy.Type proxyType = Proxy.Type.HTTP;

	/** Optionally specifies the user name to use when connecting through a proxy. */
	private String proxyUsername = null;

	/** Optionally specifies the password to use when connecting through a proxy. */
	private String proxyPassword = null;

	// private int maxErrorRetry = DEFAULT_MAX_ERROR_RETRY;

	/** enable streaming of a HTTP request body without internal buffering, when the content length is not known in advance. */
	private int chunkSize = DEFAULT_CHUNK_SIZE;

	/**
	 * The DNS Resolver to resolve IP addresses
	 */
	private DnsResolver dnsResolver = null;// new SystemDefaultDnsResolver();

	/** The maximum number of open HTTP connections. */
	// private int maxConnections = 50;

	// private boolean sslVerify = false;

	// private String[] supportedProtocols = new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" };

	// private String urlEncode = "UTF-8";

	private SSLSocketFactory sslSocketFactory = null;

	private HostnameVerifier hostnameVerifier = null;

	private int networkAddressCacheTTL = DEFAULT_NETWORKADDRESS_CACHE_TTL;
	private int networkAddressCacheNegativeTTL = DEFAULT_NETWORKADDRESS_CACHE_NEGATIVE_TTL;

	// 设置解析成功的域名记录JVM中缓存的有效时间，JVM默认是永远有效，这样一来域名IP重定向必须重启JVM，这里修改为5秒钟有效，0表示禁止缓存，-1表示永远有效
	private String[] supportedProtocols = DEFAULT_SUPPORTED_PROTOCOLS;
	// 设置解析失败的域名记录JVM中缓存的有效时间，JVM默认是10秒，0表示禁止缓存，-1表示永远有效
	private String[] supportedCipherSuites = DEFAULT_SUPPORTED_CIPHERSUITES;

	/** The maximum number of connections allowed for a route. **/
	private int defaultMaxConnectionsPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE;

	/** The maximum number of connections allowed across all routes. **/
	private int maxConnections = DEFAULT_MAX_CONNECTIONS;

	public ClientConfiguration() {
		// InMemoryDnsResolver
		ignoreSslVerification();
		ignoreHostnameVerification();

		setNetworkAddressCacheTTL(DEFAULT_NETWORKADDRESS_CACHE_TTL);
		setNetworkAddressCacheNegativeTTL(DEFAULT_NETWORKADDRESS_CACHE_NEGATIVE_TTL);
	}

	public ClientConfiguration(ClientConfiguration config) {
		this.chunkSize = config.chunkSize;
		this.connectTimeout = config.connectTimeout;
		this.requestTimeout = config.requestTimeout;
		// this.readTimeout = config.readTimeout;
		this.defaultMaxConnectionsPerRoute = config.defaultMaxConnectionsPerRoute;
		this.dnsResolver = config.dnsResolver;
		this.hostnameVerifier = config.hostnameVerifier;
		this.maxConnections = config.maxConnections;
		// this.maxErrorRetry = config.maxErrorRetry;
		this.protocol = config.protocol;
		this.proxyHost = config.proxyHost;
		this.proxyPassword = config.proxyPassword;
		this.proxyPort = config.proxyPort;
		this.proxyType = config.proxyType;
		this.proxyUsername = config.proxyUsername;
		this.sslSocketFactory = config.sslSocketFactory;

		setNetworkAddressCacheTTL(config.getNetworkAddressCacheTTL());
		setNetworkAddressCacheNegativeTTL(config.getNetworkAddressCacheNegativeTTL());
	}

	/**
	 * Returns the protocol (HTTP or HTTPS)
	 */
	public Protocol getProtocol() {
		return protocol;
	}

	/**
	 * Sets the protocol (i.e. HTTP or HTTPS)
	 */
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
		if (this.protocol == null) {
			this.protocol = Protocol.HTTP;
		}
	}

	public int getNetworkAddressCacheTTL() {
		return networkAddressCacheTTL;
	}

	/**
	 * Specified in java.security to indicate the caching policy for successful name lookups from the name service. The value is specified as
	 * integer to indicate the number of seconds to cache the successful lookup. The default behavior is to cache forever when a security
	 * manager is installed, and to cache for an implementation specific period of time, when a security manager is not installed.
	 * 
	 * @param networkAddressCacheTTL
	 *            value of -1 indicates "cache forever".
	 */
	public void setNetworkAddressCacheTTL(int networkAddressCacheTTL) {
		this.networkAddressCacheTTL = networkAddressCacheTTL;
		java.security.Security.setProperty("networkaddress.cache.ttl", String.valueOf(networkAddressCacheTTL));
		System.setProperty("sun.net.inetaddr.ttl", String.valueOf(networkAddressCacheTTL));
	}

	public int getNetworkAddressCacheNegativeTTL() {
		return networkAddressCacheNegativeTTL;
	}

	/**
	 * Specified in java.security to indicate the caching policy for un-successful name lookups from the name service.. The value is specified
	 * as integer to indicate the number of seconds to cache the failure for un-successful lookups.
	 * 
	 * @param networkAddressCacheNegativeTTL
	 *            value of 0 indicates "never cache". value of -1 indicates "cache forever".
	 */
	public void setNetworkAddressCacheNegativeTTL(int networkAddressCacheNegativeTTL) {
		this.networkAddressCacheNegativeTTL = networkAddressCacheNegativeTTL;
		java.security.Security.setProperty("networkaddress.cache.negative.ttl", String.valueOf(networkAddressCacheNegativeTTL));
		System.setProperty("sun.net.inetaddr.negative.ttl", String.valueOf(networkAddressCacheNegativeTTL));
	}

	/**
	 * Returns the value for the given system property.
	 */
	private String getSystemProperty(String property) {
		return System.getProperty(property);
	}

	/**
	 * Returns the Java system property for proxy host. if protocol is https, returns the value of the system property https.proxyHost,
	 * otherwise returns value of http.proxyHost.
	 */
	private String getProxyHostProperty() {
		return getProtocol() == Protocol.HTTPS ? getSystemProperty("https.proxyHost") : getSystemProperty("http.proxyHost");
	}

	/**
	 * Returns the optional proxy host the client will connect through. if protocol is https, returns the value of the system property
	 * https.proxyHost, otherwise returns value of http.proxyHost.
	 */
	public String getProxyHost() {
		return (proxyHost != null) ? proxyHost : getProxyHostProperty();
	}

	/**
	 * Sets the optional proxy host the client will connect through.
	 */
	public void setProxy(String proxyHost, int proxyPort) {
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
	}

	public void setProxy(String proxyHost, int proxyPort, Proxy.Type proxyType) {
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.proxyType = proxyType;
	}

	public Proxy.Type getProxyType() {
		return proxyType;
	}

	public void setProxyType(Proxy.Type proxyType) {
		this.proxyType = proxyType;
	}

	/**
	 * Returns the Java system property for proxy port. if protocol is https, returns the value of the system property https.proxyPort,
	 * otherwise returns value of http.proxyPort.
	 */
	private int getProxyPortProperty() {
		final String proxyPortString = (getProtocol() == Protocol.HTTPS) ? getSystemProperty("https.proxyPort") : getSystemProperty("http.proxyPort");
		try {
			return Integer.parseInt(proxyPortString);
		} catch (NumberFormatException e) {
			return proxyPort;
		}
	}

	/**
	 * Returns the optional proxy port the client will connect through. Returns either the proxyPort. if protocol is https, returns the value of
	 * the system property https.proxyPort, otherwise returns value of http.proxyPort.
	 *
	 * @return The proxy port the client will connect through.
	 */
	public int getProxyPort() {
		return (proxyPort >= 0) ? proxyPort : getProxyPortProperty();
	}

	/**
	 * Returns the Java system property for proxy user name. if protocol is https, returns the value of the system property https.proxyUser,
	 * otherwise returns value of http.proxyUser.
	 */
	private String getProxyUsernameProperty() {
		return (getProtocol() == Protocol.HTTPS) ? getSystemProperty("https.proxyUser") : getSystemProperty("http.proxyUser");
	}

	/**
	 * Returns the optional proxy user name to use if connecting through a proxy. Returns either the proxyUsername. if protocol is https,
	 * returns the value of the system property https.proxyUser, otherwise returns value of http.proxyUser.
	 */
	public String getProxyUsername() {
		return (proxyUsername != null) ? proxyUsername : getProxyUsernameProperty();
	}

	/**
	 * Sets the optional proxy user name to use if connecting through a proxy. The proxy user name to use if connecting through a proxy.
	 */
	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}

	/**
	 * Returns the Java system property for proxy password. if protocol is https, returns the value of the system property https.proxyPassword,
	 * otherwise returns value of http.proxyPassword.
	 */
	private String getProxyPasswordProperty() {
		return (getProtocol() == Protocol.HTTPS) ? getSystemProperty("https.proxyPassword") : getSystemProperty("http.proxyPassword");
	}

	/**
	 * Returns the optional proxy password to use if connecting through a proxy. Returns either the proxyPassword. if protocol is https, returns
	 * the value of the system property https.proxyPassword, otherwise returns value of http.proxyPassword.
	 */
	public String getProxyPassword() {
		String pwd = (proxyPassword != null) ? proxyPassword : getProxyPasswordProperty();
		if (pwd == null) {
			return "";
		}

		return pwd;
	}

	/**
	 * Sets the optional proxy password to use when connecting through a proxy.
	 */
	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	/**
	 * Returns the DnsResolver for resolving AWS IP addresses.
	 */
	public DnsResolver getDnsResolver() {
		return dnsResolver;
	}

	/**
	 * Sets the DNS Resolver that should be used to for resolving AWS IP addresses.
	 */
	public void setDnsResolver(final DnsResolver resolver) {
		if (resolver == null) {
			throw new IllegalArgumentException("resolver cannot be null");
		}
		this.dnsResolver = resolver;

		// The Host header is filled by the HttpURLConnection based on the URL. You can't open foo.com with Host=bar.com. From the RFC
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
	}

	public Proxy getProxy() {
		Proxy proxy = null;
		if (StringUtils.isNotEmpty(this.getProxyHost()) && this.getProxyPort() > 0) {
			InetSocketAddress addr = new InetSocketAddress(this.getProxyHost(), this.getProxyPort());
			proxy = new Proxy(proxyType, addr); // http 代理
		}

		return proxy;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * Refers the maximum time in milliseconds client will wait for connection establishment with server. Within give time the server must have
	 * established a connection with Client other it will throw an Exception.
	 * 
	 * @param connectTimeout
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	// public int getReadTimeout() {
	// return readTimeout;
	// }
	//
	// public void setReadTimeout(int readTimeout) {
	// this.readTimeout = readTimeout;
	// }

	// public int getMaxConnections() {
	// return maxConnections;
	// }
	//
	// public void setMaxConnections(int maxConnections) {
	// if (maxConnections <= 0) {
	// maxConnections = 1;
	// }
	// this.maxConnections = maxConnections;
	// }

	public int getRequestTimeout() {
		return requestTimeout;
	}

	/**
	 * specific for configuring the connection manager. It returns the timeout in milliseconds used when requesting a connection from the
	 * connection manager.
	 * 
	 * @param requestTimeout
	 */
	public void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}

	// public int getMaxErrorRetry() {
	// return maxErrorRetry;
	// }
	//
	// public void setMaxErrorRetry(int maxErrorRetry) {
	// this.maxErrorRetry = maxErrorRetry;
	// }

	public SSLSocketFactory getSslSocketFactory() {
		return sslSocketFactory;
	}

	public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
		this.sslSocketFactory = sslSocketFactory;
	}

	public HostnameVerifier getHostnameVerifier() {
		return hostnameVerifier;
	}

	public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
		this.hostnameVerifier = hostnameVerifier;
	}

	public String[] getSupportedProtocols() {
		return supportedProtocols;
	}

	public void setSupportedProtocols(String[] supportedProtocols) {
		this.supportedProtocols = supportedProtocols;
	}

	public String[] getSupportedCipherSuites() {
		return supportedCipherSuites;
	}

	public void setSupportedCipherSuites(String[] supportedCipherSuites) {
		this.supportedCipherSuites = supportedCipherSuites;
	}

	public void ignoreSslVerification() {
		try {
			
//			SSLContext sc = SSLContext.getInstance("SSL");
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, HttpUtils.DUMMY_TRUST_MGR, new java.security.SecureRandom());
			this.sslSocketFactory = sc.getSocketFactory();
			// this.sslSocketFactory = new DefaultSSLSocketFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ignoreHostnameVerification() {
		this.hostnameVerifier = HttpUtils.DUMMY_HOST_NAME_VERIFIER;
	}

	public int getChunkSize() {
		return this.chunkSize;
	}

	public void setChunkSize(int chunkSize) {
		if (chunkSize < 0) {
			this.chunkSize = 0;
		} else {
			this.chunkSize = chunkSize;
		}
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public int getDefaultMaxConnectionsPerRoute() {
		return defaultMaxConnectionsPerRoute;
	}

	public void setDefaultMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
		this.defaultMaxConnectionsPerRoute = maxConnectionsPerRoute;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	// public boolean isSslVerify() {
	// return sslVerify;
	// }
	//
	// public void setSslVerify(boolean sslVerify) {
	// this.sslVerify = sslVerify;
	// }

	// public String[] getSupportedProtocols() {
	// return supportedProtocols;
	// }
	//
	// public void setSupportedProtocols(String[] supportedProtocols) {
	// this.supportedProtocols = supportedProtocols;
	// }

	// public String getUrlEncode() {
	// return urlEncode;
	// }
	//
	// public void setUrlEncode(String urlEncode) {
	// this.urlEncode = urlEncode;
	// }

}
