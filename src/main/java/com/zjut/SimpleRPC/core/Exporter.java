package com.zjut.SimpleRPC.core;

/**
 * Created by Ryan on 2016/11/26.
 */
public interface Exporter {
    void export(Class<?> clazz, Object object, String version);

    Class<?> getExportedServiceBeanClass(String className, String version);

    Object getExportedServiceBeanClass(String classNameAndVersion);

    Object getExportedServiceBean(String className, String version);

    Object getExportedServiceBean(String classNameAndVersion);



}
