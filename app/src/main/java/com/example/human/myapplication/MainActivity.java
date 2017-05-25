package com.example.human.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import junit.framework.Protectable;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private  List<summary> summaryList=new ArrayList<>();
    madapter adapter;
    ListView  listView;
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
        summaryList.clear();
        initsummary();
        adapter = new madapter(MainActivity.this, R.layout.sum_item,summaryList);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                summary s=summaryList.get(position);
                String name=s.getDate();
                Intent intent = new Intent(MainActivity.this,second.class);
                intent.putExtra("extra_data",name);
                startActivity(intent);
            }
        });
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
            summary s = new summary(m.getDate(),m.getName());
            summaryList.add(s);
        }
    }
}
