package com.pk.eager.firebase;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by therangersolid on 9/30/17.
 */

public class FirebaseCore {
    public static FirebaseDatabase firebaseDatabase;

    public static void start() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public static void stop() {
        firebaseDatabase = null;
    }
}
