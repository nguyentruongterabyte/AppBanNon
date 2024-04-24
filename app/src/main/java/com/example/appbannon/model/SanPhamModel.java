package com.example.appbannon.model;

import java.util.List;

public class SanPhamModel {
    private boolean success;
    private String message;
    private List<SanPham> result;
    private List<SanPham2> content;

    public List<SanPham2> getContent() {
        return content;
    }

    public void setContent(List<SanPham2> content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SanPham> getResult() {
        return result;
    }

    public void setResult(List<SanPham> result) {
        this.result = result;
    }
}
