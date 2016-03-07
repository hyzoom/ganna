package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ahmed on 3/7/2016.
 */
@DatabaseTable(tableName = "instructor")
public class Instructor {
    @DatabaseField(id = true, columnName = "id")
    private Integer InstructorCode;

    @DatabaseField
    private String InstructorCodeNameAra;

    ////////////////////////////////////////////////////////////////////////

    public Integer getInstructorCode() {
        return InstructorCode;
    }

    public void setInstructorCode(Integer instructorCode) {
        InstructorCode = instructorCode;
    }

    public String getInstructorCodeNameAra() {
        return InstructorCodeNameAra;
    }

    public void setInstructorCodeNameAra(String instructorCodeNameAra) {
        InstructorCodeNameAra = instructorCodeNameAra;
    }

}
