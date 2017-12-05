package com.pk.eager.core.model;


import android.support.annotation.NonNull;

/**
 * Created by therangersolid on 9/28/17.
 */

public class Message implements Comparable {
    private String id = null;
    /**
     * Check Lazy Loading. If this is null, then read the data. However if it's still null,
     * then the Message is basically nonexistent in the firebase!
     */
    private String message = null;
    private String blobname;
    private byte[] blob;
    private long timestamp;
    private long order = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBlobname(String blobname) {
        this.blobname = blobname;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getBlobname() {
        return blobname;
    }

    public byte[] getBlob() {
        return blob;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getOrder() {
        return order;
    }

    /**
     * Set this order by DAutoincrement!
     * @param order
     */
    public void setOrder(long order) {
        this.order = order;
    }

    @Override
    public int compareTo(@NonNull Object message) {
        return (int)(this.getOrder() - ((Message)message).getOrder());
    }
}
