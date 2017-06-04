package com.example.human.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import junit.framework.Protectable;

import org.litepal.crud.DataSupport;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private  List<summary> summaryList=new ArrayList<>();
    madapter adapter;
    ListView  listView;
    public  SearchPopwin searchPopwin;
    int flag=0;
    int flag1 =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=null;
                Intent intent = new Intent(MainActivity.this,second.class);
                intent.putExtra("extra_data",name);
                startActivity(intent);
            }
        });

        if(flag==0) {
            summaryList.clear();
            initsummary();
        }
        else{
            flag=0;
        }
        adapter = new madapter(MainActivity.this, R.layout.sum_item,summaryList);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                summary s=summaryList.get(position);
                String name=s.getDate();
                String time = s.getName();
                Intent intent = new Intent(MainActivity.this,second.class);
                intent.putExtra("extra_data",name);
                intent.putExtra("extra_time",time);
                startActivity(intent);
            }
        });
        Log.d("MainActivity", "onResume: ");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String string = data.getStringExtra("data_return");
                    if(string.equals("yes")) {
                        for (int i = 0; i < adapter.list.size(); i++) {
                            summary s = summaryList.get(adapter.list.get(i));
                            String name = s.getDate();
                            deleteFile(name);
                            DataSupport.deleteAll(memory.class, "name = ?", name);
                        }
                    }
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    String string = data.getStringExtra("data_return");
                    if(string.equals("yes")){
                        for(int i = 0;i<summaryList.size();i++){
                            summary s = summaryList.get(i);
                            String name = s.getDate();
                            deleteFile(name);
                            DataSupport.deleteAll(memory.class,"name = ?",name);
                        }
                    }
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item:
                String name=null;
                Intent intent = new Intent(MainActivity.this,second.class);
                intent.putExtra("extra_data",name);
                startActivity(intent);
                break;
            case R.id.search_item:
                popwin();
                break;
            case R.id.del_item:
                Intent intent1=new Intent(MainActivity.this,DialogActivity.class);
                startActivityForResult(intent1,1);
                break;
            case R.id.clc_item:
                Intent intent2 = new Intent(MainActivity.this,DialogActivity.class);
                startActivityForResult(intent2,2);
                break;
            default:
        }
        return true;
    }

    private void initsummary() {
        List<memory> ms= DataSupport.order("date desc").find(memory.class);
        for(memory m:ms){
            String string = m.getLoca();
            String c = ".";
            if(string.contains(c)){
                string = string.split("\\.")[3];
            }
            summary s = new summary(m.getDate(),m.getName(),string);
            summaryList.add(s);
        }
    }

    private void searchsummary(String year,String month,String day){
        summaryList.clear();
        List<memory> ms;
        if(!year.equals("")&&!month.equals("")&&!day.equals("")){
            ms = DataSupport.where("day=? and month=? and year=?",day,month,year).order("date desc").find(memory.class);
        }
        else if(year.equals("")&&!month.equals("")&&!day.equals("")){
            ms = DataSupport.where("day=? and month=?",day,month).order("date desc").find(memory.class);

        }
        else if(day.equals("")&&!year.equals("")&&!month.equals("")){
            ms = DataSupport.where("year=? and month=?",year,month).order("date desc").find(memory.class);
        }
        else if(month.equals("")&&!year.equals("")&&!day.equals("")){
            ms = DataSupport.where("year=? and day=?",year,day).order("date desc").find(memory.class);
        }
        else if(year.equals("")&&month.equals("")&&!day.equals("")){
            ms = DataSupport.where("day=?",day).order("date desc").find(memory.class);
        }
        else if(year.equals("")&&day.equals("")&&!month.equals("")){
            ms = DataSupport.where("month=?",month).order("date desc").find(memory.class);
        }
        else if(month.equals("")&&day.equals("")&&!year.equals("")){
            ms = DataSupport.where("year=?",year).order("date desc").find(memory.class);
        }
        else {
            ms = null;
        }
        for(memory m:ms){
            String string = m.getLoca();
            String c = ".";
            if(string.contains(c)){
                string = string.split("\\.")[3];
            }
            summary s = new summary(m.getDate(),m.getName(),string);
            summaryList.add(s);
        }
        flag=1;
        flag1=1;
        onResume();
    }
    public void popwin(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.search:
                        String day = searchPopwin.day.getText().toString();
                        String month = searchPopwin.month.getText().toString();
                        String year = searchPopwin.year.getText().toString();
                        int years = searchPopwin.datePicker.getYear();
                        int months = searchPopwin.datePicker.getMonth()+1;
                        int days = searchPopwin.datePicker.getDayOfMonth();
                        Log.d("MainActivity", years+""+months+""+days+"");
                        if(year.equals("")&&month.equals("")&&day.equals(""))
                            searchsummary(years+"",months+"",days+"");
                        else
                            searchsummary(year,month,day);
                        searchPopwin.dismiss();
                        break;
                    case R.id.thisday:
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        String  fuck =df.format(new Date());// new Date()为获取当前系统时间
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = null;
                        try {
                            date = formatter.parse(fuck);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        int m= date.getMonth()+1;
                        int d  = date.getDay()-3;

                        searchsummary("",m+"",d+"");
                        searchPopwin.dismiss();
                }
            }
        };
        searchPopwin = new SearchPopwin(this,onClickListener);
        searchPopwin.showAtLocation(findViewById(R.id.main), Gravity.CENTER,0,0);
    }

    @Override
    public void onBackPressed() {
        if(flag1==1) {
            flag1=0;
            onResume();
        }
        else
            super.onBackPressed();
    }
}
