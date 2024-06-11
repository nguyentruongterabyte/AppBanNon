package com.example.appbannon.model.Address;

import com.example.appbannon.Interface.IResponse;

import java.util.List;

public class DistrictResponse extends GenericsBaseResponse<List<District>> implements IResponse<List<District>> {

    public DistrictResponse(int error_code, String error_text, String data_name, List<District> data) {
        super(error_code, error_text, data_name, data);
    }

    @Override
    public List<District> getData() {
        return super.getData();
    }

    @Override
    public int getError() {
        return super.getError();
    }

    @Override
    public String getData_name() {
        return super.getData_name();
    }

    @Override
    public String getError_text() {
        return super.getError_text();
    }
}
