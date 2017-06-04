package com.example.human.myapplication;

import java.io.Serializable;

/**
 * Created by human on 2017/4/3.
 */

public class summary implements Serializable{
    private String name;
    private String date;
    private String loca;
    private boolean checked;

    public summary(String name,String date,String loca){
        this.name=name;
        this.date=date;
        this.loca=loca;
    }
    public String getName() { return name;}

    public String getDate() {
        return date;
    }

    public String getLoca() {   return loca;}

}
