package uk.ac.tees.w9218308.chatsapp.User;

public class UserObject {

    private String uid, name, phone, notificationKey;

    public UserObject(String uid) {
        this.uid = uid;
    }

    public UserObject(String uid, String name, String phone) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public void setName(String name) {
        this.name = name;
    }
}
