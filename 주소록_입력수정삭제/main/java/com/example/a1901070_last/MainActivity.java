package com.example.a1901070_last;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    String nameadd;
    String numadd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MODE_PRIVATE);

        myHelper = new myDBHelper(this);
        Button btnadd=(Button) findViewById(R.id.button);
        Button btnbackup=(Button)findViewById(R.id.button2);
        Button btnreset=(Button) findViewById(R.id.button3);


        final ArrayList<String> midList=new ArrayList<>();
        midList.add("홍길동");
        sqlDB = myHelper.getWritableDatabase();
        sqlDB.execSQL("INSERT INTO groupTBL VALUES('"+"홍길동"+"' ,'"+"123-456-789"+"');");
        sqlDB.close();
        Toast.makeText(getApplicationContext(),"DB입력",Toast.LENGTH_SHORT).show();

        ListView list = (ListView) findViewById(R.id.listView);

        final ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice,midList);
        list.setAdapter(adapter);

        ActivityResultLauncher resultLauncher;
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
           if(result.getResultCode()==RESULT_OK) {
               nameadd=result.getData().getStringExtra("name");
               numadd=result.getData().getStringExtra("num");
               Toast.makeText(getApplicationContext(),nameadd+numadd,Toast.LENGTH_SHORT).show();
                midList.add(nameadd);
                adapter.notifyDataSetChanged();
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO groupTBL VALUES('"+nameadd+"' ,'"+numadd+"');");
                sqlDB.close();
                Toast.makeText(getApplicationContext(),"DB입력",Toast.LENGTH_SHORT).show();
           }
        });


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                resultLauncher.launch(intent);
            }
        });
        btnbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    FileOutputStream outFs = new FileOutputStream("/storage/emulated/0/1901070.txt");
                    sqlDB = myHelper.getReadableDatabase();
                    Cursor cursor;
                    cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;",null);
                    String strNames = "";
                    String strnum ="";
                    while(cursor.moveToNext()){
                        strNames+=cursor.getString(0)+" : "+cursor.getString(1)+"\r\n";
                        outFs.write(strNames.getBytes());
                    }
                    outFs.close();
                }catch(IOException e1){

                }
            }
        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("데이터베이스 초기화");
                dlg.setMessage("전화번호부 데이터베이스를 초기화 합니다.");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sqlDB = myHelper.getWritableDatabase();
                        myHelper.onUpgrade(sqlDB,1,2);
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("INSERT INTO groupTBL VALUES('"+"홍길동"+"' ,'"+"123-456-789"+"');");
                        sqlDB.close();
                        Toast.makeText(getApplicationContext(),"DB입력",Toast.LENGTH_SHORT).show();
                        sqlDB.close();
                    }
                });
                dlg.setNegativeButton("취소",null);
                dlg.show();

            }
        });

        ActivityResultLauncher resultLauncher2;
        resultLauncher2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
            if(result.getResultCode()==RESULT_OK) {
                nameadd=result.getData().getStringExtra("name1");
                numadd=result.getData().getStringExtra("num1");
                Toast.makeText(getApplicationContext(),nameadd+numadd,Toast.LENGTH_SHORT).show();
                midList.add(nameadd);
                adapter.notifyDataSetChanged();
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO groupTBL VALUES('"+nameadd+"' ,'"+numadd+"');");
                sqlDB.close();
                Toast.makeText(getApplicationContext(),"DB입력",Toast.LENGTH_SHORT).show();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;",null);
                String strNames1 = "";
                String strnum1 ="";
                while(cursor.moveToNext()){
                    if(midList.get(i).equals(cursor.getString(0))){
                        strNames1=cursor.getString(0);
                        strnum1=cursor.getString(1);
                        break;
                    }
                }
                Uri uri= Uri.parse("tel:"+strnum1);
                Intent intent4 = new Intent(Intent.ACTION_DIAL,uri);
                startActivity(intent4);

                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent2 = new Intent(getApplicationContext(),MainActivity3.class);
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;",null);
                String strNames1 = "";
                String strnum1 ="";
                while(cursor.moveToNext()){
                    if(midList.get(i).equals(cursor.getString(0))){
                        strNames1=cursor.getString(0);
                        strnum1=cursor.getString(1);
                        midList.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }

                intent2.putExtra("rename",strNames1);
                intent2.putExtra("renum",strnum1);
                resultLauncher2.launch(intent2);
            }
        });


    }
    public class myDBHelper extends SQLiteOpenHelper{

        public myDBHelper(@Nullable Context context) {
            super(context, "groupDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE groupTBL (name CHAR(20),number CHAR(20));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(sqLiteDatabase);
        }
    }
}