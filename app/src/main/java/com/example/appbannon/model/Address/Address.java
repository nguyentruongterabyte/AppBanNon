package com.example.appbannon.model.Address;

public class Address {
    private String number;
    private String street;
    private String ward;
    private String district;
    private String city;
    private String province;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    private Address(Builder builder) {
        this.number = builder.number;
        this.street = builder.street;
        this.ward = builder.ward;
        this.district = builder.district;
        this.city = builder.city;
        this.province = builder.province;
    }

    public static class Builder {
        private String number;
        private String street;
        private String ward;
        private String district;
        private String city;
        private String province;


        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder ward(String ward) {
            this.ward = ward;
            return this;
        }

        public Builder district(String district) {
            this.district = district;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder province(String province) {
            this.province = province;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }


}
