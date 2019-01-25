package com.hugui.rpc.netty;

import java.lang.reflect.Method;

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

public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		RpcInfo rpcInfo = (RpcInfo) msg;

		String interfaceName = rpcInfo.getInterfaceName();
		String methodName = rpcInfo.getMethodName();
		Class<?>[] parameterTypes = rpcInfo.getParameterTypes();
		Object[] args = rpcInfo.getArgs();

		Class<?> clazz = Class.forName(interfaceName);
		Method method = clazz.getMethod(methodName, parameterTypes);

		ctx.writeAndFlush(method.invoke(clazz.newInstance(), args));
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
