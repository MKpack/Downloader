package com.example.downloaddemo.utils;

public class Response<T> {
    public int code;
    public T data;

    public Response(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
