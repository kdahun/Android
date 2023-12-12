package com.example.b19010700911;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    EditText editText1;
    EditText editText2;
    TextView textView;
    Button btn;
    View dlgView;
    EditText dlgEdit;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"덧셈");
        menu.add(0,2,0,"뺄셈");
        menu.add(0,3,0,"곱셈");
        menu.add(0,4,0,"나눗셈");


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 1:
                editText1.setText("+");
                break;
            case 2:
                editText1.setText("-");
                break;
            case 3:
                editText1.setText("*");
                break;
            case 4:
                editText1.setText("/");
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,1,0,"빨강");
        menu.add(0,2,0,"초록");
        menu.add(0,3,0,"파랑");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 1:
                btn.setBackgroundColor(Color.rgb(255,0,0));
                break;
            case 2:
                btn.setBackgroundColor(Color.rgb(0,255,0));
                break;
            case 3:
                btn.setBackgroundColor(Color.rgb(0,0,255));
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("calculate");
        editText = (EditText) findViewById(R.id.editTextText);
        editText1 = (EditText) findViewById(R.id.editTextText2);
        editText2 = (EditText) findViewById(R.id.editTextText3);

        textView = (TextView) findViewById(R.id.textView4);

        btn = (Button) findViewById(R.id.button);

        registerForContextMenu(btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Integer a,b;
                    a = Integer.parseInt(editText.getText().toString());
                    String c = editText1.getText().toString();
                    b = Integer.parseInt(editText2.getText().toString());

                    //Toast.makeText(getApplicationContext(),editText.getText().toString()+editText1.getText().toString()+editText2.getText().toString(),Toast.LENGTH_SHORT).show();

                    String s,sum;
                    switch (c){
                        case "+":
                            sum = Integer.toString(a+b);
                            textView.setText("결과 : "+sum);
                            break;
                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"입력오류입니다.",Toast.LENGTH_SHORT).show();
                }

            }
        });
        /*
        * 만약 editText에 setonclickLisener를 하면 포커싱이 풀
        * */
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // hasFocus가 ture이면 들어갈떄 false이면 나갈떄
                if(hasFocus){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("대화상자 제목");

                    //dlg.setMessage("대화상자 내용");
                    dlgView = (View) View.inflate(MainActivity.this,R.layout.dialog,null);
                    dlg.setView(dlgView);

                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dlgEdit = (EditText) dlgView.findViewById(R.id.editTextText4);
                            editText.setText(dlgEdit.getText().toString());
                        }
                    });
                    dlg.setNegativeButton("취소",null);
                    dlg.show();
                }

            }
        });

    }
}