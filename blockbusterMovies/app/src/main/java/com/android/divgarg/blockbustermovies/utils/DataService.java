package com.android.divgarg.blockbustermovies.utils;

import android.content.Context;
import android.database.Cursor;

import com.android.divgarg.blockbustermovies.BuildConfig;
import com.android.divgarg.blockbustermovies.data.MovieContract;
import com.android.divgarg.blockbustermovies.models.MenuItemTypes;
import com.android.divgarg.blockbustermovies.models.MovieItem;
import com.android.divgarg.blockbustermovies.models.MovieResponse;
import com.android.divgarg.blockbustermovies.rest.ApiClient;
import com.android.divgarg.blockbustermovies.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by divgarg on 6/17/17.
 */

public class DataService {

    private static final DataService dataService = new DataService();

    private List<MovieItem> movies = new ArrayList<>();

    private DataService() {

    }

    public static DataService getInstance() {
        return dataService;
    }

    public void getDataOfType(Context context, final MenuItemTypes type, final DataCallback callback, int page) {

        switch (type) {
            case POPULAR: {
                getPopularMovies(callback, page);
                break;
            }
            case TOP_RATED: {
                getTopRatedMovies(callback, page);
                break;
            }
            case FAVOURITE: {
                getFavouriteMovies(callback, context);
                break;
            }

        }
    }


    private void getPopularMovies(final DataCallback callback, int page) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, page);
        call.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();
                movies.addAll(movieResponse.getResults());
                callback.onSuccess(movieResponse);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    private void getTopRatedMovies(final DataCallback callback, int page) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, page);
        call.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();
                movies.addAll(movieResponse.getResults());
                callback.onSuccess(movieResponse);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    private void getFavouriteMovies(final DataCallback callback, Context context) {

        Cursor data = context.getContentResolver().query(MovieContract.FavouriteMovieEntry.CONTENT_URI, null, null, null, null);
        ArrayList<MovieItem> movieList = new ArrayList<>();

        Integer movieIdIndex = data.getColumnIndex(MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID);
        Integer columnTitleIndex = data.getColumnIndex(MovieContract.FavouriteMovieEntry.COLUMN_TITLE);
        Integer columnPosterIndex = data.getColumnIndex(MovieContract.FavouriteMovieEntry.COLUMN_POSTER);
        Integer overviewIndex = data.getColumnIndex(MovieContract.FavouriteMovieEntry.COLUMN_OVERVIEW);
        Integer columnRatingIndex = data.getColumnIndex(MovieContract.FavouriteMovieEntry.COLUMN_RATING);
        Integer columnReleaseDateIndex = data.getColumnIndex(MovieContract.FavouriteMovieEntry.COLUMN_RELEASE_DATE);


        while (data.moveToNext()) {
            MovieItem movieItem = new MovieItem();
            movieItem.setMovieId(Integer.valueOf(data.getString(movieIdIndex)));
            movieItem.setOriginalTitle(data.getString(columnTitleIndex));
            movieItem.setPosterPath(data.getString(columnPosterIndex));
            movieItem.setReleaseDate(data.getString(columnReleaseDateIndex));
            movieItem.setOverview(data.getString(overviewIndex));
            movieItem.setVoteAvg(Double.valueOf(data.getString(columnRatingIndex)));
            movieList.add(movieItem);
        }

        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setResults(movieList);
        movieResponse.setTotalPages(1);
        movies.addAll(movieList);
        callback.onSuccess(movieResponse);
    }

    public List<MovieItem> getMovies() {
        return movies;
    }
}
