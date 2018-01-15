package com.github.dimanolog.flickr.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationManagerCompat;

import com.github.dimanolog.flickr.R;
import com.github.dimanolog.flickr.activities.PhotoGalleryActivity;
import com.github.dimanolog.flickr.api.FlickrApiPhotoClient;
import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.preferences.QueryPreferences;
import com.github.dimanolog.flickr.util.LogUtil;

import java.util.List;

public class FlickrPollService extends IntentService {
    private static final String TAG = FlickrPollService.class.getSimpleName();

    private static final long POLL_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
    public static final String ACTION_SHOW_NOTIFICATION =
            "com.github.dimanolog.flickr.SHOW_NOTIFICATION";
    public static final String PERM_PRIVATE =
            "com.github.dimanolog.flickr.PRIVATE";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";

    public static Intent newIntent(Context context) {
        return new Intent(context, FlickrPollService.class);
    }

    public static void setServiceAlarm(Context pContext, boolean pIsOn) {
        Intent i = FlickrPollService.newIntent(pContext);
        PendingIntent pi = PendingIntent.getService(
                pContext, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)
                pContext.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        if (pIsOn) {

            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context pContext) {
        Intent i = FlickrPollService.newIntent(pContext);
        PendingIntent pi = PendingIntent
                .getService(pContext, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    public FlickrPollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent pIntent) {
        checkFlickrUpdates();
    }

    private void checkFlickrUpdates() {
        if (!isNetworkAvailableAndConnected()) {
            return;
        }
        String query = QueryPreferences.getStoredQuery(this);
        Long lastResultId = QueryPreferences.getLastResultId(this);
        IResponse<List<IPhoto>> items;
        FlickrApiPhotoClient apiClient = new FlickrApiPhotoClient();
        if (query == null) {
            items = apiClient.getRecent(1);
        } else {
            items = apiClient.searchPhotos(1, query);
        }
        if (items.getResult().size() == 0 || items.isError()) {
            return;
        }
        Long resultId = items.getResult().get(0).getId();
        if (resultId.equals(lastResultId)) {
            LogUtil.i(TAG, "Got an old result: " + resultId);
        } else {
            LogUtil.i(TAG, "Got a new result: " + resultId);
            Resources resources = getResources();
            Intent i = PhotoGalleryActivity.newIntent(this);
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
            Notification notification = new
                    android.support.v4.app.NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID)
                    .setTicker(resources.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.new_pictures_title))
                    .setContentText(resources.getString(R.string.new_pictures_text))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            QueryPreferences.setLastResultId(this, resultId);

            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(this);
            notificationManager.notify(0, notification);
        }
    }


    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = false;
        if (cm != null) {
            isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        }

        return isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
    }
}
