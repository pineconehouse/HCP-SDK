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
package com.hitachivantara.core.http.content;

public abstract class DefaultHttpEntity implements HttpEntity {

	protected static final int OUTPUT_BUFFER_SIZE = 4096;

	private String contentType = null;
	private String contentEncoding = null;
	private boolean chunked = false;

	public DefaultHttpEntity() {
	}

	/**
	 * Obtains the Content-Type header.
	 */
	@Override
	public String getContentType() {
		return this.contentType;
	}

	/**
	 * Obtains the Content-Encoding header.
	 */
	@Override
	public String getContentEncoding() {
		return this.contentEncoding;
	}

	/**
	 * Specifies the Content-Type header.
	 */
	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Specifies the Content-Encoding header.
	 */
	public void setContentEncoding(final String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	/**
	 * Obtains the 'chunked' flag.
	 */
	@Override
	public boolean isChunked() {
		return this.chunked;
	}

	/**
	 * Specifies the 'chunked' flag.
	 */
	public void setChunked(final boolean b) {
		this.chunked = b;
	}
}
