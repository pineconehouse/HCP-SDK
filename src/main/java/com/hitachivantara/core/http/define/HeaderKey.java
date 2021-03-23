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
package com.hitachivantara.core.http.define;

import com.amituofo.common.api.StringValueBuilder;
import com.amituofo.common.api.StringValueParser;
import com.amituofo.common.ex.BuildException;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.core.http.model.Header;

public class HeaderKey<T> extends CustomKey<T> {

	/**
	 * Specifies the hostname for the request. The host name identifies either a tenant or a bucket.<br>
	 * For a tenant, use this format:<br>
	 * tenant-name.hcp-domain-name <br>
	 * For a bucket, use this format:<br>
	 * bucket-name.tenant-name.hcp-domain-name
	 **/
	public final static HeaderKey<String> HOST = new HeaderKey<String>("Host");
	/**
	 * The size, in bytes, of the response body if HCP can determine the size before formulating the response.<br>
	 * If the response does not include a response body, the value of the Content-Length is 0 (zero).
	 **/
	public final static HeaderKey<Long> CONTENT_LENGTH = new HeaderKey<Long>("Content-Length", StringValueParser.LONG_TYPE_PARSER);
	/**
	 * The Internet media type of the response body if HCP can determine the Internet media type. <br>
	 * If HCP cannot determine the Internet media type, the value of this header is application/octet-stream.<br>
	 * Because HCP returns error information in a response body, the response to any request can include a Content-Type header.
	 **/
	public final static HeaderKey<String> CONTENT_TYPE = new HeaderKey<String>("Content-Type");
	public final static HeaderKey<String> ACCEPT = new HeaderKey<String>("Accept");
	public final static HeaderKey<String> ACCEPT_ENCODING = new HeaderKey<String>("Accept-Encoding");
	public final static HeaderKey<String> USER_AGENT = new HeaderKey<String>("User-Agent");
	public final static HeaderKey<String> CONNECTION = new HeaderKey<String>("Connection");
	/**
	 * Always chunked. This header is returned if the response includes a response body but HCP cannot determine the size of the response body
	 * before formulating the response.<br>
	 * Because HCP returns error information in a response body, the response to any request can include a Transfer-Encoding header.
	 **/
	public final static HeaderKey<String> TRANSFER_ENCODING = new HeaderKey<String>("Transfer-Encoding");
	/****/
	public final static HeaderKey<String> CONTENT_ENCODING = new HeaderKey<String>("Content-Encoding");
	
	public HeaderKey(String keyname, StringValueBuilder<T> requestValueBuilder) {
		super(keyname, requestValueBuilder);
	}

	public HeaderKey(String keyname, StringValueParser<T> responseHeadValueParser) {
		super(keyname, responseHeadValueParser);
	}

	public HeaderKey(String keyname) {
		super(keyname);
	}

	public T parse(HttpResponse response) {
		Header header = response.getHeader(this.keyname);
		if (header != null) {
			return super.parse(header.getValue());
		}

		return null;
	}
	
	public Header createHeader(T value) throws BuildException {
		return new Header(this.getKeyname(), this.build(value));
	}

}
