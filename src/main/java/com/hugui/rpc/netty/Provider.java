package com.hugui.rpc.netty;

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

	public static void main(String[] args) throws InterruptedException {
		new NettyRpcExporter().run();
	}

}
