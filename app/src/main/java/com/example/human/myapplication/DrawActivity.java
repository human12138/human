package com.example.human.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.path;

public class DrawActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        this.imageView = (ImageView) findViewById(R.id.image);
        init();
        Button save =(Button) findViewById(R.id.save);
        Button clean = (Button) findViewById(R.id.clean);
        save.setOnClickListener(this);
        clean.setOnClickListener(this);
    }

    private void init(){
        bitmap = Bitmap.createBitmap(480,680, Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.GRAY);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        canvas.drawBitmap(bitmap,new Matrix(),paint);
        imageView.setImageBitmap(bitmap);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            int startx;
            int starty;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startx = (int)event.getX();
                        starty = (int)event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int stopx = (int)event.getX();
                        int stopy = (int)event.getY();
                        canvas.drawLine(startx,starty,stopx,stopy,paint);
                        startx = (int)event.getX();
                        starty = (int)event.getY();
                        imageView.setImageBitmap(bitmap);
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clean:
                init();
                break;
            case R.id.save:
                save();
                Intent intent = new Intent();
                System.out.println(uri.getPath());
                intent.putExtra("extra_path",uri.getPath());
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
    public void save(){
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String  fuck =df.format(new Date());
            File file = new File(getExternalCacheDir(),fuck+".jpg");
            if(file.exists()){
                file.delete();
            }else{
                file.createNewFile();
            }
            OutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.close();
            if(Build.VERSION.SDK_INT>=24) {
                uri = FileProvider.getUriForFile(DrawActivity.this, "com.example.human.drawtest.fileprovider", file);
            }
            else {
                uri = Uri.fromFile(file);
            }
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this,"保存失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
