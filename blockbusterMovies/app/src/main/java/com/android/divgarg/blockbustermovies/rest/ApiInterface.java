package com.android.divgarg.blockbustermovies.rest;

import com.android.divgarg.blockbustermovies.models.MovieResponse;
import com.android.divgarg.blockbustermovies.models.MovieReviewsResponse;
import com.android.divgarg.blockbustermovies.models.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by divgarg on 4/26/17.
 */

public interface ApiInterface {


    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{id}/videos")
    Call<TrailerResponse> fetchTrailersForMovie(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MovieReviewsResponse> fetchReviews(@Path("id") int movieId, @Query("api_key") String apiKey);

}
