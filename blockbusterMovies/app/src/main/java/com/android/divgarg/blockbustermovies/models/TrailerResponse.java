package com.android.divgarg.blockbustermovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by divgarg on 5/28/17.
 */

public class TrailerResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<MovieTrailer> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieTrailer> getResults() {
        return results;
    }

    public void setResults(List<MovieTrailer> results) {
        this.results = results;
    }
}