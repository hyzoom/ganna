package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ahmed on 3/7/2016.
 */
@DatabaseTable(tableName = "gst")
public class Gst {
    @DatabaseField(id = true, columnName = "id")
    private Integer id;
}
