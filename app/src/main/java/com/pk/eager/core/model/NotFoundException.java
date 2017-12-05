package com.pk.eager.core.model;

import java.lang.reflect.Field;

/**
 * Created by therangersolid on 9/30/17.
 */

public class NotFoundException extends Exception {

    public void setMessage(String message){
        try {
            Field privateStringField = this.getClass().getSuperclass()
                    .getSuperclass().getDeclaredField("detailMessage");
            privateStringField.setAccessible(true);
            privateStringField.set(this, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fillInStackTrace();
    }
}
