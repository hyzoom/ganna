package com.ishraq.janna.viewholder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ishraq.janna.R;
import com.ishraq.janna.activity.MainActivity;
import com.ishraq.janna.fragment.main.EventDetailsFragment;
import com.ishraq.janna.fragment.main.EventFragment;
import com.ishraq.janna.fragment.main.EventQuestionFragment;
import com.ishraq.janna.fragment.main.QuestionFragment;
import com.ishraq.janna.model.Event;

import java.util.List;

/**
 * Created by Ahmed Saeed on 12/29/2015.
 */
public class EventItemViewHolder extends RecyclerView.ViewHolder {

    private View row;
    private Context context;
    private List<Event> events;
    private String tag;

    private TextView eventNameTextView;

    public EventItemViewHolder(View parent, Context context, List<Event> events, String tag) {
        super(parent);
        row = parent;

        this.tag = tag;
        this.context = context;
        this.events = events;

        eventNameTextView = (TextView) parent.findViewById(R.id.eventNameTextView);
    }

    public void setEventItems(int position) {
        final Event event = events.get(position);

        eventNameTextView.setText(event.getEventNameEng());
        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag.equals(EventFragment.TAG)) {
                    Fragment eventDetailsFragment = new EventDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("eventId", event.getEventCode());

                    eventDetailsFragment.setArguments(bundle);
                    ((MainActivity) context).addFragment(eventDetailsFragment, true, null);
                }

                if (tag.equals(QuestionFragment.TAG)) {
                    Fragment eventQuestionFragment = new EventQuestionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("eventId", event.getEventCode());

                    eventQuestionFragment.setArguments(bundle);
                    ((MainActivity) context).addFragment(eventQuestionFragment, true, null);
                }
            }
        });
    }

}