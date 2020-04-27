package uk.ac.tees.w9218308.chatsapp.Chat;

import java.io.Serializable;
import java.util.ArrayList;

import uk.ac.tees.w9218308.chatsapp.User.UserObject;

public class ChatObject implements Serializable {

    private String chatId, chatTitle;
    private ArrayList<UserObject> userObjectArrayList = new ArrayList<>();

    public ChatObject(String chatId) {
        this.chatId = chatId;
    }


    //Getters

    public String getChatId() {
        return chatId;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public ArrayList<UserObject> getUserObjectArrayList() {
        return userObjectArrayList;
    }


    //Setters

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }


    //Method to add user object into arraylist

    public void addUserToArrayList(UserObject mUser) {
        userObjectArrayList.add(mUser);
    }
}
