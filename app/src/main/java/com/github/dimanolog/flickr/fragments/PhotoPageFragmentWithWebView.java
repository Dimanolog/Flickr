package com.github.dimanolog.flickr.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.github.dimanolog.flickr.R;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;

/**
 * Created by Dimanolog on 05.01.2018.
 */

public class PhotoPageFragmentWithWebView extends VisibleFragment {
    public static final String ARG_PHOTO = "photo";
    private static final String IMAGE_HTML_TEMPLATE = "<html><body><div class=\"bg\"></div></body></html>";
    private static final String IMAGE_STYLE_TEMPLATE = "<style>body, html {" +
            "    height: 100%%;" +
            "    border: 0px;" +
            "margin-top: 0px;" +
            "margin-left: 0px;" +
            "}" +
            ".bg {" +
            "    background-image: url(\"%s\");" +
            "    height: 100%%; " +
            "    background-position: center;" +
            "    background-repeat: no-repeat;" +
            "    background-size: contain;" +
            "}</style>";
    private WebView mWebView;
    private IPhoto mPhoto;
    private TextView mPhotoTittle;


    public static PhotoPageFragmentWithWebView newInstance(IPhoto pPhoto) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO, pPhoto);
        PhotoPageFragmentWithWebView fragment = new PhotoPageFragmentWithWebView();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhoto = (IPhoto) getArguments().getSerializable(ARG_PHOTO);
        setRetainInstance(true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_view, container, false);
        mWebView = v.findViewById(R.id.photo_fragment_image_web_view);
        mPhotoTittle = v.findViewById(R.id.photo_fragment_image_name_text_view);
        String image = String.format(IMAGE_STYLE_TEMPLATE, mPhoto.getOriginalUrl());
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        mWebView.loadDataWithBaseURL(null, image + IMAGE_HTML_TEMPLATE, "text/html", "UTF-8", null);

        return v;

    }
}
