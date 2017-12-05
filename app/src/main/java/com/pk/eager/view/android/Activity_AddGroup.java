package com.pk.eager.view.android;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pk.eager.R;
import com.pk.eager.core.controller.GlobalData;
import com.pk.eager.core.model.User;
import com.pk.eager.firebase.dao.DUser;
import com.pk.eager.firebase.dao.DUserList;

import java.util.ArrayList;
import java.util.Set;

public class Activity_AddGroup extends AppCompatActivity {

    class ListType {
        String id;
        boolean selected = false;
        boolean broadcast;
    }

    private Activity_AddGroup activityAddGroup;


    public void setContent(final String messageType, String displayName, final String key, LinearLayout messageList, ArrayList<ListType> listTypes) {
        final LinearLayout linearLayout = new LinearLayout(activityAddGroup);
        final ListType listType = new ListType();
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        ImageView imageView = new ImageView(activityAddGroup);
        if (messageType.equals(Activity_Contact.BROADCAST) || messageType.equals(Activity_Contact.BOT)) {
            imageView.setBackgroundResource(R.drawable.admin_man);
            listType.broadcast = true;
        } else {
            imageView.setBackgroundResource(R.drawable.man);
            listType.broadcast = false;
        }
        linearLayout.addView(imageView);
        TextView tempTextView = new TextView(activityAddGroup);
        tempTextView.setText(displayName);
        listType.id = key;
        tempTextView.setPadding(Utility.dipToPX(10), Utility.dipToPX(3), 0, 0);
        tempTextView.setTextColor(getResources().getColor(R.color.black));
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listType.selected) {
                    linearLayout.setBackgroundColor(Color.GREEN);
                    listType.selected = true;
                } else {
                    linearLayout.setBackgroundColor(Color.TRANSPARENT);
                    listType.selected = false;
                }

            }
        });
        linearLayout.addView(tempTextView);
        linearLayout.setPadding(0, Utility.dipToPX(3), 0, Utility.dipToPX(10));
        messageList.addView(linearLayout);
        listTypes.add(listType);
        LinearLayout lineLinearLayout = new LinearLayout(activityAddGroup);
        lineLinearLayout.setPadding(0, Utility.dipToPX(1), 0, 0);
        lineLinearLayout.setBackgroundColor(Color.GRAY);
        messageList.addView(lineLinearLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgroup);

        activityAddGroup = this;
        final LinearLayout messageList = (LinearLayout) findViewById(R.id.groupList);
        final ArrayList<ListType> listTypes = new ArrayList<ListType>();
        Thread t = new Thread(new Runnable() { // get the user group first!
            @Override
            public void run() {


                try {
                    GlobalData.u = DUser.crud(GlobalData.u, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("Test", GlobalData.u.getEmail());
                final User us = GlobalData.u;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Set<String> keysets = us.getBroadcast().keySet();
                        for (final String key : keysets) {
                            setContent(Activity_Contact.BROADCAST, key, key, messageList, listTypes);
                            Log.e("Test", key);
                        }
                    }
                });
                try {
                    final ArrayList<User> users3 = DUserList.r();// Get full array of users
                    {
                        for (int i = 0; i < users3.size(); i++) {
                            if (!us.getId().equals(users3.get(i).getId())) {// do not show the current id!
                                final String userID = users3.get(i).getId();
                                User user = new User();
                                user.setId(userID);
                                user = DUser.crud(user, true);
                                final User fuser = user;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setContent(Activity_Contact.INDIVIDUAL, fuser.getEmail(), userID, messageList,listTypes);
                                    }
                                });
                                Log.e("Test", users3.get(i).getId());
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("Test", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        t.start();

        // Add and remove group
        TextView addGroup = (TextView) findViewById(R.id.addgroup);
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = listTypes.size();
                for (int i = 0; i < size; i++){
                    // check here
                }
            }
        });
        TextView removeGroup = (TextView) findViewById(R.id.removegroup);

    }
}
