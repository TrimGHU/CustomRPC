package com.hugui.rpc.netty;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * Copyright © 2019 Hugui. All rights reserved.
 * 
 * @Title: RpcInfo.java
 * @Prject: custom-rpc
 * @Package: com.hugui.rpc.netty
 * @Description:
 * @author: HuGui
 * @date: 2019年1月24日 下午4:57:47
 * @version: V1.0
 */

@Data
@Builder
public class RpcInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String interfaceName;
	private String methodName;
	private Class<?>[] parameterTypes;
	private Object[] args;

}
