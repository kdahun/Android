package com.example.a1901128_1004;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    Location lastLocation;
    TextView textView1,textView2,textView3;
    Geocoder geocoder;

    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        textView1=(TextView) findViewById(R.id.textView);
        geocoder=new Geocoder(this);
        textView2=(TextView) findViewById(R.id.textView2);
        textView3=(TextView) findViewById(R.id.textView3);
        dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.KOREA);

//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MODE_PRIVATE);
//        }

        ActivityResultLauncher<String[]> locationPermisstionRequest=registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),result -> {
            Boolean fineLocationGranted=result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION,false);
            Boolean coarseLocationGranted=result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION,false);

            if(fineLocationGranted!=null&&fineLocationGranted)
            {
                Toast.makeText(getApplicationContext(),"자세한 위치권한 허용됨",Toast.LENGTH_SHORT).show();
            } else if(coarseLocationGranted!=null&&coarseLocationGranted)
            {
                Toast.makeText(getApplicationContext(),"대략적 위치권한 허용됨",Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(getApplicationContext(),"권한 없음",Toast.LENGTH_SHORT).show();
            }


        });

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            locationPermisstionRequest.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION});
        }

        lastLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,1,locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,2000,1,locationListener);

        textView3.setText("제공자 : "+lastLocation.getProvider()+"\n위도 : "+lastLocation.getLatitude()+"\n경도 : "+lastLocation.getLongitude()+"\n생성시간 : "+dateFormat.format(lastLocation.getTime()));
    }

    final LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            List<Address> address=null;;
            String provider=location.getProvider();
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            double altitude=location.getAltitude();

            textView1.setText("제공자 : "+provider+" \n위도 : "+latitude+" \n경도 : "+longitude+" \n고도 : "+altitude);

            try{
                address=geocoder.getFromLocation(latitude,longitude,5);
                textView2.setText(address.get(0).getAddressLine(0).toString());
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    };
}