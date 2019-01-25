package com.hugui.rpc.socket;

import java.net.InetSocketAddress;

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
		// 调用方rpc调用
		RpcImporter<EchoService> rpcImporter = new RpcImporter<>();
		// 调用服务提供方的实现类
		EchoService echoService = rpcImporter.importer(EchoServiceImpl.class, new InetSocketAddress("localhost", 8080));
		System.out.println("Consumer : " + echoService.echo("我的中国我的家"));
	}

}
