package com.example.moviereview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter {
    Context context;
    int resId;
    List<ReviewVO> reviews;

    public ReviewAdapter(@NonNull Context context, int resource, @NonNull List<ReviewVO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resId = resource;
        this.reviews = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final  int pos = position;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resId, null);
        }
        final ReviewVO review = reviews.get(position);
        TextView movieTitle = convertView.findViewById(R.id.list_title);
        TextView movieDate = convertView.findViewById(R.id.list_date);
        RatingBar movieRate = convertView.findViewById(R.id.list_rate);

        movieTitle.setText(review.getTitle());
        movieDate.setText(review.getDate());
        movieRate.setRating((float)review.getRating());

        return convertView;
    }
}
