package com.android.divgarg.blockbustermovies.utils;

import com.android.divgarg.blockbustermovies.models.MovieResponse;


/**
 * Created by divgarg on 6/17/17.
 */

public interface DataCallback {

    void onSuccess(MovieResponse movieItems);

    void onFailure(Throwable t);
}
