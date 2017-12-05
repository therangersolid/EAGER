package com.pk.eager.core.model;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by therangersolid on 10/1/17.
 */

public interface RunnableDataSnapshot {

    public void run(DataSnapshot dataSnapshot, Object object);
}
