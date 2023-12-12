package com.example.b1901070_0927;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static int dx=0,dy=0;
    static int cx,r;
    static int cy, layoutWidth,layoutHeight;

    static int rx, ry,w;

    LinearLayout layout;
    SensorManager sensorManager;
    Sensor acc;

    static TextView textView;
    MyGrapicView myGrapicView;

    Button btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 화면 꺼짐 막기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 센서 메니저 만들어 놓기
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        acc=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        layout = (LinearLayout) findViewById(R.id.viewlayout);
        textView = (TextView) findViewById(R.id.textView);


        btn1 = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);


        myGrapicView= new MyGrapicView(this);
        layout.addView(myGrapicView);
//        MyThread myThread = new MyThread();
//        myThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener,acc,sensorManager.SENSOR_DELAY_NORMAL);
    }

    final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //sensorEvent.valuse는 기본적으로 float이고
            //여러개(거의3개)의 정보가 넘어와 배열로 넘어온다
            if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                textView.setText("x축 값 : "+sensorEvent.values[0]
                        +"  y축 값 : "+sensorEvent.values[1]
                        +"  z축 값 : "+sensorEvent.values[2]);

                dx=-(int)(sensorEvent.values[0]*2);
                dy=(int)(sensorEvent.values[1]*2);


                if(((cx-40+dx)>0&&(cx+40+dx<layoutWidth))){
                    cx=cx+dx;
                }
                if(((cy-40+dy)>0&&(cy+40+dy<layoutHeight))){
                    cy=cy+dy;
                }

                myGrapicView.invalidate();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        layoutHeight = layout.getHeight();
        layoutWidth = layout.getWidth();
        cx = layout.getWidth()/2;
        cy = layout.getHeight()/2;
        r = (int)(layoutHeight*0.02);

        Random random = new Random();

        w=r*4;
        rx = random.nextInt(layoutWidth-w);
        ry = random.nextInt(layoutHeight);

        myGrapicView.invalidate();
    }

    private static class MyGrapicView extends View {
        public MyGrapicView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint paint1 = new Paint();
            paint1.setColor(Color.BLUE);

            Paint paint = new Paint();
            paint.setColor(Color.RED);

            canvas.drawRect(rx,ry,rx+w,ry+w,paint1);
            canvas.drawCircle(cx,cy,r,paint);

            if(cx-r>rx&&cx+r<rx+w&&cy-r>ry&&cy+r<ry+w){
                textView.setText("도착함");
            }else{
                textView.setText("안들어옴");
            }
        }

    }

    private class MyThread extends Thread{
        public void run(){ // 동작할 내용 기술
            int i=0;
            while(true){
                i += 1;
                try {
                    Thread.sleep(100);
                    if(cx-40+dx>0) {
                        cx = cx + dx;
                        cy = cy + dy;
                    }
                    myGrapicView.invalidate();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Log.d("i = " , Integer.toString(i));
            }
        }
    }
}