package com.android.divgarg.blockbustermovies.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by divgarg on 4/22/17.
 */

public class MovieDetailActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
        {
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
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
            finish();
        }

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
