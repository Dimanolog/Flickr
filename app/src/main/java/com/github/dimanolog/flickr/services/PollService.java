package com.github.dimanolog.flickr.services;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.github.dimanolog.flickr.R;
import com.github.dimanolog.flickr.activities.PhotoGalleryActivity;
import com.github.dimanolog.flickr.api.FlickrApiClient;
import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.preferences.QueryPreferences;

import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PollService extends JobService {
    private static final String TAG = "PollService";
    private static final int JOB_ID = 1;

    public static final String ACTION_SHOW_NOTIFICATION =
            "com.github.dimanolog.flickr.SHOW_NOTIFICATION";
    public static final String PERM_PRIVATE =
            "com.github.dimanolog.flickr.PRIVATE";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";

    private PollTask mCurrentTask;

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public static boolean isServiceAlarmOn(Context context) {
        JobScheduler scheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        boolean hasBeenScheduled = false;
        for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == JOB_ID) {
                hasBeenScheduled = true;
            }
        }
        return hasBeenScheduled;
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        JobScheduler scheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (isOn) {
            JobInfo jobInfo = new JobInfo.Builder(
                    JOB_ID, new ComponentName(context, PollService.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setPeriodic(1000 * 60 * 15)
                    .setPersisted(true)
                    .build();
            scheduler.schedule(jobInfo);
        } else {
            scheduler.cancel(JOB_ID);
        }
        QueryPreferences.setAlarmOn(context, isOn);

    }

    @Override
    public boolean onStartJob(JobParameters params) {
        mCurrentTask = new PollTask();
        mCurrentTask.execute(params);
        return true;
    }


    protected void checkFlickrUpdates() {
        if (!isNetworkAvailableAndConnected()) {
            return;
        }
        String query = QueryPreferences.getStoredQuery(this);
        Long lastResultId = QueryPreferences.getLastResultId(this);
        IResponse<List<IPhoto>> items;
        FlickrApiClient apiClient = new FlickrApiClient();
        if (query == null) {
            items = apiClient.getRecent(1);
        } else {
            items = apiClient.searchPhotos(1, query);
        }
        if (items.getResult().size() == 0) {
            return;
        }
        Long resultId = items.getResult().get(0).getId();
        if (resultId.equals(lastResultId)) {
            Log.i(TAG, "Got an old result: " + resultId);
        } else {
            Log.i(TAG, "Got a new result: " + resultId);
            Resources resources = getResources();
            Intent i = PhotoGalleryActivity.newIntent(this);
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(resources.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.new_pictures_title))
                    .setContentText(resources.getString(R.string.new_pictures_text))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            showBackgroundNotification(0, notification);
            sendBroadcast(new Intent(ACTION_SHOW_NOTIFICATION));
        }

        QueryPreferences.setLastResultId(this, resultId);
    }

    private void showBackgroundNotification(int requestCode,
                                            Notification notification) {
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra(REQUEST_CODE, requestCode);
        i.putExtra(NOTIFICATION, notification);
        sendOrderedBroadcast(i, PERM_PRIVATE, null, null,
                Activity.RESULT_OK, null, null);
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private class PollTask extends AsyncTask<JobParameters, Void, Void> {
        @Override
        protected Void doInBackground(JobParameters... params) {
            JobParameters jobParams = params[0];
            checkFlickrUpdates();
            jobFinished(jobParams, false);

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
}
