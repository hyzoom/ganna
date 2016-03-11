package com.ishraq.janna.fragment.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishraq.janna.R;
import com.ishraq.janna.adapter.EventListAdapter;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.service.EventService;
import com.ishraq.janna.webservice.CommonRequest;
import com.ishraq.janna.webservice.EventWebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by hp on 20/02/2016.
 */
public class EventFragment extends HomeFragment {

    private EventWebService eventWebService;
    private EventService eventService;
    private RecyclerView recyclerView;

    private List<Event> events;
    private EventListAdapter adapter;
    private boolean loadMore = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventService = new EventService(getMainActivity());
        eventWebService = getWebService(EventWebService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getMainActivity().getToolbar().setTitle(getResources().getString(R.string.app_name));
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideToolbar();
                hideTabBar();
            }

            @Override
            public void onShow() {
                showToolbar();
                showTabBar();
            }

            @Override
            public void swipeRefresh(boolean state) {
//                getMainActivity().getSwipeRefreshLayout().setEnabled(state);
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
        ListEventRequest request = new ListEventRequest();
        request.execute();
    }

    private class ListEventRequest implements CommonRequest {
        @Override
        public void execute() {
            getMainActivity().startLoadingAnimator();
            eventWebService.getAllEvents().enqueue(new RequestCallback<List<Event>>(this) {
                @Override
                public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                    events = response.body();
                    eventService.saveEvents(events);

                    if (events.size() == 10) {
                        loadMore = true;
                    } else {
                        loadMore = false;
                    }
                    adapter = new EventListAdapter(getMainActivity(), events, loadMore);
                    recyclerView.setAdapter(adapter);

                    getMainActivity().stopLoadingAnimator();
                }

                @Override
                public void onFailure(Call<List<Event>> call, Throwable t) {
                    loadMore = false;
                    events = eventService.getEvents();

                    adapter = new EventListAdapter(getMainActivity(), events, loadMore);
                    recyclerView.setAdapter(adapter);

                    getMainActivity().stopLoadingAnimator();
                }
            });
        }
    }

}