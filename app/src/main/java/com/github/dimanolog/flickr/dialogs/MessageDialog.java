package com.github.dimanolog.flickr.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Dimanolog on 14.01.2018.
 */


public class MessageDialog extends DialogFragment {
    public static final String ARG_TITLE = "message_dialog_title";
    public static final String ARG_MESSAGE = "message_dialog_message";

    public static MessageDialog newInstance(int pMessage, int pTittle) {

        MessageDialog messageDialog = new MessageDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE, pTittle);
        args.putInt(ARG_MESSAGE, pMessage);
        messageDialog.setArguments(args);

        return messageDialog;
    }

    public MessageDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString(ARG_TITLE);
        String message = args.getString(ARG_MESSAGE);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                    }
                })
                .create();
    }
}

