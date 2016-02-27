package com.ishraq.janna.viewholder;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ishraq.janna.R;

/**
 * Created by Ahmed on 12/26/2015.
 */
public class RecyclerLoadMoreViewHolder extends RecyclerView.ViewHolder {

    private ContentLoadingProgressBar endlessProgressBar;

    public RecyclerLoadMoreViewHolder(View parent, boolean loadMore) {
        super(parent);
        endlessProgressBar = (ContentLoadingProgressBar) parent.findViewById(R.id.endlessProgressBar);

        if (!loadMore) {
            parent.findViewById(R.id.endlessProgressBar).setVisibility(View.GONE);
        }
    }

    public void isLoadMore(boolean loadMore) {
        if (loadMore) {
            endlessProgressBar.setVisibility(View.VISIBLE);
        } else {
            endlessProgressBar.setVisibility(View.GONE);
        }
    }
}