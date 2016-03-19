package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishraq.janna.R;
import com.ishraq.janna.activity.MainActivity;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.News;
import com.ishraq.janna.service.NewsService;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;
import com.ishraq.janna.viewholder.RecyclerLoadMoreViewHolder;
import com.ishraq.janna.webservice.CommonRequest;
import com.ishraq.janna.webservice.EventWebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ahmed on 3/18/2016.
 */
public class NewsFragment extends MainCommonFragment {
    private EventWebService eventWebService;
    private NewsService newsService;

    private RecyclerView recyclerView;
    private List<News> newses;
    private NewsListAdapter adapter;

    private boolean refresh = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventWebService = getWebService(EventWebService.class);
        newsService = new NewsService(getMainActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getMainActivity().getToolbar().setTitle(getResources().getString(R.string.newses));
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
        newses = newsService.getNewses();
        if (newses == null || newses.size() == 0 || refresh) {
            ListNewsesRequest request = new ListNewsesRequest();
            request.execute();
        } else {
            adapter = new NewsListAdapter(getMainActivity(), newses, false, null);
            recyclerView.setAdapter(adapter);

            refresh = false;
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
                    newses = response.body();

                    if (newses.size() > 0) {
                        newsService.saveNewses(newses);
                    }

                    adapter = new NewsListAdapter(getMainActivity(), newses, false, null);
                    recyclerView.setAdapter(adapter);

                    refresh = false;
                    getMainActivity().stopLoadingAnimator();
                    getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
                }
            });

        }
    }


    //////////////////////////////////////////// Adapter //////////////////////////////////////////
    class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private List<News> newses;
        private boolean loadMore;
        private String tag;

        /*
        * Type More: Endless progress view
        * Type header : layout of toolbar which will be empty
        * Type first : layout of subcategories and show more button
        * Type Item : Recipe Item
        * */
        private static final int TYPE_MORE = 4;
        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public NewsListAdapter(Context context, List<News> newses, boolean loadMore, String tag) {
            this.context = context;
            this.newses = newses;
            this.loadMore = loadMore;
            this.tag = tag;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.row_news, parent, false);
                return new NewsItemViewHolder(view, context, newses, tag);
            } else if (viewType == TYPE_HEADER) {
                final View view = LayoutInflater.from(context).inflate(R.layout.recycler_header, parent, false);
                return new RecyclerHeaderViewHolder(view, -1);
            } else if (viewType == TYPE_MORE) {
                final View view = LayoutInflater.from(context).inflate(R.layout.recycler_progress_view_holder, parent, false);
                return new RecyclerLoadMoreViewHolder(view, loadMore);
            }
            throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (!isPositionHeader(position)) {
                if (position == getItemCount() -1){
                    RecyclerLoadMoreViewHolder holder = (RecyclerLoadMoreViewHolder) viewHolder;
                    holder.isLoadMore(loadMore);
                } else {
                    NewsItemViewHolder holder = (NewsItemViewHolder) viewHolder;
                    holder.setEventItems(position - 1);
                }
            }
            else {
                RecyclerHeaderViewHolder holder = (RecyclerHeaderViewHolder) viewHolder;
            }
        }

        @Override
        public int getItemCount() {
            return getBasicItemCount() + 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position)) {
                return TYPE_HEADER;
            }
            if (position == getItemCount() -1) {
                return TYPE_MORE;
            }
            return TYPE_ITEM;
        }

        public int getBasicItemCount() {
            return newses == null ? 0 : newses.size();
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        public void setLoadMore(boolean state) {
            this.loadMore = state;
        }
    }

    class NewsItemViewHolder extends RecyclerView.ViewHolder {

        private View row;
        private Context context;
        private List<News> newses;
        private String tag;

        private TextView newsNameTextView;

        public NewsItemViewHolder(View parent, Context context, List<News> newses, String tag) {
            super(parent);
            row = parent;

            this.tag = tag;
            this.context = context;
            this.newses = newses;

            newsNameTextView = (TextView) parent.findViewById(R.id.newsNameTextView);

        }

        public void setEventItems(int position) {
            final News news = newses.get(position);
            newsNameTextView.setText(news.getEventsNewsNameLat());


            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment newsDetailsFragment = new NewsDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("newsId", news.getEventsNewsCode());

                    newsDetailsFragment.setArguments(bundle);
                    ((MainActivity) context).addFragment(newsDetailsFragment, true, null);
                }
            });
        }

    }
}
