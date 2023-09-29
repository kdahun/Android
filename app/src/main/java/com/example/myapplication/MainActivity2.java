package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity2 extends AppCompatActivity {
    LinearLayout layout; // 게임 화면
    SensorManager sensorManager;
    Sensor acc;
    static TextView textView;

    static int dx=0,dy=0;
    static int cx, cy,r; // 원의 좌표 및 반지름
    static int layoutWidth,layoutHeight; // 디스플레이 크기
    static int rx,ry,w;// 정사각형 오른쪽 위 좌표와 길이

    MyGrapicView myGrapicView;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {// 이 메서드는 화면의 포커스가 변경될 떄 호출된다.
        // hasFocus 매개변수는 화면이 포커스를 얻었는지 또는 잃었는지 여부를 나타내는 부울 값이다.
        super.onWindowFocusChanged(hasFocus);

        // 디스플레이 크기 확인?
        layoutHeight = layout.getHeight();
        layoutWidth = layout.getWidth();

        // 원의 위치를 디스플레이 가운데에 위치시키기 위해서
        cx = layout.getWidth()/2;
        cy = layout.getHeight()/2;

        // 원의 반지름
        r= (int)(layoutHeight*0.02);

        Random random = new Random();

        w=r*4;
        rx = random.nextInt(layoutWidth-w);
        ry = random.nextInt(layoutHeight-w);

        myGrapicView.invalidate();
    }

    // 사용자 지정 뷰를 지정
    private static class MyGrapicView extends View {
        // View 클래스 상속
        // View 클래스는 화면에 그래픽을 그리기 위한 기본적인 뷰를 나타내고
        // 여기에서 이 클래스를 확장하여 사용자 지정 그래픽을 그릴 수 있다.
        public MyGrapicView(Context context) {
            // context는 Android 앱의 실행환경에 대한 정보를 포함
            // 앱의 리소스에 액세스하거나 시스템 서비스와 상호작용 하는 데 사용
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) { // 그래픽을 그리는 메서드
            //canvas는 그림을 그리는 데 사용되는 그래픽 컨텍스트를 제공한다.
            super.onDraw(canvas);

            Paint paintC = new Paint();
            paintC.setColor(Color.RED);

            Paint paintR = new Paint();
            paintR.setColor(Color.BLUE);

            canvas.drawRect(rx,ry,rx+w,ry+w,paintR);
            canvas.drawCircle(cx,cy,r,paintC);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        layout = (LinearLayout) findViewById(R.id.viwlayout);

        textView=(TextView) findViewById(R.id.textView2);

        myGrapicView = new MyGrapicView(this);
        layout.addView(myGrapicView);
    }
}