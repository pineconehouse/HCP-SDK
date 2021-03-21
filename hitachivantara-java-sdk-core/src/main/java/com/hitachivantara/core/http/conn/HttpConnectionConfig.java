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
package com.hitachivantara.core.http.conn;

import com.hitachivantara.core.http.client.ClientConfiguration;

public class HttpConnectionConfig extends ClientConfiguration {
	/**
	 * 
	 */
	public final boolean DEFAULT_FOLLOW_REDIRECTS = true;

	/**
	 * 
	 */
	public final boolean DEFAULT_USE_CACHES = false;

	/**
	 * 
	 */
	private boolean followRedirects = DEFAULT_FOLLOW_REDIRECTS;

	/**
	 * 
	 */
	private boolean useCaches = DEFAULT_USE_CACHES;

	public boolean isFollowRedirects() {
		return followRedirects;
	}

	public void setFollowRedirects(boolean followRedirects) {
		this.followRedirects = followRedirects;
	}

	public boolean isUseCaches() {
		return useCaches;
	}

	public void setUseCaches(boolean useCaches) {
		this.useCaches = useCaches;
	}

}
