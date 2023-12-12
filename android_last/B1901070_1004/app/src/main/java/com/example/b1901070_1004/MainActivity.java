package com.example.b1901070_1004;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    // Location lastLocation;
    TextView textView1,textView2,textView3,textView4,textView5,textView6;
    // Geocoder geocoder; //주소
    SimpleDateFormat dateFormat;

    Button startbtn,endbtn;

    double currentLat, currentLog, currentTime;
    double lastLat, lastLog,lastTime;
    double startLat,startLog,startTime;
    double endLat,endLog,endTime;

    double totalDis,totalTime;

    boolean sw = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        textView1 = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);

        startbtn = (Button) findViewById(R.id.button);
        endbtn = (Button) findViewById(R.id.button2);


        // geocoder = new Geocoder(this);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

        // registerForActivityResult 메서드를 사용하여 권한 요청을 위한 ActivityResultLauncher를 생성한다.
        //RequestMultiplePermissions를 사용하여 여러 개의 권한을 요청할 수 있도록 설정.

        ActivityResultLauncher<String[]> locationPermisstionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),result -> {
            //이 람다 함수는 권한 요청의 결과를 처리한다.
            //result 매개변수를 통해 사용자의 응답과 허용 여부를 확인할 수 있다.
            // Manifest.permission.ACCESS_FINE_LOCATION과 Manifest.permission.ACCESS_COARSE_LOCATION 권한에 대한 사용자의 응답을 확인한다.


            //result : 이 변수는 권한 요청 결과를 나타낸다. 권한 요청의 결과는 사용자가 권한을 부여하거나 거부한 경우에 대한 정보를 포함하고 있다
            Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION,false);
            Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION,false);

            if(fineLocationGranted!=null&&fineLocationGranted){
                Toast.makeText(getApplicationContext(),"자세한 위치권한 허용됨.",Toast.LENGTH_SHORT).show();
            }else if(coarseLocationGranted!=null&&coarseLocationGranted){
                Toast.makeText(getApplicationContext(), "대략적 위치권한 허용됨", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "권한 없음", Toast.LENGTH_SHORT).show();
            }
        });


        // 권한을 받았으면 다음부터는 권한 요청을 보내지 않는다.
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            locationPermisstionRequest.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION});
        }

        // lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,locationListener);

//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,2000,1,locationListener);
//
//        textView3.setText("제공자 : "+lastLocation.getProvider()+"\n위도 : "+lastLocation.getLatitude()+"\n경도 : "+lastLocation.getLatitude()+
//                "\n생성시간 : "+dateFormat.format(lastLocation.getTime()));

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLat = currentLat;
                startLog = currentLog;
                startTime = System.currentTimeMillis();
                textView2.setText("제공자 : "+" \n위도 : "+startLat+" \n경도 : "+startLog+" \n시간 : "+ dateFormat.format(startTime));
                sw = true;
            }
        });

        endbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endLat = currentLat;
                endLog = currentLog;
                endTime = System.currentTimeMillis();
                textView3.setText("제공자 : "+" \n위도 : "+endLat+" \n경도 : "+endLog+" \n시간 : "+ dateFormat.format(endTime));

                double distanceLatLog = distance(startLat,startLog,endLat,endLog);
                double timeDiff = (endTime-startTime)/1000; // 밀리 세컨드 고려하지 않기 위해서 /1000
                double averageSpeed= distanceLatLog/timeDiff;

                textView4.setText("이동 거리 : "+distanceLatLog+" \n시간 : "+timeDiff+" \n평균 속도 : "+ averageSpeed);
                sw=false;
            }
        });
    }

    final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            // List<Address> address=null;;
            String provider=location.getProvider();
            currentLat=location.getLatitude();
            currentLog=location.getLongitude();
            currentTime = System.currentTimeMillis();

            // double altitude=location.getAltitude();

            textView1.setText("제공자 : "+provider+" \n위도 : "+currentLat+" \n경도 : "+currentLog+" \n시간 : "+ dateFormat.format(currentTime));

            totalDis+=distance(lastLat,lastLog,currentLat,currentLog);
            totalTime=totalTime+(currentTime-lastTime)/1000;

            if(sw){

                totalDis+=distance(lastLat,lastLog,currentLat,currentLog);
                totalTime=totalTime+(currentTime-lastTime)/1000;
                double avageSpeed = totalDis/totalTime;
                textView5.setText("누적 거리 : "+totalDis+" \n누적 시간 : "+totalTime+" \n속도 : "+ avageSpeed);
            }

            double nowSpeed = distance(lastLat,lastLog,currentLat,currentLog);
            double timeDiff = (currentTime-lastTime)/1000;
            double speedMs=nowSpeed/timeDiff; // 메타퍼 섹크
            int speedKh = (int)((nowSpeed/1000)/(timeDiff*3600));

            textView5.setText("순간속도 : "+nowSpeed/timeDiff+"\n 순간속도 Km : "+(nowSpeed/1000)/(timeDiff*3600));

            lastLat=currentLat;
            lastLog = currentLog;
            lastTime = currentTime;

//            try{
//                address=geocoder.getFromLocation(latitude,longitude,5);
//                textView2.setText(address.get(0).getAddressLine(0).toString());
//            }catch (IOException e)
//            {
//                e.printStackTrace();
//            }
        }
    };

    private static double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;


        dist = dist * 1609.344;// 미터로 구하기

        return (dist);
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}