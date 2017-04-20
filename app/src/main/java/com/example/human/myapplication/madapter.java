package com.example.human.myapplication;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by human on 2017/4/3.
 */

public class madapter extends ArrayAdapter<summary> {
    private int rid;
    private Map<Integer,Boolean> map= new HashMap<>();
    public List<Integer> list = new ArrayList<>();

    public madapter(Context context, int tid, List<summary> objects){
        super(context,tid,objects);
        rid=tid;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        summary s = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(rid,parent,false);

        CheckBox select= (CheckBox) view.findViewById(R.id.list_select);
        TextView name = (TextView) view.findViewById(R.id.text_name);
        TextView date = (TextView) view.findViewById(R.id.text_date);
        select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    map.put(position,true);
                    list.add(position);
                }else {
                    map.remove(position);
                }
            }
        });
        if(map!=null&&map.containsKey(position)){
            select.setChecked(true);
            list.add(position);
        }else {
            if (!list.isEmpty())
                list.remove(position);
            select.setChecked(false);
        }
        name.setText(s.getName());
        date.setText(s.getDate());
        return view;
    }
}
