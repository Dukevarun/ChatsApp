package uk.ac.tees.w9218308.chatsapp.User;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class UserObject implements Serializable {

    private String uid, name, phone, status, notificationKey;
    Bitmap image;
    private Boolean selected = false;

    public UserObject(String uid) {
        this.uid = uid;
    }

    public UserObject(String uid, String name, String phone , String status) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.status = status;
    }

    /*public UserObject(String uid, String name, String phone, String status, Bitmap image) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.image = image;
    }*/


    //Getters

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public Boolean getSelected() {
        return selected;
    }


    //Setters

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }


}
