package com.pk.eager.firebase.dao;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.pk.eager.core.model.NotFoundException;
import com.pk.eager.core.model.User;
import com.pk.eager.firebase.FirebaseCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by therangersolid on 10/2/17.
 */

public class DBroadcast {

    /**
     * To read, use the DUser function! This is only to push or delete a broadcast!
     *
     * @param user
     * @param broadcast At least an array of user. Will be cleared after this method is called.
     * @param push If false, we delete the whole broadcast string
     * @return
     * @throws NotFoundException
     */
    public static User uBroadcastPushorDelete(final User user, final HashMap<String,ArrayList<User>> broadcast, boolean push) throws NotFoundException {
        DatabaseReference myRef = FirebaseCore.firebaseDatabase.getReference("Users").child(user.getId()).child("broadcast");
        final CountDownLatch doneSignal = new CountDownLatch(1);
        final NotFoundException notFoundException = new NotFoundException();
        if (push) { // Push
            // In order to maintain unique data, we have to delete all prevoius data from firebase
            DBroadcast.uBroadcastPushorDelete(user, broadcast, false);

            for (String groupName:broadcast.keySet()) {
                DatabaseReference groupRef = myRef.child(groupName).push();
                ArrayList<User> userMember =user.getBroadcast().get(groupName);
                if (userMember == null){
                    userMember = new ArrayList<User>();
                }
                for (User u : broadcast.get(groupName)
                        ) {
                    groupRef.setValue(u.getId());
                    Log.e(u.getId(), "Set with key " + groupRef.getKey());
                    userMember.add(u);
                    groupRef = myRef.child(groupName).push();
                }
            }


            broadcast.clear();

        } else { // Delete
            for (String groupName:broadcast.keySet()) {
                user.getBroadcast().put(groupName,null);
                myRef.child(groupName).removeValue();
            }
        }

        return user;
    }
}
