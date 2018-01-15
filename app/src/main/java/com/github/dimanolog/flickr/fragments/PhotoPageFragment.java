package com.github.dimanolog.flickr.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dimanolog.flickr.R;
import com.github.dimanolog.flickr.activities.CommentsActivity;
import com.github.dimanolog.flickr.imageloader.VanGogh;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.ui.customview.zoomimageview.ImageViewWithZoom;

public class PhotoPageFragment extends VisibleFragment {
    public static final String ARG_PHOTO = "photo";
    private static final int MAX_IMAGE_WIDTH = 2048;
    private static final int MAX_IMAGE_HEIGHT = 2048;

    private IPhoto mPhoto;
    private TextView mPhotoTittle;
    private ImageViewWithZoom mImageViewWithZoom;


    public static PhotoPageFragment newInstance(IPhoto pPhoto) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO, pPhoto);
        PhotoPageFragment fragment = new PhotoPageFragment();
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
        String loadingUrl;

        if (mPhoto.getOriginalUrl() != null) {
            loadingUrl = mPhoto.getOriginalUrl();
        } else {
            loadingUrl = mPhoto.getSmallUrl();
        }

        VanGogh.with(getActivity())
                .load(loadingUrl)
                .placeHolder(R.drawable.no_photo)
                .resize(MAX_IMAGE_HEIGHT, MAX_IMAGE_WIDTH)
                .into(mImageViewWithZoom);


        mPhotoTittle = v.findViewById(R.id.photo_fragment_image_name_text_view);

        mPhotoTittle.setText(mPhoto.getTittle());

        TextView numberOfCommentsTxtVw = v.findViewById(R.id.photo_fragment_number_of_comment_txt_view);
        String countComments = String.valueOf(mPhoto.getCountComments());
        numberOfCommentsTxtVw.setText(countComments);
        TextView numberFavesTextVw = v.findViewById(R.id.photo_fragment_number_of_favor_txt_view);
        String countFaves = String.valueOf(mPhoto.getCountFaves());
        numberFavesTextVw.setText(countFaves);

        ImageView viewComment = v.findViewById(R.id.photo_fragment_imageview_comments);

        viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CommentsActivity.newIntent(getActivity(), mPhoto);
                startActivity(intent);
            }
        });

        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.hide();

        return v;
    }
}
