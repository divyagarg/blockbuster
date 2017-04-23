package com.android.divgarg.blockbustermovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.divgarg.blockbustermovies.models.MovieItem;
import com.android.divgarg.blockbustermovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by divgarg on 4/22/17.
 */

public class MovieDetailActivity extends AppCompatActivity {

    private TextView movieTitle;
    private TextView movieRating;
    private TextView movieSynopsis;
    private ImageView moviePoster;
    private TextView movieReleaseDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Divya", "oncreate");
        setContentView(R.layout.movie_detail_layout);

        movieTitle = (TextView)findViewById(R.id.movie_title);
        movieRating = (TextView) findViewById(R.id.movie_rating);
        movieSynopsis = (TextView) findViewById(R.id.movie_synopsis);
        moviePoster = (ImageView) findViewById(R.id.movie_poster);
        movieReleaseDate = (TextView) findViewById(R.id.movie_release_date);

        MovieItem item = (MovieItem) getIntent().getSerializableExtra("movieItem");
        movieTitle.setText(item.getOriginalTitle());
        movieSynopsis.setText(item.getOverview());
        movieRating.setText(item.getVoteAvg().toString());
        String picasoUrl = NetworkUtils.getPicasoCompletePath(item.getPosterPath());
        Picasso.with(this).load(picasoUrl).into(moviePoster);
        movieReleaseDate.setText(item.getReleaseDate());

    }
}
