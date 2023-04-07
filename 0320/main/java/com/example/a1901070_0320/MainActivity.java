package com.example.a1901070_0320;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edit1, edit2; // 숫자 넣기
    Button btnAdd, btnSub,btnMul,btnDiv;// 버튼

    Button[] numButtons = new Button[10];
    int[] numBtnIDs={R.id.btnNum0,R.id.btnNum1,R.id.btnNum2,R.id.btnNum3,R.id.btnNum4,
            R.id.btnNum5,R.id.btnNum6,R.id.btnNum7,R.id.btnNum8,R.id.btnNum9};

    TextView textResult; // 계산결과

    String num1,num2;
    Double result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("초간단 계산기");

        edit1=(EditText)findViewById(R.id.editTextTextPersonName2);
        edit2=(EditText)findViewById(R.id.editTextTextPersonName);

        btnAdd=(Button)findViewById(R.id.button);
        btnDiv=(Button)findViewById(R.id.button4);

        textResult=(TextView) findViewById(R.id.textView);

        for(int i=0;i<10;i++){
            numButtons[i]=(Button) findViewById(numBtnIDs[i]);
        }

        for(int i=0;i<10;i++){
            final int index;
            index=i;
            numButtons[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(edit1.isFocused()==true){
                        num1=edit1.getText().toString()+numButtons[index].getText().toString();
                        //edit1.setText(num1);
                        edit1.append(numButtons[index].getText().toString());
                    }else if(edit2.isFocused()==true){
                        num2=edit2.getText().toString()+numButtons[index].getText().toString();
                        //edit2.setText(num2);
                    }else{
                        Toast.makeText(getApplicationContext(),"에디트텍스트를 선택하세요.",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    num1 = edit1.getText().toString();
                    num2 = edit2.getText().toString();

                    result = Double.parseDouble(num1) + Double.parseDouble(num2);
                    textResult.setText("계산 결과 : " + result.toString());
                }catch (Exception e){
                    textResult.setText("계산 결과 : 입력오류입니다.");
                    Toast.makeText(getApplicationContext(),"입력오류입니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDiv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    num1 = edit1.getText().toString();
                    num2 = edit2.getText().toString();
                    if(num2.trim().equals("0")){// trim() : 앞뒤의 공백을 없애준다
                        textResult.setText("계산 결과 : 0으로 나눌수 없습니다.");
                    }else{
                        result = Double.parseDouble(num1) / Double.parseDouble(num2);
                        textResult.setText("계산 결과 : " + result.toString());
                    }


                }catch (Exception e){
                    textResult.setText("계산 결과 : 입력오류입니다.");
                    Toast.makeText(getApplicationContext(),"입력오류입니다.",Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });


    }
}