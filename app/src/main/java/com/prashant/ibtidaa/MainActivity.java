package com.prashant.ibtidaa;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 500;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.activity_main);

        //SPLASH SCREEN
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MainActivity.this.startActivity(new Intent(MainActivity.this, HomeActivity.class));
                MainActivity.this.finish();
            }
        }, (long) SPLASH_SCREEN);

        //NOTIFICATION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("IbtidaaNotification", "IbtidaaNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


    }

}
