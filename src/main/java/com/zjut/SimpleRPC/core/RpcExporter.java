package com.zjut.SimpleRPC.core;

import io.netty.util.internal.StringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ryan on 2016/11/26.
 */
public class RpcExporter implements Exporter {

    private static Map<String, Object> serviceEngine = new ConcurrentHashMap<String, Object>();
    private static final String DEFAULT_VERSION = "0.0";
    @Override
    public void export(Class<?> clazz, Object object, String version) {
        if(StringUtil.isNullOrEmpty(version)){
            version = DEFAULT_VERSION;
        }
        String classNameAndVersion = clazz.getName() + "_" + version;

        Object o = serviceEngine.get(classNameAndVersion);
        if (o != null) {
            return;
        }
        serviceEngine.put(classNameAndVersion,object);
    }

    @Override
    public Class<?> getExportedServiceBeanClass(String className, String version) {
        if(StringUtil.isNullOrEmpty(version)){
            version = DEFAULT_VERSION;
        }
        String classNameAndVersion = className + "_" + version;
        return getExportedServiceBeanClass(classNameAndVersion);
    }

    @Override
    public Class<?> getExportedServiceBeanClass(String classNameAndVersion) {
        Object o = serviceEngine.get(classNameAndVersion);
        if (o != null) {
            return o.getClass();
        }
        throw new RuntimeException(String.format("can not find service bean by key: %s", classNameAndVersion));
    }

    @Override
    public Object getExportedServiceBean(String className, String version) {
        if(StringUtil.isNullOrEmpty(version)){
            version = DEFAULT_VERSION;
        }
        String classNameAndVersion = className + "_" + version;
        return getExportedServiceBean(classNameAndVersion);
    }

    @Override
    public Object getExportedServiceBean(String classNameAndVersion) {
        Object o = serviceEngine.get(classNameAndVersion);
        if (o != null) {
            return o;
        }
        throw new RuntimeException(String.format("can not find service bean by key: %s", classNameAndVersion));
    }
}
