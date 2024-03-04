package com.example.e_comm1;

import java.io.Serializable;

public class z_profilimodel implements Serializable
{
    String name,imageUri,address,phone;
    public z_profilimodel() {}

    public z_profilimodel(String name, String imageUri, String address, String phone) {
        this.name = name;
        this.imageUri = imageUri;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
