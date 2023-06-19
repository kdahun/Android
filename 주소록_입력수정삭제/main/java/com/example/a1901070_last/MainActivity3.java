package com.example.a1901070_last;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Button btnre = (Button)findViewById(R.id.button5);
        Button btnde = (Button)findViewById(R.id.button6);
        EditText ed1= (EditText) findViewById(R.id.editTextTextPersonName4);
        EditText ed2= (EditText) findViewById(R.id.editTextTextPersonName5);

        Intent intent = getIntent();
        String name = intent.getStringExtra("rename");
        String num=intent.getStringExtra("renum");

        ed1.setText(name);
        ed2.setText(num);

        btnre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str1=ed1.getText().toString();
                String str2=ed2.getText().toString();
                Intent outIntent =new Intent(getApplicationContext(),
                        MainActivity.class);
                outIntent.putExtra("name1",str1);
                outIntent.putExtra("num1",str2);
                setResult(RESULT_OK,outIntent);
                finish();
            }
        });
        btnde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}