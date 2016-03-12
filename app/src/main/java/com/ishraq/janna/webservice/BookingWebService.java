package com.ishraq.janna.webservice;

import com.ishraq.janna.model.Booking;
import com.ishraq.janna.model.Clinic;
import com.ishraq.janna.model.Specialization;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ahmed on 3/11/2016.
 */
public interface BookingWebService {

    //get bookings
    @GET("JsonViewBooking.aspx")
    Call<List<Booking>> getUserBookings(@Query("id") Integer userId);

    @GET("JsonInsertReservations.aspx")
    Call<List<Booking.BookingResult>> addBooking(@Query("name") String name,
                                          @Query("mobile") String mobile,
                                          @Query("clinic") String clinic,
                                          @Query("Specialization") String specialization,
                                          @Query("reservedate") String date,
                                          @Query("notes") String notes);
}
