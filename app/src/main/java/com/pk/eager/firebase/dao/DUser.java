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
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by therangersolid on 9/30/17.
 */

/**
 * Contains the CRUD (Create Read Update Delete) of the user!
 */
public class DUser {

    /**
     * This function will create user if the user.id is null, otherwise
     * will update the current user status. To read, create a new object
     * with ID set, then let this function do the magic!
     *
     * @param user The user object you want to CRUD
     * @param read Set to true if you only read! Else set to false. The broadcast only have ID
     * @return Updated user.
     */
    public static User crud(final User user, boolean read) throws NotFoundException {
        DatabaseReference myRef = FirebaseCore.firebaseDatabase.getReference("Users");
        final CountDownLatch doneSignal = new CountDownLatch(1);
        final NotFoundException notFoundException = new NotFoundException();
        if (user.getId() == null) { // Create
            DatabaseReference groupRef = myRef.push();
            user.setId(groupRef.getKey());
            groupRef.child("email").setValue(user.getEmail());
            Log.e("Test","New user saved as "+groupRef.getKey()+" and "+user.getEmail());
        } else if (read) { // Read
            myRef = myRef.child(user.getId());
            ValueEventListener vel = new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    String email = (String)hashMap.get("email");
                    if (email == null){
                        notFoundException.setMessage("The user is not found!");
                    }else {
                        user.setEmail(email);
                        dataSnapshot = dataSnapshot.child("broadcast");
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()
                             ) {
                            String groupname = snapshot.getKey();
                            ArrayList<User> userMember =user.getBroadcast().get(groupname);
                            if (userMember == null){
                                userMember = new ArrayList<User>();
                            }
                            for (DataSnapshot member:snapshot.getChildren()
                                 ) {
                                User u = new User();
                                u.setId(member.getValue().toString());
                                Log.e("Test", "User id: "+u.getId());
                                userMember.add(u);
                            }
                            user.getBroadcast().put(groupname,userMember);
                        }
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
        } else{ // Update and Delete

        }

        return user;
    }
}
