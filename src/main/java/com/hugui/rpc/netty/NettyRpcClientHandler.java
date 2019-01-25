package com.hugui.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Copyright © 2019 Hugui. All rights reserved.
 * 
 * @Title: ServerHandler.java
 * @Prject: custom-rpc
 * @Package: com.hugui.rpc.netty
 * @Description:
 * @author: HuGui
 * @date: 2019年1月24日 下午4:51:50
 * @version: V1.0
 */

public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {

	private Object response;

	public Object getResponse() {
		return this.response;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("client receive message: " + msg);
		response = msg;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.flush();
		ctx.close();
	}

}
