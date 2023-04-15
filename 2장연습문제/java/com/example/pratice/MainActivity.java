package com.example.pratice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn1;
    Button btn2;
    TextView textView;
    RadioButton rdo1;
    RadioButton rdo2;
    ImageView img;
    Intent mInternet = new Intent(Intent.ACTION_VIEW, Uri.parse("https://naver.com"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.t);

        setTitle("dkdk");

        btn1=(Button) findViewById(R.id.button);
        btn2=(Button)findViewById(R.id.button2);
        textView=(TextView) findViewById(R.id.textView);
        rdo1=(RadioButton) findViewById(R.id.radioButton);
        rdo2=(RadioButton) findViewById(R.id.radioButton2);
        img=(ImageView)findViewById(R.id.imageView);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=textView.getText().toString();
                if(text.equals("")){
                    Toast.makeText(getApplicationContext(),"글자를 입력해 주세요.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), text,Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(mInternet);
            }
        });

        rdo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.setImageResource(R.drawable.snow_corn);
            }
        });

        rdo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.setImageResource(R.drawable.tiramisu01);
            }
        });
    }
}