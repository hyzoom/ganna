package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
import com.ishraq.janna.component.ExpandableHeightGridView;
import com.ishraq.janna.component.ExpandableHeightListView;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Guest;
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

        private TextView nameTextView, chairTextView, lectureTextView;
        private ExpandableHeightListView lectureListView;
        private ExpandableHeightGridView chairGridView;

        private View chairmanLayout, chairmanLine, lectureLayout, lectureLine;

        public ItemViewHolder(View parent, Session session) {
            super(parent);
            this.session = session;

            nameTextView = (TextView) parent.findViewById(R.id.nameTextView);
            chairTextView = (TextView) parent.findViewById(R.id.chairTextView);
            lectureTextView = (TextView) parent.findViewById(R.id.lectureTextView);

            chairGridView = (ExpandableHeightGridView) parent.findViewById(R.id.chairGridView);
            chairGridView.setExpanded(true);

            lectureListView = (ExpandableHeightListView) parent.findViewById(R.id.lectureListView);
            lectureListView.setExpanded(true);

            chairmanLayout = parent.findViewById(R.id.chairmanLayout);
            chairmanLine = parent.findViewById(R.id.chairmanLine);
            lectureLayout = parent.findViewById(R.id.lectureLayout);
            lectureLine = parent.findViewById(R.id.lectureLine);
        }

        public void setEventItem() {
            nameTextView.setText(session.getEventsSessionNameAra() + "");

            final List<Lecture> lectures = new ArrayList<Lecture>(session.getLect());
            final List<Guest> guests = new ArrayList<Guest>(lectures.get(0).getGst());

            if (guests.size() == 0) {
                chairmanLayout.setVisibility(View.GONE);
                chairmanLine.setVisibility(View.GONE);
            }
            if (lectures.size() == 0) {
                lectureLayout.setVisibility(View.GONE);
                lectureLine.setVisibility(View.GONE);
            }

            GuestListAdapter guestListAdapter = new GuestListAdapter(JannaApp.getContext(), R.layout.row_chairman, guests);
            chairGridView.setAdapter(guestListAdapter);

            LectureListAdapter lectureListAdapter = new LectureListAdapter(JannaApp.getContext(), R.layout.row_lecture, lectures);
            lectureListView.setAdapter(lectureListAdapter);

//            lectureListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Fragment lectureDetailsFragment = new LectureDetailsFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("lectureId", lectures.get(position).getEventsLectureCode());
//                    lectureDetailsFragment.setArguments(bundle);
//                    getMainActivity().addFragment(lectureDetailsFragment, true, null);
//                }
//            });
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(JannaApp.getContext());
                row = inflater.inflate(layoutResourceId, parent, false);
            }

            ImageView imageView = (ImageView) row.findViewById(R.id.imageView);
            final Button playVideoButton = (Button) row.findViewById(R.id.playVideoButton);
            TextView nameTextView = (TextView) row.findViewById(R.id.nameTextView);
            TextView lecturerNameTextView = (TextView) row.findViewById(R.id.lecturerNameTextView);
            TextView dateTextView = (TextView) row.findViewById(R.id.dateTextView);

            nameTextView.setText(lectures.get(position).getEventsLectureNameAra());
            lecturerNameTextView.setText(lectures.get(position).getInstructorName());
            dateTextView.setText(lectures.get(position).getEventsLectureDate());

            if ((lectures.get(position).getInstructorName()).equals("0"))
                lecturerNameTextView.setVisibility(View.GONE);

            if ((lectures.get(position).getInstructorName()).startsWith("Ezzat")
                    || (lectures.get(position).getInstructorName()).startsWith("Aym")
                    || (lectures.get(position).getInstructorName()).startsWith("Az"))
                lecturerNameTextView.setVisibility(View.VISIBLE);


            if ((lectures.get(position).getEventsLectureDate()).equals("0"))
                dateTextView.setVisibility(View.GONE);

