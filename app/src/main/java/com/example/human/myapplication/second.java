package com.example.human.myapplication;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.provider.DocumentFile;
import android.support.v4.widget.Space;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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
    private static final int take = 1;
    private static final int find = 2;
    private static final int draw = 3;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            case R.id.take:
                npicture();
                break;
            case R.id.find:
                if (ContextCompat.checkSelfPermission(second.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(second.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                }
                else{
                    open();
                }
                break;
            case R.id.share:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String data = edit.getText().toString();
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, data+"\n"+"一来自随记");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
                break;
            case R.id.draw:
                draw();
                break;
            default:
        }
        return true;
    }

    private void take_photo(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,take);
    }

    private void open(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,find);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    take_photo();
                }
                else {
                    Toast.makeText(this,"无法使用相机",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    open();
                }
                else{
                    Toast.makeText(this,"无法获取相册",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case take:
                if(resultCode==RESULT_OK){
                    second.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,imageUri));
                    String path = imageUri.getPath();
                    SpannableString ss = yasuo(path);
                    insertimage(ss,1);
                }
                break;
            case find :
                if (resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }
                    else {

                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case draw:
                if(resultCode==RESULT_OK){
                    String path =data.getStringExtra("extra_path");
                    System.out.println(path);
                    SpannableString ss = yasuo(path);
                    insertimage(ss,1);
                }
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }
            else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }
        else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }
        else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath != null){
           SpannableString ss = yasuo(imagePath);
            insertimage(ss,1);

        }
        else {
            Toast.makeText(this,"获取图片失败",Toast.LENGTH_SHORT).show();
        }
    }
    //压缩图片
    private SpannableString yasuo(String imagePath){
        BitmapFactory.Options newopts = new BitmapFactory.Options();
        newopts.inJustDecodeBounds = true;
        newopts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath,newopts);
        newopts.inJustDecodeBounds = false;
        int w = newopts.outWidth;
        int h = newopts.outHeight;

        float ww = 250f;
        float hh = 500f;

        int be  = 1;
        if(w>=h && w>ww)
            be = (int)(w/ww);
        else if (w<h && h>hh)
            be = (int)(h/hh);
        if(be<=0)
            be = 1;
        newopts.inSampleSize=be;
        bitmap = BitmapFactory.decodeFile(imagePath,newopts);

        SpannableString ss = new SpannableString(imagePath);
        ImageSpan span = new ImageSpan(this,bitmap);
        ss.setSpan(span,0,imagePath.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
    //新建一个image
    private void npicture(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String  fuck =df.format(new Date());

        File outputImage = new File(getExternalCacheDir(),fuck+".jpg");
        if(outputImage.exists()){
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT>=24){
            imageUri = FileProvider.getUriForFile(second.this,"com.example.human.myapplication.fileprovider",outputImage);
        }
        else{
            imageUri = Uri.fromFile(outputImage);
        }

        if(ContextCompat.checkSelfPermission(second.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(second.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        else{
                take_photo();
        }
    }
    protected void draw(){
        Intent intent = new Intent(second.this,DrawActivity.class);
        startActivityForResult(intent,draw);
    }
    //插入图片
    private void insertimage(SpannableString ss,int flag2){
        Editable et= edit.getText();
        int start = edit.getSelectionStart();
        String hh = "\n";
        if(flag2==0){
            et.insert(start,ss);
            et.insert(start+ss.length(),hh);
        }
        else {
            et.insert(start,hh);
            et.insert(start+hh.length(),ss);
            et.insert(start+ss.length()+hh.length(),hh);
        }
        edit.setText(et);
        et = edit.getText();
        edit.setSelection(et.length());
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
        StringBuilder stringBuilder = new StringBuilder();
        try{
            in = openFileInput(name);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine())!=null){
                if(line.contains("/")){
                    stringBuilder.append(line);
                }
                if(!(new String(stringBuilder).equals(""))){
                    String imagepath = stringBuilder.toString();
                    SpannableString spannableString =yasuo(imagepath);
                    insertimage(spannableString,0);
                    stringBuilder.delete(0,stringBuilder.length());
                }
                else{
                    content.append(line);
                    content.append('\n');
                    Editable et =edit.getText();
                    int start = edit.getSelectionStart();
                    et.insert(start,content.toString());
                    edit.setText(et);
                    edit.setSelection(start+content.toString().length());
                    et.clear();
                    content.delete(0,content.length());
                }

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
