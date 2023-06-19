package com.example.a1901070_last;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        EditText ed1=(EditText) findViewById(R.id.editTextTextPersonName2);
        EditText ed2=(EditText)findViewById(R.id.editTextTextPersonName3);
        Button button=(Button)findViewById(R.id.buttonadd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str1=ed1.getText().toString();
                String str2=ed2.getText().toString();
                Intent outIntent =new Intent(getApplicationContext(),
                        MainActivity.class);
                outIntent.putExtra("name",str1);
                outIntent.putExtra("num",str2);
                setResult(RESULT_OK,outIntent);
                finish();
            }
        });
    }
}