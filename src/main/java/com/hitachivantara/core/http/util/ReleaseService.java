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
package com.hitachivantara.core.http.util;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.hitachivantara.core.http.conn.HttpConnection;

public class ReleaseService {
	private final Thread releaseServer;
	private final Queue<HttpConnection> cs = new ConcurrentLinkedQueue<HttpConnection>();
	private boolean stop = false;

	public ReleaseService() {
		this.releaseServer = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!stop) {
					
					HttpConnection connection = cs.poll();
					while (connection != null) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
						}
						
						try {
							connection.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						connection = cs.poll();
					}
				}
			}
		});

		this.releaseServer.start();
	}

	public void releaseConnection(HttpConnection connection) {
		cs.add(connection);
	}

	public void stop() {
		stop = true;
	}

}
