package com.example.b1901070_1016;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    double currentLat, currentLog, currentTime;
    double lastLat, lastLog, lastTime;
    LocationManager locationManager;
    GoogleMap gMap;
    SupportMapFragment mapFragment;
    boolean toggle = true;
    int mapType = GoogleMap.MAP_TYPE_SATELLITE;
    PolylineOptions polylineOptions;
    ArrayList<LatLng> arrayList;
    boolean sw = false;
    TextView textView, textView2;
    Button btnZoomIn, btnZoomOut, btnMapType, btnStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this); // 동기화를 시켜주면 = > onMapReady가 실행된다.

        btnZoomIn = (Button) findViewById(R.id.button);
        btnZoomOut = (Button) findViewById(R.id.button2);
        btnMapType = (Button) findViewById(R.id.button3);
        btnStart = (Button) findViewById(R.id.button4);

        arrayList = new ArrayList<LatLng>();

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        ActivityResultLauncher<String[]> locationPermisstionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            //이 람다 함수는 권한 요청의 결과를 처리한다.
            //result 매개변수를 통해 사용자의 응답과 허용 여부를 확인할 수 있다.
            // Manifest.permission.ACCESS_FINE_LOCATION과 Manifest.permission.ACCESS_COARSE_LOCATION 권한에 대한 사용자의 응답을 확인한다.


            //result : 이 변수는 권한 요청 결과를 나타낸다. 권한 요청의 결과는 사용자가 권한을 부여하거나 거부한 경우에 대한 정보를 포함하고 있다
            Boolean fineLocationGranted = result.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false);
            Boolean coarseLocationGranted = result.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false);

            if (fineLocationGranted != null && fineLocationGranted) {
                Toast.makeText(getApplicationContext(), "자세한 위치권한 허용됨.", Toast.LENGTH_SHORT).show();
            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                Toast.makeText(getApplicationContext(), "대략적 위치권한 허용됨", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "권한 없음", Toast.LENGTH_SHORT).show();
            }
        });


        // 권한을 받았으면 다음부터는 권한 요청을 보내지 않는다.
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermisstionRequest.launch(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        }

        // lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);

//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,2000,1,locationListener);
//
//        textView3.setText("제공자 : "+lastLocation.getProvider()+"\n위도 : "+lastLocation.getLatitude()+"\n경도 : "+lastLocation.getLatitude()+
//                "\n생성시간 : "+dateFormat.format(lastLocation.getTime()));


        btnZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gMap.moveCamera(CameraUpdateFactory.zoomIn());
            }
        });
        btnZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gMap.moveCamera(CameraUpdateFactory.zoomOut());
            }
        });
        btnMapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapType == GoogleMap.MAP_TYPE_SATELLITE) {
                    gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mapType = GoogleMap.MAP_TYPE_NORMAL;
                    btnMapType.setText("위성");
                } else if (mapType == GoogleMap.MAP_TYPE_NORMAL) {
                    gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    mapType = GoogleMap.MAP_TYPE_SATELLITE;
                    btnMapType.setText("일반");
                }

            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw) {
                    sw = false;
                    btnStart.setText("시작");
                } else {
                    sw = true;
                    arrayList.clear(); // 리스트 초기화
                    gMap.clear();// 마커랑 폴리라인을 없애준다.
                    btnStart.setText("종료");
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(googleMap.MAP_TYPE_SATELLITE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gMap.setMyLocationEnabled(true);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(35.94471, 126.6828),18 ));
        // 지도 클릭은 onMapReady로
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {//LatLng latLng이게 클릭시 위도 경도
                //gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.RED);
                polylineOptions.width(5);

                arrayList.add(latLng);
                polylineOptions.addAll(arrayList);
                gMap.addPolyline(polylineOptions);
            }
        });
    }
    final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            // List<Address> address=null;;

            currentLat=location.getLatitude();
            currentLog=location.getLongitude();
            currentTime = System.currentTimeMillis();

            textView.setText("latitude : "+currentLat+" longittude : "+ currentLog);

            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(currentLat, currentLog),18 ));
            if(sw){

                double nowSpeed = distance(lastLat,lastLog,currentLat,currentLog);
                double timeDiff = (currentTime-lastTime)/1000;
                double speedMs=nowSpeed/timeDiff; // 메타퍼 섹크

                if(speedMs<3){
                    textView2.setText("속도 : "+String.format("%.1f",speedMs)+"m/s"); // 초속을 시속으로 바꿀때는 3.6을 곱하면 된다.
                }else{
                    Integer speedKMH = (int)(speedMs*3.6);
                    textView2.setText("속도 : "+speedKMH+"km/h"); // 초속을 시속으로 바꿀때는 3.6을 곱하면 된다.
                }

                LatLng latLng  = new LatLng(currentLat,currentLog);

                polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.RED);
                polylineOptions.width(5);

                arrayList.add(latLng);
                polylineOptions.addAll(arrayList);
                gMap.addPolyline(polylineOptions);
            }
            lastLat=currentLat;
            lastLog = currentLog;
            lastTime = currentTime;
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