package com.kanyakubjbrahminsamiti.akhil.wordgames;

import java.util.Date;

/**
 * Created by akhil on 19-02-2018.
 */

public class ChatMultiplayChatMessage {

private String messageText;
private String messageUser;
boolean isme;
String lastletter;
int score;
    public boolean isIsme() {
        return isme;
    }

    public void setIsme(boolean isme) {
        this.isme = isme;
    }
//private long messagetime;

    public String getLastletter() {
        return lastletter;
    }

    public void setLastletter(String lastletter) {
        this.lastletter = lastletter;
    }


    public ChatMultiplayChatMessage(boolean isme, String messageText, String messageUser, String lastletter,int score)

    {


        this.messageText=messageText;
        this.messageUser=messageUser;
this.isme=isme;
this.score=score;

this.lastletter=lastletter;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ChatMultiplayChatMessage()
    {

    }
}
