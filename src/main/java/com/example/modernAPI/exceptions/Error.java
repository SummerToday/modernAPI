package com.example.modernAPI.exceptions;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class Error {
    private static final long serialVersionUID = 1L;
    private String ErrorCode;
    private String message;
    private Integer status;
    private String url = "Not available";
    private String reqMethod = "Not available";

    public Error setUrl(String url) {
        this.url = url;
        return this;
    }

    public Error setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
        return this;
    }
}