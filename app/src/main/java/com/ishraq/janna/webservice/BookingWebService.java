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
//    @GET("JsonViewEvents.aspx")
//    Call<List<Booking>> getUserBookings(@Query("id") Integer userId);

    //get clinics
//    @GET("JsonViewEvents.aspx")
//    Call<List<Clinic>> getClinics();

    //get Specialization
//    @GET("JsonViewEvents.aspx")
//    Call<List<Specialization>> getSpecialization();

    @GET("JsonInsertReservations.aspx")
    Call<List<Booking.BookingResult>> addBooking(@Query("name") String name,
                                          @Query("mobile") String mobile,
                                          @Query("clinic") String clinic,
                                          @Query("Specialization") String specialization,
                                          @Query("reservedate") String reservedate,
                                          @Query("notes") String notes);

//    @GET("JsonInsertReservations.aspx")
//    Call<List<Booking.BookingResult>> addBooking(@Query("id") Integer id,
//                                                 @Query("clinic") Integer clinic,
//                                                 @Query("Specialization") Integer specialization,
//                                                 @Query("date") Long date,
//                                                 @Query("notes") String notes);


}
