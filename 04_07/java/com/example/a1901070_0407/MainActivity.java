package com.example.a1901070_0407;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Chronometer chrono;
    Button btnStar, btnEnd;
    RadioButton rdoCal,rdoTime;
    CalendarView calView;
    TimePicker timePicker;
    TextView textView;

    String sYear,sMonth,sDay,sHour,sMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chrono = (Chronometer) findViewById(R.id.chronometer1);
        btnStar = (Button) findViewById(R.id.btnstart);
        rdoCal =(RadioButton) findViewById(R.id.rdoCal);
        rdoTime = (RadioButton) findViewById(R.id.rdoTime);
        calView = (CalendarView) findViewById(R.id.calendarView);
        timePicker=(TimePicker)findViewById(R.id.timepicker);
        btnEnd=(Button) findViewById(R.id.button2);
        textView=(TextView) findViewById(R.id.textView);

        calView.setVisibility(View.INVISIBLE);
        timePicker.setVisibility(View.INVISIBLE);

        rdoCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calView.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.INVISIBLE);
            }
        });
        rdoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calView.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.VISIBLE);
            }
        });

        btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chrono.setBase(SystemClock.elapsedRealtime());
                chrono.start();
                chrono.setTextColor(Color.RED);
            }
        });

        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                sYear = Integer.toString(i);
                sMonth = Integer.toString(i1);
                sDay=Integer.toString(i2);


            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                sHour=Integer.toString(i);
                sMinute=Integer.toString(i1);
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!chrono.getContentDescription().equals("0 seconds")) {


                    if (sYear == null) {
                        Toast.makeText(getApplicationContext(), "먼저 날짜를 선택 하시오", Toast.LENGTH_SHORT).show();
                    } else if (sHour == null) {
                        Toast.makeText(getApplicationContext(), "먼저 시간을 설정 하시오", Toast.LENGTH_SHORT).show();
                    } else {

                        chrono.stop();
                        chrono.setTextColor(Color.BLUE);
                        textView.setText(sYear+"년 "+sMonth+"월 "+ sDay+"일 "+sHour+"시 "+sMinute+"분 예약됨");

                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"예약시작누르시오",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}