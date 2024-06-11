package com.example.appbannon.model.Address;

import com.example.appbannon.Interface.IResponse;

import java.util.List;

public class WardResponse extends GenericsBaseResponse<List<Ward>> implements IResponse<List<Ward>> {

    public WardResponse(int error_code, String error_text, String data_name, List<Ward> data) {
        super(error_code, error_text, data_name, data);
    }

    @Override
    public List<Ward> getData() {
        return super.getData();
    }

    @Override
    public String getData_name() {
        return super.getData_name();
    }

    @Override
    public int getError() {
        return super.getError();
    }

    @Override
    public String getError_text() {
        return super.getError_text();
    }
}
