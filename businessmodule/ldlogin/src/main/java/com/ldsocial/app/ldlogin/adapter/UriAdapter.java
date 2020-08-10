package com.ldsocial.app.ldlogin.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldsocial.app.ldlogin.R;

import java.util.List;

/**
 * @ClassName UriAdapter
 * @Description: 描述
 * @Author: Lary.huang
 * @CreateDate: 2020/8/2 4:19 PM
 * @Version: 1.0
 */
public class UriAdapter extends RecyclerView.Adapter<UriAdapter.UriViewHolder> {
    private List<Uri> mUris;
    private List<String> mPaths;

    public void setData(List<Uri> uris, List<String> paths) {
        mUris = uris;
        mPaths = paths;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UriViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.uri_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull UriViewHolder holder, int position) {
        if (mUris != null && !mUris.isEmpty()) {
            holder.mUri.setText(mUris.get(position).toString());
            holder.mPath.setText(mPaths.get(position));

            holder.mUri.setAlpha(position % 2 == 0 ? 1.0f : 0.54f);
            holder.mPath.setAlpha(position % 2 == 0 ? 1.0f : 0.54f);
        }
    }

    @Override
    public int getItemCount() {
        return mUris == null ? 0 : mUris.size();
    }

    static class UriViewHolder extends RecyclerView.ViewHolder {
        private TextView mUri;
        private TextView mPath;

        public UriViewHolder(@NonNull View itemView) {
            super(itemView);
            mUri = itemView.findViewById(R.id.uri);
            mPath = itemView.findViewById(R.id.path);
        }
    }
}
