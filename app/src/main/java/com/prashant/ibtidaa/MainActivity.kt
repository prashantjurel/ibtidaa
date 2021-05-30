package com.prashant.ibtidaa

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    /* access modifiers changed from: protected */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(1024, 1024)
        setContentView(R.layout.activity_main)

        //SPLASH SCREEN
        Handler().postDelayed({
            this@MainActivity.startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            finish()
        }, SPLASH_SCREEN.toLong())

        //NOTIFICATION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("IbtidaaNotification", "IbtidaaNotification", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener { task ->
                    var msg = "Successful"
                    if (!task.isSuccessful) {
                        msg = "Failed"
                    }
                    // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
    }

    companion object {
        private const val SPLASH_SCREEN = 500
    }
}