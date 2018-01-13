package com.github.dimanolog.flickr.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dimanolog.flickr.R;
import com.github.dimanolog.flickr.datamanagers.CommentsManager;
import com.github.dimanolog.flickr.datamanagers.IManagerCallback;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.imageloader.VanGogh;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;


public class CommentsFragment extends Fragment {
    private static final String TAG = CommentsFragment.class.getSimpleName();

    private CommentsManager mCommentsManager;

    public static CommentsFragment newInstance(long photoId) {
        return new CommentsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mCommentsManager = CommentsManager.getInstance(getActivity());
        mCommentsManager.registerCallback(new IManagerCallback<ICommentary>() {
            @Override
            public void onStartLoading() {

            }

            @Override
            public void onSuccessResult(ICommentary result) {

            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.comments_fragment
                , container, false);
        RecyclerView commentsRecyclerView = v.findViewById(R.id.comments_fragment_recycler_view);
        TextView numberOfComments = v.findViewById(R.id.comments_fragment_number_of_commets_text_view);
        EditText writeCommentry = v.findViewById(R.id.comments_fragment_write_comment_text);
        ImageView sendCommenatary = v.findViewById(R.id.comments_fragment_send_comment);

        return v;
    }




    private  class CommentHolder extends RecyclerView.ViewHolder {
        private TextView mCommentTittleTxtVw;
        private TextView mCommentContentTxtVw;
        private ImageView mUserAvatatImgVw;

        CommentHolder(View pItemView) {
            super(pItemView);
            mUserAvatatImgVw = pItemView.findViewById(R.id.comments_fragment_item_user_avatar_image_view);
            mCommentContentTxtVw=pItemView.findViewById(R.id.comments_fragment_item_user_coment_text_view);
            mCommentTittleTxtVw=pItemView.findViewById(R.id.comments_fragment_item_user_name_text_view);
        }

        void bindCommentaryItem(ICommentary pCommentary) {
            mCommentTittleTxtVw.setText(pCommentary.getAuthorName());
            mCommentContentTxtVw.setText(pCommentary.getContent());
            VanGogh.with(getActivity())
                    .load(pCommentary.getAvatarUrl())
                    .placeHolder(R.drawable.no_photo)
                    .into(mUserAvatatImgVw);
        }

    }
    private  class CommentsAdapter extends RecyclerView.Adapter<CommentHolder> {

        private ICustomCursorWrapper<ICommentary> mCommentsCursor;

        CommentsAdapter(ICustomCursorWrapper<ICommentary> pCommentsCursor) {
            mCommentsCursor = pCommentsCursor;
        }

        @Override
        public CommentHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, viewGroup,
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
            if ( pCommentsCursor != null) {
                mCommentsCursor.close();
            }
            mCommentsCursor =  pCommentsCursor;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mCommentsCursor.getCount();
        }
    }
}


