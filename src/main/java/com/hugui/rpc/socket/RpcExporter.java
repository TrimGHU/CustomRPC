package com.hugui.rpc.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Copyright © 2019 Hugui. All rights reserved.
 * 
 * @Title: RpcExportor.java
 * @Prject: custom-rpc
 * @Package: com.hugui.rpc
 * @Description:
 * @author: HuGui
 * @date: 2019年1月24日 上午9:48:56
 * @version: V1.0
 */

public class RpcExporter {

	static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public static void exporter(String hostname, int port) throws IOException {

		ServerSocket ss = new ServerSocket();
		ss.bind(new InetSocketAddress(hostname, port));

		try {
			while (true) {
				executor.execute(new ExportTask(ss.accept()));
			}
		} finally {
			ss.close();
		}
	}

	private static class ExportTask implements Runnable {

		Socket client = null;

		public ExportTask(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {

			ObjectInputStream input = null;
			ObjectOutputStream output = null;

			try {

				input = new ObjectInputStream(client.getInputStream());
				// 读取interface名
				String interfaceName = input.readUTF();
				
				Class<?> clazz = Class.forName(interfaceName);
				// 读取方法名
				String methodName = input.readUTF();
				// 读取参数类型
				Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
				// 读取参数
				Object[] args = (Object[]) input.readObject();
				// class中到method
				Method method = clazz.getMethod(methodName, parameterTypes);

				// 反射调用方法
				Object result = method.invoke(clazz.newInstance(), args);

				output = new ObjectOutputStream(client.getOutputStream());

				// 写入output
				output.writeObject(result);
			} catch (IOException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
