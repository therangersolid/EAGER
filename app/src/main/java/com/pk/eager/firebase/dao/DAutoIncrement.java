package com.pk.eager.firebase.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.pk.eager.core.model.LongOrder;
import com.pk.eager.core.model.NotFoundException;
import com.pk.eager.firebase.FirebaseCore;

import java.util.concurrent.CountDownLatch;

/**
 * Created by therangersolid on 9/30/17.
 */

/**
 * Auto incrementer for the firebase
 */
public class DAutoIncrement {
    public static final String MESSAGE = "messageOrder";

    // Fill nameOrder with DAutoIncrement.MESSAGE
    public static long order(String nameOrder) throws NotFoundException {
        DatabaseReference myRef = FirebaseCore.firebaseDatabase.getReference("AutoIncrement").child(nameOrder);
        final CountDownLatch doneSignal = new CountDownLatch(1);
        final NotFoundException notFoundException = new NotFoundException();
        final LongOrder longOrder = new LongOrder();
        myRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(Long.parseLong(mutableData.getValue().toString()) + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    Log.e("DAutoIncrement", databaseError.getMessage());
                    notFoundException.setMessage("Firebase counter increment failed!");
                } else {
                    longOrder.order = Long.parseLong(dataSnapshot.getValue().toString());
                }
                doneSignal.countDown();
            }
        });
        try {
            doneSignal.await(); // Block until the user data return!
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (notFoundException.getMessage() != null) {
            throw notFoundException;
        }
        return longOrder.order;
    }
}
