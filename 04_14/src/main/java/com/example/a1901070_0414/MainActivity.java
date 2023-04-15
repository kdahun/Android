package com.example.a1901070_0414;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LinearLayout baseLayout;
    Button button;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //MenuInflater mInflater =getMenuInflater();
        //mInflater.inflate(R.menu.menu1,menu);

        menu.add(0,1,0,"빨강");
        menu.add(0,2,0,"초록");
        menu.add(0,3,0,"파랑");
        SubMenu subMenu = menu.addSubMenu("버튼 변경");
        subMenu.add(0,4,0,"45도 회전");
        subMenu.add(0,5,0,"2배 확장");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case 1:
                baseLayout.setBackgroundColor(Color.RED);
                break;

            case 2:
                baseLayout.setBackgroundColor(Color.GREEN);
                break;

            case 3:
                baseLayout.setBackgroundColor(Color.BLUE);
                break;
            case 4:
                button.setRotation(45);
                break;
            case 5:
                button.setScaleX(2);
                button.setScaleY(2);
                break;


        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mInflater = getMenuInflater();

        mInflater.inflate(R.menu.menu1,menu);


    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.itemRed:
                baseLayout.setBackgroundColor(Color.RED);
                break;

            case R.id.itemGreen:
                baseLayout.setBackgroundColor(Color.GREEN);
                break;

            case R.id.itemBlue:
                baseLayout.setBackgroundColor(Color.BLUE);
                break;
            case R.id.subRotate:
                button.setRotation(45);
                break;
            case R.id.subSize:
                button.setScaleX(2);
                button.setScaleY(2);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baseLayout = (LinearLayout) findViewById(R.id.baseLayout);
        button=(Button) findViewById(R.id.button);

        registerForContextMenu(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"토스트 연습",Toast.LENGTH_LONG).show();


            }
        });

    }
}