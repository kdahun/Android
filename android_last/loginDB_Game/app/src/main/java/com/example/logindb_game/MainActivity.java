package com.example.logindb_game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    EditText editText1,editText2,editTextId, editTextPw;
    Button btnLogin,btnMembership;
    Handler handler = new Handler();
    View dialogView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("로그인 화면");

        btnLogin = (Button) findViewById(R.id.button);
        btnMembership = (Button) findViewById(R.id.button2);

        editText1 = (EditText) findViewById(R.id.editTextText);
        editText2 = (EditText) findViewById(R.id.editTextText2);

        myHelper = new myDBHelper(this,"memberDB",null,1);

        sqlDB=myHelper.getWritableDatabase();
        myHelper.onUpgrade(sqlDB,1,2);
        sqlDB.close();

        final String urlStr = "https://dahun5223.iwinv.net/json_login.php";

        new Thread(new Runnable() {
            @Override
            public void run() {
                requestUrl(urlStr);
            }
        }).start();

        sqlDB = myHelper.getReadableDatabase();
        String sql = "select * from membertable";
        Cursor cursor = sqlDB.rawQuery(sql,null);
        while(cursor.moveToNext()){
            Log.d("idpw",cursor.getString(1)+cursor.getString(2));
        }
        cursor.close();
        sqlDB.close();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText1.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "ID를 입력해 주세요", Toast.LENGTH_SHORT).show();
                }else if(editText2.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"passward를 입력해 주세요",Toast.LENGTH_SHORT).show();
                }else{
                    sqlDB = myHelper.getReadableDatabase();
                    String sql = "select * from membertable where id='"+editText1.getText().toString()+"' and pw='"+editText2.getText().toString()+"'";
                    Cursor cursor = sqlDB.rawQuery(sql,null);
                    cursor.moveToLast();
                    try{
                        String s = cursor.getString(1);
                        Toast.makeText(getApplicationContext(), s+"로그인 성공", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"정보 없음",Toast.LENGTH_SHORT).show();
                    }

//                    while(cursor.moveToNext()){
//                        if(cursor.getString(1).equals(editText1.getText().toString())&&cursor.getString(2).equals(editText2.getText().toString())){
//                            Log.d("tlqkf",cursor.getString(1)+cursor.getString(2));
//                        }
//                    }

                    cursor.close();
                    sqlDB.close();

                }
            }
        });

        btnMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView = (View) View.inflate(MainActivity.this,R.layout.dialog,null);

                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("회원가입");
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean check = false;
                        editTextId = (EditText) dialogView.findViewById(R.id.editTextText3);
                        editTextPw = (EditText) dialogView.findViewById(R.id.editTextText4);

                        sqlDB = myHelper.getReadableDatabase();
                        String sql = "select * from membertable";
                        Cursor cursor = sqlDB.rawQuery(sql,null);
                        while(cursor.moveToNext()){
                            if(editTextId.getText().toString().equals(cursor.getString(1))){
                                check = true;
                            }
                        }
                        cursor.close();
                        sqlDB.close();
                        if(check){
                            Toast.makeText(getApplicationContext(), "Id가 중복 됩니다 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        }else{
                            sqlDB=myHelper.getWritableDatabase();
                            sqlDB.execSQL("insert into membertable (id,pw) values ('"+editTextId.getText().toString()+"','"+editTextPw.getText().toString()+"')");
                            sqlDB.close();

                            final String urlStr="https://dahun5223.iwinv.net/json_membership.php?id="+editTextId.getText().toString()+"&pw="+editTextPw.getText().toString();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    requestUrl(urlStr);
                                }
                            }).start();
                            Toast.makeText(getApplicationContext(), "회원 가입 성공123.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dlg.show();
            }
        });
    }
    public void requestUrl(String urlStr){
        StringBuilder output = new StringBuilder();

        try{
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if(connection!=null){
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                //int resCode = connection.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while(true){
                    line = reader.readLine();
                    if(line==null){
                        break;
                    }

                    output.append(line+"\n");
                }
                reader.close();;
                connection.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        println(output.toString());
    }
    public void println(String data){
        handler.post(new Runnable() {
            @Override
            public void run() {
                jsonParsing(data);
            }
        });
    }

    public void jsonParsing(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray phoneBookArray = jsonObject.getJSONArray("member");

            sqlDB = myHelper.getWritableDatabase();

            for(int i=0;i<phoneBookArray.length();i++){
                JSONObject memberObject = phoneBookArray.getJSONObject(i);

                Integer no =memberObject.getInt("no");
                String id = memberObject.getString("id");
                String pw = memberObject.getString("pw");

                String sqli = "insert into membertable  values ("+no+",'"+id+"','"+pw+"')";

                sqlDB.execSQL(sqli);

            }
            sqlDB.close();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}