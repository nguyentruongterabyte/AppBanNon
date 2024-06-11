package com.example.appbannon.model.Address;

public class Province {
    private String id;
    private String name;
    private String name_en;
    private String full_name;
    private String full_name_en;
    private float latitude;
    private float longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getFull_name_en() {
        return full_name_en;
    }

    public void setFull_name_en(String full_name_en) {
        this.full_name_en = full_name_en;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Province{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", name_en='" + name_en + '\'' +
                ", full_name='" + full_name + '\'' +
                ", full_name_en='" + full_name_en + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    //    id": "89",
//            "name": "An Giang",
//            "name_en": "An Giang",
//            "full_name": "Tỉnh An Giang",
//            "full_name_en": "An Giang Province",
//            "latitude": "10.5392057",
//            "longitude": "105.2312822"
}
