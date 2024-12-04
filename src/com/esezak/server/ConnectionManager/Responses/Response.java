package com.esezak.server.ConnectionManager.Responses;

import java.io.Serializable;

public abstract class Response implements Serializable {
    private boolean status;
    public Response(boolean status) {
        this.status = status;
    }
    public boolean getStatus() {
        return status;
    }
}
