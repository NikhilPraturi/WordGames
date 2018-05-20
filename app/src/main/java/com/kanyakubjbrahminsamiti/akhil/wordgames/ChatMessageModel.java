package com.kanyakubjbrahminsamiti.akhil.wordgames;

/**
 * Created by akhil on 23-01-2018.
 */

public class ChatMessageModel {
    private long id;
    private boolean isMe;
    private String message;
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastletter() {
        return lastletter;
    }

    public void setLastletter(String lastletter) {
        this.lastletter = lastletter;
    }

    String lastletter;
    private Long userId;
    private String dateTime;
    public ChatMessageModel(long id,boolean isMe,String message,String dateTime){
this.id=id;
this.isMe=isMe;
this.message=message;
this.userId=userId;
this.dateTime=dateTime;


}


    public ChatMessageModel(boolean isMe,String message,String email,String lastletter){

        this.isMe=isMe;
        this.message=message;

        this.dateTime=dateTime;


    }
    public ChatMessageModel(long id,boolean isMe,String message){
        this.id=id;
        this.isMe=isMe;
        this.message=message;


    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public boolean getIsme() {
        return isMe;
    }
    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDate() {
        return dateTime;
    }

    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }

}
