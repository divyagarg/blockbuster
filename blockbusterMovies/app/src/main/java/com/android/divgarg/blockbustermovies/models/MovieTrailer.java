package com.android.divgarg.blockbustermovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by divgarg on 5/28/17.
 */

public class MovieTrailer implements Parcelable{

        @SerializedName("id")
        private String id;

        @SerializedName("key")
        private String key;

        @SerializedName("name")
        private String trailerName;

        @SerializedName("site")
        private String site;

        @SerializedName("type")
        private String type;

        public MovieTrailer() {
    }


        protected MovieTrailer(Parcel in) {
        key = in.readString();
        trailerName = in.readString();
        site = in.readString();
        type = in.readString();
    }

        public static final Parcelable.Creator<MovieTrailer> CREATOR = new Parcelable.Creator<MovieTrailer>() {
            @Override
            public MovieTrailer createFromParcel(Parcel in) {
                return new MovieTrailer(in);
            }

            @Override
            public MovieTrailer[] newArray(int size) {
                return new MovieTrailer[size];
            }
        };

        @Override
        public int describeContents() {
        return 0;
    }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(trailerName);
        dest.writeString(site);
        dest.writeString(type);
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }
}
