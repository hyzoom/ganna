package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishraq.janna.R;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Question;
import com.ishraq.janna.service.EventService;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;
import com.ishraq.janna.webservice.CommonRequest;
import com.ishraq.janna.webservice.EventWebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ahmed on 3/12/2016.
 */
public class EventQuestionFragment extends MainCommonFragment {
    private EventWebService eventWebService;
    private EventService eventService;
    private RecyclerView recyclerView;

    private List<Question> questions;

    private Integer eventId;
    private EventQuestionAdapter adapter;

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
    public void refreshContent() {

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
        EventQuestionsRequest request = new EventQuestionsRequest();
        request.execute();
    }

    private class EventQuestionsRequest implements CommonRequest {
        @Override
        public void execute() {
            getMainActivity().startLoadingAnimator();
            eventWebService.getEventQuestions(eventId).enqueue(new RequestCallback<List<Question>>(this) {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                    questions = response.body();
                    adapter = new EventQuestionAdapter(questions);
                    recyclerView.setAdapter(adapter);
                    getMainActivity().stopLoadingAnimator();
                }

                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {
                    Log.w("AhmedLog", call.request().url().toString());
                }
            });

        }
    }


    ///////////////////////////////////// Adapter //////////////////////////////////////////////////
    class EventQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Question> questions;

        /*
        * Type header : layout of toolbar which will be empty
        * Type Item : Recipe Item
        * */

        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public EventQuestionAdapter(List<Question> questions) {
            this.questions = questions;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.row_question, parent, false);
                return new ItemViewHolder(view, questions);
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
                holder.setQuestionItem(position);
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

        private List<Question> questions;

        private TextView questionTextView;

        public ItemViewHolder(View parent, List<Question> questions) {
            super(parent);
            this.questions = questions;

            questionTextView = (TextView) parent.findViewById(R.id.questionTextView);
        }

        public void setQuestionItem(int position) {
            final Question question = questions.get(position);
            questionTextView.setText(question.getEventQuestion());
        }

    }

}
