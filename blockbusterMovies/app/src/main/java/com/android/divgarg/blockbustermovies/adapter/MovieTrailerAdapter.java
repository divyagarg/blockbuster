package com.android.divgarg.blockbustermovies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.divgarg.blockbustermovies.R;
import com.android.divgarg.blockbustermovies.models.MovieTrailer;

import java.util.List;

/**
 * Created by divgarg on 6/11/17.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder>
{


    private List<MovieTrailer> trailers;

    public MovieTrailerAdapter(List<MovieTrailer> trailers) {
        this.trailers = trailers;
    }

    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_trailer_item, viewGroup, false);

        return new MovieTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerViewHolder holder, final int position) {
        final MovieTrailer trailer = trailers.get(position);
        holder.mTrailerName.setText(trailer.getTrailerName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Play video
                //Toast.makeText(view.getContext(), "Recycle Click" + position, Toast.LENGTH_LONG).show();
                watchYoutubeVideo(view.getContext(), trailer.getKey());
            }
        });

    }

    public static void watchYoutubeVideo(Context context, String id) {
        Intent applicationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(applicationIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(browserIntent);
        }
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class MovieTrailerViewHolder extends RecyclerView.ViewHolder {

        TextView mTrailerName;
        public MovieTrailerViewHolder(View itemView) {
            super(itemView);
            mTrailerName = (TextView) itemView.findViewById(R.id.trailer_name);
        }


    }

}
