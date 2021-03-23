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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.amituofo.common.define.Constants;

public class StringEntity extends DefaultHttpEntity {

	protected final byte[] content;

	/**
	 * Creates a StringEntity with the specified content and content type.
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public StringEntity(final String content, final String contentType) throws UnsupportedEncodingException {
		super();
		this.content = content.getBytes(Constants.DEFAULT_URL_ENCODE);
		if (contentType != null) {
			setContentType(contentType);
		}
	}

	public StringEntity(final String content) throws UnsupportedEncodingException {
		super();
		this.content = content.getBytes(Constants.DEFAULT_URL_ENCODE);
	}

	@Override
	public long getContentLength() {
		return this.content.length;
	}

	@Override
	public InputStream getContent() throws IOException {
		return new ByteArrayInputStream(this.content);
	}

	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		outstream.write(this.content);
		outstream.flush();
	}

	/**
	 * Tells that this entity is not streaming.
	 */
	@Override
	public boolean isStreaming() {
		return false;
	}

}
