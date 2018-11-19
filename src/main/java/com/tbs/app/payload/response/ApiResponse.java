package com.tbs.app.payload.response;

/**
 * Created by hp on 19/08/17.
 */
public class ApiResponse {

    private Boolean success;
    private String errorCode;
    private String message;
    private Object payloads;

    public ApiResponse(Boolean success, String errorCode, String message, Object payloads) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.payloads = payloads;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Object getPayloads() {
        return payloads;
    }

    public void setPayloads(Object payloads) {
        this.payloads = payloads;
    }

    
    
}
