package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.os.Bundle;
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
import com.ishraq.janna.model.LectureInstructor;
import com.ishraq.janna.service.LectureService;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 3/11/2016.
 */
public class LectureDetailsFragment extends MainCommonFragment {
    private LectureService lectureService;

    private Lecture lecture;
    private Integer lectureId;
    private RecyclerView recyclerView;
    private LectureItemAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lectureService = new LectureService(getMainActivity());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            lectureId = bundle.getInt("lectureId");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(getMainActivity(), "دي صفحة المحاضرين اللى في المحاضرة", Toast.LENGTH_LONG).show();
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
        lecture = lectureService.getLecture(lectureId);
        adapter = new LectureItemAdapter(lecture);
        recyclerView.setAdapter(adapter);

        getMainActivity().stopLoadingAnimator();
    }



    /////////////////////////////////////////// Adapter //////////////////////////////////////////
    class LectureItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Lecture lecture;

        /*
        * Type header : layout of toolbar which will be empty
        * Type Item : Session Item
        * */

        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public LectureItemAdapter(Lecture lecture) {
            getMainActivity().getToolbar().setTitle(lecture.getEventsLectureNameAra());
            this.lecture = lecture;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.fragment_lecture_details, parent, false);
                return new ItemViewHolder(view, lecture);
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
            return lecture == null ? 0 : 1;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private Lecture lecture;

        private TextView nameTextView;
        private ExpandableHeightListView lecturersListView;

        public ItemViewHolder(View parent, Lecture lecture) {
            super(parent);
            this.lecture = lecture;

            nameTextView = (TextView) parent.findViewById(R.id.nameTextView);

            lecturersListView = (ExpandableHeightListView) parent.findViewById(R.id.lecturersListView);
            lecturersListView.setExpanded(true);
        }

        public void setEventItem() {
            nameTextView.setText(lecture.getEventsLectureCode() + "");

            List<LectureInstructor> lectureInstructors = new ArrayList<LectureInstructor>(lecture.getLectureInstructors());

            LecturerListAdapter lecturerListAdapter = new LecturerListAdapter(JannaApp.getContext(), R.layout.row_session, lectureInstructors);
            lecturersListView.setAdapter(lecturerListAdapter);

            lecturersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                }
            });
        }
    }

    class LecturerListAdapter extends ArrayAdapter<LectureInstructor> {
        private List<LectureInstructor> lectureInstructors;
        private Context context;
        private int layoutResourceId;


        public LecturerListAdapter(Context context, int layoutResourceId, List<LectureInstructor> lectureInstructors) {
            super(context, layoutResourceId, lectureInstructors);
            this.lectureInstructors = lectureInstructors;
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
            nameTextView.setText(lectureInstructors.get(position).getInstructor().getInstructorCode() + "");
            return row;
        }
    }

}
