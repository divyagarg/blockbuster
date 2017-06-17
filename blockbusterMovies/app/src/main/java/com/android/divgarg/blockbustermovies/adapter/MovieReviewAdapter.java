package com.android.divgarg.blockbustermovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.divgarg.blockbustermovies.R;
import com.android.divgarg.blockbustermovies.models.MovieReview;

import java.util.List;


/**
 * Created by divgarg on 6/17/17.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {

    private List<MovieReview> reviewList;

    public MovieReviewAdapter(List<MovieReview> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.moview_review_item, parent, false);
        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        final MovieReview movieReview = reviewList.get(position);
        holder.mAuthor.setText(movieReview.getAuthor());
        holder.mReviewContent.setText(movieReview.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class MovieReviewViewHolder extends RecyclerView.ViewHolder{

        public TextView mAuthor;
        public TextView mReviewContent;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            mAuthor = (TextView) itemView.findViewById(R.id.author);
            mReviewContent = (TextView) itemView.findViewById(R.id.review_content);
        }
    }
}
