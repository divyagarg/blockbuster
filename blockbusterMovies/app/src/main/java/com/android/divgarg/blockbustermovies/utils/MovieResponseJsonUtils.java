package com.android.divgarg.blockbustermovies.utils;

import com.android.divgarg.blockbustermovies.MovieGridActivity;
import com.android.divgarg.blockbustermovies.models.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by divgarg on 4/15/17.
 */

public class MovieResponseJsonUtils {

    public static ArrayList<MovieItem> getMovieListFromJson(String jsonMovieResponse) throws JSONException {

        ArrayList<MovieItem> movieList = new ArrayList<>();
        JSONObject movieJson = new JSONObject(jsonMovieResponse);

        if (movieJson.has("status_code")) {
            return movieList;
        } else {
            JSONArray results = movieJson.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {

                JSONObject obj = results.getJSONObject(i);
                MovieItem movieItem = new MovieItem();
                movieItem.setMovieId(obj.getInt("id"));
                movieItem.setPosterPath(obj.getString("poster_path"));
                movieItem.setOriginalTitle(obj.getString("original_title"));
                movieItem.setOverview(obj.getString("overview"));
                movieItem.setReleaseDate(obj.getString("release_date"));
                movieItem.setVoteAvg(obj.getDouble("vote_average"));
                movieList.add(movieItem);
            }
            return movieList;
        }

    }

    public static int getTotalNumberOfPages(String jsonMovieResponse) throws JSONException {
        JSONObject movieJson = new JSONObject(jsonMovieResponse);
        return movieJson.getInt("total_pages");

    }
}
