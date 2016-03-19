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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
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
    private boolean refresh = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventService = new EventService(getMainActivity());
        eventWebService = getWebService(EventWebService.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            eventId = bundle.getInt("eventId");
        } else {
            eventId = 1;
        }

    }

    @Override
    public void refreshContent() {
        refresh = true;
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        showToolbar();
        getMainActivity().startLoadingAnimator();
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

        recyclerView.setPadding(0, (int) getResources().getDimension(R.dimen.common_margin_padding_medium), 0, 0);

        initData();

        return view;
    }


    private void initData() {
        event = eventService.getEvent(eventId);
        if (event == null || refresh) {
            EventDetailsRequest request = new EventDetailsRequest();
            request.execute();
        } else {
            adapter = new EventItemAdapter(event);
            recyclerView.setAdapter(adapter);
            getMainActivity().stopLoadingAnimator();
            getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
        }

    }

    private class EventDetailsRequest implements CommonRequest {
        @Override
        public void execute() {
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
                    refresh = false;
                    getMainActivity().stopLoadingAnimator();
                    getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
                }

                @Override
                public void onFailure(Call<List<Event>> call, Throwable t) {
                    event = eventService.getEvent(eventId);
                    if (event == null) {
                        super.onFailure(call, t);
                        return;
                    }
                    if (event.getEventSponsors().size() == 0 && event.getRules().size() == 0 && event.getSess().size() == 0) {
                        super.onFailure(call, t);
                        return;
                    } else {
                        adapter = new EventItemAdapter(event);
                        recyclerView.setAdapter(adapter);

                        getMainActivity().stopLoadingAnimator();
                    }
                    refresh = false;
                    getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
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

        private Button addQuestionButton, rulesButton, sponsorButton, sessionButton,
                faceButton, twitterButton, linkedButton, siteButton, attendeesButton, newsButton, locationButton;

        public ItemViewHolder(View parent, Event event) {
            super(parent);
            this.event = event;

            nameTextView = (TextView) parent.findViewById(R.id.nameTextView);
            startDateTextView = (TextView) parent.findViewById(R.id.startDateTextView);
            endDateTextView = (TextView) parent.findViewById(R.id.endDateTextView);
            structureTextView = (TextView) parent.findViewById(R.id.structureTextView);
            addressTextView = (TextView) parent.findViewById(R.id.addressTextView);
            notesTextView = (TextView) parent.findViewById(R.id.notesTextView);

            rulesButton = (Button) parent.findViewById(R.id.rulesButton);
            sponsorButton = (Button) parent.findViewById(R.id.sponsorButton);
            sessionButton = (Button) parent.findViewById(R.id.sessionButton);

            newsButton = (Button) parent.findViewById(R.id.newsButton);
            attendeesButton = (Button) parent.findViewById(R.id.attendeesButton);
            faceButton = (Button) parent.findViewById(R.id.faceButton);
            twitterButton = (Button) parent.findViewById(R.id.twitterButton);
            linkedButton = (Button) parent.findViewById(R.id.linkedButton);
            siteButton = (Button) parent.findViewById(R.id.siteButton);
            locationButton = (Button) parent.findViewById(R.id.locationButton);

            addQuestionButton = (Button) parent.findViewById(R.id.addQuestionButton);
        }

        public void setEventItem() {
            nameTextView.setText(event.getEventNameAra());
            startDateTextView.setText(event.getEventStartDate());
            endDateTextView.setText(event.getEventEndDate());
            structureTextView.setText(event.getEventStructure());
            addressTextView.setText(event.getEventAddress());
            notesTextView.setText(event.getNotes());

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

            newsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMainActivity().addFragment(new EventNewsFragment(), true, null);
                }
            });

            attendeesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMainActivity().addFragment(new AttendantFragment(), true, null);
                }
            });
            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment webFragment = new WebViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("webViewNumber", 1);
                    webFragment.setArguments(bundle);
                    getMainActivity().addFragment(webFragment, true, null);
                }
            });
            siteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment webFragment = new WebViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("webViewNumber", 2);
                    webFragment.setArguments(bundle);
                    getMainActivity().addFragment(webFragment, true, null);
                }
            });

            faceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment webFragment = new WebViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("webViewNumber", 3);
                    webFragment.setArguments(bundle);
                    getMainActivity().addFragment(webFragment, true, null);
                }
            });

            twitterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment webFragment = new WebViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("webViewNumber", 4);
                    webFragment.setArguments(bundle);
                    getMainActivity().addFragment(webFragment, true, null);
                }
            });

            linkedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment webFragment = new WebViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("webViewNumber", 5);
                    webFragment.setArguments(bundle);
                    getMainActivity().addFragment(webFragment, true, null);
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
