package com.android.divgarg.blockbustermovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by divgarg on 4/15/17.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String MOVIE_API_BASE_ENDPOINT = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY_QUERY_PARAM = "api_key";
    //Replace with your personal key
    private static final String API_KEY_VAL = "YOUR_API_KEY";
    private static final String PAGE_NUMBER_QUERY_PARAM = "page";
    private static final String MOVIE_POSTER_BASEPATH = "http://image.tmdb.org/t/p/";


    public static URL buildUrl(String filterChoice, Integer pageNumber) {
        if (null == pageNumber)
        {
            pageNumber = 1;
        }
        Uri builtUri = Uri.parse(MOVIE_API_BASE_ENDPOINT + filterChoice).buildUpon()
                .appendQueryParameter(API_KEY_QUERY_PARAM, API_KEY_VAL)
                .appendQueryParameter(PAGE_NUMBER_QUERY_PARAM, String.valueOf(pageNumber))
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static String getPicasoCompletePath(String partialPath)
    {
        return MOVIE_POSTER_BASEPATH + "/w185" + partialPath;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }

}
