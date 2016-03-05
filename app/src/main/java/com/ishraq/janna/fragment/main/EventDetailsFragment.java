package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
import com.ishraq.janna.activity.MainActivity;
import com.ishraq.janna.adapter.EventListAdapter;
import com.ishraq.janna.component.ExpandableHeightListView;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.Session;
import com.ishraq.janna.service.EventService;
import com.ishraq.janna.viewholder.EventItemViewHolder;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;
import com.ishraq.janna.viewholder.RecyclerLoadMoreViewHolder;
import com.ishraq.janna.webservice.CommonRequest;
import com.ishraq.janna.webservice.EventWebService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ahmed on 3/5/2016.
 */
public class EventDetailsFragment extends MainCommonFragment {

    private EventWebService eventWebService;
    private EventService eventService;
    private RecyclerView recyclerView;

    private Integer eventId;
    private Event event;
    private EventItemAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventService = new EventService(getMainActivity());
        eventWebService = getWebService(EventWebService.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            eventId = bundle.getInt("eventId");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            }

            @Override
            public void onLoadMore() {
            }
        });

        recyclerView.setPadding(0, (int) getResources().getDimension(R.dimen.common_margin_padding_medium), 0, 0);

        initData();

        return view;
    }


    private void initData() {
        EventDetailsRequest request = new EventDetailsRequest();
        request.execute();
    }

    private class EventDetailsRequest implements CommonRequest {
        @Override
        public void execute() {
            getMainActivity().startLoadingAnimator();
            eventWebService.getEvent(eventId).enqueue(new RequestCallback<List<Event>>(this) {
                @Override
                public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                    event = response.body().get(0);
                    getMainActivity().getToolbar().setTitle(event.getEventNameAra());

                    adapter = new EventItemAdapter(event);
                    recyclerView.setAdapter(adapter);

                    getMainActivity().stopLoadingAnimator();
                }
            });
        }
    }




    ///////////////////////////////////// Item //////////////////////////////////////////////////
    class EventItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Event event;

        /*
        * Type header : layout of toolbar which will be empty
        * Type Item : Recipe Item
        * */

        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public EventItemAdapter(Event event) {
            this.event = event;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.fragment_event_details, parent, false);
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
            }
            else {
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

        private TextView nameTextView, startDateTextView, endDateTextView,
                structureTextView, addressTextView, notesTextView;

        private ExpandableHeightListView sessionListView;

        public ItemViewHolder(View parent, Event event) {
            super(parent);
            this.event = event;

            nameTextView = (TextView) parent.findViewById(R.id.nameTextView);
            startDateTextView = (TextView) parent.findViewById(R.id.startDateTextView);
            endDateTextView = (TextView) parent.findViewById(R.id.endDateTextView);
            structureTextView = (TextView) parent.findViewById(R.id.structureTextView);
            addressTextView = (TextView) parent.findViewById(R.id.addressTextView);
            notesTextView = (TextView) parent.findViewById(R.id.notesTextView);

            sessionListView = (ExpandableHeightListView) parent.findViewById(R.id.sessionListView);
            sessionListView.setExpanded(true);
        }

        public void setEventItem(){
            nameTextView.setText(event.getEventNameAra());
            startDateTextView.setText(event.getEventStartDate());
            endDateTextView.setText(event.getEventEndDate());
            structureTextView.setText(event.getEventStructure());
            addressTextView.setText(event.getEventAddress());
            notesTextView.setText(event.getNotes());

            SessionListAdapter sessionListAdapter = new SessionListAdapter(JannaApp.getContext(), R.layout.row_session, new ArrayList<Session>(event.getSess()));
            sessionListView.setAdapter(sessionListAdapter);

            sessionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });


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
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(JannaApp.getContext());
                row = inflater.inflate(layoutResourceId, parent, false);
            }

            TextView nameTextView = (TextView)row.findViewById(R.id.nameTextView);
            nameTextView.setText(sessions.get(position).getEventsSessionCode()+"");

            return row;
        }
    }
}
