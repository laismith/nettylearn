package RpcExsample;

import com.zjut.SimpleRPC.core.*;
import org.junit.Test;

/**
 * Created by Ryan on 2016/11/25.
 */
public class Client {
    @Test
    public void test() throws Exception {
        RpcBoot rpcBoot = new RpcBoot();
        RpcClient rpcClient= rpcBoot.getRpcClient();
        rpcClient.start();
        RpcProxy proxy = new RpcClientProxyByCglib(rpcClient,rpcClient.getRpcResponses());
        HelloService helloService = proxy.create(HelloService.class);

        System.out.println(helloService.hello("Ryan"));
        System.out.println(helloService.hello2());

//        Thread.sleep(100);
        rpcClient.shutdown();
    }

}
