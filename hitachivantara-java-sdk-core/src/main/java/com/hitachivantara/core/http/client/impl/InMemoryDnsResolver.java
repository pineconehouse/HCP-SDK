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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hitachivantara.core.http.DnsResolver;
import com.hitachivantara.core.http.client.DnsResolverStrategy;

public class InMemoryDnsResolver implements DnsResolver {
	private boolean unsolvableException = false;
	private DnsResolverStrategy resolverStrategy = DnsResolverStrategy.RANDOM;

	/**
	 * In-memory collection that will hold the associations between a host name and an array of InetAddress instances.
	 */
	private final Map<String, InetAddress[]> dnsMap;

	/**
	 * Builds a DNS resolver that will resolve the host names against a collection held in-memory.
	 */
	public InMemoryDnsResolver() {
		dnsMap = new ConcurrentHashMap<String, InetAddress[]>();
	}

	public boolean isUnsolvableException() {
		return unsolvableException;
	}

	public void setUnsolvableException(boolean unsolvableException) {
		this.unsolvableException = unsolvableException;
	}

	public DnsResolverStrategy getResolverStrategy() {
		return resolverStrategy;
	}

	public void setResolverStrategy(DnsResolverStrategy resolverStrategy) {
		if (resolverStrategy == null) {
			this.resolverStrategy = DnsResolverStrategy.RANDOM;
		} else {
			this.resolverStrategy = resolverStrategy;
		}
	}

	/**
	 * Associates the given array of IP addresses to the given host in this DNS overrider. The IP addresses are assumed to be already resolved.
	 *
	 * @param host
	 *            The host name to be associated with the given IP.
	 * @param ips
	 *            array of IP addresses to be resolved by this DNS overrider to the given host name.
	 */
	public void add(final String host, final InetAddress... ips) {
		if (ips == null || ips.length == 0) {
			return;
		}

		InetAddress[] hostIPs = dnsMap.get(host);
		if (hostIPs == null) {
			hostIPs = ips;
		} else {
			List<InetAddress> list = new ArrayList<InetAddress>();
			for (InetAddress ip : hostIPs) {
				list.add(ip);
			}
			for (InetAddress ip : ips) {
				if (!hasIp(hostIPs, ip)) {
					list.add(ip);
				}
			}

			hostIPs = list.toArray(new InetAddress[list.size()]);
		}

		dnsMap.put(host, hostIPs);
	}

	private boolean hasIp(InetAddress[] hostIPs, InetAddress ip) {
		for (InetAddress inetAddress : hostIPs) {
			if (inetAddress.equals(ip)) {
				return true;
			}
		}

		return false;
	}

	public void add(final String host, final String... ips) throws UnknownHostException {
		if (ips == null || ips.length == 0) {
			return;
		}

		InetAddress[] ipAds = new InetAddress[ips.length];
		for (int i = 0; i < ips.length; i++) {
			ipAds[i] = InetAddress.getByName(ips[i]);
		}

		add(host, ipAds);
	}

	public InetAddress[] resolve(final String host) throws UnknownHostException {
		final InetAddress[] resolvedAddresses = dnsMap.get(host);
		if (resolvedAddresses == null) {
			if (unsolvableException) {
				throw new UnknownHostException(host + " cannot be resolved!");
			}

			return InetAddress.getAllByName(host);
		}

		return resolverStrategy.pick(resolvedAddresses);

	}

}
