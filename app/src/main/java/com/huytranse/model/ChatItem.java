package com.huytranse.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by HuyTranSE on 10/16/2017.
 */
@IgnoreExtraProperties
public class ChatItem {
    public String uID;
    public String chatAuthor;
    public String chatContent;

    public ChatItem() {
    }

    public ChatItem(String uID, String chatAuthor, String chatContent) {
        this.uID = uID;
        this.chatAuthor = chatAuthor;
        this.chatContent = chatContent;
    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result= new HashMap<>();
        result.put("uID",uID);
        result.put("chatAuthor",chatAuthor);
        result.put("chatContent",chatContent);

        return  result;
    }
}
