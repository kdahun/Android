package com.example.jsondbphonebook1030;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
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

public class MainActivity extends AppCompatActivity {


    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    EditText editText1,editText2;
    Button btnInit, btnInsert, btnModify;
    ListView listView;
    Integer selectNo;
    ListViewAdapter adapter;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("전화번호부");

        btnInit = (Button) findViewById(R.id.button);
        btnInsert = (Button) findViewById(R.id.button2);
        btnModify = (Button) findViewById(R.id.button3);

        editText1 = (EditText) findViewById(R.id.editTextText);
        editText2 = (EditText) findViewById(R.id.editTextText2);

        adapter = new ListViewAdapter();

        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);


        myHelper = new myDBHelper(this,"phonebookDB",null,1);

        sqlDB = myHelper.getWritableDatabase();
        myHelper.onUpgrade(sqlDB,1,2);
        sqlDB.close();
        adapter.clearItem();
        adapter.notifyDataSetChanged();


        final String urlStr = "https://dahun5223.iwinv.net/json.php";

        new Thread(new Runnable() {
            @Override
            public void run() {
                requestUrl(urlStr);
            }
        }).start();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item = adapter.getItem(position);
                selectNo = item.getNo();
                editText1.setText(item.getName());
                editText2.setText(item.getTel());
                Toast.makeText(getApplicationContext(),"no : "+selectNo,Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item = adapter.getItem(position);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("데이터 삭제");
                dlg.setMessage(item.getNo()+"를 삭제 하시겠습니까?");
                dlg.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String urlStr = "https://dahun5223.iwinv.net/json_delete.php?no="+selectNo;

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                requestUrl(urlStr);
                            }
                        }).start();

                        sqlDB = myHelper.getWritableDatabase();
                        myHelper.onUpgrade(sqlDB,1,2);
                        sqlDB.close();
                        adapter.clearItem();
                        adapter.notifyDataSetChanged();


                        final String urlStr2 = "https://dahun5223.iwinv.net/json.php";

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                requestUrl(urlStr2);
                            }
                        }).start();
                    }
                });
                dlg.show();

                return false;
            }
        });

        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("데이터 초기화");
                dlg.setMessage("데이터를 초기화 하시겠습니까?");
                dlg.setPositiveButton("초기화", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqlDB = myHelper.getWritableDatabase();
                        myHelper.onUpgrade(sqlDB,1,2);
                        sqlDB.close();
                        adapter.clearItem();
                        adapter.notifyDataSetChanged();


                        final String urlStr = "https://dahun5223.iwinv.net/json.php";

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                requestUrl(urlStr);
                            }
                        }).start();
                    }
                });
                dlg.setNegativeButton("취소",null);
                dlg.show();
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText1.getText().toString().equals("")||editText2.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"값을 잘 넣어주세요",Toast.LENGTH_SHORT).show();
                }else{

                    sqlDB = myHelper.getWritableDatabase();
                    myHelper.onUpgrade(sqlDB,1,2);
                    sqlDB.close();
                    adapter.clearItem();
                    adapter.notifyDataSetChanged();

                    final String urlStr = "https://dahun5223.iwinv.net/json_insert.php?name="+editText1.getText().toString()+"&tel="+editText2.getText().toString().toString();
                    final String urlStr2 = "https://dahun5223.iwinv.net/json.php";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            requestUrl(urlStr);
                            requestUrl(urlStr2);
                        }
                    }).start();


                }
            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase();
                myHelper.onUpgrade(sqlDB,1,2);
                sqlDB.close();
                adapter.clearItem();
                adapter.notifyDataSetChanged();

                final String urlStr = "https://dahun5223.iwinv.net/json_modify.php?no="+selectNo+"&name="+editText1.getText().toString()+"&tel="+editText2.getText().toString().toString();
                final String urlStr2 = "https://dahun5223.iwinv.net/json.php";

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        requestUrl(urlStr);
                        requestUrl(urlStr2);
                    }
                }).start();
            }
        });
    }

    public class myDBHelper extends SQLiteOpenHelper {

        public myDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table phonebooktable (no integer primary key autoincrement, name char(20),tel char(20))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists phonebooktable");
            onCreate(db);
        }
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
            JSONArray phoneBookArray = jsonObject.getJSONArray("PhoneBook");

            sqlDB = myHelper.getWritableDatabase();

            for(int i=0;i<phoneBookArray.length();i++){
                JSONObject memberObject = phoneBookArray.getJSONObject(i);

                Integer no =memberObject.getInt("no");
                String name = memberObject.getString("name");
                String tel = memberObject.getString("tel");

                String sqli = "insert into phonebooktable values ("+no+",'"+name+"','"+tel+"')";

                sqlDB.execSQL(sqli);

                adapter.addItem(no,name,tel);
            }
            sqlDB.close();
            adapter.notifyDataSetChanged();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}