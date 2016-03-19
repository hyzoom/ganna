package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.News;
import com.ishraq.janna.model.User;
import com.ishraq.janna.service.NewsService;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;
import com.ishraq.janna.webservice.CommonRequest;
import com.ishraq.janna.webservice.EventWebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ahmed on 3/19/2016.
 */
public class NewsDetailsFragment extends MainCommonFragment {
    private EventWebService eventWebService;
    private NewsService newsService;

    private Integer newsId;
    private News news;
    private RecyclerView recyclerView;

    private NewsItemAdapter adapter;

    private boolean refresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventWebService = getWebService(EventWebService.class);
        newsService = new NewsService(getMainActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            newsId = bundle.getInt("newsId");
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        return view;
    }

    private void initData() {
        news = newsService.getNews(newsId);
        if (refresh) {
            ListNewsesRequest request = new ListNewsesRequest();
            request.execute();
        } else {
            adapter = new NewsItemAdapter(news);
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


    /////////////////////////////////////// Request /////////////////////////////////////


    private class ListNewsesRequest implements CommonRequest {
        @Override
        public void execute() {
            eventWebService.getAllNewses(1).enqueue(new RequestCallback<List<News>>(this) {
                @Override
                public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                    List<News> newses = response.body();

                    if (newses.size() > 0) {
                        newsService.saveNewses(newses);
                    }

                    news = newsService.getNews(newsId);

                    try {
                        adapter = new NewsItemAdapter(news);
                        recyclerView.setAdapter(adapter);

                        refresh = false;
                        getMainActivity().stopLoadingAnimator();
                        getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
                    } catch (Exception e) {
                        Log.i(JannaApp.LOG_TAG, e + "This fragment is finished.");
                    }

                }
            });

        }
    }



    ///////////////////////////////////// Adapter //////////////////////////////////////////////////
    class NewsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private News news;

        /*
        * Type header : layout of toolbar which will be empty
        * Type Item : Recipe Item
        * */

        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public NewsItemAdapter(News news) {
            getMainActivity().getToolbar().setTitle(news.getEventsNewsNameLat());
            this.news = news;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.fragment_news_details, parent, false);
                return new ItemViewHolder(view, news);
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
                holder.setNewsItem();
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
            return news == null ? 0 : 1;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private News news;

        private ImageView newsImageView;
        private TextView nameTextView, dateTextView, detailsTextView;

        public ItemViewHolder(View parent, News news) {
            super(parent);
            this.news = news;

            newsImageView = (ImageView) parent.findViewById(R.id.newsImageView);

            nameTextView = (TextView) parent.findViewById(R.id.nameTextView);
            dateTextView = (TextView) parent.findViewById(R.id.dateTextView);
            detailsTextView = (TextView) parent.findViewById(R.id.detailsTextView);

        }

        public void setNewsItem() {
            newsService.displayImage(news.getImage(), newsImageView);

            nameTextView.setText(news.getEventsNewsNameLat());
            dateTextView.setText(news.getEventsNewsDate());
            detailsTextView.setText(news.getEventsNewsDetails());
        }

    }
}
