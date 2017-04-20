package com.example.human.myapplication;


import org.litepal.crud.DataSupport;

/**
 * Created by human on 2017/3/30.
 */

public class memory extends DataSupport{

    private String name;
    private String date;
    private int year;
    private int month;
    private int day;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
