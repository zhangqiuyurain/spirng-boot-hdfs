package com.cssrc.orient.hdfsService.bean;

import java.io.Serializable;

public class CommonAjaxResponse<T> implements Serializable {

    public CommonAjaxResponse() {

    }

    public CommonAjaxResponse(String msg, Boolean success, T result) {
        this.msg = msg;
        this.success = success;
        this.results = result;
    }

    private String msg;

    private Boolean success = false;

    private T results;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
