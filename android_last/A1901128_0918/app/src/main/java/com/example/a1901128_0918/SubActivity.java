package com.example.a1901128_0918;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity {

    Button btn;
    TextView textView1,textView2;
    EditText editText;
    Integer num1,num2,result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        setTitle("서브 액티비티");
        btn=(Button) findViewById(R.id.button2);
        textView1=(TextView) findViewById(R.id.textView3);
        textView2=(TextView) findViewById(R.id.textView4);
        editText=(EditText) findViewById(R.id.editTextText3);

        Intent inIntent=getIntent();
        num1=inIntent.getIntExtra("Num1",0);
        num2=inIntent.getIntExtra("Num2",0);

        textView1.setText("숫자 1 : "+num1.toString());
        textView2.setText("숫자 2 : "+num2.toString());



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals("+"))
                {
                    result=num1+num2;
                }
                Intent outIntent=new Intent(getApplicationContext(), MainActivity.class);
                outIntent.putExtra("Result",result);
                setResult(RESULT_OK,outIntent);
                finish();
            }
        });
    }
}