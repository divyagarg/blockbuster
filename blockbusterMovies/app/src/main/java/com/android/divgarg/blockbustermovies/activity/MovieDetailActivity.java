package com.android.divgarg.blockbustermovies.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.divgarg.blockbustermovies.BuildConfig;
import com.android.divgarg.blockbustermovies.R;
import com.android.divgarg.blockbustermovies.adapter.MovieReviewAdapter;
import com.android.divgarg.blockbustermovies.adapter.MovieTrailerAdapter;
import com.android.divgarg.blockbustermovies.models.MovieItem;
import com.android.divgarg.blockbustermovies.models.MovieReview;
import com.android.divgarg.blockbustermovies.models.MovieReviewsResponse;
import com.android.divgarg.blockbustermovies.models.MovieTrailer;
import com.android.divgarg.blockbustermovies.models.TrailerResponse;
import com.android.divgarg.blockbustermovies.rest.ApiClient;
import com.android.divgarg.blockbustermovies.rest.ApiInterface;
import com.android.divgarg.blockbustermovies.utils.PicasoUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by divgarg on 4/22/17.
 */

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    @BindView(R.id.movie_title)
    TextView movieTitle;

    @BindView(R.id.movie_rating)
    TextView movieRating;

    @BindView(R.id.movie_synopsis)
    TextView movieSynopsis;

    @BindView(R.id.movie_poster)
    ImageView moviePoster;

    @BindView(R.id.movie_release_date)
    TextView movieReleaseDate;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.trailers_list)
    RecyclerView mTrailerRecyclerView;

    MovieTrailerAdapter mTrailerAdapter;
    RecyclerView.LayoutManager mTrailerLayoutManager;

    @BindView(R.id.reviews_list)
    RecyclerView mReviewsRecyclerView;

    MovieReviewAdapter mReviewAdapter;
    RecyclerView.LayoutManager mReviewLayoutManager;

    @BindView(R.id.user_reviews_header)
    TextView mUserReviewsTxtView;

    @BindView(R.id.trailers_header)
    TextView mTrailerTxtView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_layout);
        ButterKnife.bind(this);

        mTrailerLayoutManager = new LinearLayoutManager(this);
        mTrailerRecyclerView.setLayoutManager(mTrailerLayoutManager);

        mReviewLayoutManager = new LinearLayoutManager(this);
        mReviewsRecyclerView.setLayoutManager(mReviewLayoutManager);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(getString(R.string.movie_detail_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        }

        if (getIntent().hasExtra("movieItem")) {
            MovieItem item = getIntent().getParcelableExtra("movieItem");
            movieTitle.setText(item.getOriginalTitle());
            movieSynopsis.setText(item.getOverview());
            movieRating.setText(item.getVoteAvg().toString());
            String picasoUrl = PicasoUtil.getPicasoCompletePath(item.getPosterPath());
            Picasso
                    .with(this)
                    .load(picasoUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(moviePoster);
            movieReleaseDate.setText(item.getReleaseDate());
            int movieId = item.getMovieId();
            getMovieTrailersInBackground(movieId);
            getUserReviewsInBackground(movieId);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void getUserReviewsInBackground(final int movieId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieReviewsResponse> call = apiService.fetchReviews(movieId, BuildConfig.THE_MOVIE_DB_API_TOKEN);
        call.enqueue(new Callback<MovieReviewsResponse>() {
            @Override
            public void onResponse(Call<MovieReviewsResponse> call, Response<MovieReviewsResponse> response) {
                MovieReviewsResponse reviewResponse = response.body();
                if ( reviewResponse != null && reviewResponse.getResults().size() > 0)
                {
                    List<MovieReview> reviews = reviewResponse.getResults();
                    mReviewAdapter = new MovieReviewAdapter(reviews);
                    mReviewsRecyclerView.setAdapter(mReviewAdapter);
                }
                else
                {
                    mUserReviewsTxtView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MovieReviewsResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void getMovieTrailersInBackground(final int movieId) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TrailerResponse> call = apiService.fetchTrailersForMovie(movieId, BuildConfig.THE_MOVIE_DB_API_TOKEN);
        call.enqueue(new Callback<TrailerResponse>(){

            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                TrailerResponse res = response.body();
                if ( res != null && res.getResults() != null)
                {
                    List<MovieTrailer> trailers = res.getResults();
                    mTrailerAdapter = new MovieTrailerAdapter(trailers);
                    mTrailerRecyclerView.setAdapter(mTrailerAdapter);
                }
                else
                {
                    mTrailerTxtView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
