package com.treCoops.watchg1.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.treCoops.watchg1.HomeActivity;
import com.treCoops.watchg1.R;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        sendMyNotification(message.getNotification().getTitle(), message.getNotification().getBody());
    }


    private void sendMyNotification(String title, String message) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "channel_id");
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Test");
        bigText.setBigContentTitle(message);

        mBuilder.setSmallIcon(R.drawable.notification);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());
    }

}
