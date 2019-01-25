package com.hugui.rpc.netty;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Copyright © 2019 Hugui. All rights reserved.
 * 
 * @Title: NettyRpcImporter.java
 * @Prject: custom-rpc
 * @Package: com.hugui.rpc.netty
 * @Description:
 * @author: HuGui
 * @date: 2019年1月24日 下午4:38:21
 * @version: V1.0
 */

public class NettyRpcImporter {

	@SuppressWarnings("unchecked")
	public static <T> T proxy(Class<?> clazz) {
		MethodProxy proxy = new MethodProxy(clazz);
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz.getInterfaces()[0] }, proxy);
	}
}

class MethodProxy implements InvocationHandler {

	private Class<?> clazz;

	public MethodProxy(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		if (Object.class.equals(clazz.getDeclaringClass())) {
			return method.invoke(this, args);
		} else {
			return rpcInvoke(method, args);
		}
	}

	private Object rpcInvoke(Method method, Object[] args) {

		RpcInfo rpcInfo = RpcInfo.builder().interfaceName(clazz.getName()).methodName(method.getName())
				.parameterTypes(method.getParameterTypes()).args(args).build();

		final NettyRpcClientHandler handler = new NettyRpcClientHandler();

		EventLoopGroup e = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(e);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
					pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
					
					//此处必须定义传递内容的编码和解码，因为是对象。
					pipeline.addLast("encoder", new ObjectEncoder());
					pipeline.addLast("decoder",
							new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
					pipeline.addLast("handler", handler);
				}
			});

			ChannelFuture future = b.connect("localhost", 8080).sync();

			future.channel().writeAndFlush(rpcInfo).sync();
			future.channel().closeFuture().sync();

			return handler.getResponse();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			e.shutdownGracefully();
		}

		return null;
	}
}