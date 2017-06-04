package com.example.human.myapplication;


import org.litepal.crud.DataSupport;

/**
 * Created by human on 2017/3/30.
 */

public class memory extends DataSupport{

    private String name;
    private String date;
    private String loca;
    private String year;
    private String month;
    private String day;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {return date;}

    public void setDate(String date) {
        this.date = date;
    }

    public void setLoca(String loca)  {this.loca = loca; }

    public String getLoca(){return loca;}

}
