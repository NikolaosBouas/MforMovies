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

class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {


    private final Context mContext;
    final private TrailerAdapterOnClickHandler mClickHandler;
    private int mSize = 0;


    public TrailerAdapter(@NonNull Context context, TrailerAdapterOnClickHandler clickHandler, int size) {
        mContext = context;
        mClickHandler = clickHandler;
        mSize = size;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_list_item, parent, false);

        view.setFocusable(true);

        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        trailerAdapterViewHolder.trailerView.setText("Trailer " + String.valueOf(position + 1));
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

    public interface TrailerAdapterOnClickHandler {
        void onClick(int position);
    }

    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView trailerView;

        TrailerAdapterViewHolder(View view) {
            super(view);

            trailerView = (TextView) view.findViewById(R.id.trailer_tv);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }
}
