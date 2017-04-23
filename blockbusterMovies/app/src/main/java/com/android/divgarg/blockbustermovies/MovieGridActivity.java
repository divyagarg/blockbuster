package com.android.divgarg.blockbustermovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.divgarg.blockbustermovies.models.MovieItem;
import com.android.divgarg.blockbustermovies.utils.MovieResponseJsonUtils;
import com.android.divgarg.blockbustermovies.utils.NetworkUtils;
import com.android.divgarg.blockbustermovies.utils.UserPreferenceUtils;

import java.net.URL;
import java.util.ArrayList;

public class MovieGridActivity extends AppCompatActivity {

    private static final String TAG = MovieGridActivity.class.getSimpleName();


    private GridView mGridView;
    private ProgressBar mProgressBar;

    private GridViewAdapter mGridAdapter;
    private ArrayList<MovieItem> mGridData;

    private ImageView mRefreshBtn;
    private RelativeLayout mNoNetworkLayout;
    private String mMovieSortOrder;
    private int mCurrentPage = 1;
    private int mMaxNumPages = 1;
    private boolean mLastPage = false;
    private int mPrevTotal = 0;

    private UserPreferenceUtils prefUtil = new UserPreferenceUtils();

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_grid_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGridView = (GridView) findViewById(R.id.grid_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mGridView.setOnScrollListener(new EndlessScrollListener());

        if (!isNetworkAvailable())
        {
            showNoInternetMessage();
        }
        else
        {
            mMovieSortOrder = prefUtil.getUserPref(this);

            mGridData = new ArrayList<>();
            mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
            mGridView.setAdapter(mGridAdapter);
            new AsyncHttpTask().execute(mCurrentPage);
        }
    }

    private void showNoInternetMessage() {
        mNoNetworkLayout = (RelativeLayout) findViewById(R.id.no_internet_layout);
        mRefreshBtn = (ImageView) findViewById(R.id.refresh_btn);

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
        getMenuInflater().inflate(R.menu.settings, menu);
        MenuItem menuItem = menu.getItem(0);
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

    public void loadData() {

        mGridAdapter.clear();
        mPrevTotal = 0;

        if (isNetworkAvailable()) {
            new AsyncHttpTask().execute(mCurrentPage);
        } else {
            showNoInternetMessage();
        }

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    class AsyncHttpTask extends AsyncTask<Integer, Void, ArrayList<MovieItem>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected ArrayList<MovieItem> doInBackground(Integer... params) {
            if (null == params) {
                return null;
            }
            try {

                URL movieUrl = NetworkUtils.buildUrl(mMovieSortOrder, params[0]);
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieUrl);

                ArrayList<MovieItem> movieList = MovieResponseJsonUtils.getMovieListFromJson(jsonMovieResponse);
                if (params[0] == 1) {
                    mMaxNumPages = MovieResponseJsonUtils.getTotalNumberOfPages(jsonMovieResponse);
                }
                return movieList;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<MovieItem> movieItems) {
            Log.v(TAG, "onPost");
            if (movieItems != null) {
                ArrayList<MovieItem> movies = mGridAdapter.getGidData();
                if (movies == null) {
                    movies = new ArrayList<>();
                }
                movies.addAll(movieItems);
                mGridAdapter.setGridData(movies);
            } else {
                Toast.makeText(MovieGridActivity.this, R.string.generic_error, Toast.LENGTH_LONG).show();
            }
            mProgressBar.setVisibility(View.GONE);
            //mLoading = false;
        }
    }

    class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 15;
        private boolean loading = true;

        public EndlessScrollListener() {
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
                new AsyncHttpTask().execute(mCurrentPage + 1);
                loading = true;
            }
        }
    }

}
