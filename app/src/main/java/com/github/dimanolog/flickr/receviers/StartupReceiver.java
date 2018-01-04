package com.github.dimanolog.flickr.receviers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.dimanolog.flickr.preferences.QueryPreferences;
import com.github.dimanolog.flickr.services.FlickrPollService;


public class StartupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isOn = QueryPreferences.isAlarmOn(context);
        FlickrPollService.setServiceAlarm(context, isOn);
    }
}
