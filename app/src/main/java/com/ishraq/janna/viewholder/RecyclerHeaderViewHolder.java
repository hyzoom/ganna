package com.ishraq.janna.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ishraq.janna.R;

/**
 * Created by Ahmed on 12/26/2015.
 */
public class RecyclerHeaderViewHolder extends RecyclerView.ViewHolder {

    public RecyclerHeaderViewHolder(View parent, int size) {
        super(parent);
        if (size == 0) {
            parent.findViewById(R.id.noEventsLayout).setVisibility(View.VISIBLE);
        }
    }
}