//            if (!lectures.get(position).getNotes().equals("0")) {
//                playVideoButton.setVisibility(View.VISIBLE);
//            } else {
//                playVideoButton.setVisibility(View.GONE);
//            }
            if (lectures.get(position).getEventsLectureCode() == 1
                    || lectures.get(position).getEventsLectureCode() == 3
                    || lectures.get(position).getEventsLectureCode() == 30
                    || lectures.get(position).getEventsLectureCode() == 11
                    || lectures.get(position).getEventsLectureCode() == 13
                    || lectures.get(position).getEventsLectureCode() == 15
                    || lectures.get(position).getEventsLectureCode() == 19
                    || lectures.get(position).getEventsLectureCode() == 20
                    || lectures.get(position).getEventsLectureCode() == 21
                    || lectures.get(position).getEventsLectureCode() == 25
                    || lectures.get(position).getEventsLectureCode() == 27
                    ) {
                playVideoButton.setVisibility(View.VISIBLE);
            } else {
                playVideoButton.setVisibility(View.GONE);
            }

//            if (lectures.get(position).getEventsLectureCode() == 35) {
//                playVideoButton.setVisibility(View.GONE);
//            }

            playVideoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = null;
                    Intent intent;
                    switch (lectures.get(position).getEventsLectureCode()) {

                        case 1:
//                            uri = Uri.parse("https://www.youtube.com/watch?v=CGfsMofEWlM");
                            uri = Uri.parse("https://www.youtube.com/watch?v=PXm3lUD97Bg");
                            break;

                        case 3:
                            uri = Uri.parse("https://www.youtube.com/watch?v=NfsgYtymzuc");
                            break;

                        case 30:
                            uri = Uri.parse("https://www.youtube.com/watch?v=EnG34OaFmZk");
                            break;

                        case 11:
                            uri = Uri.parse("https://www.youtube.com/watch?v=rZoVCPLmlVc");
                            break;

                        case 13:
                            uri = Uri.parse("https://www.youtube.com/watch?v=-P5Uyn8k3eg");
                            break;

                        case 15:
                            uri = Uri.parse("https://www.youtube.com/watch?v=GRQGJ6_H3J8");
                            break;

                        case 19:
                            uri = Uri.parse("https://www.youtube.com/watch?v=3J_zrJ1pelE");
                            break;

                        case 20:
                            uri = Uri.parse("https://www.youtube.com/watch?v=neCqvKt_sp0");
                            break;

                        case 21:
                            uri = Uri.parse("https://www.youtube.com/watch?v=yC8Vl4xImb0");
                            break;

                        case 25:
                            uri = Uri.parse("https://www.youtube.com/watch?v=yoKmZ_m8YHg");
                            break;

                        case 27:
                            uri = Uri.parse("https://www.youtube.com/watch?v=Ec0L8Ik5UWU");
                            break;
                    }

                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    getMainActivity().startActivity(intent);
                }
            });

            String url = (lectures.get(position).getImage())
                    .replace("http://ganah.zagel1.com/", "http://nkamel-001-site1.gtempurl.com/");

            if (!url.endsWith("/0"))
                sessionService.displayImage(url, imageView);

            return row;
        }
    }

    class GuestListAdapter extends ArrayAdapter<Guest> {
        private List<Guest> guests;
        private Context context;
        private int layoutResourceId;


        public GuestListAdapter(Context context, int layoutResourceId, List<Guest> guests) {
            super(context, layoutResourceId, guests);
            this.guests = guests;
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

            ImageView imageView = (ImageView) row.findViewById(R.id.imageView);
            TextView nameTextView = (TextView) row.findViewById(R.id.nameTextView);

            nameTextView.setText(guests.get(position).getGuestsCodeNameAra());

            String url = (guests.get(position).getGuestsImageUrl())
                    .replace("http://ganah.zagel1.com/", "http://nkamel-001-site1.gtempurl.com/");
            sessionService.displayImage(url, imageView);
            return row;
        }
    }

}
