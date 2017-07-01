package com.android.divgarg.blockbustermovies.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.divgarg.blockbustermovies.R;
import com.android.divgarg.blockbustermovies.adapter.GridViewAdapter;
import com.android.divgarg.blockbustermovies.models.MenuItemTypes;
import com.android.divgarg.blockbustermovies.models.MovieItem;
import com.android.divgarg.blockbustermovies.models.MovieResponse;
import com.android.divgarg.blockbustermovies.utils.DataCallback;
import com.android.divgarg.blockbustermovies.utils.DataService;
import com.android.divgarg.blockbustermovies.utils.UserPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieGridActivity extends AppCompatActivity {

    private static final String TAG = MovieGridActivity.class.getSimpleName();

    @BindView(R.id.grid_view)
    GridView mGridView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private GridViewAdapter mGridAdapter;
    private List<MovieItem> mGridData;

    private MenuItemTypes mMovieSortOrder = MenuItemTypes.POPULAR;
    private int mCurrentPage = 1;
    private int mMaxNumPages = 1;
    private boolean mLastPage = false;
    private int mPrevTotal = 0;
    private String sortOrderKey = "movie_sort_order";
    private String gridViewIndex = "grid_view_index";
    private String currentPage = "current_page";
    private String maxPages = "max_pages";
    private Menu menu;
    private UserPreferenceUtils prefUtil = new UserPreferenceUtils();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_grid_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mGridView.setOnScrollListener(new MovieScrollListener());

        if (!isNetworkAvailable()) {
            showNoInternetMessage();
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            if (DataService.getInstance().getMovies() != null) {
                mGridData = DataService.getInstance().getMovies();
            } else {
                mGridData = new ArrayList<>();
            }
            mGridAdapter = new GridViewAdapter(MovieGridActivity.this, R.layout.grid_item_layout, mGridData);
            mGridView.setAdapter(mGridAdapter);
            getDataForCurrentPage(mCurrentPage);
        }

        if (null != savedInstanceState) {
            if (savedInstanceState.get(sortOrderKey) != null) {
                int lastSortOrder = savedInstanceState.getInt(sortOrderKey);
                mMovieSortOrder = MenuItemTypes.getType(lastSortOrder);
            }
            if (savedInstanceState.getInt(gridViewIndex) > 0) {
                mGridView.smoothScrollToPosition(savedInstanceState.getInt(gridViewIndex));
            }
            if (savedInstanceState.getInt(currentPage) != 0) {
                mCurrentPage = savedInstanceState.getInt(currentPage);
            }
            if (savedInstanceState.getInt(maxPages) != 0) {
                mMaxNumPages = savedInstanceState.getInt(maxPages);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMovieSortOrder == MenuItemTypes.FAVOURITE) {
            loadData();
        }
    }

    private void getDataForCurrentPage(int page) {
        DataService.getInstance().getDataOfType(getApplicationContext(), mMovieSortOrder, new DataCallback() {

            @Override
            public void onSuccess(MovieResponse movieResponse) {
                if (mCurrentPage == 1) {
                    mMaxNumPages = movieResponse.getTotalPages();
                }
                List<MovieItem> movies = movieResponse.getResults();
                if (movies != null) {
                    mGridData = mGridAdapter.getGidData();
                    if (mGridData == null) {
                        mGridData = new ArrayList<>();
                    }
                    mGridData.addAll(movies);
                }
                mGridAdapter.setGridData(mGridData);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, t.toString());
            }
        }, page);
    }


    private void showNoInternetMessage() {
        RelativeLayout mNoNetworkLayout = (RelativeLayout) findViewById(R.id.no_internet_layout);
        ImageView mRefreshBtn = (ImageView) findViewById(R.id.refresh_btn);

        mNoNetworkLayout.setVisibility(View.VISIBLE);
        mGridView.setVisibility(View.GONE);
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;
        getMenuInflater().inflate(R.menu.settings, menu);
        if ( mMovieSortOrder == MenuItemTypes.FAVOURITE)
            menuItem = menu.getItem(2);
        else if (mMovieSortOrder == MenuItemTypes.TOP_RATED)
            menuItem = menu.getItem(1);
        else
            menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int selectedSettingId = item.getItemId();

        int size = menu.size();
        for (int i = 0; i < size; i++) {
            menu.getItem(i).setChecked(false);
        }
        switch (selectedSettingId) {
            case R.id.popularity:

                mMovieSortOrder = MenuItemTypes.POPULAR;
                prefUtil.saveSortOrder(this, mMovieSortOrder);
                item.setChecked(true);

                break;
            case R.id.top_rating:
                mMovieSortOrder = MenuItemTypes.TOP_RATED;
                prefUtil.saveSortOrder(this, mMovieSortOrder);
                item.setChecked(true);
                break;
            case R.id.favourite:
                mMovieSortOrder = MenuItemTypes.FAVOURITE;
                prefUtil.saveSortOrder(this, mMovieSortOrder);
                item.setChecked(true);

        }
        loadData();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(sortOrderKey, mMovieSortOrder.getMenuType());
        outState.putInt(gridViewIndex, mGridView.getFirstVisiblePosition());
        outState.putInt(currentPage, mCurrentPage);
        outState.putInt(maxPages, mMaxNumPages);
        super.onSaveInstanceState(outState);
    }

    public void loadData() {

        if (mGridAdapter != null) {
            mGridAdapter.clear();
        }
        mPrevTotal = 0;
        mCurrentPage = 1;
        if (isNetworkAvailable()) {
            getDataForCurrentPage(mCurrentPage);
        } else {
            showNoInternetMessage();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    class MovieScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 15;
        private boolean loading = true;

        MovieScrollListener() {
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            if (loading) {
                if (totalItemCount > mPrevTotal) {
                    loading = false;
                    mPrevTotal = totalItemCount;
                    mCurrentPage++;

                    if (mCurrentPage + 1 > mMaxNumPages) {
                        mLastPage = true;
                    }
                }
            }
            if (!loading && (totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) && !mLastPage) {
                getDataForCurrentPage(mCurrentPage + 1);
                loading = true;
            }
        }
    }

}
