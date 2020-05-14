package com.cssrc.orient.hdfsService.exception;

public class GlobalException extends Exception{
    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
    }


}
