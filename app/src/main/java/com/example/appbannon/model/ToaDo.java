package com.example.appbannon.model;

import androidx.annotation.NonNull;

public class ToaDo {
    private int id;
    private Double kinhDo;
    private Double viDo;

    private String tenViTri;

    public ToaDo() {
    }

    public String getTenViTri() {
        return tenViTri;
    }

    public void setTenViTri(String tenViTri) {
        this.tenViTri = tenViTri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getKinhDo() {
        return kinhDo;
    }

    public void setKinhDo(Double kinhDo) {
        this.kinhDo = kinhDo;
    }

    public Double getViDo() {
        return viDo;
    }

    public void setViDo(Double viDo) {
        this.viDo = viDo;
    }

    @NonNull
    @Override
    public String toString() {
        return "ToaDo{" +
                "id=" + id +
                ", kinhDo='" + kinhDo + '\'' +
                ", viDo='" + viDo + '\'' +
                ", tenViTri='" + tenViTri + '\'' +
                '}';
    }
}
