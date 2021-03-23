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

public class CustomKey<T> {
	protected String keyname = null;
	protected StringValueParser<T> valueParser;
	protected StringValueBuilder<T> valueBuilder;

	public CustomKey(String keyname) {
		this.keyname = keyname;
	}

	public CustomKey(String keyname, StringValueBuilder<T> valueBuilder) {
		this.keyname = keyname;
		this.valueBuilder = valueBuilder;
	}

	public CustomKey(String keyname, StringValueParser<T> valueParser) {
		this.keyname = keyname;
		this.valueParser = valueParser;
	}

	public String getKeyname() {
		return keyname;
	}

	public StringValueParser<T> getValueParser() {
		return valueParser;
	}

	public StringValueBuilder<T> getValueBuilder() {
		return valueBuilder;
	}

	@SuppressWarnings("unchecked")
	public T parse(String value) {
		if (value != null) {
			if (valueParser != null) {
				return valueParser.parse(value);
			}

			return (T) value;
		}

		return null;
	}

	public String build(T value) throws BuildException {
		if (value != null) {
			if (valueBuilder != null) {
				return valueBuilder.build(value);
			}

			return value.toString();
		}

		return null;
	}
}
