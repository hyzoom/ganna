package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ahmed on 3/7/2016.
 */
@DatabaseTable(tableName = "sponsor")
public class Sponsor {
    @DatabaseField(id = true, columnName = "id")
    private Integer SponserCode;

    @DatabaseField(columnName = "name_ar")
    private String SponserNameAra;

    @DatabaseField(columnName = "name_lat")
    private String SponserNameLat;


    /////////////////////////////////////////////////////////////////

    public Integer getSponserCode() {
        return SponserCode;
    }

    public void setSponserCode(Integer sponserCode) {
        SponserCode = sponserCode;
    }

    public String getSponserNameAra() {
        return SponserNameAra;
    }

    public void setSponserNameAra(String sponserNameAra) {
        SponserNameAra = sponserNameAra;
    }

    public String getSponserNameLat() {
        return SponserNameLat;
    }

    public void setSponserNameLat(String sponserNameLat) {
        SponserNameLat = sponserNameLat;
    }
}
