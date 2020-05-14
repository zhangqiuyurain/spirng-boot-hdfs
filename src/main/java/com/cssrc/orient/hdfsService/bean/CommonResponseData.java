package com.cssrc.orient.hdfsService.bean;

public class CommonResponseData {

    private boolean success = true;
    private String msg;
    //是否提示消息
    private Boolean alertMsg = true;

    public CommonResponseData(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public CommonResponseData() {
    }

    public Boolean getAlertMsg() {
        return alertMsg;
    }

    public void setAlertMsg(Boolean alertMsg) {
        this.alertMsg = alertMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
