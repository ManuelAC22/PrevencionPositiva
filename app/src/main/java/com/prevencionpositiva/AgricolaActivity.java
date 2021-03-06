package com.prevencionpositiva;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AgricolaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String direccion;
    String mensaje;
    double lat = 0.0;
    double log = 0.0;
    LatLng latLng;
    MarkerOptions markerOptions = new MarkerOptions();
    ImageButton lista,riesgo,clima;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agricola);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgricolaActivity.this,CalendarioActivity.class);
                startActivity(intent);
            }
        });
        lista = findViewById(R.id.lista);
        lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgricolaActivity.this,IncidenciaActivity.class);
                startActivity(intent);
            }
        });

        riesgo = findViewById(R.id.riesgo);
        riesgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgricolaActivity.this,Webview.class);
                startActivity(intent);
            }
        });

        clima = findViewById(R.id.clima);
        clima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgricolaActivity.this,AgricolaActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.maps_positiva));

            if (!success) {
                Log.e("", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("", "Can't find style. Error: ", e);
        }

        LatLng sydney = new LatLng( -12.077517, -77.07928);

        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Clima").icon(BitmapDescriptorFactory.fromResource(R.drawable.cloud)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));




        String url = "http://api.openweathermap.org/data/2.5/weather?lat=-12.082176&lon=-77.085214&lang=es&appid=3a2ac70cc5baff47027c8853167245c2";
        RequestQueue queue = Volley.newRequestQueue(AgricolaActivity.this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    try {
                        JSONArray ja = response.getJSONArray("main");
                        Toast.makeText(AgricolaActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        for(int i=0;i<ja.length();i++){
                            try {
                                JSONObject encuesta = ja.getJSONObject(i);
                                String titulo = encuesta.getString("pressure");
                                String dirigidos = encuesta.getString("humidity");
                                Toast.makeText(AgricolaActivity.this, titulo+"  "+dirigidos, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AgricolaActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);


        miUbicacion();
    }

    public void locationStart(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!gpsEnabled){
            Intent settingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingIntent);
        }
    }

    public void setLocation(Location loc){
        if(loc.getLatitude()!=0.0 && loc.getLongitude()!=0.0){
            try{
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(),loc.getLongitude(),1);
                if(!list.isEmpty()){
                    Address dircalle = list.get(0);
                    direccion = (dircalle.getAddressLine(0));
                }
            } catch (IOException e) {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void agregarMarcador(double lat, double log) {
        LatLng asd = new LatLng(lat, log);
        Marker MiUbicación = mMap.addMarker(new MarkerOptions().position(asd).title("Mi Ubicación").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(asd, 15));
    }

    public void actulizarMarker(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            log = location.getLongitude();
            agregarMarcador(lat, log);
        }
    }

    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //actulizarMarker(location);
            setLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            mensaje = ("GPS Activado");
            Mensaje();
        }

        @Override
        public void onProviderDisabled(String s) {
            mensaje = ("GPS Desactivado");
            locationStart();
            Mensaje();
        }
    };

    private void Mensaje() {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},101);
            return;
        }else{
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            actulizarMarker(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1200,0,locListener);
        }
    }
}
