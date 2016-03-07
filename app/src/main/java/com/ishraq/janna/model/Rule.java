package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ahmed on 3/7/2016.
 */
@DatabaseTable(tableName = "rule")
public class Rule {
    @DatabaseField(id = true, columnName = "id")
    private Integer RuleCode;

    @DatabaseField(columnName = "name_ar")
    private String RuleName;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "event_id")
    private Event event;


    public Integer getRuleCode() {
        return RuleCode;
    }

    public void setRuleCode(Integer ruleCode) {
        RuleCode = ruleCode;
    }

    public String getRuleName() {
        return RuleName;
    }

    public void setRuleName(String ruleName) {
        RuleName = ruleName;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
