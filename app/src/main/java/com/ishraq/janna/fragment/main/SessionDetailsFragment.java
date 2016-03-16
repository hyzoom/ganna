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
import android.widget.TextView;
import android.widget.Toast;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
import com.ishraq.janna.component.ExpandableHeightListView;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Lecture;
import com.ishraq.janna.model.Session;
import com.ishraq.janna.service.EventService;
import com.ishraq.janna.service.SessionService;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;
import com.ishraq.janna.webservice.CommonRequest;
import com.ishraq.janna.webservice.EventWebService;
import com.ishraq.janna.webservice.SessionWebService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ahmed on 3/5/2016.
 */
public class SessionDetailsFragment extends MainCommonFragment {
    private SessionWebService sessionWebService;
    private SessionService sessionService;

    private Integer eventId, sessionId;

    private RecyclerView recyclerView;
    private SessionItemAdapter adapter;

    private Session session;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionService = new SessionService(getMainActivity());
        sessionWebService = getWebService(SessionWebService.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            eventId = bundle.getInt("eventId");
            sessionId = bundle.getInt("sessionId");
        }

    }

    @Override
    public void refreshContent() {
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        session = sessionService.getSession(sessionId);
        adapter = new SessionItemAdapter(session);
        recyclerView.setAdapter(adapter);

        getMainActivity().stopLoadingAnimator();
        getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
//        SessionDetailsRequest request = new SessionDetailsRequest();
//        request.execute();
    }

    private class SessionDetailsRequest implements CommonRequest {
        @Override
        public void execute() {
            getMainActivity().startLoadingAnimator();
            sessionWebService.getSession(eventId, sessionId).enqueue(new RequestCallback<List<Session>>(this) {
                @Override
                public void onResponse(Call<List<Session>> call, Response<List<Session>> response) {
                    session = response.body().get(0);

                    adapter = new SessionItemAdapter(session);
                    recyclerView.setAdapter(adapter);

                    getMainActivity().stopLoadingAnimator();
                }
            });
        }
    }


    /////////////////////////////////////////// Adapter //////////////////////////////////////////
    class SessionItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Session session;

        /*
        * Type header : layout of toolbar which will be empty
        * Type Item : Session Item
        * */

        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public SessionItemAdapter(Session session) {
            getMainActivity().getToolbar().setTitle(session.getEventsSessionNameAra());
            this.session = session;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.fragment_session_details, parent, false);
                return new ItemViewHolder(view, session);
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
            return session == null ? 0 : 1;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private Session session;

        private TextView nameTextView;
        private ExpandableHeightListView lectureListView;

        public ItemViewHolder(View parent, Session session) {
            super(parent);
            this.session = session;

            nameTextView = (TextView) parent.findViewById(R.id.nameTextView);

            lectureListView = (ExpandableHeightListView) parent.findViewById(R.id.lectureListView);
            lectureListView.setExpanded(true);
        }

        public void setEventItem() {
            nameTextView.setText(session.getEventsSessionNameAra() + "");

            final List<Lecture> lectures = new ArrayList<Lecture>(session.getLect());

            LectureListAdapter lectureListAdapter = new LectureListAdapter(JannaApp.getContext(), R.layout.row_session, lectures);
            lectureListView.setAdapter(lectureListAdapter);

            lectureListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Fragment lectureDetailsFragment = new LectureDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("lectureId", lectures.get(position).getEventsLectureCode());
                    lectureDetailsFragment.setArguments(bundle);
                    getMainActivity().addFragment(lectureDetailsFragment, true, null);
                }
            });
        }
    }

    class LectureListAdapter extends ArrayAdapter<Lecture> {
        private List<Lecture> lectures;
        private Context context;
        private int layoutResourceId;


        public LectureListAdapter(Context context, int layoutResourceId, List<Lecture> lectures) {
            super(context, layoutResourceId, lectures);
            this.lectures = lectures;
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
            nameTextView.setText(lectures.get(position).getEventsLectureCode() + "");
            return row;
        }
    }

}
