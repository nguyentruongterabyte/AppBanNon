package com.example.appbannon.Interface;

public interface IResponse<T> {
    int getError();

    String getError_text();

    String getData_name();

    T getData();
}
