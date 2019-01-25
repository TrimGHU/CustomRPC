package com.hugui.rpc.api;

/**
 * Copyright © 2019 Hugui. All rights reserved.
 * 
 * @Title: EchoServiceImpl.java
 * @Prject: custom-rpc
 * @Package: com.hugui.rpc
 * @Description:
 * @author: HuGui
 * @date: 2019年1月24日 上午9:43:01
 * @version: V1.0
 */

public class EchoServiceImpl implements EchoService {

	@Override
	public String echo(String ping) {
		System.out.println("Provider : received " + ping);
		return "Hi " + ping + ", world.";
	}

}
