package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishraq.janna.R;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.User;
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
public class AttendantFragment extends MainCommonFragment {
    private EventWebService eventWebService;
//    private

    private RecyclerView recyclerView;
    private List<User> attendants;
    private AttendantListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventWebService = getWebService(EventWebService.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getMainActivity().getToolbar().setTitle(getResources().getString(R.string.attendant));
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
        ListAttendantsRequest request = new ListAttendantsRequest();
        request.execute();
    }

    @Override
    public void refreshContent() {
        initData();
    }




    /////////////////////////////////////// Request /////////////////////////////////////


    private class ListAttendantsRequest implements CommonRequest {
        @Override
        public void execute() {

            eventWebService.getAllAttendants().enqueue(new RequestCallback<List<User>>(this) {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    attendants = response.body();

                    adapter = new AttendantListAdapter(getMainActivity(), attendants, false, null);
                    recyclerView.setAdapter(adapter);

                    getMainActivity().stopLoadingAnimator();
                    getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
                }
            });
        }
    }


    //////////////////////////////////////////// Adapter //////////////////////////////////////////
    class AttendantListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private List<User> attendants;
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

        public AttendantListAdapter(Context context, List<User> attendants, boolean loadMore, String tag) {
            this.context = context;
            this.attendants = attendants;
            this.loadMore = loadMore;
            this.tag = tag;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.row_attendant, parent, false);
                return new AttendantItemViewHolder(view, context, attendants, tag);
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
                    AttendantItemViewHolder holder = (AttendantItemViewHolder) viewHolder;
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
            return attendants == null ? 0 : attendants.size();
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        public void setLoadMore(boolean state) {
            this.loadMore = state;
        }
    }

    class AttendantItemViewHolder extends RecyclerView.ViewHolder {

        private View row;
        private Context context;
        private List<User> attendants;
        private String tag;

        private TextView userNameTextView, addressTextView;

        public AttendantItemViewHolder(View parent, Context context, List<User> attendants, String tag) {
            super(parent);
            row = parent;

            this.tag = tag;
            this.context = context;
            this.attendants = attendants;

            userNameTextView = (TextView) parent.findViewById(R.id.userNameTextView);
            addressTextView = (TextView) parent.findViewById(R.id.addressTextView);

        }

        public void setEventItems(int position) {
            final User user = attendants.get(position);
            userNameTextView.setText(user.getUseName());
            addressTextView.setText(user.getAddress());
        }

    }
}
