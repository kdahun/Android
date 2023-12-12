package com.example.googlemap_realadd_polyline;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gMap;
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    Button btnRealAdd,btnDateSelect;
    Handler handler = new Handler();
    View dialogView;
    View dialogView2;
    ListViewAdapter adapter;
    ListViewAdapter2 adapter2;
    ListView listView;
    ListView listView2;
    PolylineOptions polylineOptions;

    SupportMapFragment mapFragment;
    String dateSave=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnDateSelect = (Button) findViewById(R.id.button);
        btnRealAdd = (Button) findViewById(R.id.button2);

        adapter=new ListViewAdapter(this);
        adapter2=new ListViewAdapter2();

        dialogView = (View) View.inflate(MainActivity.this,R.layout.dialog,null);
        listView=(ListView) dialogView.findViewById(R.id.listview);
        listView.setAdapter(adapter);

        dialogView2 = (View) View.inflate(MainActivity.this,R.layout.menu_dialog,null);
        listView2=(ListView) dialogView2.findViewById(R.id.listview2);
        listView2.setAdapter(adapter2);

        myHelper = new myDBHelper(this,"gpsdb",null,1);

        sqlDB=myHelper.getWritableDatabase();
        myHelper.onUpgrade(sqlDB,1,2);
        sqlDB.close();

        final String urlStr = "https://bak3482.iwinv.net/gpsjson.php";

        new Thread(new Runnable() {
            @Override
            public void run() {
                requestUrl(urlStr);
            }
        }).start();

//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//
//        mapFragment.getMapAsync(this); // 동기화를 시켜주면 = > onMapReady가 실행된다.


        ActivityResultLauncher<String[]> locationPermisstionRequest=registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            Boolean fineLocationGranted=result.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION,false);
            Boolean coarseLocationGranted=result.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION,false);

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

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            locationPermisstionRequest.launch(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        }



        btnRealAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup parentViewGroup = (ViewGroup) dialogView.getParent();
                if (parentViewGroup != null) {
                    parentViewGroup.removeView(dialogView);
                }

                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);

                dlg.setTitle("실제 주소");
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인",null);
                dlg.show();


            }
        });

        btnDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup parentViewGroup = (ViewGroup) dialogView2.getParent();
                if (parentViewGroup != null) {
                    parentViewGroup.removeView(dialogView2);
                }
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);

                dlg.setTitle("실제 주소");
                dlg.setView(dialogView2);


                listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ListViewItem li =  adapter2.getItem(position);

                        Toast.makeText(getApplicationContext(),li.getDate(),Toast.LENGTH_SHORT).show();
                        dateSave = li.getDate();

                        gMap.clear();
                        polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.RED);
                        polylineOptions.width(5);
                        ArrayList<LatLng> latLngs = new ArrayList<>();
                        ListViewItem list;
                        for(int i=0;i<adapter.getCount();i++){
                            list = adapter.getItem(i);

                            if(list.getDate().equals(dateSave)){
                                latLngs.add(new LatLng(list.getLat(),list.getLog()));

                                polylineOptions.addAll(latLngs);
                            }
                        }

                        gMap.addPolyline(polylineOptions);

                    }
                });
                dlg.setPositiveButton("확인",null);

                dlg.show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        ListViewItem li = adapter.getItem(0);
        gMap = googleMap;
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(li.getLat(),li.getLog()),18));



    }
    public void requestUrl(String urlStr){
        StringBuilder output = new StringBuilder();

        try{
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if(connection!=null){
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                //int resCode = connection.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while(true){
                    line = reader.readLine();
                    if(line==null){
                        break;
                    }

                    output.append(line+"\n");
                }
                reader.close();;
                connection.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        println(output.toString());
    }
    public void println(String data){
        handler.post(new Runnable() {
            @Override
            public void run() {
                jsonParsing(data);
            }
        });
    }

    public void jsonParsing(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray phoneBookArray = jsonObject.getJSONArray("gpsdb");

            sqlDB = myHelper.getWritableDatabase();

            for(int i=0;i<phoneBookArray.length();i++){
                JSONObject memberObject = phoneBookArray.getJSONObject(i);

                Integer no =memberObject.getInt("no");
                Double lat = memberObject.getDouble("latitude");
                Double log = memberObject.getDouble("longtitude");
                String date = memberObject.getString("date");

                String sqli = "insert into gpstable  values ("+no+","+lat+","+log+",'"+date+"')";

                sqlDB.execSQL(sqli);

                adapter.addItem(no,lat,log,date);
                adapter.notifyDataSetChanged();

                adapter2.addItem(no,lat,log,date);
                adapter2.notifyDataSetChanged();

                mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

                mapFragment.getMapAsync(this); // 동기화를 시켜주면 = > onMapReady가 실행된다.

            }
            sqlDB.close();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}