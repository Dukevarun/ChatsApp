package uk.ac.tees.w9218308.chatsapp.Chat;

import java.io.Serializable;
import java.util.ArrayList;

import uk.ac.tees.w9218308.chatsapp.User.UserObject;

public class ChatObject implements Serializable {

    private String chatId;

    private ArrayList<UserObject> userObjectArrayList = new ArrayList<>();

    public ChatObject(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    public ArrayList<UserObject> getUserObjectArrayList() {
        return userObjectArrayList;
    }



    public void addUserToArrayList(UserObject mUser) {
        userObjectArrayList.add(mUser);
    }
}
