package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ishraq.janna.R;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Booking;
import com.ishraq.janna.model.Session;
import com.ishraq.janna.service.BookingService;
import com.ishraq.janna.service.CommonService;
import com.ishraq.janna.service.SettingsService;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;
import com.ishraq.janna.viewholder.RecyclerLoadMoreViewHolder;
import com.ishraq.janna.webservice.BookingWebService;
import com.ishraq.janna.webservice.CommonRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by hp on 20/02/2016.
 */
public class ReservationFragment extends MainCommonFragment {
    private BookingWebService bookingWebService;

    private SettingsService settingsService;
    private BookingService bookingService;

    private List<Booking> bookings;
    private boolean loadMore = true;

    private FloatingActionButton floatingActionButton;


    private RecyclerView recyclerView;
    private BookingAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingWebService = getWebService(BookingWebService.class);
        settingsService = new SettingsService(getMainActivity());
        bookingService = new BookingService(getMainActivity());
    }

    @Override
    public void refreshContent() {
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        showToolbar();
        getMainActivity().stopLoadingAnimator();
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setVisibility(View.VISIBLE);

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

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().addFragment(new BookingFragment(), true, null);
            }
        });
        return view;
    }


    private void initData() {
//        BookingsRequest request = new BookingsRequest();
//        request.execute();
        getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
    }

    private class BookingsRequest implements CommonRequest {
        @Override
        public void execute() {
            getMainActivity().startLoadingAnimator();
            bookingWebService.getUserBookings(settingsService.getSettings().getLoggedInUser().getId()).enqueue(new RequestCallback<List<Booking>>(this) {
                @Override
                public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                    bookings = response.body();

                    if (bookings.size() < 10)
                        loadMore = false;

                    adapter = new BookingAdapter(bookings, loadMore);
                    recyclerView.setAdapter(adapter);
                    getMainActivity().stopLoadingAnimator();
                }

                @Override
                public void onFailure(Call<List<Booking>> call, Throwable t) {
                    bookings = bookingService.getBookings();
                    adapter = new BookingAdapter(bookings, false);
                    recyclerView.setAdapter(adapter);
                    getMainActivity().stopLoadingAnimator();
                }
            });

        }
    }


    ///////////////////////////////////////////  Adapter ////////////////////////////////////////
    class BookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Booking> bookings;
        private boolean loadMore;

        /*
        * Type More: Endless progress view
        * Type header : layout of toolbar which will be empty
        * Type Item : Session Item
        * */

        private static final int TYPE_MORE = 4;
        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public BookingAdapter(List<Booking> bookings, boolean loadMore) {
            this.bookings = bookings;
            this.loadMore = loadMore;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.row_booking, parent, false);
                return new ItemViewHolder(view, bookings);
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
                    ItemViewHolder holder = (ItemViewHolder) viewHolder;
                    holder.setItem(position);
                }

            } else {
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
            return bookings == null ? 0 : bookings.size();
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private List<Booking> bookings;

        public ItemViewHolder(View parent, List<Booking> bookings) {
            super(parent);
            this.bookings = bookings;
        }

        public void setItem(int position) {
            final Booking booking = bookings.get(position);
        }
    }

}