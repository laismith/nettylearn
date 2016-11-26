package RpcExsample;

import com.zjut.SimpleRPC.core.RpcBoot;
import com.zjut.SimpleRPC.core.RpcExporter;
import org.junit.Test;

/**
 * Created by Ryan on 2016/11/25.
 */
public class Server {

    @Test
    public void test() throws Exception {
        RpcBoot rpcBoot = new RpcBoot();

        rpcBoot.getRpcServer()
                .init(new RpcExporter(),"127.0.0.1",8007)
                .export(HelloService.class,new HelloServiceImpl())
                .start();
    }
}
