package com.ishraq.janna.fragment.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
import com.ishraq.janna.component.ExpandableHeightListView;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.EventSponsor;
import com.ishraq.janna.model.Rule;
import com.ishraq.janna.model.Session;
import com.ishraq.janna.service.EventService;
import com.ishraq.janna.service.SettingsService;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;
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

    private Dialog dialog;


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
//        Toast.makeText(getMainActivity(), "دي صفحة  تفاصيل الحدث او ال event", Toast.LENGTH_LONG).show();

//        Toast.makeText(getMainActivity(), "دي صفحة  تفاصيل الحدث او ال event", Toast.LENGTH_LONG).show();

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
                    try {
                        event = response.body().get(0);
                        eventService.saveEvent(event);

                        event = eventService.getEvent(event.getEventCode());

                        adapter = new EventItemAdapter(event);
                        recyclerView.setAdapter(adapter);
                    } catch (Exception ex) {

                    }
                    getMainActivity().stopLoadingAnimator();
                }

                @Override
                public void onFailure(Call<List<Event>> call, Throwable t) {
                    event = eventService.getEvent(eventId);
                    if (event.getEventSponsors().size() == 0 && event.getRules().size() == 0 && event.getSess().size() == 0) {
                        super.onFailure(call, t);
                    } else {
                        adapter = new EventItemAdapter(event);
                        recyclerView.setAdapter(adapter);

                        getMainActivity().stopLoadingAnimator();
                    }
                }
            });
        }
    }

    private class EventQuestionRequest implements CommonRequest {
        private String question;

        public EventQuestionRequest(String question) {
            this.question = question;
        }

        @Override
        public void execute() {
            getMainActivity().startLoadingAnimator();
            SettingsService settingsService = new SettingsService(getMainActivity());
            eventWebService.sendEventQuestion(eventId,
                    settingsService.getSettings().getLoggedInUser().getId(),
                    question).enqueue(new RequestCallback<List<Event.QuestionResult>>(this) {

                @Override
                public void onResponse(Call<List<Event.QuestionResult>> call, Response<List<Event.QuestionResult>> response) {
                    if (response.body().get(0).getQuestionSaved() == 1) {
                        Toast.makeText(getMainActivity(), getResources().getString(R.string.question_send_successfully), Toast.LENGTH_SHORT).show();
                    }

                    getMainActivity().stopLoadingAnimator();
                }
            });
        }
    }


    ///////////////////////////////////// Adapter //////////////////////////////////////////////////
    class EventItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Event event;

        /*
        * Type header : layout of toolbar which will be empty
        * Type Item : Recipe Item
        * */

        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public EventItemAdapter(Event event) {
            getMainActivity().getToolbar().setTitle(event.getEventNameAra());
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

        private TextView nameTextView, startDateTextView, endDateTextView,
                structureTextView, addressTextView, notesTextView;

//        private ExpandableHeightListView sessionListView, rulesListView, sponsorListView;

        private Button addQuestionButton, rulesButton, sponsorButton, sessionButton;

        public ItemViewHolder(View parent, Event event) {
            super(parent);
            this.event = event;

            nameTextView = (TextView) parent.findViewById(R.id.nameTextView);
            startDateTextView = (TextView) parent.findViewById(R.id.startDateTextView);
            endDateTextView = (TextView) parent.findViewById(R.id.endDateTextView);
            structureTextView = (TextView) parent.findViewById(R.id.structureTextView);
            addressTextView = (TextView) parent.findViewById(R.id.addressTextView);
            notesTextView = (TextView) parent.findViewById(R.id.notesTextView);

//            sessionListView = (ExpandableHeightListView) parent.findViewById(R.id.sessionListView);
//            sessionListView.setExpanded(true);
//
//            rulesListView = (ExpandableHeightListView) parent.findViewById(R.id.rulesListView);
//            rulesListView.setExpanded(true);
//
//            sponsorListView = (ExpandableHeightListView) parent.findViewById(R.id.sponsorListView);
//            sponsorListView.setExpanded(true);

            rulesButton = (Button) parent.findViewById(R.id.rulesButton);
            sponsorButton = (Button) parent.findViewById(R.id.sponsorButton);
            sessionButton = (Button) parent.findViewById(R.id.sessionButton);

            addQuestionButton = (Button) parent.findViewById(R.id.addQuestionButton);
        }

        public void setEventItem() {
            nameTextView.setText(event.getEventNameAra());
            startDateTextView.setText(event.getEventStartDate());
            endDateTextView.setText(event.getEventEndDate());
            structureTextView.setText(event.getEventStructure());
            addressTextView.setText(event.getEventAddress());
            notesTextView.setText(event.getNotes());

//            final List<Session> sessions = new ArrayList<Session>(event.getSess());
//            SessionListAdapter sessionListAdapter = new SessionListAdapter(JannaApp.getContext(), R.layout.row_session, sessions);
//            sessionListView.setAdapter(sessionListAdapter);
//
//            sessionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Fragment sessionDetailsFragment = new SessionDetailsFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("eventId", eventId);
//                    bundle.putInt("sessionId", sessions.get(position).getEventsSessionCode());
//                    sessionDetailsFragment.setArguments(bundle);
//                    getMainActivity().addFragment(sessionDetailsFragment, true, null);
//                }
//            });


//            final List<Rule> rules = new ArrayList<Rule>(event.getRules());
//
//            RuleListAdapter ruleListAdapter = new RuleListAdapter(JannaApp.getContext(), R.layout.row_rule, rules);
//            rulesListView.setAdapter(ruleListAdapter);
//
//            final List<EventSponsor> eventSponsors = new ArrayList<EventSponsor>(event.getEventSponsors());
//            SponsorListAdapter sponsorListAdapter = new SponsorListAdapter(JannaApp.getContext(), R.layout.row_sponsor, eventSponsors);
//            sponsorListView.setAdapter(sponsorListAdapter);
//

            rulesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMainActivity().addFragment(new EventRulesFragment(), true, null);
                }
            });

            sponsorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMainActivity().addFragment(new SponsorFragment(), true, null);
                }
            });

            sessionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMainActivity().addFragment(new SessionFragment(), true, null);
                }
            });

            addQuestionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddQuestionDialog();
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

            TextView nameTextView = (TextView) row.findViewById(R.id.nameTextView);
            nameTextView.setText(sessions.get(position).getEventsSessionNameAra() + "");

            return row;
        }
    }

    class RuleListAdapter extends ArrayAdapter<Rule> {
        private List<Rule> rules;
        private Context context;
        private int layoutResourceId;


        public RuleListAdapter(Context context, int layoutResourceId, List<Rule> rules) {
            super(context, layoutResourceId, rules);
            this.rules = rules;
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

            TextView nameTextView = (TextView) row.findViewById(R.id.nameTextView);
            nameTextView.setText(rules.get(position).getRuleName() + "");

            return row;
        }
    }

    class SponsorListAdapter extends ArrayAdapter<EventSponsor> {
        private List<EventSponsor> sponsors;
        private Context context;
        private int layoutResourceId;


        public SponsorListAdapter(Context context, int layoutResourceId, List<EventSponsor> sponsors) {
            super(context, layoutResourceId, sponsors);
            this.sponsors = sponsors;
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

            TextView nameTextView = (TextView) row.findViewById(R.id.nameTextView);
            nameTextView.setText(sponsors.get(position).getSponsor().getSponserNameAra() + "");

            return row;
        }
    }


    ////////////////////////////////// Methods ///////////////////////////////////////
    // show add question dialog
    private void showAddQuestionDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_add_question, null);

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(dialogView);
        dialog.show();

        final EditText questionEditText = (EditText) dialog.findViewById(R.id.questionEditText);

        Button sendQuestionButton = (Button) dialog.findViewById(R.id.sendQuestionButton);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);

        sendQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionEditText.getText().toString() == null || questionEditText.getText().toString().equals("")) {
                    Toast.makeText(getMainActivity(), getResources().getString(R.string.question_empty), Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    EventQuestionRequest request = new EventQuestionRequest(questionEditText.getText().toString());
                    request.execute();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
}
