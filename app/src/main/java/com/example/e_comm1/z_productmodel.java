package com.example.e_comm1;
import java.io.Serializable;

public class z_productmodel implements Serializable
{
    String name,image,price,category,id1,discription,user;


    public z_productmodel(String name, String image, String price, String category, String id1, String discription,String user) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.category = category;
        this.id1 = id1;
        this.discription = discription;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }
    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }


    public z_productmodel() {
    }
}
