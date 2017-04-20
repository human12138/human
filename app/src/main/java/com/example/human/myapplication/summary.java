package com.example.human.myapplication;

import java.io.Serializable;

/**
 * Created by human on 2017/4/3.
 */

public class summary implements Serializable{
    private String name;
    private String date;
    private boolean checked;

    public summary(String name,String date){
        this.name=name;
        this.date=date;
    }
    public String getName() { return name;}

    public String getDate() {
        return date;
    }

}
