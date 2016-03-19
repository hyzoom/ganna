package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
import com.ishraq.janna.component.ExpandableHeightListView;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.EventSponsor;
import com.ishraq.janna.model.Rule;
import com.ishraq.janna.service.EventService;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 3/15/2016.
 */
public class SponsorFragment extends MainCommonFragment {
    private EventService eventService;

    private RecyclerView recyclerView;

    private Event event;
    private EventSponsorsAdapter adapter;

    LinearLayout li1, li2, li3, li4, li5, li6, li7, li8;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventService = new EventService(getMainActivity());
    }

    @Override
    public void refreshContent() {
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getMainActivity().getToolbar().setTitle(getResources().getString(R.string.sponsor_title));
        showToolbar();
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        Toast.makeText(getActivity(), "يمكنك زيارة موقع الراعي بالضغط على الصورة", Toast.LENGTH_LONG).show();
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
//        visitSites();

        return view;
    }

    private void initData() {
        event = eventService.getEvent(1);
        adapter = new EventSponsorsAdapter(event);
        recyclerView.setAdapter(adapter);
        getMainActivity().stopLoadingAnimator();
        getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
    }

    public void visitSites() {
        li1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment webFragment = new WebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("webViewNumber", 6);
                webFragment.setArguments(bundle);
                getMainActivity().addFragment(webFragment, true, null);
            }
        });

        li2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment webFragment = new WebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("webViewNumber", 7);
                webFragment.setArguments(bundle);
                getMainActivity().addFragment(webFragment, true, null);
            }
        });

        li3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment webFragment = new WebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("webViewNumber", 8);
                webFragment.setArguments(bundle);
                getMainActivity().addFragment(webFragment, true, null);
            }
        });

        li4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment webFragment = new WebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("webViewNumber", 9);
                webFragment.setArguments(bundle);
                getMainActivity().addFragment(webFragment, true, null);
            }
        });

        li5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment webFragment = new WebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("webViewNumber", 10);
                webFragment.setArguments(bundle);
                getMainActivity().addFragment(webFragment, true, null);
            }
        });

        li6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment webFragment = new WebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("webViewNumber", 11);
                webFragment.setArguments(bundle);
                getMainActivity().addFragment(webFragment, true, null);
            }
        });

        li7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment webFragment = new WebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("webViewNumber", 12);
                webFragment.setArguments(bundle);
                getMainActivity().addFragment(webFragment, true, null);
            }
        });

        li8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment webFragment = new WebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("webViewNumber", 13);
                webFragment.setArguments(bundle);
                getMainActivity().addFragment(webFragment, true, null);
            }
        });
    }


    ///////////////////////////////////// Adapter //////////////////////////////////////////////////
    class EventSponsorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Event event;

        /*
        * Type header : layout of toolbar which will be empty
        * Type Item : Recipe Item
        * */

        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public EventSponsorsAdapter(Event event) {
            this.event = event;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.fragment_sponsor, parent, false);
                li1 = (LinearLayout) view.findViewById(R.id.li1);
                li2 = (LinearLayout) view.findViewById(R.id.li2);
                li3 = (LinearLayout) view.findViewById(R.id.li3);
                li4 = (LinearLayout) view.findViewById(R.id.li4);
                li5 = (LinearLayout) view.findViewById(R.id.li5);
                li6 = (LinearLayout) view.findViewById(R.id.li6);
                li7 = (LinearLayout) view.findViewById(R.id.li7);
                li8 = (LinearLayout) view.findViewById(R.id.li8);
                visitSites();
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

        private ExpandableHeightListView sponsorListView;

        public ItemViewHolder(View parent, Event event) {
            super(parent);
            this.event = event;

            sponsorListView = (ExpandableHeightListView) parent.findViewById(R.id.sponsorListView);
            sponsorListView.setExpanded(true);

        }

        public void setEventItem() {

            final List<EventSponsor> eventSponsors = new ArrayList<EventSponsor>(event.getEventSponsors());
            SponsorListAdapter sponsorListAdapter = new SponsorListAdapter(JannaApp.getContext(), R.layout.row_sponsor, eventSponsors);
            sponsorListView.setAdapter(sponsorListAdapter);

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
}
