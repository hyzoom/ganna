package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ahmed on 3/11/2016.
 */
@DatabaseTable(tableName = "book")
public class Booking {
    @DatabaseField(id = true, columnName = "id")
    private Integer id;

    @DatabaseField(columnName = "date")
    private String date;

    @DatabaseField(columnName = "notes")
    private String notes;


    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "clinic_id")
    private Clinic clinic;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "specialization_id")
    private Specialization specialization;


    /////////////////////////////////////////////// //////////////////////////////
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }


    /////////////////////////////////////////////////// Booking result //////////////////////////////
    public class BookingResult {
        private Integer userExist;
        private Integer id;
        private String errorMessage;

        public Integer getUserExist() {
            return userExist;
        }

        public void setUserExist(Integer userExist) {
            this.userExist = userExist;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
