package com.pk.eager.view.android;

import android.content.Intent;
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

public class Activity_Contact extends AppCompatActivity {
    public static final String MESSAGE = "com.team5.emergencyapp.MESSAGE"; // The key of the message
    public static final String MESSAGE_TYPE = "com.team5.emergencyapp.MESSAGE_TYPE"; // the data will be broadcast or individual or bot
    public static final String BROADCAST = "BROADCAST"; // the data will be broadcast or individual or bots
    public static final String INDIVIDUAL = "INDIVIDUAL"; // the data will be broadcast or individual or bots
    public static final String BOT = "BOT"; // the data will be broadcast or individual or bots
    public static final String ADDGROUP = "ADDGROUP"; // Window to add the group to Activity_AddGroup
    private Activity_Contact activityContact;

    public void setContent(final String messageType, String displayName, final String key, LinearLayout messageList) {
        LinearLayout linearLayout = new LinearLayout(activityContact);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        ImageView imageView = new ImageView(activityContact);
        if (messageType.equals(BROADCAST) || messageType.equals(BOT)) {
            imageView.setBackgroundResource(R.drawable.admin_man);
        } else if(messageType.equals(INDIVIDUAL)){
            imageView.setBackgroundResource(R.drawable.man);
        }
        linearLayout.addView(imageView);
        TextView tempTextView = new TextView(activityContact);
        tempTextView.setText(displayName);
        tempTextView.setPadding(Utility.dipToPX(10), Utility.dipToPX(3), 0, 0);
        tempTextView.setTextColor(getResources().getColor(R.color.black));
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageType.equals(ADDGROUP)){
                    Intent intent = new Intent(activityContact, Activity_AddGroup.class);
                    activityContact.startActivity(intent);
                }else {
                    Intent intent = new Intent(activityContact, Activity_Message.class);
                    intent.putExtra(MESSAGE_TYPE, messageType);
                    intent.putExtra(MESSAGE, key);
                    activityContact.startActivity(intent);
                }
            }
        });
        linearLayout.addView(tempTextView);
        linearLayout.setPadding(0, Utility.dipToPX(3), 0, Utility.dipToPX(10));
        messageList.addView(linearLayout);
        LinearLayout lineLinearLayout = new LinearLayout(activityContact);
        lineLinearLayout.setPadding(0, Utility.dipToPX(1), 0, 0);
        lineLinearLayout.setBackgroundColor(Color.GRAY);
        messageList.addView(lineLinearLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        activityContact = this;
        final LinearLayout messageList = (LinearLayout) findViewById(R.id.groupList);
        setContent(ADDGROUP, "Add or delete a group!", "", messageList);
        setContent(BOT, "Chat with bot!", "", messageList);
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
                            setContent(BROADCAST, key, key, messageList);
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
                                        setContent(INDIVIDUAL, fuser.getEmail(), userID, messageList);
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

    }
}
