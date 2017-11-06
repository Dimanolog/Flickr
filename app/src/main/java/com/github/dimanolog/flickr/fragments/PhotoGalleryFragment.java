package com.github.dimanolog.flickr.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.dimanolog.flickr.R;
import com.github.dimanolog.flickr.activities.PhotoPageActivity;
import com.github.dimanolog.flickr.model.flickr.IPhoto;
import com.github.dimanolog.flickr.preferences.QueryPreferences;
import com.github.dimanolog.flickr.services.PollService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PhotoGalleryFragment extends VisibleFragment implements LoaderManager.LoaderCallbacks<List<IPhoto>> {

    private static final String TAG = PhotoGalleryFragment.class.getSimpleName();

    private RecyclerView mPhotoRecyclerView;
    private ProgressBar mProgressBar;
    private List<IPhoto> mItems = new ArrayList<>();
    private Integer mCurrentPage = 1;
    private boolean mLoading;
    private boolean mUpdating;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(0, null, this);
        updateItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mProgressBar = v.findViewById(R.id.progressBar);
        mPhotoRecyclerView = v.findViewById(R.id.fragment_photo_gallery_recycler_view);

        mPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(RecyclerView.VERTICAL)) {
                    if (!mLoading) {
                        mLoading = true;
                        updateItems();
                    }
                }
            }
        });
        setupOrUpdateAdapter();
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mUpdating = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_photo_gallery, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener
                (new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        Log.d(TAG, "QueryTextSubmit: " + s);
                        QueryPreferences.setStoredQuery(getActivity(), s);
                        newPage();
                        updateItems();
                        searchView.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        Log.d(TAG, "QueryTextChange: " + s);
                        return false;
                    }
                });
        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if (PollService.isServiceAlarmOn(getActivity())) {
            toggleItem.setTitle(R.string.stop_polling);
        } else {
            toggleItem.setTitle(R.string.start_polling);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                QueryPreferences.setStoredQuery(getActivity(), null);
                newPage();
                updateItems();
                return true;
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm =
                        !PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(), shouldStartAlarm);
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void newPage() {
        mCurrentPage = 1;
        mUpdating = false;
    }

    private void loading() {
        if (isAdded()) {
            mProgressBar.setVisibility(View.VISIBLE);
            if (!mUpdating) {
                mPhotoRecyclerView.setVisibility(View.GONE);
            }
        }
    }

    private void updateItems() {
        loading();
        String query = QueryPreferences.getStoredQuery(getActivity());

    }

    private void setupOrUpdateAdapter() {
        if (isAdded()) {
            if (!mUpdating) {
                mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
                mPhotoRecyclerView.setVisibility(View.VISIBLE);
                mUpdating = true;
            } else {
                mPhotoRecyclerView.getAdapter().notifyDataSetChanged();
            }

            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<List<IPhoto>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<IPhoto>> loader, List<IPhoto> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<IPhoto>> loader) {

    }


    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private IPhoto mPhoto;

        private final TextView mInfoTxtVw;
        private final ImageView mPhotoImageVw;

        PhotoHolder(View itemView) {
            super(itemView);

            mInfoTxtVw = itemView.findViewById(R.id.info_text);
            mPhotoImageVw = itemView.findViewById(R.id.photo_image_view);

            itemView.setOnClickListener(this);
        }

        void bindPhotoItem(IPhoto photoItem) {
            mPhoto = photoItem;
            mInfoTxtVw.setText(mPhoto.getCaption());
            Picasso.with(getActivity())
                    .load(mPhoto.getUrl())
                    .placeholder(R.drawable.no_photo)
                    .error(R.drawable.no_photo)
                    .into(mPhotoImageVw);
        }

        @Override
        public void onClick(View v) {
            Intent i = PhotoPageActivity
                    .newIntent(getActivity(), mPhoto.getPhotoPageUri());
            startActivity(i);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<IPhoto> mPhotoList;

        PhotoAdapter(List<IPhoto> photoList) {
            mPhotoList = photoList;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, viewGroup,
                    false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            IPhoto photoItem = mPhotoList.get(position);
            photoHolder.bindPhotoItem(photoItem);
        }

        @Override
        public int getItemCount() {
            return mPhotoList.size();
        }
    }
}
