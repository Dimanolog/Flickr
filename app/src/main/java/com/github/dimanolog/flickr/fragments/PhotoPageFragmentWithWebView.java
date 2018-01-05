package com.github.dimanolog.flickr.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.github.dimanolog.flickr.R;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;

/**
 * Created by Dimanolog on 05.01.2018.
 */

public class PhotoPageFragmentWithWebView extends VisibleFragment {
    public static final String PHOTO = "photo";
    private WebView mWebView;
    private IPhoto mPhoto;
    private TextView mPhotoTittle;

    public static PhotoPageFragmentWithWebView newInstance(IPhoto pPhoto) {
        Bundle args = new Bundle();
        args.putSerializable(PHOTO, pPhoto);
        PhotoPageFragmentWithWebView fragment = new PhotoPageFragmentWithWebView();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhoto = (IPhoto) getArguments().getSerializable(PHOTO);
        setRetainInstance(true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_view, container, false);

        mWebView =  v.findViewById(R.id.photo_fragment_image_web_view);
/*
        mWebView.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" +
                post.getContent(), "text/html", "UTF-8", null);*/
        mWebView.loadUrl( mPhoto.getOriginalUrl());
        mPhotoTittle = v.findViewById(R.id.photo_fragment_image_name_text_view);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mPhotoTittle.setText(mPhoto.getTittle());

        return v;

    }
}
