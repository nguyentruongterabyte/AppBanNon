package com.example.appbannon.model;

public class HoaDonModel {
    private int status;
    private String message;
    private DonHang result;

    public HoaDonModel(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public HoaDonModel() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DonHang getResult() {
        return result;
    }

    public void setResult(DonHang result) {
        this.result = result;
    }
}
