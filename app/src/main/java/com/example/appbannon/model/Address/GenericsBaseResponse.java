package com.example.appbannon.model.Address;

public class GenericsBaseResponse<T> {
    private int error;
    private T data;
    private String error_text;

    private String data_name;

    public GenericsBaseResponse(int error_code, String error_text, String data_name, T data) {
        this.error = error_code;
        this.error_text = error_text;
        this.data_name = data_name;
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError_text() {
        return error_text;
    }

    public void setError_text(String error_text) {
        this.error_text = error_text;
    }

    public String getData_name() {
        return data_name;
    }

    public void setData_name(String data_name) {
        this.data_name = data_name;
    }
}
