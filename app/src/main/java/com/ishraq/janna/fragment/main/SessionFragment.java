package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
import com.ishraq.janna.component.ExpandableHeightListView;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.Guest;
import com.ishraq.janna.model.Rule;
import com.ishraq.janna.model.Session;
import com.ishraq.janna.service.EventService;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;
import com.ishraq.janna.webservice.CommonRequest;
import com.ishraq.janna.webservice.EventWebService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ahmed on 3/15/2016.
 */
public class SessionFragment extends MainCommonFragment {
    private EventWebService eventWebService;
    private EventService eventService;

    private RecyclerView recyclerView;

    private Event event;
    private EventSessionAdapter adapter;

    private boolean refresh = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventWebService = getWebService(EventWebService.class);
        eventService = new EventService(getMainActivity());
    }

    @Override
    public void refreshContent() {
        refresh= true;
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getMainActivity().getToolbar().setTitle(getResources().getString(R.string.session_title));
        showToolbar();
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideToolbar();
            }

            @Override
            public void onShow() {
                showToolbar();
            }

            @Override
            public void swipeRefresh(boolean state) {
                getMainActivity().getSwipeRefreshLayout().setEnabled(state);
            }

            @Override
            public void onLoadMore() {
            }
        });

        initData();

        return view;
    }

    private List<Session> reArrangeData(List<Session> sessions) {
        for (int i = 0; i < sessions.size(); i++){
            if (sessions.get(i).getEventsSessionNameAra().equals("Registration")) {
                Session tempSession = sessions.get(i);
                sessions.remove(i);
                sessions.add(0, tempSession);
            }
        }
        return sessions;
    }

    private void initData() {
        event = eventService.getEvent(1);
        if (event == null || refresh) {
            EventDetailsRequest request = new EventDetailsRequest();
            request.execute();
        } else {
            adapter = new EventSessionAdapter(event);
            recyclerView.setAdapter(adapter);
            getMainActivity().stopLoadingAnimator();
            getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
        }

    }


    ///////////////////////////////////// Adapter //////////////////////////////////////////////////
    class EventSessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Event event;

        /*
        * Type header : layout of toolbar which will be empty
        * Type Item : Recipe Item
        * */

        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public EventSessionAdapter(Event event) {
            this.event = event;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.fragment_session, parent, false);
                return new ItemViewHolder(view, event);
            } else if (viewType == TYPE_HEADER) {
                final View view = LayoutInflater.from(context).inflate(R.layout.recycler_header, parent, false);
                return new RecyclerHeaderViewHolder(view, -1);
            }
            throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (!isPositionHeader(position)) {
                ItemViewHolder holder = (ItemViewHolder) viewHolder;
                holder.setEventItem();
            } else {
                RecyclerHeaderViewHolder holder = (RecyclerHeaderViewHolder) viewHolder;
            }
        }

        @Override
        public int getItemCount() {
            return getBasicItemCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position)) {
                return TYPE_HEADER;
            }
            return TYPE_ITEM;
        }

        public int getBasicItemCount() {
            return event == null ? 0 : 1;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private Event event;

        private ExpandableHeightListView chairListView, sessionListView;

        public ItemViewHolder(View parent, Event event) {
            super(parent);
            this.event = event;

            sessionListView = (ExpandableHeightListView) parent.findViewById(R.id.sessionListView);
            sessionListView.setExpanded(true);

        }

        public void setEventItem() {
            final List<Session> sessions = reArrangeData(new ArrayList<Session>(event.getSess()));

            SessionListAdapter sessionListAdapter = new SessionListAdapter(JannaApp.getContext(), R.layout.row_session, sessions);
            sessionListView.setAdapter(sessionListAdapter);
        }

    }

    class SessionListAdapter extends ArrayAdapter<Session> {
        private List<Session> sessions;
        private Context context;
        private int layoutResourceId;


        public SessionListAdapter(Context context, int layoutResourceId, List<Session> sessions) {
            super(context, layoutResourceId, sessions);
            this.sessions = sessions;
            this.context = context;
            this.layoutResourceId = layoutResourceId;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(JannaApp.getContext());
                row = inflater.inflate(layoutResourceId, parent, false);
            }

            CardView cardView = (CardView) row.findViewById(R.id.cardView);
            TextView nameTextView = (TextView) row.findViewById(R.id.nameTextView);
            nameTextView.setText(sessions.get(position).getEventsSessionNameLat() + "");

            if (sessions.get(position).getEventsSessionCode() == 3) {

                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));

                String text = sessions.get(position).getEventsSessionNameAra() + "\n   "
                        + sessions.get(position).getEventsSessionNameLat();

                if (!sessions.get(position).getEventsSessionInformations().equals("0")) {
                    text = sessions.get(position).getEventsSessionNameAra() + "\n   "
                            + sessions.get(position).getEventsSessionNameLat()
                            + sessions.get(position).getEventsSessionInformations();
                }
                nameTextView.setText(text);
            } else {
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sessions.get(position).getEventsSessionCode() != 3) {
                        Fragment sessionDetailsFragment = new SessionDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("eventId", 1);
                        bundle.putInt("sessionId", sessions.get(position).getEventsSessionCode());
                        sessionDetailsFragment.setArguments(bundle);
                        getMainActivity().addFragment(sessionDetailsFragment, true, null);
                    }
                }
            });
            return row;
        }
    }



    ///////////////////////////////////////////////// Request ////////////////////////////////

    private class EventDetailsRequest implements CommonRequest {
        @Override
        public void execute() {
            eventWebService.getEvent(1).enqueue(new RequestCallback<List<Event>>(this) {
                @Override
                public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                    event = response.body().get(0);
                    eventService.saveEvent(event);
                    event = eventService.getEvent(event.getEventCode());

                    try {
                        adapter = new EventSessionAdapter(event);
                        recyclerView.setAdapter(adapter);

                        refresh = false;
                        getMainActivity().stopLoadingAnimator();
                        getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
                    } catch (Exception e) {
                        Log.i(JannaApp.LOG_TAG, e + "This fragment is finished.");
                    }

                }

                @Override
                public void onFailure(Call<List<Event>> call, Throwable t) {
                    getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
                }
            });
        }
    }

}
