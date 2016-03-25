package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Survey;
import com.ishraq.janna.service.SettingsService;
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
    private SettingsService settingsService;
    private SurveyWebService surveyWebService;
    private SurveyService surveyService;

    private Integer eventId;
    private List<Survey> surveys;

    private RecyclerView recyclerView;
    private SurveyAdapter adapter;

    private Button submitButton;

    private boolean refresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surveyWebService= getWebService(SurveyWebService.class);
        surveyService = new SurveyService(getMainActivity());
        settingsService = new SettingsService(getMainActivity());

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


        submitButton = (Button) view.findViewById(R.id.submitButton);
        submitButton.setText(getResources().getString(R.string.survey_button_text));
        submitButton.setVisibility(View.VISIBLE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (surveys != null && surveys.size() != 0) {
                    if (checkAllQuestionsSolved()) {

                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.all_survey_questions_not_solved),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }


    private void initData() {
        surveys = surveyService.getSurveys();

        if (surveys == null || surveys.size() == 0 || refresh) {
            ListSurveyQuestionsRequest request = new ListSurveyQuestionsRequest();
            request.execute();
        } else {
            adapter = new SurveyAdapter();
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


    private boolean checkAllQuestionsSolved() {
        boolean result = true;
        for (int i = 0; i < surveys.size(); i++) {
            if (surveys.get(i).getAnswerId() == null) {
                result = false;
                break;
            }
        }
        return result;
    }


    /////////////////////////////////////////////////////////////////////////////////////

    private class ListSurveyQuestionsRequest implements CommonRequest {
        @Override
        public void execute() {
            surveyWebService.getAllSurveyQuestions().enqueue(new RequestCallback<List<Survey>>(this) {
                @Override
                public void onResponse(Call<List<Survey>> call, Response<List<Survey>> response) {
                    surveys = response.body();
                    surveyService.saveSurveys(surveys);

                    try {
                        adapter = new SurveyAdapter();
                        recyclerView.setAdapter(adapter);
                        getMainActivity().stopLoadingAnimator();
                        getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
                        refresh = false;
                    } catch (Exception e) {
                        Log.i(JannaApp.LOG_TAG, e + "This fragment is finished.");
                    }
                }
            });
        }
    }

    private class SendSurveyRequest implements CommonRequest {

        @Override
        public void execute() {
            getMainActivity().startLoadingAnimator();
            Survey survey = surveys.get(0);
            surveyWebService.sendAllSurveyQuestions(1,
                    settingsService.getSettings().getLoggedInUser().getId(),
                    survey.getSurveyCode(),
                    survey.getAnswerId()).enqueue(new RequestCallback<List<Survey>>(this) {

                @Override
                public void onResponse(Call<List<Survey>> call, Response<List<Survey>> response) {
                    surveys.remove(0);
                    if (surveys.size() > 0) {
                        execute();
                    } else {
                        getMainActivity().stopLoadingAnimator();
                    }
                }
            });
        }
    }


    ///////////////////////////////////// Adapter //////////////////////////////////////////////////
    class SurveyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        /*
        * Type header : layout of toolbar which will be empty
        * Type Item : Recipe Item
        * */

        private static final int TYPE_SUBMIT = 3;
        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public SurveyAdapter() {
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.row_survey, parent, false);
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
            return surveys == null ? 0 : surveys.size();
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView surveyNameTextView;
        private RadioGroup radioGroup;
        private Button button;

        public ItemViewHolder(View parent) {
            super(parent);

            surveyNameTextView = (TextView) parent.findViewById(R.id.surveyNameTextView);
            radioGroup = (RadioGroup) parent.findViewById(R.id.radioGroup);
            button = (Button) parent.findViewById(R.id.button);

        }

        public void setSurveyItem(final int position) {
            final Survey survey = surveys.get(position);
            surveyNameTextView.setText(survey.getSurveyNameAra());

            if (position == surveys.size()) {
                button.setVisibility(View.VISIBLE);
            }

            if (survey.getAnswers() == null || survey.getAnswers().size() ==0)
                return;

            for (int i = 0; i < survey.getAnswers().size(); i++) {
                RadioButton radioButton = new RadioButton(JannaApp.getContext());

                radioGroup.addView(radioButton);
                radioButton.setText(survey.getAnswers().get(i).getAnswerName());
                radioButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                final int finalI = i;
                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            surveys.get(position).setAnswerId(survey.getAnswers().get(finalI).getAnswerCode());
                        }
                    }
                });

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    radioButton.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.common_title)));
                } else{
                    radioGroup.setBackgroundColor(getResources().getColor(R.color.common_title));
                }

            }
        }

    }

}
