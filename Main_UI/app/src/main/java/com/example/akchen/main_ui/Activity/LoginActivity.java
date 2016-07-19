package com.example.akchen.main_ui.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.akchen.main_ui.R;
import com.example.akchen.main_ui.others.utils.WeatherDB;
import com.example.akchen.main_ui.others.widget.DateTimeSelectorDialogBuilder;


public class LoginActivity extends Activity {

    // 帐号和密码
    private EditText edname;
    private EditText edpassword;

    private Button btregister;
    private Button btlogin;
    // 创建SQLite数据库
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_activity);
        edname = (EditText) findViewById(R.id.edname);
        edpassword = (EditText) findViewById(R.id.edpassword);
        btregister = (Button) findViewById(R.id.btregister);

        btlogin = (Button) findViewById(R.id.btlogin);
        db = SQLiteDatabase.openOrCreateDatabase(LoginActivity.this.getFilesDir().toString()
                + "/test.dbs", null);
        // 跳转到注册界面
        btregister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegistersActivity.class);
                startActivity(intent);

            }
        });
        btlogin.setOnClickListener((View.OnClickListener) new LoginListener());
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        db.close();
    }


    class LoginListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            String name = edname.getText().toString();
            String password = edpassword.getText().toString();
            if (name.equals("") || password.equals("")) {
                // 弹出消息框
                new AlertDialog.Builder(LoginActivity.this).setTitle("错误")
                        .setMessage("帐号或密码不能空").setPositiveButton("确定", null)
                        .show();
            } else {
                isUserinfo(name, password);
            }
        }

        // 判断输入的用户是否正确
        public Boolean isUserinfo(String name, String pwd) {
            try{
                String str="select * from tb_user where name=? and password=?";
                Cursor cursor = db.rawQuery(str, new String []{name,pwd});
                if(cursor.getCount()<=0){
                    new AlertDialog.Builder(LoginActivity.this).setTitle("错误")
                            .setMessage("帐号或密码错误！").setPositiveButton("确定", null)
                            .show();
                    return false;
                }else{
                    new AlertDialog.Builder(LoginActivity.this).setTitle("正确")
                            .setMessage("成功登录").setPositiveButton("确定", null)
                            .show();
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainUIActivity.class);
                    startActivity(intent);
                    db.close();
                    finish();
                    return true;
                }

            }catch(SQLiteException e){
                createDb();
            }
            return false;
        }

    }
    // 创建数据库和用户表



    public void createDb() {
        db.execSQL("create table tb_user( name varchar(30) primary key,password varchar(30))");
    }



}