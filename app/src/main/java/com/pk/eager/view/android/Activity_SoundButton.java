package com.pk.eager.view.android;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.pk.eager.R;

public class Activity_SoundButton extends AppCompatActivity {
    boolean started = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundbutton);

        final MediaPlayer catSoundMediaPlayer = MediaPlayer.create(this, R.raw.cat_sound);

        final Button playCatMeow = (Button) this.findViewById(R.id.play_cat_meow);

        playCatMeow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!started) {
                    catSoundMediaPlayer.start();
                    started = true;
                }else{
                    catSoundMediaPlayer.pause();
                    started = false;
                }
            }
        });


    }
}