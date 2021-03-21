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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.methods.HttpRequestBase;

import com.amituofo.common.api.IOAbortable;

public class ApacheAbortableInputStream extends FilterInputStream implements IOAbortable {
	private HttpRequestBase httprequest = null;
	private long contentLength;
	private long readLength;
	private boolean aborted = false;
	private boolean eofReached = false;

	public ApacheAbortableInputStream(HttpRequestBase httprequest, InputStream in, long contentLength) {
		super(in);
		this.httprequest = httprequest;
		this.contentLength = contentLength;
	}

	public int read() throws IOException {
		int v = in.read();
		if (v != -1) {
			readLength++;
		} else {
			eofReached = true;
		}
		return v;
	}

	public int read(byte[] b) throws IOException {
		int readLen = in.read(b);
		if (readLen > 0) {
			readLength += readLen;
		} else {
			eofReached = true;
		}
		return readLen;
	}

	public int read(byte[] b, int off, int len) throws IOException {
		int readLen = in.read(b, off, len);
		if (readLen > 0) {
			readLength += readLen;
		} else {
			eofReached = true;
		}
		return readLen;
	}

	public void abort() throws IOException {
		httprequest.abort();
		super.close();
		aborted = true;
	}

	@Override
	public void close() throws IOException {
		if (aborted) {
			return;
		}

		if (eofReached || readLength >= contentLength) {
			super.close();
		} else {
			httprequest.abort();
			super.close();
		}
	}

}
