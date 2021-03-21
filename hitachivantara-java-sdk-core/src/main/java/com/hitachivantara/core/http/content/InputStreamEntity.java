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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InputStreamEntity extends DefaultHttpEntity {

	private final InputStream content;
	private final long length;

	public InputStreamEntity(final InputStream instream) {
		this(instream, -1);
	}

	public InputStreamEntity(final InputStream instream, final long length) {
		this(instream, length, null);
	}

	public InputStreamEntity(final InputStream instream, final String contentType) {
		this(instream, -1, contentType);
	}

	public InputStreamEntity(final InputStream instream, final long length, final String contentType) {
		super();
		this.content = instream;
		this.length = length;
		if (contentType != null) {
			setContentType(contentType.toString());
		}
	}

	@Override
	public long getContentLength() {
		return this.length;
	}

	@Override
	public InputStream getContent() throws IOException {
		return this.content;
	}

	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		final InputStream instream = this.content;
		try {
			final byte[] buffer = new byte[OUTPUT_BUFFER_SIZE];
			int l;
			if (this.length < 0) {
				// consume until EOF
				while ((l = instream.read(buffer)) != -1) {
					outstream.write(buffer, 0, l);
				}
			} else {
				// consume no more than length
				long remaining = this.length;
				while (remaining > 0) {
					l = instream.read(buffer, 0, (int) Math.min(OUTPUT_BUFFER_SIZE, remaining));
					if (l == -1) {
						break;
					}
					outstream.write(buffer, 0, l);
					remaining -= l;
				}
			}
		} finally {
			instream.close();
		}
	}

	@Override
	public boolean isStreaming() {
		return true;
	}

}
