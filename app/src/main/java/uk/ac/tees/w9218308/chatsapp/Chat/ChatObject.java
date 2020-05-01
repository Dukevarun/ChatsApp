package uk.ac.tees.w9218308.chatsapp.Chat;

import java.io.Serializable;
import java.util.ArrayList;

import uk.ac.tees.w9218308.chatsapp.User.UserObject;

public class ChatObject implements Serializable {

    private String chatId, chatName, chatMessage, chatImage;
    private ArrayList<UserObject> userObjectArrayList = new ArrayList<>();

    public ChatObject(String chatId) {
        this.chatId = chatId;
    }

    //Getters

    public String getChatId() {
        return chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public String getChatImage() {
        return chatImage;
    }

    public ArrayList<UserObject> getUserObjectArrayList() {
        return userObjectArrayList;
    }


    //Setters

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void setChatImage(String chatImage) {
        this.chatImage = chatImage;
    }

    public void setUserObjectArrayList(ArrayList<UserObject> userObjectArrayList) {
        this.userObjectArrayList = userObjectArrayList;
    }


    //Method to add user object into arraylist

    public void addUserToArrayList(UserObject mUser) {
        userObjectArrayList.add(mUser);
    }
}
