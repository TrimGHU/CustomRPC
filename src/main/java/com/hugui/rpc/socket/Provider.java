package com.hugui.rpc.socket;

import java.io.IOException;

/**
 * Copyright © 2019 Hugui. All rights reserved.
 * 
 * @Title: main.java
 * @Prject: custom-rpc
 * @Package: com.hugui.rpc
 * @Description:
 * @author: HuGui
 * @date: 2019年1月24日 上午11:26:29
 * @version: V1.0
 */

public class Provider {

	public static void main(String[] args) {
		// 启动服务提供方服务
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					RpcExporter.exporter("localhost", 8080);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
