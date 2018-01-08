package com.github.dimanolog.flickr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.dimanolog.flickr.R;
import com.github.dimanolog.flickr.imageloader.VanGogh;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.ui.customview.ImageViewWithZoom;

/**
 * Created by Dimanolog on 05.01.2018.
 */

public class PhotoPageFragmentWithWebView extends VisibleFragment {
    public static final String ARG_PHOTO = "photo";
    private static final int MAX_IMAGE_WIDTH = 2048;
    private static final int MAX_IMAGE_HEIGHT = 2048;

    private IPhoto mPhoto;
    private TextView mPhotoTittle;
    private ImageViewWithZoom mImageViewWithZoom;


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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_view, container, false);

        mImageViewWithZoom = v.findViewById(R.id.photo_fragment_image_view_with_zoom);

        VanGogh.with(getActivity())
                .load(mPhoto.getOriginalUrl())
                .placeHolder(R.drawable.no_photo)
                .resize(MAX_IMAGE_HEIGHT, MAX_IMAGE_WIDTH)
                .into(mImageViewWithZoom);

        mPhotoTittle = v.findViewById(R.id.photo_fragment_image_name_text_view);
        mPhotoTittle.setText(mPhoto.getTittle());

        return v;

    }
}
