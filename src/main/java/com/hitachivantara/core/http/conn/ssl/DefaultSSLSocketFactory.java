package com.hitachivantara.core.http.conn.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.hitachivantara.core.http.util.HttpUtils;

public class DefaultSSLSocketFactory extends SSLSocketFactory {
	private final static String[] supportedProtocols = new String[] { "TLSv1"};
	private final static String[] supportedCipherSuites = new String[] { "TLS_RSA_WITH_AES_128_CBC_SHA" };
	private SSLSocketFactory sslSocketFactory;

	public DefaultSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance("TLSv1");
		sc.init(null, HttpUtils.DUMMY_TRUST_MGR, new java.security.SecureRandom());
		this.sslSocketFactory = sc.getSocketFactory();
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return supportedCipherSuites;
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return supportedCipherSuites;
	}

	@Override
	public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
		Socket socket = sslSocketFactory.createSocket(s, host, port, autoClose);
		((SSLSocket)socket).setEnabledProtocols(supportedProtocols);
		((SSLSocket)socket).setEnabledCipherSuites(supportedCipherSuites);
		return socket;
	}

	@Override
	public Socket createSocket(String arg0, int arg1) throws IOException, UnknownHostException {
		Socket socket = sslSocketFactory.createSocket(arg0, arg1);
		((SSLSocket)socket).setEnabledProtocols(supportedProtocols);
		((SSLSocket)socket).setEnabledCipherSuites(supportedCipherSuites);
		return socket;
	}

	@Override
	public Socket createSocket(InetAddress arg0, int arg1) throws IOException {
		Socket socket = sslSocketFactory.createSocket(arg0, arg1);
		((SSLSocket)socket).setEnabledProtocols(supportedProtocols);
		((SSLSocket)socket).setEnabledCipherSuites(supportedCipherSuites);
		return socket;
	}

	@Override
	public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3) throws IOException, UnknownHostException {
		Socket socket = sslSocketFactory.createSocket(arg0, arg1, arg2, arg3);
		((SSLSocket)socket).setEnabledProtocols(supportedProtocols);
		((SSLSocket)socket).setEnabledCipherSuites(supportedCipherSuites);
		return socket;
	}

	@Override
	public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2, int arg3) throws IOException {
		Socket socket = sslSocketFactory.createSocket(arg0, arg1, arg2, arg3);
		((SSLSocket)socket).setEnabledProtocols(supportedProtocols);
		((SSLSocket)socket).setEnabledCipherSuites(supportedCipherSuites);
		return socket;
	}

}
