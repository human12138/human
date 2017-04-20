package com.example.human.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Button yes=(Button) findViewById(R.id.button_yes);
        Button no =(Button) findViewById(R.id.button_no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DialogActivity.this,MainActivity.class);
                intent.putExtra("data_return","yes");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DialogActivity.this,MainActivity.class);
                intent.putExtra("data_return","no");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
