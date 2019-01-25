package com.hugui.rpc.netty;

import com.hugui.rpc.api.EchoService;
import com.hugui.rpc.api.EchoServiceImpl;

/**
 * Copyright © 2019 Hugui. All rights reserved.
 * 
 * @Title: Consumer.java
 * @Prject: custom-rpc
 * @Package: com.hugui.rpc
 * @Description:
 * @author: HuGui
 * @date: 2019年1月24日 上午11:36:06
 * @version: V1.0
 */

public class Consumer {

	public static void main(String[] args) {

		EchoService service = NettyRpcImporter.proxy(EchoServiceImpl.class);
		String result = service.echo("我的中国我的国");
		System.out.println(result);
		
	}

}
