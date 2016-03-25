package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
import com.ishraq.janna.adapter.EventListAdapter;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.Question;
import com.ishraq.janna.model.Survey;
import com.ishraq.janna.service.EventService;
import com.ishraq.janna.service.QuestionService;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;
import com.ishraq.janna.webservice.CommonRequest;
import com.ishraq.janna.webservice.EventWebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ahmed on 3/11/2016.
 */
public class QuestionFragment extends MainCommonFragment {
    public static final String TAG = "QUESTIONS";

    private EventWebService eventWebService;

    private QuestionService questionService;

    private RecyclerView recyclerView;

    private List<Question> questions;
    private QuestionAdapter adapter;

    private boolean refresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventWebService = getWebService(EventWebService.class);

        questionService = new QuestionService(getMainActivity());
    }

    @Override
    public void refreshContent() {
        refresh = true;
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getMainActivity().getToolbar().setTitle(getResources().getString(R.string.nav_question_title));

        getMainActivity().startLoadingAnimator();
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
//                hideToolbar();
//                hideTabBar();
            }

            @Override
            public void onShow() {
//                showToolbar();
//                showTabBar();
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
        questions = questionService.getQuestions();

        if (questions == null || questions.size() == 0 || refresh) {
            ListEventRequest request = new ListEventRequest();
            request.execute();
        } else {
            adapter = new QuestionAdapter();
            recyclerView.setAdapter(adapter);
            getMainActivity().stopLoadingAnimator();
            getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
        }
    }

    private class ListEventRequest implements CommonRequest {
        @Override
        public void execute() {

            eventWebService.getEventQuestions(1).enqueue(new RequestCallback<List<Question>>(this) {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                    questions = response.body();
                    questionService.saveQuestions(questions);

                    adapter = new QuestionAdapter();
                    recyclerView.setAdapter(adapter);
                    getMainActivity().stopLoadingAnimator();
                    getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
                    refresh = false;
                }
            });
        }
    }


    ///////////////////////////////////// Adapter //////////////////////////////////////////////////
    class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        /*
        * Type header : layout of toolbar which will be empty
        * Type Item : Recipe Item
        * */

        private static final int TYPE_SUBMIT = 3;
        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public QuestionAdapter() {
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.row_question, parent, false);
                return new ItemViewHolder(view);
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
                holder.setSurveyItem(position-1);
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
            return questions == null ? 0 : questions.size();
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView questionTextView, dateTextView;

        public ItemViewHolder(View parent) {
            super(parent);

            questionTextView = (TextView) parent.findViewById(R.id.questionTextView);
            dateTextView = (TextView) parent.findViewById(R.id.dateTextView);
        }

        public void setSurveyItem(int position) {
            Question question = questions.get(position);
            questionTextView.setText(question.getEventQuestion());
            dateTextView.setText(question.getEventQuestionDate());
        }

    }


}