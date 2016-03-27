package com.ishraq.janna.fragment.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
import com.ishraq.janna.component.ExpandableHeightListView;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Event;
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
 * Created by Ahmed on 3/15/2016.
 */
public class EventRulesFragment extends MainCommonFragment implements View.OnClickListener {
    private EventService eventService;
    private EventWebService eventWebService;

    private RecyclerView recyclerView;

    private Button addQuestionButton, eventNewsButton, addBookingButton;

    private Event event;
    private EventRulesAdapter adapter;

    private Dialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventService = new EventService(getMainActivity());
        eventWebService = getWebService(EventWebService.class);
    }

    @Override
    public void refreshContent() {
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getMainActivity().getToolbar().setTitle(getResources().getString(R.string.rule_title));
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

        recyclerView.setPadding(0, (int) getResources().getDimension(R.dimen.common_margin_padding_medium), 0, 0);

        initData();

        return view;
    }

    private void initData() {
        event = eventService.getEvent(1);
        adapter = new EventRulesAdapter(event);
        recyclerView.setAdapter(adapter);
        getMainActivity().stopLoadingAnimator();
        getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addQuestionButton:
                showAddQuestionDialog();
                break;

            case R.id.eventNewsButton:
                getMainActivity().addFragment(new NewsFragment(), true, null);
                break;

            case R.id.addBookingButton:
                getMainActivity().addFragment(new BookingFragment(), true, null);
                break;
        }
    }


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


    private class EventQuestionRequest implements CommonRequest {
        private String question;

        public EventQuestionRequest(String question) {
            this.question = question;
        }

        @Override
        public void execute() {
            getMainActivity().startLoadingAnimator();
            SettingsService settingsService = new SettingsService(getMainActivity());
            eventWebService.sendEventQuestion(1,
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
    class EventRulesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Event event;

        /*
        * Type header : layout of toolbar which will be empty
        * Type Item : Recipe Item
        * */

        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public EventRulesAdapter(Event event) {
            this.event = event;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.fragment_rule, parent, false);
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

        private ExpandableHeightListView rulesListView;

        public ItemViewHolder(View parent, Event event) {
            super(parent);
            this.event = event;

            addQuestionButton = (Button) parent.findViewById(R.id.addQuestionButton);
            eventNewsButton = (Button) parent.findViewById(R.id.eventNewsButton);
            addBookingButton = (Button) parent.findViewById(R.id.addBookingButton);

            addQuestionButton.setOnClickListener(EventRulesFragment.this);
            eventNewsButton.setOnClickListener(EventRulesFragment.this);
            addBookingButton.setOnClickListener(EventRulesFragment.this);


            rulesListView = (ExpandableHeightListView) parent.findViewById(R.id.rulesListView);
            rulesListView.setExpanded(true);

        }

        public void setEventItem() {

            final List<Rule> rules = new ArrayList<Rule>(event.getRules());

            RuleListAdapter ruleListAdapter = new RuleListAdapter(JannaApp.getContext(), R.layout.row_rule, rules);
            rulesListView.setAdapter(ruleListAdapter);

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
}
