package com.zjut.SimpleRPC.codec.serialize;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Created by Ryan on 2016/11/24.
 */
@Component
public class JsonSerializer implements Serializer {

    private String charsetName;

    public <T> byte[] serialize(T obj) throws IOException {
        Gson gson = new Gson();
        if (StringUtils.isEmpty(charsetName)) {
            charsetName = "utf-8";
        }
        return gson.toJson(obj).getBytes();
    }

    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(new String(bytes), clazz);
    }

}
