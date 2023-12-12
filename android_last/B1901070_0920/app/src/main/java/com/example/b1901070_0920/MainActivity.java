package com.example.b1901070_0920;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    static int dx=0,dy=0;
    static int cx;
    static int cy;
    LinearLayout layout;
    TextView textView;
    MyGrapicView myGrapicView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.LinearLayout1);
        textView = (TextView) findViewById(R.id.textView);

        Button btn1,btn2,btn3,btn4;

        btn1 = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dy=-10;
                dx=0;
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dy=10;
                dx=0;
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dx>0){
                    dx=0;
                }
                dy=0;
                dx+=-10;
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dx<0){
                    dx=0;
                }
                dy=0;
                dx+=10;
            }
        });

        myGrapicView= new MyGrapicView(this);
        layout.addView(myGrapicView);

        MyThread myThread = new MyThread();
        myThread.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        textView.setText("너비는 : "+layout.getWidth() + " 높이 : "+layout.getHeight());

        cx = layout.getWidth()/2;
        cy = layout.getHeight()/2;
        myGrapicView.invalidate();
    }

    private static class MyGrapicView extends View {

        public MyGrapicView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint paint = new Paint();
            paint.setColor(Color.RED);
            canvas.drawCircle(cx,cy,40,paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    cx=(int)event.getX();
                    cy=(int)event.getY();
                    invalidate();
            }


            return super.onTouchEvent(event);
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