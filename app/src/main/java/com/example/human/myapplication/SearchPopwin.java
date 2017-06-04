package com.example.human.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by human on 2017/6/2.
 */

public class SearchPopwin extends PopupWindow {

    private Context mcontext;
    private View view;
    private Button thisday;
    private Button search;
    public EditText year;
    public EditText month;
    public EditText day;
    public DatePicker datePicker;
    public SearchPopwin(Activity mcontext,View.OnClickListener itemsOnClick){
        this.mcontext = mcontext;
        this.view= LayoutInflater.from(mcontext).inflate(R.layout.searchopwin,null);

        thisday = (Button) view.findViewById(R.id.thisday);
        search = (Button) view.findViewById(R.id.search);
        year = (EditText) view.findViewById(R.id.year);
        month = (EditText) view.findViewById(R.id.month);
        day = (EditText) view.findViewById(R.id.day);
        datePicker = (DatePicker) view.findViewById(R.id.date);

        thisday.setOnClickListener(itemsOnClick);
        search.setOnClickListener(itemsOnClick);


        this.setOutsideTouchable(true);
        this.setContentView(this.view);

        Window dialogWindow = mcontext.getWindow();
        WindowManager m = mcontext.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        ColorDrawable dw = new ColorDrawable(Color.GRAY);
        this.setBackgroundDrawable(dw);

        this.setHeight(p.WRAP_CONTENT);
        this.setWidth((int)(d.getWidth()*0.8));
        this.setFocusable(true);

    }
}
