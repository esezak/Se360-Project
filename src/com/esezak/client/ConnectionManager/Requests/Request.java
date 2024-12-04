package com.esezak.client.ConnectionManager.Requests;

import java.io.Serializable;

public abstract class Request implements Serializable {
    private RequestType requestType;
    public Request(RequestType requestType) {
        this.requestType = requestType;
    }
    public RequestType getRequestType() {
        return requestType;
    }
}
