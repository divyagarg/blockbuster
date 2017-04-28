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

import com.android.divgarg.blockbustermovies.BuildConfig;
import com.android.divgarg.blockbustermovies.R;
import com.android.divgarg.blockbustermovies.adapter.GridViewAdapter;
import com.android.divgarg.blockbustermovies.models.MovieItem;
import com.android.divgarg.blockbustermovies.models.MovieResponse;
import com.android.divgarg.blockbustermovies.rest.ApiClient;
import com.android.divgarg.blockbustermovies.rest.ApiInterface;
import com.android.divgarg.blockbustermovies.utils.UserPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieGridActivity extends AppCompatActivity {

    private static final String TAG = MovieGridActivity.class.getSimpleName();


    private GridView mGridView;
    private ProgressBar mProgressBar;

    private GridViewAdapter mGridAdapter;
    private List<MovieItem> mGridData;

    private String mMovieSortOrder;
    private int mCurrentPage = 1;
    private int mMaxNumPages = 1;
    private boolean mLastPage = false;
    private int mPrevTotal = 0;
    private String sortOrderKey= "movie_sort_order";
    private UserPreferenceUtils prefUtil = new UserPreferenceUtils();

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_grid_layout);

        if(null != savedInstanceState && savedInstanceState.get(sortOrderKey) != null)
        {
            mMovieSortOrder = savedInstanceState.get(sortOrderKey).toString();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mGridView = (GridView) findViewById(R.id.grid_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mGridView.setOnScrollListener(new MovieScrollListener());

        if (!isNetworkAvailable()) {
            showNoInternetMessage();
        } else {
            if( null == mMovieSortOrder)
            {
                mMovieSortOrder = prefUtil.getUserPref(this);
            }
            mProgressBar.setVisibility(View.VISIBLE);
            mGridData = new ArrayList<>();
            mGridAdapter = new GridViewAdapter(MovieGridActivity.this, R.layout.grid_item_layout, mGridData);
            mGridView.setAdapter(mGridAdapter);
            makeAPICall(mCurrentPage);
        }
    }

    private void makeAPICall(int page)
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = null;
        switch (mMovieSortOrder)
        {
            case "popular":
            {
                call = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, page);
                break;
            }
            case "top_rated":
            {
                call = apiService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, page);
                break;
            }

        }
        call.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse= response.body();
                if(mCurrentPage == 1)
                {
                    mMaxNumPages = movieResponse.getTotalPages();
                }
                List<MovieItem> movies= movieResponse.getResults();
                if(movies != null)
                {
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
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, t.toString());
            }
        });

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
        if(mMovieSortOrder != null && mMovieSortOrder.equals("top_rated"))
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
        switch (selectedSettingId) {
            case R.id.popularity:
                mMovieSortOrder = getResources().getString(R.string.popularity_pref_val);
                prefUtil.saveSortOrder(this, mMovieSortOrder);
                item.setChecked(true);
                MenuItem menuItem = menu.getItem(1);
                menuItem.setChecked(false);
                break;
            case R.id.top_rating:
                mMovieSortOrder = getResources().getString(R.string.top_rated_pref_val);
                prefUtil.saveSortOrder(this, mMovieSortOrder);
                item.setChecked(true);
                menuItem = menu.getItem(0);
                menuItem.setChecked(false);
        }
        loadData();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(sortOrderKey, mMovieSortOrder);
        super.onSaveInstanceState(outState);
    }
    
    public void loadData() {

        if(mGridAdapter != null)
        {
            mGridAdapter.clear();
        }
        mPrevTotal = 0;
        mCurrentPage = 1;
        if (isNetworkAvailable()) {
            makeAPICall(mCurrentPage);
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
                makeAPICall( mCurrentPage + 1);
                loading = true;
            }
        }
    }

}
