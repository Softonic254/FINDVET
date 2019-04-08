package com.example.user.locvet.models;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class VetChat implements Comparable<VetChat>{
   private String id;
   private String name;
   private String message;

    @ServerTimestamp
    private Date date;

    public VetChat(){

    }

    public VetChat(String id,String name,String message){
        this.id = id;
        this.name = name;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(@NonNull VetChat chat) {
        return this.date.compareTo(chat.getDate());
    }
}
