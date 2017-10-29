package com.github.dimanolog.flickr;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.github.dimanolog.flickr.backend.FlickAppBackendClient;
import com.github.dimanolog.flickr.backend.model.Version;

public class MainActivity extends AppCompatActivity {
    private Version mVersion;
    private int currentVersion = BuildConfig.VERSION_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FlickAppBackendClient client = new FlickAppBackendClient();
        if (mVersion == null) {
            client.checkVersion(new FlickAppBackendClient.BackEndCallback() {
                @Override
                public void onResult() {
                    mVersion = client.getVersion();
                    checkUpdate();
                }
            });
        }
        checkUpdate();
    }

    private void checkUpdate() {
        boolean isNeedUpdate = mVersion != null && mVersion.getVersion() != null && mVersion.getVersion() > currentVersion;

        if (isNeedUpdate) {
            updateOnNewVerionAlertDialog(mVersion.gethardUpdate());
        }
    }

    private void updateOnNewVerionAlertDialog(boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        builder.setTitle(R.string.new_version)
                .setMessage(R.string.new_version_msg)
                .setCancelable(cancelable)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.lingualeo.android")));
                    }
                });
        builder.show();
    }
}
