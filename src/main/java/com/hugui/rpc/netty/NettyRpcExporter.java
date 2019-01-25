package com.hugui.rpc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Copyright © 2019 Hugui. All rights reserved.
 * 
 * @Title: NettyRpcExporter.java
 * @Prject: custom-rpc
 * @Package: com.hugui.rpc.netty
 * @Description:
 * @author: HuGui
 * @date: 2019年1月24日 下午4:38:04
 * @version: V1.0
 */

public class NettyRpcExporter {

	public void run() throws InterruptedException {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();

		ServerBootstrap sb = new ServerBootstrap();

		try {
			sb.group(boss, worker).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
							pipeline.addLast(new LengthFieldPrepender(4));

							// 此处必须定义传递内容的编码和解码，因为是对象。
							pipeline.addLast("encoder", new ObjectEncoder());
							pipeline.addLast("decoder",
									new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
							pipeline.addLast(new NettyRpcServerHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_KEEPALIVE, true);

			// 绑定端口，开始接收进来的连接
			ChannelFuture f = sb.bind(8080).sync();

			// 等待服务器 socket 关闭 。
			// 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

}
