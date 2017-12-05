package com.pk.eager.firebase.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pk.eager.core.model.Message;
import com.pk.eager.core.model.NotFoundException;
import com.pk.eager.firebase.FirebaseCore;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by therangersolid on 9/30/17.
 */

/**
 * Contains the CRUD (Create Read Update Delete) of the Message!
 * On delete, the message is not nulled!
 */
public class DMessage {

    public static Message crud(final Message message, boolean read, boolean delete) throws NotFoundException {
        DatabaseReference myRef = FirebaseCore.firebaseDatabase.getReference("Message");
        final CountDownLatch doneSignal = new CountDownLatch(1);
        final NotFoundException notFoundException = new NotFoundException();
        if (read) { // Read
            myRef = myRef.child(message.getId());
            ValueEventListener vel = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    String tempMessage = (String) hashMap.get("message");
                    if (tempMessage == null) {
                        notFoundException.setMessage("There is no such message!");
                    } else {
                        message.setMessage(tempMessage);
                        message.setBlobname((String) hashMap.get("blobname"));
                        message.setBlob((byte[]) hashMap.get("blob"));
                        message.setTimestamp(Long.valueOf(hashMap.get("timestamp").toString()));
                        message.setOrder(Long.valueOf(hashMap.get(DAutoIncrement.MESSAGE).toString()));
                    }
                    doneSignal.countDown();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("DMessage", "Failed to read value.", error.toException());
                    notFoundException.setMessage("DMessage failed to read value!");
                    doneSignal.countDown();
                }
            };
            myRef.addValueEventListener(vel);
            try {
                doneSignal.await(); // Block until the user data return!
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myRef.removeEventListener(vel);
            if (notFoundException.getMessage() != null) {
                throw notFoundException;
            }
        } else {
            if (message.getId() != null) {
                myRef = myRef.child(message.getId()); // Update
                if (delete) { // Delete
                    myRef.removeValue();
                    return message;
                }
            } else { // Create
                myRef = myRef.push();
            }
            HashMap<String, Object> dataInsert = new HashMap<String, Object>();
            dataInsert.put("message", message.getMessage());
            dataInsert.put("blobname", message.getBlobname());
            dataInsert.put("blob", message.getBlob());
            dataInsert.put("timestamp", message.getTimestamp());
            dataInsert.put(DAutoIncrement.MESSAGE, message.getOrder());
            myRef.setValue(dataInsert);
            message.setId(myRef.getKey());
        }
        return message;
    }
}
