package com.hugui.rpc.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Copyright © 2019 Hugui. All rights reserved.
 * 
 * @Title: RpcImporter.java
 * @Prject: custom-rpc
 * @Package: com.hugui.rpc
 * @Description:
 * @author: HuGui
 * @date: 2019年1月24日 上午9:49:13
 * @version: V1.0
 */

public class RpcImporter<S> {

	@SuppressWarnings("unchecked")
	public S importer(final Class<?> serviceClass, final InetSocketAddress address) {
		return (S) Proxy.newProxyInstance(serviceClass.getClassLoader(),
				new Class<?>[] { serviceClass.getInterfaces()[0] }, new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						ObjectOutputStream output = null;
						ObjectInputStream input = null;
						Socket socket = null;

						try {
							socket = new Socket();
							// 連接socket
							socket.connect(address);
							// 获取socket的输出
							output = new ObjectOutputStream(socket.getOutputStream());

							// 写入需调用的interface名
							output.writeUTF(serviceClass.getName());
							// 写入需调用的方法名
							output.writeUTF(method.getName());
							// 写入需调用的方法的参数类型
							output.writeObject(method.getParameterTypes());
							// 写入需调用的方法的参数
							output.writeObject(args);

							input = new ObjectInputStream(socket.getInputStream());
							return input.readObject();
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							if (socket != null) {
								socket.close();
							}
							if (output != null) {
								output.close();
							}
							if (input != null) {
								input.close();
							}
						}

						return null;
					}
				});
	}

}
