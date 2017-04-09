package com.codepath.photospot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.photospot.R;
import com.codepath.photospot.daos.FlickrPhoto;

import java.util.List;

/**
 * Created by pauljmin on 4/8/17.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPhoto;
        public TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }

    private List<FlickrPhoto> mPhotos;
    private Context mContext;

    public PhotoAdapter(Context context, List<FlickrPhoto> photos) {
        mContext = context;
        mPhotos = photos;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View photoView = inflater.inflate(R.layout.item_photo, parent, false);
        ViewHolder viewHolder = new ViewHolder(photoView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(PhotoAdapter.ViewHolder viewHolder, int position) {
        FlickrPhoto photo = mPhotos.get(position);

        ImageView ivPhoto = viewHolder.ivPhoto;

        Glide.with(getContext())
                .load(photo.getUrl())
                .centerCrop()
                .into(ivPhoto);

        viewHolder.tvTitle.setText(photo.getTitle());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mPhotos.size();
    }
}
