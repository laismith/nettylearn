package com.zjut.SimpleRPC.core;

import java.io.Serializable;

public class RpcResponse implements Serializable {
    private static final long serialVersionUID = -8748551394431909890L;
    private String requestId;
    private Throwable error;
    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RpcResponse{");
        sb.append("requestId=").append(requestId);
        sb.append(", error=").append(error);
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }
}
