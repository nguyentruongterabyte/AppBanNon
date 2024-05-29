package com.example.appbannon.Interface;

import com.example.appbannon.model.ResponseObject;

public interface ImageUploadCallback {
    void onSuccess(ResponseObject<String> imageFile);

    void onFailure(Throwable t);
}
