# CustomRPC
参考RPC的基本原理，基本实现其主干流程。

## RPC基本流程图
![RPC基本流程图](https://github.com/TrimGHU/CustomRPC/blob/master/src/main/resources/RPC%E7%AE%80%E6%98%93%E5%B7%A5%E4%BD%9C%E5%8E%9F%E7%90%86.png)

## RPC的基本模块构成
1. 服务提供者，服务调用者，服务代理者
2. 序列化和反序列化（很重要，其关系到传递内容的大小和速度）
3. 通讯框架（CustomRPC基于socket和netty都实现调用）
