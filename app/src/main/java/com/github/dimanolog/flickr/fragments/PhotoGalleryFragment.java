package com.github.dimanolog.flickr.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.dimanolog.flickr.R;
import com.github.dimanolog.flickr.activities.PhotoPageActivity;
import com.github.dimanolog.flickr.dataloader.IDataProviderCallback;
import com.github.dimanolog.flickr.dataloader.PhotoDataProvider;
import com.github.dimanolog.flickr.db.dao.ICustomCursorWrapper;
import com.github.dimanolog.flickr.imageloader.VanGogh;
import com.github.dimanolog.flickr.model.flickr.IPhoto;
import com.github.dimanolog.flickr.preferences.QueryPreferences;
import com.github.dimanolog.flickr.services.PollService;

import java.util.ArrayList;
import java.util.List;


public class PhotoGalleryFragment extends VisibleFragment implements IDataProviderCallback<ICustomCursorWrapper<IPhoto>> {

    private static final String TAG = PhotoGalleryFragment.class.getSimpleName();
    private static final int WIDTH_COLUMNS = 360;

    private RecyclerView mPhotoRecyclerView;
    private ProgressBar mProgressBar;
    private List<IPhoto> mItems = new ArrayList<>();
    private Integer mCurrentPage = 1;
    private boolean mLoading;
    private boolean mUpdating;
    private PhotoDataProvider mPhotoDataProvider;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mPhotoDataProvider = PhotoDataProvider.getInstance(getActivity());
        mPhotoDataProvider.registerCallback(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mProgressBar = v.findViewById(R.id.progressBar);
        mPhotoRecyclerView = v.findViewById(R.id.fragment_photo_gallery_recycler_view);

        mPhotoRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPhotoRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int pxl = mPhotoRecyclerView.getWidth();
                int nmbOfClmns = pxl / WIDTH_COLUMNS;
                mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), nmbOfClmns));
            }
        });
        mPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(RecyclerView.VERTICAL)) {
                    if (!mLoading) {
                        updateItems();
                    }
                }
            }
        });
        updateItems();
        return v;
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

    private void updateItems() {
        loading(true);
        String query = QueryPreferences.getStoredQuery(getActivity());
        if (TextUtils.isEmpty(query)) {
            mPhotoDataProvider.getRecent(mCurrentPage, mUpdating);
        } else {
            mPhotoDataProvider.searchPhotos(mCurrentPage, query, mUpdating);
        }
    }

    private void setupOrUpdateAdapter(ICustomCursorWrapper<IPhoto> pResult) {
        if (isAdded()) {
            if (!mUpdating) {
                mPhotoRecyclerView.setAdapter(new PhotoAdapter(pResult));
                mUpdating = true;
            } else {
                PhotoAdapter photoAdapter = (PhotoAdapter) mPhotoRecyclerView.getAdapter();
                photoAdapter.swapCursor(pResult);
            }
        }
    }

    void loading(boolean state) {
        mLoading = state;
        mProgressBar.setVisibility(state ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onStartLoading() {
        loading(true);
    }

    @Override
    public void onSuccessResult(ICustomCursorWrapper<IPhoto> result) {
        loading(false);
        setupOrUpdateAdapter(result);
        mCurrentPage++;
    }

    @Override
    public void onError(Throwable t) {

    }

    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private IPhoto mPhoto;
        private ImageView mPhotoImageVw;

        PhotoHolder(View itemView) {
            super(itemView);
            mPhotoImageVw = itemView.findViewById(R.id.fragment_photo_gallery_image_view);
            mPhotoImageVw.setOnClickListener(this);
        }

        void bindPhotoItem(IPhoto photoItem) {
            mPhoto = photoItem;

            VanGogh.with(getActivity())
                    .load(mPhoto.getUrl())
                    .placeHolder(R.drawable.no_photo)
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

        private ICustomCursorWrapper<IPhoto> mPhotoCursor;

        PhotoAdapter(ICustomCursorWrapper<IPhoto> pPhotoCursor) {
            mPhotoCursor = pPhotoCursor;
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
            mPhotoCursor.moveToPosition(position);
            IPhoto photoItem = mPhotoCursor.get();
            photoHolder.bindPhotoItem(photoItem);
        }

        public void swapCursor(ICustomCursorWrapper<IPhoto> pPhotoCursor){
            mPhotoCursor.close();
            mPhotoCursor = pPhotoCursor;
        }

        @Override
        public int getItemCount() {
            return mPhotoCursor.getCount();
        }
    }
}
