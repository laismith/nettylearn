package com.zjut.SimpleRPC.core;

import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Ryan on 2016/11/26.
 */
public interface RpcFuture<V> extends Future<V>{

    boolean await(long timeout, TimeUnit unit) throws InterruptedException;

    Exception getException();

    RpcResponse getResponse() throws IOException, TimeoutException;

    void setException(Exception exception);

    void setResponse(RpcResponse response);

    Callback getCallback();

    @Override
    boolean cancel(boolean mayInterruptIfRunning);

    @Override
    boolean isCancelled();

    @Override
    boolean isDone();

    @Override
    V get() throws InterruptedException, ExecutionException;

    @Override
    V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
}
