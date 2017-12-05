package com.pk.eager.firebase.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pk.eager.core.model.Message;
import com.pk.eager.core.model.NotFoundException;
import com.pk.eager.core.model.RunnableDataSnapshot;
import com.pk.eager.core.model.User;
import com.pk.eager.firebase.FirebaseCore;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by therangersolid on 9/30/17.
 */

/**
 * Contains the CRD (Create Read Delete) of the Message!
 * On delete, the message is not nulled!
 */
public class DMessageList {

    /**
     * To create a new record, simply fill the id of the toUser, fromUser, and message.
     * To delete, simply fill the id of the toUser, fromUser, and message, and set delete to true!
     * To read, especially all Message from a messaging, set read to true and set message to null:
     * crd(toUser, fromUser,null, true, false); // Message sent to you from user, and
     * crd(fromUser, toUser,null, true, false); // Message sent by you to user, and
     * Combine both array list and sort!!
     *
     * @param toUser Fill this out at least with id!
     * @param fromUser Fill this out at least with id!
     * @param message Set to null if read is true!
     * @param read     Set true for read
     * @param delete   Set true for delete
     * @return Only when it's read. When read, Message with only the id known. Return empty ArrayList if no record exist!(Not null)
     * @throws NotFoundException
     */
    public static ArrayList<Message> crd(final User toUser, final User fromUser, final Message message, boolean read, boolean delete) throws NotFoundException {
        DatabaseReference myRef = FirebaseCore.firebaseDatabase.getReference("MessageList").child(toUser.getId()).child(fromUser.getId());
        final CountDownLatch doneSignal = new CountDownLatch(1);
        final NotFoundException notFoundException = new NotFoundException();
        final ArrayList<Message> messages = new ArrayList<Message>();
        if (read && (message == null)) {
            ValueEventListener vel = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Message m = new Message();
                        m.setId(snapshot.getValue().toString());
                        messages.add(m);
                    }
                    doneSignal.countDown();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("DMessageList", "Failed to read value.", error.toException());
                    notFoundException.setMessage("DMessageList failed to read value!");
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
        } else if (delete && (message!=null)) {
            final DatabaseReference fmyRef = myRef;
            ValueEventListener vel = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getValue().toString().equals(message.getId())) {
                            fmyRef.child(snapshot.getKey()).removeValue();
                            break;
                        }
                    }
                    doneSignal.countDown();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("DMessageList", "Failed to delete value.", error.toException());
                    notFoundException.setMessage("DMessageList failed to delete value!");
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

        } else if((message != null)&& !read && !delete){ // Create
            myRef.push().setValue(message.getId());
        } else{
            notFoundException.setMessage("DMessageList Wrong parameters!");
            throw notFoundException;
        }
        return messages;
    }

    public static void nonblockRead(final User toUser, final User fromUser,final RunnableDataSnapshot rds) throws NotFoundException {
        DatabaseReference myRef = FirebaseCore.firebaseDatabase.getReference("MessageList").child(toUser.getId()).child(fromUser.getId());
        final NotFoundException notFoundException = new NotFoundException();
            ValueEventListener vel = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Message> messages = new ArrayList<Message>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Message m = new Message();
                        m.setId(snapshot.getValue().toString());
                        messages.add(m);
                    }
                    rds.run(dataSnapshot,messages);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("DMessageList", "Failed to read value.", error.toException());
                    notFoundException.setMessage("DMessageList failed to read value!");

                }
            };
            myRef.addValueEventListener(vel);

    }
}
