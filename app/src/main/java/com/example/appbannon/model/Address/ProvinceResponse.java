package com.example.appbannon.model.Address;

import com.example.appbannon.Interface.IResponse;

import java.util.List;

public class ProvinceResponse extends GenericsBaseResponse<List<Province>> implements IResponse<List<Province>> {
    private int error;
    private String error_text;
    private String data_name;
    private List<Province> data;


    public ProvinceResponse(int error_code, String error_text, String data_name, List<Province> data) {
        super(error_code, error_text, data_name, data);
    }

    @Override
    public String getData_name() {
        return data_name;
    }

    @Override
    public List<Province> getData() {
        return data;
    }

    @Override
    public int getError() {
        return error;
    }

    @Override
    public String getError_text() {
        return error_text;
    }
}
