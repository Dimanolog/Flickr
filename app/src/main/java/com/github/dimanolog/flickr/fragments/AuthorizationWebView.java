package com.github.dimanolog.flickr.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.github.dimanolog.flickr.R;
import com.github.dimanolog.flickr.activities.WebViewActivity;
import com.github.dimanolog.flickr.datamanagers.AutheficationManager;
import com.github.dimanolog.flickr.datamanagers.IManagerCallback;

/**
 * Created by Dimanolog on 07.01.2018.
 */

public class AuthorizationWebView extends VisibleFragment {
    private static final String TAG = AuthorizationWebView.class.getSimpleName();
    private static final String ARG_URI = "photo_page_url";

   // private Uri mUri;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private AutheficationManager mAutheficationManager;


    public static AuthorizationWebView newInstance(Uri pUri) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, pUri);
        AuthorizationWebView fragment = new AuthorizationWebView();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  mUri = getArguments().getParcelable(ARG_URI);
        mAutheficationManager = AutheficationManager.getInstance(getActivity());

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_page, container,
                false);

        mProgressBar =
                v.findViewById(R.id.fragment_photo_page_progress_bar);
        mProgressBar.setMax(100);

        mWebView = v.findViewById
                (R.id.fragment_photo_page_web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }

            public void onReceivedTitle(WebView webView, String title) {
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.getSupportActionBar().setSubtitle(title);
            }
        });


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Log.d("WebView", "your current url when webpage loading.." + url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                String scheme = uri.getScheme();
                if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) {
                    return false;
                }
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
                return true;
            }
        });

        WebViewActivity webViewActivity = (WebViewActivity) getActivity();
        webViewActivity.setOnBackPressedListener(new WebViewFragment.OnBackPressedListener() {
            @Override
            public boolean doBack() {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                    return false;
                }
                return true;
            }
        });

        mAutheficationManager.setAutheficationMangerCallback(new IManagerCallback<Uri>() {
            @Override
            public void onStartLoading() {

            }

            @Override
            public void onSuccessResult(Uri result) {
                mWebView.loadUrl(result.toString());
            }

            @Override
            public void onError(Throwable t) {

            }
        });

        mAutheficationManager.getAuthorizationUri();

        return v;
    }
}
