package RpcExsample;

import com.zjut.object.User;

public class HelloServiceImpl implements HelloService {
	static {
//		System.out.println(" init ...");
	}
	  
    public String hello(String name) {  
    	System.out.println("执行rpc调用。。。");
        return "Hello " + name+"2312312312";
    }

    public User hello2() {
        return new User("Ryan",20161123,19);
    }
}  