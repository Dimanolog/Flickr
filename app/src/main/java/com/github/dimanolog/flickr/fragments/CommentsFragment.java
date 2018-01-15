package com.github.dimanolog.flickr.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.github.dimanolog.flickr.R;
import com.github.dimanolog.flickr.datamanagers.IManagerCallback;
import com.github.dimanolog.flickr.datamanagers.authorization.AuthorizationManager;
import com.github.dimanolog.flickr.datamanagers.authorization.UserSession;
import com.github.dimanolog.flickr.datamanagers.comment.CommentsManager;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.imageloader.VanGogh;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.model.flickr.interfaces.IResponseStatus;


public class CommentsFragment extends Fragment {
    private static final String TAG = CommentsFragment.class.getSimpleName();
    private static final String ARG_PHOTO = "photo";

    private IPhoto mPhoto;

    private RecyclerView mCommentsRecyclerView;
    private EditText mWriteCommentryEditTxt;
    private View mSendCommenataryBtn;

    private boolean mUpdating;

    private CommentsManager mCommentsManager;
    private AuthorizationManager mAuthorizationManager;

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
        mAuthorizationManager = AuthorizationManager.getInstance(getActivity());
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
        mWriteCommentryEditTxt = v.findViewById(R.id.comments_fragment_write_comment_text);
        mSendCommenataryBtn = v.findViewById(R.id.comments_fragment_send_comment);

        mSendCommenataryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendCommentClick();
            }
        });

        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.hide();

        updateItems();

        return v;
    }

    void onSendCommentClick() {
        String comment = mWriteCommentryEditTxt.getText().toString();

        if (!mAuthorizationManager.checkUserSession()) {
            Toast.makeText(getActivity(), R.string.need_aithorization_message, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(comment)) {
            UserSession userSession = mAuthorizationManager.getUserSession();
            mCommentsManager.addCommentToPhoto(mPhoto, comment, userSession, new IManagerCallback<IResponseStatus>() {
                @Override
                public void onStartLoading() {
                    Toast.makeText(getActivity(), R.string.commentary_send_message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccessResult(IResponseStatus result) {
                    if (result.isSuccess()) {
                        updateItems();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(getActivity(), R.string.add_comment_error_message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mUpdating = false;
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
                    .placeHolder(R.drawable.default_avtar)
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


