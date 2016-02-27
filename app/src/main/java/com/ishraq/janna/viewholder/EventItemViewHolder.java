package com.ishraq.janna.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ishraq.janna.R;
import com.ishraq.janna.model.Event;

import java.util.List;

/**
 * Created by Ahmed Saeed on 12/29/2015.
 */
public class EventItemViewHolder extends RecyclerView.ViewHolder {

    private View row;
    private Context context;
    private List<Event> events;

    private TextView eventNameTextView;

    public EventItemViewHolder(View parent, Context context, List<Event> events) {
        super(parent);
        row = parent;

        this.context = context;
        this.events = events;

        eventNameTextView = (TextView) parent.findViewById(R.id.eventNameTextView);
    }

    public void setEventItems(int position){
        final Event event = events.get(position);

        eventNameTextView.setText(event.getEventNameAra());
        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

}