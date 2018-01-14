package com.github.dimanolog.flickr.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dimanolog.flickr.R;
import com.github.dimanolog.flickr.api.IResponseStatus;
import com.github.dimanolog.flickr.datamanagers.IManagerCallback;
import com.github.dimanolog.flickr.datamanagers.comment.CommentsManager;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.imageloader.VanGogh;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;


public class CommentsFragment extends Fragment {
    private static final String TAG = CommentsFragment.class.getSimpleName();
    private static final String ARG_PHOTO = "photo";

    private IPhoto mPhoto;
    private CommentsManager mCommentsManager;
    private RecyclerView mCommentsRecyclerView;
    private TextView mNumberOfCommentsTxtVw;
    private EditText mWriteCommentryEditTxt;
    private ImageView mSendCommenataryBtn;
    private boolean mUpdating;

    public static CommentsFragment newInstance(IPhoto pPhoto) {

        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO, pPhoto);
        commentsFragment.setArguments(args);

        return commentsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhoto = (IPhoto) getArguments().getSerializable(ARG_PHOTO);
        setRetainInstance(true);

        mCommentsManager = CommentsManager.getInstance(getActivity());
        mCommentsManager.registerCallback(new IManagerCallback<ICustomCursorWrapper<ICommentary>>() {

            @Override
            public void onStartLoading() {
                loading(true);
            }

            @Override
            public void onSuccessResult(ICustomCursorWrapper<ICommentary> pResult) {
                setupOrUpdateAdapter(pResult);
                loading(false);
            }

            @Override
            public void onError(Throwable t) {
                loading(false);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.comments_fragment, container, false);

        mCommentsRecyclerView = v.findViewById(R.id.comments_fragment_recycler_view);
        mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNumberOfCommentsTxtVw = v.findViewById(R.id.comments_fragment_number_of_commets_text_view);
        mWriteCommentryEditTxt = v.findViewById(R.id.comments_fragment_write_comment_text);
        mSendCommenataryBtn = v.findViewById(R.id.comments_fragment_send_comment);

        mSendCommenataryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendCommentClick();
            }
        });

        updateItems();

        return v;
    }

    void onSendCommentClick() {
        String comment = mWriteCommentryEditTxt.getText().toString();
        if (!TextUtils.isEmpty(comment)) {
            mCommentsManager.addCommentToPhoto(mPhoto, comment, new IManagerCallback<IResponseStatus>() {
                @Override
                public void onStartLoading() {

                }

                @Override
                public void onSuccessResult(IResponseStatus result) {

                }

                @Override
                public void onError(Throwable t) {

                }
            });
        }
    }


    private void updateItems() {
        mCommentsManager.getCommentsForPhoto(mPhoto);
    }

    private void setupOrUpdateAdapter(ICustomCursorWrapper<ICommentary> pResult) {
        if (isAdded()) {
            if (!mUpdating) {
                mCommentsRecyclerView.setAdapter(new CommentsAdapter(pResult));
                mUpdating = true;
            } else {
                CommentsAdapter commentsAdapter = (CommentsAdapter) mCommentsRecyclerView.getAdapter();
                commentsAdapter.swapCursor(pResult);
            }
        }
    }

    private void loading(boolean pB) {

    }

    private class CommentHolder extends RecyclerView.ViewHolder {
        private TextView mCommentTittleTxtVw;
        private TextView mCommentContentTxtVw;
        private ImageView mUserAvatarImgVw;

        CommentHolder(View pItemView) {
            super(pItemView);

            mUserAvatarImgVw = pItemView.findViewById(R.id.comments_fragment_item_user_avatar_image_view);
            mCommentContentTxtVw = pItemView.findViewById(R.id.comments_fragment_item_user_coment_text_view);
            mCommentTittleTxtVw = pItemView.findViewById(R.id.comments_fragment_item_user_name_text_view);
        }

        void bindCommentaryItem(ICommentary pCommentary) {
            mCommentTittleTxtVw.setText(pCommentary.getAuthorName());
            mCommentContentTxtVw.setText(Html.fromHtml(pCommentary.getContent()));

            VanGogh.with(getActivity())
                    .load(pCommentary.getAvatarUrl())
                    .placeHolder(R.drawable.no_photo)
                    .into(mUserAvatarImgVw);
        }
    }

    private class CommentsAdapter extends RecyclerView.Adapter<CommentHolder> {

        private ICustomCursorWrapper<ICommentary> mCommentsCursor;

        CommentsAdapter(ICustomCursorWrapper<ICommentary> pCommentsCursor) {
            mCommentsCursor = pCommentsCursor;
        }

        @Override
        public CommentHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.comments_item, viewGroup,
                    false);
            return new CommentHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentHolder pCommentHolder, int position) {
            mCommentsCursor.moveToPosition(position);
            ICommentary commentItem = mCommentsCursor.get();
            pCommentHolder.bindCommentaryItem(commentItem);
        }

        public void swapCursor(ICustomCursorWrapper<ICommentary> pCommentsCursor) {
            if (pCommentsCursor != null) {
                mCommentsCursor.close();
            }
            mCommentsCursor = pCommentsCursor;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mCommentsCursor.getCount();
        }
    }
}


