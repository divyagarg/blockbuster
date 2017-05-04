package com.android.divgarg.blockbustermovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.divgarg.blockbustermovies.R;
import com.android.divgarg.blockbustermovies.activity.MovieDetailActivity;
import com.android.divgarg.blockbustermovies.models.MovieItem;
import com.android.divgarg.blockbustermovies.utils.PicasoUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by divgarg on 4/15/17.
 */

public class GridViewAdapter extends ArrayAdapter<MovieItem> {
    private Context mContext;
    private int layoutResourceId;
    private List<MovieItem> mGridData;


    public GridViewAdapter(Context mContext, int layoutResourceId, List<MovieItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }

    public List<MovieItem> getGidData() {
        return this.mGridData;
    }

    public void setGridData(List<MovieItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MovieItem item = mGridData.get(position);
        String picasoUrl = PicasoUtil.getPicasoCompletePath(item.getPosterPath());
        Picasso
                .with(mContext)
                .load(picasoUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imageView);
        //Keeping it outside of if else as, ui will be re-cycled in that case position will be wrong
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MovieDetailActivity.class);
                intent.putExtra("movieItem", mGridData.get(position));
                v.getContext().startActivity(intent);
            }
        });
        return convertView;
    }


    @Override
    public int getCount() {
        return mGridData.size();
    }

    @Override
    public MovieItem getItem(int position) {
        return mGridData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        @BindView(R.id.grid_item_image) ImageView imageView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
