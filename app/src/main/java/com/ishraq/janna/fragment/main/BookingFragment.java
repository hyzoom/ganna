package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ishraq.janna.R;
import com.ishraq.janna.listner.HidingScrollListener;
import com.ishraq.janna.model.Booking;
import com.ishraq.janna.viewholder.RecyclerHeaderViewHolder;
import com.ishraq.janna.webservice.BookingWebService;
import com.ishraq.janna.webservice.CommonRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ahmed on 3/11/2016.
 */
public class BookingFragment extends MainCommonFragment {
    private BookingWebService bookingWebService;

    private RecyclerView recyclerView;
    private CreateBookingAdapter adapter;
    String[] clinics = {"اختر الفرع", "فرع المهندسين", "فرع الهرم", "فرع فيصل"};
    String[] diseaType = {"اختر العيادة", "عيادة تأخر الحمل واطفال الانابيب", "عيادة متابعة الحمل", "عيادة امراض النساء", "عيادة الموجات فوق الصوتية", "عيادة الذكورة", "عيادة الاطفال", "عيادة الاسنان", "عيادة الباطنة", "عيادة جراحة الاطفال", "عيادة جراحة التجميل", "عيادة الحمل فوق الاربعين", "عيادة ربيع العمر", "عيادة الاشعة", "عيادة الجلدية", "عيادة المناظير"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingWebService = getWebService(BookingWebService.class);
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
        adapter = new CreateBookingAdapter();
        recyclerView.setAdapter(adapter);
        getMainActivity().getSwipeRefreshLayout().setRefreshing(false);
    }

    /////////////////////////////////////////// Adapter ///////////////////////////////////
    class CreateBookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        /*
        * Type header : layout of toolbar which will be empty
        * Type Item : Recipe Item
        * */

        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        public CreateBookingAdapter() {
            getMainActivity().getToolbar().setTitle(getResources().getString(R.string.title_booking));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.fragment_create_booking, parent, false);
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
                holder.setAdapterItem();
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
            return 1;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private EditText nameEditText, mobileEditText, notesEditText;
        private Spinner clinicSpinner, specialSpinner;
        private Button addBookingButton;

        public ItemViewHolder(View parent) {
            super(parent);

            nameEditText = (EditText) parent.findViewById(R.id.nameEditText);
            mobileEditText = (EditText) parent.findViewById(R.id.mobileEditText);
            notesEditText = (EditText) parent.findViewById(R.id.notesEditText);

            clinicSpinner = (Spinner) parent.findViewById(R.id.clinicSpinner);
            specialSpinner = (Spinner) parent.findViewById(R.id.specialSpinner);

            clinicSpinner.setAdapter(new ArrayAdapter(
                    getActivity(), R.layout.spinner_layout, clinics));

            specialSpinner.setAdapter(new ArrayAdapter(
                    getActivity(), R.layout.spinner_layout, diseaType));

            addBookingButton = (Button) parent.findViewById(R.id.addBookingButton);

        }

        public void setAdapterItem() {
            addBookingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateBooking()) {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        String formattedDate = df.format(c.getTime());

                        BookingsRequest request = new BookingsRequest(nameEditText.getText().toString(),
                                mobileEditText.getText().toString(),
                                clinicSpinner.getSelectedItem().toString(),
                                specialSpinner.getSelectedItem().toString(),
                                formattedDate,
                                notesEditText.getText().toString());

                        request.execute();
                    }
                }
            });
        }

        private boolean validateBooking() {
            boolean result = true;

            if (nameEditText.getText().toString().equals("") || nameEditText.getText().toString() == null) {
                Toast.makeText(getActivity(), getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
                return false;
            }
            if (mobileEditText.getText().toString() == null || mobileEditText.getText().toString().equals("")) {
                Toast.makeText(getActivity(), getString(R.string.mobile_empty), Toast.LENGTH_SHORT).show();
                return false;
            }
            if (notesEditText.getText().toString() == null || notesEditText.getText().toString().equals("")) {
                notesEditText.setText("");
            }

            if (clinicSpinner.getSelectedItemPosition() == 0) {
                Toast.makeText(getActivity(), getString(R.string.clinic_spinner_empty), Toast.LENGTH_SHORT).show();
                return false;
            }
            if (specialSpinner.getSelectedItemPosition() == 0) {
                Toast.makeText(getActivity(), getString(R.string.special_spinner_empty), Toast.LENGTH_SHORT).show();
                return false;
            }

            return result;
        }

    }


    /////////////////////////////////////// Request ////////////////////////////////////////
    private class BookingsRequest implements CommonRequest {

        private String name, mobile, clinic, specialization, date, notes;

        public BookingsRequest(String name, String mobile, String clinic, String specialization, String date, String notes) {
            this.name = name;
            this.mobile = mobile;
            this.clinic = clinic;
            this.specialization = specialization;
            this.date = date;
            this.notes = notes;
        }

        @Override
        public void execute() {
            getMainActivity().startLoadingAnimator();
            bookingWebService.addBooking(name, mobile, clinic, specialization, date, notes).enqueue(new RequestCallback<List<Booking.BookingResult>>(this) {
                @Override
                public void onResponse(Call<List<Booking.BookingResult>> call, Response<List<Booking.BookingResult>> response) {
                    getFragmentManager().popBackStack();
                }
            });

        }
    }
}
