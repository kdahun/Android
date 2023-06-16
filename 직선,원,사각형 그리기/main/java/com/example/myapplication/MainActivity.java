package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    final static int LINE =1,CIRCLE=2,Rect=3,Red=4,Green=5,Blue=6;
    static int curShape = LINE;
    static int setColor = Color.RED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyGraphicView(this));
        setTitle("간단 그림판");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0,1,0,"선 그리기");
        menu.add(0,2,0,"원 그리기");
        menu.add(0, 3,0,"사각형 그리기");
        SubMenu sMenu = menu.addSubMenu("색 >>");
        sMenu.add(0,4,0,"빨강");
        sMenu.add(0,5,0,"초록");
        sMenu.add(0,6,0,"파랑");



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case 1:
                curShape = LINE;
                return true;
            case 2:
                curShape = CIRCLE;
                return true;
            case 3:
                curShape = Rect;
                return true;
            case 4:
                setColor = Color.RED;
                return true;
            case 5:
                setColor = Color.GREEN;
                return true;
            case 6:
                setColor = Color.BLUE;
                return true;

        }
        return super.onOptionsItemSelected(item);
    }



    private static class MyGraphicView extends View{
        int startX=0,startY=0,stopX=0,stopY=0;
        ArrayList<MyShape> myShapeArrayList = new ArrayList<>();
        MyShape currentShape;

        public MyGraphicView(Context context) {
            super(context);

        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    startX = (int)event.getX();
                    startY=(int)event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    stopX=(int)event.getX();
                    stopY=(int)event.getY();
                    this.invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    myShapeArrayList.add(currentShape);

            }
            return true;
        }


        @Override
        protected void onDraw(Canvas canvas) {

            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);//원은 안에 비어있게
            paint.setColor(setColor);

            currentShape = new MyShape(curShape,startX,startY,stopX,stopY,paint);

            for(MyShape i:myShapeArrayList){
                draw_shape(i,canvas);
            }
            if(currentShape!=null) {
                draw_shape(currentShape, canvas);
            }
        }

        public void draw_shape(MyShape myShape,Canvas canvas){
            switch(myShape.shape_type){
                case LINE:
                    canvas.drawLine(myShape.startX,myShape.startY,myShape.stopX,myShape.stopY,myShape.paint);
                    break;
                case CIRCLE:
                    int radius = (int)Math.sqrt(Math.pow(myShape.stopX-myShape.startX,2)+Math.pow(myShape.stopY-myShape.startY,2));
                    canvas.drawCircle(myShape.startX,myShape.startY,radius,myShape.paint);
                    break;
                case Rect:
                    Rect rect = new Rect(myShape.startX,myShape.startY,myShape.stopX,myShape.stopY);
                    canvas.drawRect(rect,myShape.paint);
                    break;
            }
        }
        private static class MyShape{
            int shape_type,startX,startY,stopX,stopY;
            Paint paint;
            public MyShape(int s,int startX,int startY,int stopX,int stopY,Paint paint){
                this.shape_type=s;
                this.startX = startX;
                this.startY = startY;
                this.stopX= stopX;
                this.stopY=stopY;
                this.paint = paint;
            }
        }

    }


}
