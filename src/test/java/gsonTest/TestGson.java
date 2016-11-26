package gsonTest;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import java.lang.String;

import com.zjut.SimpleRPC.core.RpcRequest;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Created by Ryan on 2016/11/25.
 */
public class TestGson {
    private static RpcRequest rpcRequest;

    static {
        rpcRequest = new RpcRequest();
        rpcRequest.setClassName("test2222");
        rpcRequest.setMethodName("MethodName123123");
        Object[] O = new Object[]{"QWE", "ABC"};
        Class<?>[] parameterTypes = new Class<?>[]{String.class,Object.class};
        rpcRequest.setParameterTypes(parameterTypes);
    }

    @Test
    public void testFastJson() throws Exception {
        String s = JSON.toJSONString(rpcRequest);
        System.out.println(s);

        RpcRequest rpcRequest2= JSON.parseObject(s,RpcRequest.class);
        System.out.println(rpcRequest2);
    }

    @Test
    public void testGson() throws Exception {
        Gson gson = new Gson();
        String s = gson.toJson(rpcRequest);
        System.out.println(s);

    }

    @Test
    public void testJackJson() throws Exception {
//        ObjectMapper mapper = JsonBinder.getInstance();
//        String json = mapper.writeValueAsString(Object);
//        Object object = mapper.readValue(json, Object.class);

    }

    @Test
    public void className() throws Exception {
        Class<?>[] parameterTypes = new Class<?>[2];
        String className = String.class.getName();
        parameterTypes[0] = Class.forName(className);
        System.out.println(parameterTypes);
    }
}
