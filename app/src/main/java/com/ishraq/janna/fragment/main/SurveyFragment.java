package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Survey;
import com.ishraq.janna.service.SurveyService;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;
import com.ishraq.janna.webservice.CommonRequest;
import com.ishraq.janna.webservice.SurveyWebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ahmed on 3/19/2016.
 */
public class SurveyFragment extends MainCommonFragment {
    private SurveyWebService surveyWebService;
    private SurveyService surveyService;

    private Integer eventId;
    private List<Survey> surveys;

    private RecyclerView recyclerView;
    private SurveyAdapter adapter;


    private boolean refresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surveyWebService= getWebService(SurveyWebService.class);
        surveyService = new SurveyService(getMainActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            eventId = bundle.getInt("eventId");
        } else {
            eventId = 1;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getMainActivity().getToolbar().setTitle(getResources().getString(R.string.survey_title));
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
        surveys = surveyService.getSurveys();
        if (surveys == null || surveys.size() == 0 || refresh) {
            ListSurveyQuestionsRequest request = new ListSurveyQuestionsRequest();
            request.execute();
        } else {
            adapter = new SurveyAdapter(surveys);
            recyclerView.setAdapter(adapter);
            getMainActivity().stopLoadingAnimator();
            getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
        }

    }


    @Override
    public void refreshContent() {
        refresh = true;
        initData();
    }



    /////////////////////////////////////////////////////////////////////////////////////

    private class ListSurveyQuestionsRequest implements CommonRequest {
        @Override
        public void execute() {
            getMainActivity().startLoadingAnimator();
            surveyWebService.getAllSurveyQuestions().enqueue(new RequestCallback<List<Survey>>(this) {
                @Override
                public void onResponse(Call<List<Survey>> call, Response<List<Survey>> response) {
                    surveys = response.body();

                    surveyService.saveSurveys(surveys);

                    try {
                        adapter = new SurveyAdapter(surveys);
                        recyclerView.setAdapter(adapter);
                        getMainActivity().stopLoadingAnimator();
                        getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
                    } catch (Exception e) {
                        Log.i(JannaApp.LOG_TAG, e + "This fragment is finished.");
                    }
                }

                @Override
                public void onFailure(Call<List<Survey>> call, Throwable t) {
//                    super.onFailure(call, t);


                    Log.w("AhmedLog", t.getMessage());


                }
            });
        }
    }


    ///////////////////////////////////// Adapter //////////////////////////////////////////////////
    class SurveyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Survey> surveys;

        /*
        * Type header : layout of toolbar which will be empty
        * Type Item : Recipe Item
        * */

        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public SurveyAdapter(List<Survey> surveys) {
            this.surveys = surveys;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.row_survey, parent, false);
                return new ItemViewHolder(view, surveys);
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
                holder.setSurveyItem(position);
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
            return surveys == null ? 0 : surveys.size();
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private List<Survey> surveys;

        private TextView surveyNameTextView;

        public ItemViewHolder(View parent, List<Survey> surveys) {
            super(parent);
            this.surveys = surveys;

            surveyNameTextView = (TextView) parent.findViewById(R.id.surveyNameTextView);
        }

        public void setSurveyItem(int position) {
            final Survey survey = surveys.get(position);

            surveyNameTextView.setText(survey.getSurveyNameAra());
        }

    }


}
