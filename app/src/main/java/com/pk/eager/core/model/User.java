package com.pk.eager.core.model;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by therangersolid on 9/28/17.
 */

public class User {
    private String id = null;
    /**
     * Check Lazy Loading. If this is null, then read the data. However if it's still null,
     * then the user is basically nonexistent in the firebase!
     */
    private String email=null;

    private HashMap<String,ArrayList<User>> broadcast = new HashMap<String,ArrayList<User>>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String,ArrayList<User>> getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(HashMap<String,ArrayList<User>> broadcast) {
        this.broadcast = broadcast;
    }
}
