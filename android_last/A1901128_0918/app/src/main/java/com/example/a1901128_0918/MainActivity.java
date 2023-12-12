package com.example.a1901128_0918;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Button btn,btn2;
    EditText editText1,editText2;

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("메인 엑티비티");

        Toast.makeText(getApplicationContext(),"onCreate",Toast.LENGTH_SHORT).show();

        btn=(Button) findViewById(R.id.button);
        editText1=(EditText) findViewById(R.id.editTextText);
        editText2=(EditText) findViewById(R.id.editTextText2);
        textView=(TextView) findViewById(R.id.textView5);
        btn2=(Button) findViewById(R.id.button3);


        ActivityResultLauncher resultLauncher;

        resultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
            if(result.getResultCode()==RESULT_OK)
        {
            Integer res=result.getData().getIntExtra("Result",0);
            textView.setText("계산결과 : "+res.toString());
        }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SubActivity.class);
                intent.putExtra("Num1",Integer.parseInt(editText1.getText().toString()));
                intent.putExtra("Num2",Integer.parseInt(editText2.getText().toString()));
                resultLauncher.launch(intent);
               // startActivityForResult(intent,0);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:119"));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(),"onStart",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(),"onSop",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"onDestroy",Toast.LENGTH_SHORT).show();
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==RESULT_OK)
//        {
//            Integer result=data.getIntExtra("Result",0);
//            textView.setText("계산결과 : "+result.toString());
//        }
//
//    }
}