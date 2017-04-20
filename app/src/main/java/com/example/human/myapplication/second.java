package com.example.human.myapplication;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.zip.DataFormatException;

public class second extends AppCompatActivity {

    private EditText edit;
    private int flag;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        flag=0;
        Intent intent = getIntent();
       name = intent.getStringExtra("extra_data");
        edit = (EditText) findViewById(R.id.editText2);
        String input;
        if(name==null){
            input=null;
        }
        else{
            input= load(name);
        }
        if(!TextUtils.isEmpty(input)){
            edit.setText(input);
            edit.setSelection(input.length());
        }
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                flag=1;
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        save();
    }
    @Override
    public void onBackPressed() {
        if(flag==1){
            AlertDialog.Builder builder = new AlertDialog.Builder(second.this);
            builder.setTitle("Warning");
            builder.setMessage("你还未保存，是否保存");
            builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    save();
                }
            });
           builder.setNegativeButton("丢弃", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(second.this,MainActivity.class);
                    startActivity(intent);
                }
            });
            builder.show();
        }
        else super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.second,menu);
        return  true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.save_item:
                if(flag!=0){
                    flag=0;
                    if(name!=null){
                        deleteFile(name);
                        DataSupport.deleteAll(memory.class, "name = ?", name);
                    }
                    save();
                }
                else {
                    Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(second.this,MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.throw_item:
                Intent intent1 = new Intent(second.this,MainActivity.class);
                startActivity(intent1);
                break;
            default:
        }
        return true;
    }
    //保存文本信息
    public void save(){

        edit=(EditText)findViewById(R.id.editText2);
        String input=edit.getText().toString();
        if(input.isEmpty()){

        }
        else {
            String c =new String("\n");
            String name= input.split(c)[0];
            if(name.length()>7) {
                name = name.substring(0, 7);
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String  fuck =df.format(new Date());// new Date()为获取当前系统时间
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = formatter.parse(fuck);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int year = date.getYear()+1900;
            int month= date.getMonth()+1;
            int day  = date.getDay()+2;
            System.out.println(year+" "+month+" "+day);

            memory m=new memory();
            m.setName(name);
            m.setDate(fuck);
            m.setYear(year);
            m.setMonth(month);
            m.setDay(day);
            m.save();

            FileOutputStream out = null;
            BufferedWriter writer= null;
            try{
                out = openFileOutput(name, Context.MODE_PRIVATE);
                writer = new BufferedWriter(new OutputStreamWriter(out));
                writer.write(input);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try { if(writer!=null){
                    writer.close();
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(second.this,MainActivity.class);
        startActivity(intent);
    }

    //读取之前存储的文本
    public String load(String name){
        FileInputStream in = null;
        BufferedReader reader= null;
        StringBuilder content = new StringBuilder();
        try{
            in = openFileInput(name);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine())!=null){
                content.append(line);
                content.append('\n');
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try{
                    reader.close();;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
}
