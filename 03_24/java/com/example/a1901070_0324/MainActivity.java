package com.example.a1901070_0324;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView text1,text2;
    CheckBox checkBox;
    RadioGroup radioGroup;
    RadioButton rdoArr[]=new RadioButton[3];
    Button button;
    ImageView imageView;
    LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBox = (CheckBox)  findViewById(R.id.checkBox);
        text2=(TextView) findViewById(R.id.textView2);
        radioGroup=(RadioGroup) findViewById(R.id.radioGroup);
        button =(Button) findViewById(R.id.button);
        imageView =(ImageView) findViewById(R.id.imageView);
        layout=(LinearLayout) findViewById(R.id.layout);
        rdoArr[0]=(RadioButton) findViewById(R.id.dog);
        rdoArr[1]=(RadioButton)findViewById(R.id.cat);
        rdoArr[2]=(RadioButton) findViewById(R.id.rabbit);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox.isChecked()==true){
                    layout.setVisibility(View.VISIBLE);
                } else{
                    layout.setVisibility(View.INVISIBLE);
                }

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.dog:
                        imageView.setImageResource(R.drawable.dog2);
                        break;
                    case R.id.cat:
                        imageView.setImageResource(R.drawable.cat);
                        break;
                    case R.id.rabbit:
                        imageView.setImageResource(R.drawable.rabbit);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "동물을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final int draw[]={R.drawable.dog2,R.drawable.cat,R.drawable.rabbit};
        for(int i=0;i<3;i++) {
            final int index;
            index=i;
            rdoArr[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageView.setImageResource(draw[index]);
                }
            });
        }
    }
}