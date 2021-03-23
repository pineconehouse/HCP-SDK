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

import java.net.InetAddress;

import com.amituofo.common.util.RandomUtils;

public interface DnsResolverStrategy {
	InetAddress[] pick(InetAddress[] addresses);

	public final static DnsResolverStrategy STANDARD = new DnsResolverStrategy() {

		@Override
		public InetAddress[] pick(InetAddress[] addresses) {
			return addresses;
		}
	};

	public final static DnsResolverStrategy RANDOM = new DnsResolverStrategy() {

		@Override
		public InetAddress[] pick(InetAddress[] addresses) {
			int len = addresses.length;
			if (len > 1) {
				InetAddress[] newaddresses = new InetAddress[len];
				for (int i = 0; i < newaddresses.length; i++) {
					newaddresses[i] = addresses[i];
				}

				int index = RandomUtils.randomInt(0, len - 1);

				InetAddress temp = addresses[0];
				addresses[0] = addresses[index];
				addresses[index] = temp;

				return newaddresses;
			} else {
				return addresses;
			}
		}
	};
}
