package com.ishraq.janna.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishraq.janna.R;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.viewholder.EventItemViewHolder;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;
import com.ishraq.janna.viewholder.RecyclerLoadMoreViewHolder;

import java.util.List;

/**
 * Created by Ahmed on 2/27/2016.
 */
public class EventListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Event> events;
    private boolean loadMore;

    /*
    * Type More: Endless progress view
    * Type header : layout of toolbar which will be empty
    * Type first : layout of subcategories and show more button
    * Type Item : Recipe Item
    * */
    private static final int TYPE_MORE = 4;
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;

    public EventListAdapter(Context context, List<Event> events, boolean loadMore) {
        this.context = context;
        this.events = events;
        this.loadMore = loadMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == TYPE_ITEM) {
            final View view = LayoutInflater.from(context).inflate(R.layout.row_event, parent, false);
            return new EventItemViewHolder(view, context, events);
        } else if (viewType == TYPE_HEADER) {
            final View view = LayoutInflater.from(context).inflate(R.layout.recycler_header, parent, false);
            return new RecyclerHeaderViewHolder(view, -1);
        } else if (viewType == TYPE_MORE) {
            final View view = LayoutInflater.from(context).inflate(R.layout.recycler_progress_view_holder, parent, false);
            return new RecyclerLoadMoreViewHolder(view, loadMore);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (!isPositionHeader(position)) {
            if (position == getItemCount() -1){
                RecyclerLoadMoreViewHolder holder = (RecyclerLoadMoreViewHolder) viewHolder;
                holder.isLoadMore(loadMore);
            } else {
                EventItemViewHolder holder = (EventItemViewHolder) viewHolder;
                holder.setEventItems(position - 1);
            }
        }
        else {
            RecyclerHeaderViewHolder holder = (RecyclerHeaderViewHolder) viewHolder;
        }
    }

    @Override
    public int getItemCount() {
        return getBasicItemCount() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        if (position == getItemCount() -1) {
            return TYPE_MORE;
        }
        return TYPE_ITEM;
    }

    public int getBasicItemCount() {
        return events == null ? 0 : events.size();
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public void setLoadMore(boolean state) {
        this.loadMore = state;
    }
}
