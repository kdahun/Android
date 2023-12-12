package com.example.a1901128_1030;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.inputmethodservice.ExtractEditText;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    myDBHelper myHelper;

    SQLiteDatabase sqlDB;
    EditText editText1,editText2;
    Button btnInit,btnInsert,btnModify;
    TextView textView3,textView4,textView5;
    ListView listView;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("전화번호부");
        btnInit=(Button) findViewById(R.id.button3);
        btnInsert=(Button) findViewById(R.id.button4);
        btnModify=(Button) findViewById(R.id.button5);
        editText1=(EditText) findViewById(R.id.editTextText);
        editText2=(EditText) findViewById(R.id.editTextText2);
        listView=(ListView) findViewById(R.id.listView1);
        ListViewAdapter adapter=new ListViewAdapter();
        listView.setAdapter(adapter);
//        textView3=(TextView) findViewById(R.id.textView3);
//        textView4=(TextView) findViewById(R.id.textView4);
//        textView5=(TextView) findViewById(R.id.textView5);

       // adapter.addItem(1,"aaa","1234");
        myHelper=new myDBHelper(this,"phonebookDB",null,1);

        sqlDB=myHelper.getReadableDatabase();
        String sql = "select * from phonebooktable";
        Cursor cursor=sqlDB.rawQuery(sql,null);
        String strNo="No\n";
        String strName="이름\n";
        String strtel="전화번호\n";
        while(cursor.moveToNext())
        {
            adapter.addItem(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
            Log.d("ITEM_ADD",cursor.getString(2));
        }



        adapter.notifyDataSetChanged();
        sqlDB.close();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item= adapter.getItem(position);
                editText1.setText(item.getName());
                editText2.setText(item.getTel());
                Toast.makeText(getApplicationContext(),"클릭"+item.getNo(),Toast.LENGTH_SHORT).show();
                position = item.getNo();
            }

        });

        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg=new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("데이터 초기화");
                dlg.setMessage("데이터를 초기화 하시겠습니까?");
                dlg.setPositiveButton("초기화", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqlDB=myHelper.getWritableDatabase();
                        myHelper.onUpgrade(sqlDB,1,2);
                        sqlDB.close();
                        adapter.clearItem();
                        adapter.notifyDataSetChanged();
                    }
                });
                dlg.setNegativeButton("취소",null);
                dlg.show();

            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB=myHelper.getWritableDatabase();
                sqlDB.execSQL("insert into phonebooktable (name,tel) values ('"+editText1.getText().toString()+"','"+editText2.getText().toString()+"')");
                sqlDB.close();
                sqlDB=myHelper.getReadableDatabase();
                String sql = "select * from phonebooktable";
                Cursor cursor=sqlDB.rawQuery(sql,null);
                cursor.moveToLast();
                adapter.addItem(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
                adapter.notifyDataSetChanged();
                cursor.close();
                sqlDB.close();
            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB=myHelper.getWritableDatabase();

                String sql = "update phonebooktable set name = '"+editText1.getText().toString()+"',tel ='"+editText2.getText().toString()+"';" ;
                sqlDB .execSQL(sql);
                sqlDB.close();

                ListViewItem item = new ListViewItem();

            }
        });

        listView .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item= adapter.getItem(position);
                AlertDialog.Builder dlg=new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("데이터 삭제");
                dlg.setMessage(item.getName()+"을 삭제하시겠습니까?");

                dlg.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        sqlDB=myHelper.getWritableDatabase();
                        String sql = "delete from phonebooktable where no = "+item.getNo();
                        sqlDB .execSQL(sql);
                        sqlDB.close();
                        adapter.deleteItem(position);
                        adapter.notifyDataSetChanged(); // 어댑터에 변경 알림
                    }
                });
                dlg.setNegativeButton("취소",null);
                dlg.show();
                return false;
            }
        });
    }


    public class myDBHelper extends SQLiteOpenHelper{
        public myDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table phonebooktable (no integer primary key autoincrement,name char(20),tel char(20))");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists phonebooktable");
            onCreate(db);

        }
    }
}