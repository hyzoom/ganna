package com.ishraq.janna.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

import com.ishraq.janna.R;
import com.ishraq.janna.service.CommonService;

/**
 * Created by Ahmed on 2/15/2016.
 */

/**
 * Created by Ahmed on 2/15/2016.
 */
public class SplashActivity extends Activity {

    private CommonService commonService;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    Button pass_song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commonService = new CommonService(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        splashPlayer();
        pass_song = (Button) findViewById(R.id.pass_song);
        pass_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpMain();
            }
        });
//        new Handler().postDelayed(new Runnable() {
//
//            /*
//             * Showing splash screen with a timer. This will be useful when you
//             * want to show case your app logo / company
//             */
//
//            @Override
//            public void run() {
//                // This method will be executed once the timer is over
//                // Start your app main activity
//
//                if (commonService.getSettings().getLoggedInUser() == null) {
//                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(i);
//                } else {
//                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
//                    startActivity(i);
//                }
//
//                // close this activity
//                finish();
//            }
//        }, SPLASH_TIME_OUT);
    }

    public void splashPlayer() {
        VideoView videoHolder = (VideoView) findViewById(R.id.ganna_song);
//        setContentView(videoHolder);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.ganna_song);
        videoHolder.setVideoURI(video);
        videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                jumpMain(); //jump to the next Activity
            }
        });
        videoHolder.start();
    }

    private synchronized void jumpMain() {
        if (commonService.getSettings().getLoggedInUser() == null) {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
        }
        finish();
    }
}