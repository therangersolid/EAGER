package com.pk.eager.core.controller;

import com.pk.eager.core.model.User;
import com.pk.eager.firebase.FirebaseCore;

/**
 * Created by therangersolid on 9/30/17.
 */

public class Run {

    // Runt it at least once on UI thread
    public static void initialize() {
        GlobalData.u = new User();
        GlobalData.u.setId("Hh7qGadAgPeRUJYAEGjvRu845DC3");//
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * Start Firebase!
                 */
                FirebaseCore.start();
//                User u = new User();
//                u.setId("6ZxSGeHS4DOoFHEE2McBcGH7XHP2");
//
//                try {
//                    u = DUser.crud(u, true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Log.e("Test", u.getEmail());
//
//                Message m = new Message();
//                m.setMessage("Hello " + System.currentTimeMillis());
//                m.setBlobname(null);
//                m.setBlob(null);
//                m.setTimestamp(System.currentTimeMillis());
//
//                try {
//                    m.setOrder(DAutoIncrement.order(DAutoIncrement.MESSAGE));
//
//                    m = DMessage.crud(m, false, false);
//                    Message m2 = new Message();
//                    m2.setId(m.getId());
//                    m2 = DMessage.crud(m2, true, false);
////            Log.w("Message",m2.getId());
//                    Message m3 = new Message();
//                    m3.setId(m.getId());
////            DMessage.crud(m3,false, true);
////            Log.w("Message2",m3.getId());
//                    DMessageList.crd(u, u, m, false, false);
//
//                    final ArrayList<Message> messages = DMessageList.crd(u, u, null, true, false);
//                    for (Message mes : messages
//                            ) {
//                        DMessage.crud(mes, true, false); // Fill the message with data
//
//                    }
//                    // Sorting require all messages element to be filled, otherwise, it won't work
//                    Collections.sort(messages);
//                    for (Message mes : messages
//                            ) {
//                        Log.e(mes.getId(), mes.getMessage());
//
//                    }
//
//                    RunnableDataSnapshot rds = new RunnableDataSnapshot() {
//                        @Override
//                        public void run(DataSnapshot dataSnapshot, Object object) {
//                            ArrayList<Message> messages2 = (ArrayList<Message>) object;
//                Collections.sort(messages2);
//                            for (int i = messages.size(); i < messages2.size(); i++) {
//                                Log.e("New Input", messages2.get(i).getId());
//                                messages.add(messages2.get(i));
//                            }
//                            Log.e("Data change", "Data is changing");
//                        }
//                    };
//                    DMessageList.nonblockRead(u, u, rds);
//                    User u2 = new User();
//                    u2.setId("Hh7qGadAgPeRUJYAEGjvRu845DC3");
//                    ArrayList<User> users = new ArrayList<User>();
//                    users.add(u2);
//                    HashMap<String, ArrayList<User>> hm = new HashMap<String, ArrayList<User>>();
//                    hm.put("Group 1", users);
//                    DBroadcast.uBroadcastPushorDelete(u, hm, true);
//                    ArrayList<User> users2 = new ArrayList<User>();
//                    users2.add(u);
//                    users2.add(u2); // it add itself which must be avoided!
//                    HashMap<String, ArrayList<User>> hm2 = new HashMap<String, ArrayList<User>>();
//                    hm.put("Group 2", users2);
//                    DBroadcast.uBroadcastPushorDelete(u2, hm2, true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
                t.start();

    }
}
