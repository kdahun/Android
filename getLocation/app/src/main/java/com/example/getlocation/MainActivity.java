package com.example.getlocation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.MimeTypeFilter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    double currentLat,currentLog,currentTime;
    double lastLat,lastLog,lastTime;
    PolylineOptions polylineOptions;

    double dialogLatitude;
    double dialogLongitude;

    GoogleMap gMap;
    SupportMapFragment mapFragment;
    LocationManager locationManager;

    Button btnZoomIn, btnZoomOut,btnMapType,btnStart;
    ArrayList<LatLng> arrayList;

    int mapType = GoogleMap.MAP_TYPE_SATELLITE;

    boolean sw = false;

    private void showMapDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("검색한 위치");
        dialogBuilder.setIcon(R.drawable.map_icon);
        dialogBuilder.setPositiveButton("확인",null);
        View dialogView = getLayoutInflater().inflate(R.layout.mini_map,null);

        dialogBuilder.setView(dialogView);

        //대화 상자에 있는 SupportMapFragment를 찾는다.
        SupportMapFragment miniMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mini_map);

        // SupportMapFragment가 준비되었을 때의 콜백을 설정한다.
        miniMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                // 구글맵 객체를 가져와서 필요한 처리를 수행한다.
                googleMap.setMapType(googleMap.MAP_TYPE_NORMAL);
                LatLng location = new LatLng(dialogLatitude,dialogLongitude);
                googleMap.addMarker(new MarkerOptions().position(location));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
            }
        });
        AlertDialog dialog = dialogBuilder.create();

        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<LatLng>();

        btnZoomIn = (Button) findViewById(R.id.button);
        btnZoomOut = (Button) findViewById(R.id.button2);
        btnMapType = (Button) findViewById(R.id.button3);
        btnStart = (Button) findViewById(R.id.button4);

        //Location 객체는 android 기기의 위치 서비스에 대한 액세스를 제공한다
        //Location 객체를 사용하여 위치 정보를 가져오거나, 위치 기반 알림을 설정하거나, 위치 기반 서비스를 사용하도록 설정할 수 있다.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //이 람다 함수는 권한 요청의 결과를 처리한다.
        //result 매개변수를 통해 사용자의 응답과 허용 여부를 확일할 수 있다.
        //Manifest.permission.ACCESS_FINE_LOCATION과 Manifest.permission.ACCESS_COARSE_LOCATION 권한에 대한 사용자의 응답을 확인한다.
        ActivityResultLauncher<String[]> locationPermisstionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),result -> {
            //result : 이 변수는 권한 요청 결과를 나타낸다. 권한 요청의 결과는 사용자가 권한을 부여하거나 거부한 경우에 대한 정보를 포함하고 있다.

            Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION,false);
            Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

            if(fineLocationGranted!=null&&fineLocationGranted){
                Toast.makeText(getApplicationContext(),"자세한 위치권한 허용됨.",Toast.LENGTH_SHORT).show();
            }else if(coarseLocationGranted!=null&&coarseLocationGranted){
                Toast.makeText(getApplicationContext(),"대략적 위치 권한 허용됨.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"권한 없음",Toast.LENGTH_SHORT).show();
            }
        });

        // 권한을 확인 받았으면 다음부터는 구너한 요청을 하지 않는다.
        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            locationPermisstionRequest.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        }


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,listener);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // API 키 초기화
        Places.initialize(getApplicationContext(), "AIzaSyAGsUp61u0hTwvWx5cJlh1aiI--rDr8pHM");
        PlacesClient placesClient = Places.createClient(this);


        //XML에서 추가한 autocompleteSupportFragment를 연결
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setCountries("KR");


        //필요한 설정을 수행한다.
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

                Log.d("TAGE",status.toString());
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {

                if (place.getLatLng() != null) {
                    dialogLatitude = place.getLatLng().latitude;
                    dialogLongitude = place.getLatLng().longitude;
                    // 위도와 경도를 사용하여 필요한 처리를 수행합니다.
                    showMapDialog();
                } else {
                    Log.d("TAG", "위도와 경도 정보가 없습니다.");
                    return;
                }
            }
        });

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
                if(mapType==GoogleMap.MAP_TYPE_SATELLITE){
                    gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mapType = GoogleMap.MAP_TYPE_NORMAL;
                    btnMapType.setText("위성");
                }else if(mapType==GoogleMap.MAP_TYPE_NORMAL){
                    gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    mapType = GoogleMap.MAP_TYPE_SATELLITE;
                    btnMapType.setText("일반");
                }
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw){
                    sw=false;
                    btnStart.setText("시작");
                }else{
                    sw = true;
                    arrayList.clear();
                    gMap.clear();
                    btnStart.setText("종료");
                }
            }
        });
    }

    //Dialog에 지도를 넣어주고 검색한 위치 출력해주기


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(googleMap.MAP_TYPE_SATELLITE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"위치정보를 가져오지 못함",Toast.LENGTH_SHORT).show();
            return;
        }
        gMap.setMyLocationEnabled(true);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.94471,126.6828) ,18));
    }

    final LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            String provider = location.getProvider();
            Date date = new Date();
            SimpleDateFormat full_sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
            Log.d("DATE",full_sdf.format(date).toString());
            currentLat = location.getLatitude();
            currentLog = location.getLongitude();
            currentTime = System.currentTimeMillis();

            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat,currentLog),18));

            if(sw){
                double nowSpeed = distance(lastLat,lastLog,currentLat,currentLog);
                double timeDiff = (currentTime-lastTime)/1000;
                double speedMs = nowSpeed/timeDiff;

                LatLng latLng = new LatLng(currentLat,currentLog);

                polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.RED);
                polylineOptions.width(5);

                arrayList.add(latLng);
                polylineOptions.addAll(arrayList);
                gMap.addPolyline(polylineOptions);

            }
            lastLat = currentLat;
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