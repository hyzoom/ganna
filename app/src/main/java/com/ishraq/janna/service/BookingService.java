package com.ishraq.janna.service;

import android.content.Context;
import android.util.Log;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.model.Booking;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ahmed on 3/11/2016.
 */
public class BookingService extends CommonService {
    public BookingService(Context context) {
        super(context);
    }

    /**
     *
     * @return Events
     */
    public List<Booking> getBookings() {
        List<Booking> bookings = null;
        try {
            bookings = bookingDao.queryForAll();
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return bookings;
    }
}
