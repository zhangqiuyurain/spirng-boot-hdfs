package com.cssrc.orient.hdfsService.bean;

import java.io.Serializable;

public class AjaxResponseData <T> extends CommonResponseData implements Serializable {

    private T results;

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public AjaxResponseData() {

    }

    public AjaxResponseData(T results) {
        this.results = results;
    }

    public AjaxResponseData(boolean success, String msg) {
        super(success, msg);
    }
}
