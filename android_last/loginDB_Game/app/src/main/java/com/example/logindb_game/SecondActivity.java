package com.example.logindb_game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecondActivity extends AppCompatActivity {

    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    EditText editTextId, editTextPw;
    Button btnBack,btnInsert, btnModify;
    ListView listView;
    Integer selectNo;
    ListViewAdapter adapter;
    Handler handler=new Handler();
    View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        btnBack=(Button) findViewById(R.id.button3);
        btnInsert=(Button) findViewById(R.id.button4);
        btnModify =(Button) findViewById(R.id.button5);

        listView=(ListView) findViewById(R.id.listview);
        adapter=new ListViewAdapter();
        listView.setAdapter(adapter);

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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView = (View) View.inflate(SecondActivity.this,R.layout.dialog,null);

                AlertDialog.Builder dlg = new AlertDialog.Builder(SecondActivity.this);
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
                            myHelper.onUpgrade(sqlDB,1,2);
                            sqlDB.close();
                            adapter.clearItem();

                            final String urlStr="https://dahun5223.iwinv.net/json_membership.php?id="+editTextId.getText().toString()+"&pw="+editTextPw.getText().toString();

                            final String urlStr1 = "https://dahun5223.iwinv.net/json_login.php";
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    requestUrl(urlStr);
                                    requestUrl(urlStr1);
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
                adapter.addItem(no,id,pw);
                adapter.notifyDataSetChanged();

            }
            sqlDB.close();
        }catch(JSONException e){
            e.printStackTrace();
        }

    }
}