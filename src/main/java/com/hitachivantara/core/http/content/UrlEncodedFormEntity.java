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

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import com.amituofo.common.kit.value.NameValuePair;
import com.amituofo.common.kit.value.Value;

public class UrlEncodedFormEntity extends StringEntity {

	public UrlEncodedFormEntity(Collection<NameValuePair<String>> formparams) throws UnsupportedEncodingException {
		super(toParams(formparams));
	}
	
	private static String toParams(Collection<NameValuePair<String>> params) {
		if (params == null || params.isEmpty()) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		int i = 0;
		for (NameValuePair<String> nm : params) {
			if (i != 0) {
				builder.append('&');
			}

			builder.append(nm.getName());
			Value<String> value =nm.getValue();
			if (value != null) {
				builder.append('=');
				builder.append(value.buildStringValue());
			}

			i++;
		}

		return builder.toString();
	}

}
