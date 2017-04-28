package com.android.divgarg.blockbustermovies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.divgarg.blockbustermovies.R;
import com.android.divgarg.blockbustermovies.models.MovieItem;
import com.android.divgarg.blockbustermovies.utils.PicasoUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by divgarg on 4/22/17.
 */

public class MovieDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        }
        TextView movieTitle = (TextView) findViewById(R.id.movie_title);
        TextView movieRating = (TextView) findViewById(R.id.movie_rating);
        TextView movieSynopsis = (TextView) findViewById(R.id.movie_synopsis);
        ImageView moviePoster = (ImageView) findViewById(R.id.movie_poster);
        TextView movieReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        if (getIntent().hasExtra("movieItem")) {
            MovieItem item = getIntent().getParcelableExtra("movieItem");
            movieTitle.setText(item.getOriginalTitle());
            movieSynopsis.setText(item.getOverview());
            movieRating.setText(item.getVoteAvg().toString());
            String picasoUrl = PicasoUtil.getPicasoCompletePath(item.getPosterPath());
            Picasso.with(this).load(picasoUrl).into(moviePoster);
            movieReleaseDate.setText(item.getReleaseDate());
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // close this activity and return to preview activity (if there is any)
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
