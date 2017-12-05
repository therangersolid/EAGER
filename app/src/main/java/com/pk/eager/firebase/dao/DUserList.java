package com.pk.eager.firebase.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pk.eager.core.model.NotFoundException;
import com.pk.eager.core.model.User;
import com.pk.eager.firebase.FirebaseCore;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by therangersolid on 9/30/17.
 */

/**
 * Contains the CRUD (Create Read Update Delete) of the user!
 */
public class DUserList {

    /**
     * This function will cout all user from the database!
     */
    public static ArrayList<User> r() throws NotFoundException {
        DatabaseReference myRef = FirebaseCore.firebaseDatabase.getReference("Users");
        final CountDownLatch doneSignal = new CountDownLatch(1);
        final NotFoundException notFoundException = new NotFoundException();
        final ArrayList<User> users = new ArrayList<User>();
            ValueEventListener vel = new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()
                             ) {
                            String userID = snapshot.getKey();
                            User temp = new User();
                            temp.setId(userID);
                            users.add(temp);

                        }

                    doneSignal.countDown();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.e("DUser", error.getMessage());
                    notFoundException.setMessage("User database error!");
                    doneSignal.countDown();
                }
            };
            myRef.addValueEventListener(vel);
            try{
                doneSignal.await(); // Block until the user data return!
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            myRef.removeEventListener(vel);
            if (notFoundException.getMessage()!=null){
                throw notFoundException;
            }


        return users;
    }
}
