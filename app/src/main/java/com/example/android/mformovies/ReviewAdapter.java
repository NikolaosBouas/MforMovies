package com.example.android.mformovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Nikos on 19/8/2017.
 */

class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {


    private final Context mContext;

    private int mSize = 0;

    public ReviewAdapter(@NonNull Context context, int size) {
        mContext = context;
        mSize = size;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_list_item, parent, false);

        view.setFocusable(true);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        reviewAdapterViewHolder.reviewTitleView.setText("Review " + String.valueOf(position + 1));
        reviewAdapterViewHolder.reviewView.setText(DetailActivity.getmMovieReviews().get(position));
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        return mSize;
    }


    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder  {

        final TextView reviewTitleView;

        final TextView reviewView;

        ReviewAdapterViewHolder(View view) {
            super(view);

            reviewTitleView = (TextView) view.findViewById(R.id.review_title_tv);

            reviewView = (TextView) view.findViewById(R.id.review_tv);

        }

    }
}