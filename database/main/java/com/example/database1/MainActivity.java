
package com.example.database1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    myDBHelper myHelper;
    EditText edtName,edtNumber,edtNameResult,edtNumberResult;
    Button btnInit,btnInsert,btnUpdate,btnDelete,btnSelect;
    SQLiteDatabase sqlDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = (EditText) findViewById(R.id.editTextTextPersonName);
        edtNumber = (EditText) findViewById(R.id.editTextTextPersonName2);
        edtNameResult = (EditText) findViewById(R.id.editTextTextPersonName3);
        edtNumberResult = (EditText) findViewById(R.id.editTextTextPersonName4);

        btnInit = (Button) findViewById(R.id.button);
        btnInsert = (Button)findViewById(R.id.button2);
        btnUpdate = (Button) findViewById(R.id.button3);
        btnDelete = (Button) findViewById(R.id.button4);
        btnSelect = (Button) findViewById(R.id.button5);

        myHelper = new myDBHelper(this);
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                myHelper.onUpgrade(sqlDB,1,2);
                sqlDB.close();
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO groupTBL VALUES ( '"
                        + edtName.getText().toString() + "' , "
                        + edtNumber.getText().toString() + ");");
                sqlDB.close();
                Toast.makeText(getApplicationContext(),"입력됨",Toast.LENGTH_SHORT).show();
                btnSelect.callOnClick();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                if(edtName.getText().toString()!=""){
                    sqlDB.execSQL("UPDATE groupTBL SET gNumber = "
                            + edtNumber.getText()+
                            " WHERE gName = '"+edtName.getText().toString()+"';"
                    );
                }
                sqlDB.close();
                Toast.makeText(getApplicationContext(),"수정됨",Toast.LENGTH_SHORT).show();
                btnSelect.callOnClick();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                if (edtName.getText().toString() != "") {
                    sqlDB.execSQL("DELETE FROM groupTBL WHERE gName = '"
                            + edtName.getText().toString() + "';");

                }
                sqlDB.close();

                Toast.makeText(getApplicationContext(), "삭제됨",
                        Toast.LENGTH_SHORT).show();
                btnSelect.callOnClick();
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);

                String strNumbers = "인원\n--------\n";
                String strNames = "그룹이름\n--------\n";

                while (cursor.moveToNext()) {
                    strNumbers += cursor.getString(1) + "\n";
                    strNames += cursor.getString(0) + "\n";
                }

                edtNameResult.setText(strNames);
                edtNumberResult.setText(strNumbers);

                cursor.close();
                sqlDB.close();
            }
        });
    }

    public class myDBHelper extends SQLiteOpenHelper{
        public myDBHelper(@Nullable Context context) {
            super(context,"groupDB",null,1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE  groupTBL ( gName CHAR(20) PRIMARY KEY, gNumber INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(sqLiteDatabase);
        }
    }
}

