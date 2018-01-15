package com.github.dimanolog.flickr.receviers;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.github.dimanolog.flickr.services.FlickrPollService;
import com.github.dimanolog.flickr.util.LogUtil;


public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = NotificationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context c, Intent i) {
        LogUtil.d(TAG, "received result: " + getResultCode());
        if (getResultCode() != Activity.RESULT_OK) {
            return;
        }
        int requestCode = i.getIntExtra(FlickrPollService.REQUEST_CODE, 0);
        Notification notification = i.getParcelableExtra(FlickrPollService.NOTIFICATION);
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(c);
        notificationManager.notify(requestCode, notification);
    }
}
