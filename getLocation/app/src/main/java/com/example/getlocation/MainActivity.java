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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import com.google.firebase.firestore.GeoPoint;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {

    double currentLat, currentLog, currentTime;
    double lastLat, lastLog, lastTime;
    PolylineOptions polylineOptions;

    double dialogLatitude;
    double dialogLongitude;

    GoogleMap gMap;
    SupportMapFragment mapFragment;
    LocationManager locationManager;

    Button btnZoomIn, btnZoomOut, btnMapType, btnStart;
    ArrayList<LatLng> arrayList;

    int mapType = GoogleMap.MAP_TYPE_SATELLITE;

    boolean sw = false;

    TextView textView;
    int currentSteps = 0;
    Sensor stepCountSensor;
    SensorManager sensorManager;

    Button menu_button;

    myDBHelper myHelper;
    SQLiteDatabase sqlDB;

    ArrayList<String> menuList;

    ArrayList<LatLng> getDBLatLng;

    SupportMapFragment miniMapFragment;
    SupportMapFragment miniMapFragment2;

    ListView listView;

    private void showMapDialog() {
        // 다이얼 로그 생성
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("검색한 위치");
        dialogBuilder.setIcon(R.drawable.map_icon);
        dialogBuilder.setPositiveButton("확인", null);

        // R.layout.mini_map은 XML 파일로 정의된 사용자 지정 레이아웃을 인플레이트하는 역할
        View dialogView = getLayoutInflater().inflate(R.layout.mini_map, null);

        // dialogBuilder 객체는 AlertDialog를 생성하는 데 사용되는 코드이고, dialogView를 다이얼로그에 추가한다.
        dialogBuilder.setView(dialogView);

        // R.id.mini_map은 사용자 지정 레이아웃 안에서 정의된 SupportMapFragment를 찾기 위한 ID
        miniMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mini_map);

        // SupportMapFragment가 준비되었을 때의 콜백을 설정
        // getMapAsync() 메서드는 안드로이드에서 구글 맵을 비동기적으로 가져오는데 사용된다.
        miniMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                // 구글맵 객체를 가져와서 필요한 처리를 수행
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                LatLng location = new LatLng(dialogLatitude, dialogLongitude);
                googleMap.addMarker(new MarkerOptions().position(location));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Dialog 종료 시 SupportMapFragment 초기화
                if (miniMapFragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(miniMapFragment).commit();
                    miniMapFragment = null;
                }
            }
        });

        dialog.show();
    }

    private void showMapDialog2() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("타임라인");
        dialogBuilder.setIcon(R.drawable.map_icon);
        dialogBuilder.setNegativeButton("좌표 확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMapDialog3();
            }
        });
        dialogBuilder.setPositiveButton("닫기",null);
        View dialogView = getLayoutInflater().inflate(R.layout.time_line_map, null);

        dialogBuilder.setView(dialogView);

        SeekBar seekBar = dialogView.findViewById(R.id.seekBar);

        seekBar.setMax(getDBLatLng.size());

        seekBar.setMax(getDBLatLng.size());
        miniMapFragment2 = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mini_map);

        // SupportMapFragment가 준비되었을 때의 콜백을 설정
        miniMapFragment2.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                // 구글맵 객체를 가져와서 필요한 처리를 수행
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getDBLatLng.get(0), 18));
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(progress==0){
                            googleMap.clear();


                        }else{
                            polylineOptions = new PolylineOptions();
                            polylineOptions.color(Color.RED);
                            polylineOptions.width(5);

                            List<LatLng> subList = getDBLatLng.subList(0,progress);
                            polylineOptions.addAll(subList);

                            googleMap.clear();
                            googleMap.addPolyline(polylineOptions);
                            if (!subList.isEmpty()) {
                                // subList가 비어있지 않은 경우, 마지막 위치로 카메라 이동
                                LatLng lastPosition = subList.get(subList.size() - 1);
                                googleMap.addMarker(new MarkerOptions().position(lastPosition));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPosition, 18));
                            }
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Dialog 종료 시 SupportMapFragment 초기화
                if (miniMapFragment2 != null) {
                    getSupportFragmentManager().beginTransaction().remove(miniMapFragment2).commit();
                    miniMapFragment2 = null;
                }
            }
        });

        dialog.show();
    }

    private void showMapDialog3() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.listview);

        ListView listView = dialog.findViewById(R.id.listview);
        ListViewAdapter adapter = new ListViewAdapter();
        listView.setAdapter(adapter);

        for (int i = 0; i < getDBLatLng.size(); i++) {
            adapter.addItem("2023", getDBLatLng.get(i).latitude, getDBLatLng.get(i).longitude);
        }

        dialog.setTitle("타임라인 좌표");
        dialog.show();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mInflater = getMenuInflater();
        if(v == menu_button){
            menu.clear();
            int count = 0;
            sqlDB = myHelper.getReadableDatabase();
            String sql = "select distinct date from latlog";
            Cursor cursor = sqlDB.rawQuery(sql,null);
            while(cursor.moveToNext()){
                menuList.clear();
                menuList.add(cursor.getString(0));
                menu.add(0,count,0,cursor.getString(0));
            }
            sqlDB.close();
            mInflater.inflate(R.menu.menu1,menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        getDBLatLng.clear();

        sqlDB = myHelper.getReadableDatabase();
        String sql = "select * from latlog where date='"+item.getTitle()+"'";
        Cursor cursor = sqlDB.rawQuery(sql,null);
        while(cursor.moveToNext()){
            Log.d("timeLineGet",cursor.getInt(0)+cursor.getString(1)+cursor.getDouble(2)+cursor.getDouble(3));
            getDBLatLng.add(new LatLng(cursor.getDouble(2),cursor.getDouble(3)));
        }
        sqlDB.close();

        for(int i=0;i<getDBLatLng.size();i++){
            Log.d("timeLineGet1",getDBLatLng.get(i).toString());
        }
        showMapDialog2();
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<LatLng>();
        menuList = new ArrayList<String>();
        getDBLatLng = new ArrayList<LatLng>();

        btnZoomIn = (Button) findViewById(R.id.button);
        btnZoomOut = (Button) findViewById(R.id.button2);
        btnMapType = (Button) findViewById(R.id.button3);
        btnStart = (Button) findViewById(R.id.button4);

        menu_button= (Button) findViewById(R.id.button5);
        registerForContextMenu(menu_button);

        textView = (TextView) findViewById(R.id.textView);


        myHelper = new myDBHelper(this,"latlog",null,1);

        sqlDB = myHelper.getReadableDatabase();
        String sql = "select distinct date from latlog";
        Cursor cursor = sqlDB.rawQuery(sql,null);
        while(cursor.moveToNext()){
           Log.d("DBItem",cursor.getString(0));
        }
        sqlDB.close();

        // 만보계 활동 퍼미션 체크
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        // 걸음 센서 연결
        // * 옵션
        // -TYPE_STEP_DETECTOR : 리턴 값이 무조건 1. 앱이 종료되면 다시 0부터 시작
        // -TYPE_STEP_COUNTER : 앱 종료와 상관이 없이 계속 기존의 값을 가지고 있다가 1씩 증가한 값을 리턴
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // 디바이스에 걸음 센서의 존재 여부 체크
        if (stepCountSensor == null)
            Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show();


        //Location 객체는 android 기기의 위치 서비스에 대한 액세스를 제공한다
        //Location 객체를 사용하여 위치 정보를 가져오거나, 위치 기반 알림을 설정하거나, 위치 기반 서비스를 사용하도록 설정할 수 있다.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //이 람다 함수는 권한 요청의 결과를 처리한다.
        //result 매개변수를 통해 사용자의 응답과 허용 여부를 확일할 수 있다.
        //Manifest.permission.ACCESS_FINE_LOCATION과 Manifest.permission.ACCESS_COARSE_LOCATION 권한에 대한 사용자의 응답을 확인한다.
        ActivityResultLauncher<String[]> locationPermisstionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            //result : 이 변수는 권한 요청 결과를 나타낸다. 권한 요청의 결과는 사용자가 권한을 부여하거나 거부한 경우에 대한 정보를 포함하고 있다.

            Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
            Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

            if (fineLocationGranted != null && fineLocationGranted) {
                Toast.makeText(getApplicationContext(), "자세한 위치권한 허용됨.", Toast.LENGTH_SHORT).show();
            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                Toast.makeText(getApplicationContext(), "대략적 위치 권한 허용됨.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "권한 없음", Toast.LENGTH_SHORT).show();
            }
        });

        // 권한을 확인 받았으면 다음부터는 구너한 요청을 하지 않는다.
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermisstionRequest.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        }

        //LocationManager.GPS_PROVIDER : 위치 정보를 제공하는 프로바이더로 GPS를 사용한다. 이것은 GPS를 통해 위치를 가져온다는 것을 의미한다.
        // minTimeMs : 위치 업데이트를 받기 위한 시간 간격
        // minDistanceM : 위치 업데이트를 받기 위한 거리 간격
        // listener : 위치 변경을 감지하고 이벤트를 처리하기 위한 리스너 객체. 이 리스너는 위치가 업데이트될 때 마다 호출된다.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, listener);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // API 키 초기화
        Places.initialize(getApplicationContext(), "AIzaSyAGsUp61u0hTwvWx5cJlh1aiI--rDr8pHM");
        PlacesClient placesClient = Places.createClient(this);


        //XML에서 추가한 autocompleteSupportFragment를 연결
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setCountries("KR");


        //필요한 설정을 수행한다.
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

                Log.d("TAGE", status.toString());
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
                    arrayList.clear();
                    gMap.clear();
                    btnStart.setText("종료");
                    currentSteps = 0;
                    textView.setText(String.valueOf(currentSteps) + " 걸음");
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
            Toast.makeText(getApplicationContext(), "위치정보를 가져오지 못함", Toast.LENGTH_SHORT).show();
            return;
        }
        gMap.setMyLocationEnabled(true);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.94471, 126.6828), 18));
    }

    final LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            String provider = location.getProvider();
            Date date = new Date();
            SimpleDateFormat full_sdf = new SimpleDateFormat("yyyy-MM-dd");
            Log.d("DATE", full_sdf.format(date).toString());
            currentLat = location.getLatitude();
            currentLog = location.getLongitude();
            currentTime = System.currentTimeMillis();

            //showNearbyRestaurants(new LatLng(currentLat,currentLog));
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat, currentLog), 18));




            if (sw) {
                // DB에 저장
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("insert into latlog (date,lat,log) values('"+full_sdf.format(date)+"','"+currentLat+"','"+currentLog+"')");
                sqlDB.close();

                double nowSpeed = distance(lastLat, lastLog, currentLat, currentLog);
                double timeDiff = (currentTime - lastTime) / 1000;
                double speedMs = nowSpeed / timeDiff;

                LatLng latLng = new LatLng(currentLat, currentLog);

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

    public void onStart() {
        super.onStart();
        // 센서 속도 결정
        // * 옵션
        // -SENSOR_DELAY_NORMAL : 20.000초 딜레이
        // -SENSOR_DELAY_UI : 6.000초 딜레이
        // -SENSOR_DELAY_GAME : 20.000초 딜레이
        // -SENSOR_DELAY_FASTEST : 딜레이
        sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0f) {
                currentSteps++;
                textView.setText(String.valueOf(currentSteps) + " 걸음");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

//    private void showNearbyRestaurants(LatLng latLng) {
//        // Places API 클라이언트 초기화
//        PlacesClient placesClient = Places.createClient(this);
//
//        // 요청 파라미터 설정
//        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.TYPES);
//
//        // 주변 카페 검색 요청
//        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
//
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // 권한 체크 코드
//            return;
//        }
//        Task<FindCurrentPlaceResponse> placeResponseTask = placesClient.findCurrentPlace(request);
//        placeResponseTask.addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                FindCurrentPlaceResponse response = task.getResult();
//                for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//
//                    Place place = placeLikelihood.getPlace();
//                    LatLng placeLatLng = place.getLatLng();
//                    List<Place.Type> placeTypes = place.getTypes();
//                    if (placeLatLng != null && placeTypes.contains(Place.Type.CAFE)) { // 카페인 경우에만 마커 표시
//                        Marker marker = gMap.addMarker(new MarkerOptions()
//                                .position(placeLatLng)
//                                .title(place.getName())
//                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                        // 마커 클릭 리스너 설정 등 추가적인 처리 가능
//                    }
//                }
//            } else {
//                Exception exception = task.getException();
//                if (exception != null) {
//                    Log.e("TAG", "주변 카페 검색 실패: " + exception.getMessage());
//                }
//            }
//        });
//    }


}