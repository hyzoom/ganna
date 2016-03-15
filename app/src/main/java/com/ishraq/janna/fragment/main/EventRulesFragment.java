package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
import com.ishraq.janna.component.ExpandableHeightListView;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.Rule;
import com.ishraq.janna.model.Session;
import com.ishraq.janna.service.EventService;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;
import com.ishraq.janna.webservice.EventWebService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 3/15/2016.
 */
public class EventRulesFragment extends MainCommonFragment {
    private EventService eventService;

    private RecyclerView recyclerView;

    private Event event;
    private EventRulesAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventService = new EventService(getMainActivity());
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
