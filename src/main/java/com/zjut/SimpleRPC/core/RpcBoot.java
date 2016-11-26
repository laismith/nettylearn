package com.zjut.SimpleRPC.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Ryan on 2016/11/25.
 */
public class RpcBoot {

    static {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:serverRpc-config.xml");
    }

    public RpcClient getRpcClient(){
        return new RpcClient();
    }
    public RpcServer getRpcServer(){
        return new RpcServer();
    }
//    public void start() throws Exception {
//        rpcClient.start();
//    }

}
