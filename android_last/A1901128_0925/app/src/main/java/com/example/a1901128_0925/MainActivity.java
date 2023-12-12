package com.example.a1901128_0925;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView1,textView2,textView3;

    SensorManager sensorManager;

    Sensor orientationSensor,accSensor,lightSensor;

    final SensorEventListener sensorEventListener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()==Sensor.TYPE_ORIENTATION){
            textView1.setText("ORIENTATION Sensor \n 방위각 : "+event.values[0]+"\n피치값 : "+event.values[1]+"\n롤 : "+event.values[2]);
            }
            else if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
            {
                textView2.setText("acc Sensor \n x축 : "+event.values[0]+"\ny축 : "+event.values[1]+"\nz축 : "+event.values[2]);
            }
            else if(event.sensor.getType()==Sensor.TYPE_LIGHT)
            {
                textView3.setText("Light Sensor \n 밝기값 : "+event.values[0]);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(sensorEventListener,orientationSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener,accSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView1=(TextView) findViewById(R.id.textView);
        textView2=(TextView) findViewById(R.id.textView2);
        textView3=(TextView) findViewById(R.id.textView3);

        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);

        orientationSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        accSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lightSensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // 센서 리스트 가져오기
//        String str="";
//        List<Sensor> sensors=sensorManager.getSensorList(Sensor.TYPE_ALL);
//        str+="전체 센서 수 : "+sensors.size()+"\n";
//        int i=1;
//        for(Sensor s:sensors)
//        {
//            str+="순번 : "+i+"\n";
//            str+="name : "+s.getName()+"\n";
//            str+="vender : "+s.getVendor()+"\n";
//            i++;
//        }
//        textView1.setMovementMethod(new ScrollingMovementMethod());
//        textView1.setText(str);
    }
}