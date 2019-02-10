package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TambahDataKKActivity extends AppCompatActivity {
    private Button getLoc, save;
    private EditText nomor_kk, nama_kk, alamat, kelurahan, rt, rw;
    private TextView statusLoc;
    private double latitude=0, longitude=0;
    private ProgressDialog progressDialog;

    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    FusedLocationProviderClient mFusedLocation;
    private SettingsClient mSettingsClient;
    private Boolean mRequestingLocationUpdates;
    private LocationCallback mLocationCallback;
    private LocationSettingsRequest mLocationSettingsRequest;
    String url = "http://mpdev.000webhostapp.com/insert_data_kk.php";

    private void init(){
        mFusedLocation = LocationServices.getFusedLocationProviderClient(TambahDataKKActivity.this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000); // 10 detik
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                if(mCurrentLocation != null){
                    //Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();
                    longitude = mCurrentLocation.getLongitude();
                    latitude = mCurrentLocation.getLatitude();
                    statusLoc.setText("Lat : "+latitude+" Lng : "+longitude);
                }

            }
        };
        mRequestingLocationUpdates = false;
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocation
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data_kk);
        
        nomor_kk = (EditText) findViewById(R.id.tambah_no_kk);
        nama_kk = (EditText) findViewById(R.id.tambah_nama_kk);
        alamat = (EditText) findViewById(R.id.tambah_alamat_kk);
        kelurahan = (EditText) findViewById(R.id.tambah_kelurahan_kk);
        rt = (EditText) findViewById(R.id.tambah_rt_kk);
        rw = (EditText) findViewById(R.id.tambah_rw_kk);
        //statusLoc = (TextView) findViewById(R.id.txt_status_location);
        //getLoc = (Button) findViewById(R.id.btn_get_location);
        save = (Button) findViewById(R.id.btn_simpan_data_kk);
        progressDialog = new ProgressDialog(this);

        if(getIntent().hasExtra("no_kk")){
            nomor_kk.setText(getIntent().getStringExtra("no_kk"));
            nama_kk.setText(getIntent().getStringExtra("nama_kk"));
            alamat.setText(getIntent().getStringExtra("alamat"));
            kelurahan.setText(getIntent().getStringExtra("kelurahan"));
            rt.setText(getIntent().getStringExtra("rt"));
            rw.setText(getIntent().getStringExtra("rw"));
            save.setText("Update");
        }
        //init();


//        getLoc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ActivityCompat.checkSelfPermission(TambahDataKKActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                        == PackageManager.PERMISSION_GRANTED
//                        && ActivityCompat.checkSelfPermission(TambahDataKKActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
//                        == PackageManager.PERMISSION_GRANTED) {
//                    Log.d("Cek","cek Permission");
//                    //mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback,null /*Looper.myLooper()*/);
//                    startLocationUpdates();
////                    mFusedLocation.getLastLocation().addOnSuccessListener(TambahDataKKActivity.this, new OnSuccessListener<Location>() {
////                        @Override
////                        public void onSuccess(Location location) {
////                            if (location != null) {
////                                latitude = location.getLatitude();
////                                longitude = location.getLongitude();
////                                Log.d("Cek Lat", ""+latitude);
////                                statusLoc.setText("Lat : "+latitude+" Lng : "+longitude);
////                            }
////                        }
////                    });
//                }
//                else
//                {
//                    checkLocationPermission();
//                }
//            }
//        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String i_noKK = nomor_kk.getText().toString().trim();
                String i_namaKK = nama_kk.getText().toString().trim();
                String i_alamatKK = alamat.getText().toString().trim();
                String i_kelurahanKK = kelurahan.getText().toString().trim();
                String i_rtKK = rt.getText().toString().trim();
                String i_rwKK = rw.getText().toString().trim();
                if(i_noKK.length()==0){
                    toast("Harap isi nomor KK");
                }else if(i_namaKK.length()==0){
                    toast("Harap isi nama Kepala Keluarga");
                }else if(i_alamatKK.length()==0){
                    toast("Harap isi data alamat");
                }else if(i_kelurahanKK.length()==0){
                    toast("Harap isi data nama Kelurahan");
                }else if(i_rtKK.length()==0 || i_rwKK.length()==0){
                    toast("Harap isi data RT dan RW");
                }else
                {
                    insert_data(i_noKK, i_namaKK, i_alamatKK, i_kelurahanKK, i_rtKK, i_rwKK);
                }

            }

            void toast(String message){
                Toast.makeText(TambahDataKKActivity.this,message,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stopLocationUpdates();
    }



    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(TambahDataKKActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Log.i(TAG, "All location settings are satisfied.");

                        //Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocation.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());


                        //updateLocationUI();
                    }
                })
                .addOnFailureListener(TambahDataKKActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                //Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                //        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(TambahDataKKActivity.this, 100);
                                } catch (IntentSender.SendIntentException sie) {
                                    //Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                //Log.e(TAG, errorMessage);

                                Toast.makeText(TambahDataKKActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        //updateLocationUI();
                    }
                });
    }

//    private LocationCallback mLocationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            List<Location> locationList = locationResult.getLocations();
//            if (locationList.size() > 0) {
//                //The last location in the list is the newest
//                Location location = locationList.get(locationList.size() - 1);
//                latitude = location.getLatitude();
//                longitude =location.getLongitude();
//                statusLoc.setText("Lat : "+latitude+" Lng : "+longitude);
//                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
//                mCurrentLocation = location;
//
//            }
//        }
//    };
    private void insert_data(final String i_noKK, final String i_namaKK, final String i_alamatKK, final String i_kelurahanKK, final String i_rtKK, final String i_rwKK) {


        progressDialog.setMessage("Menyimpan Data");
        progressDialog.show();


        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean n = jsonObject.getBoolean("error");
                            if(n==false){
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                cleanInputForm();
                            }

                            //finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("no_kk", i_noKK);
                params.put("nama_kk", i_namaKK.toUpperCase());
                params.put("alamat", i_alamatKK.toUpperCase());
                params.put("kelurahan", i_kelurahanKK.toUpperCase());
                params.put("rt", i_rtKK);
                params.put("rw", i_rwKK);


                return params;
            }
        };
        UserRequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(TambahDataKKActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void cleanInputForm(){
        nomor_kk.setText("");
        nama_kk.setText("");
        alamat.setText("");
        kelurahan.setText("");
        rt.setText("");
        rw.setText("");
        save.setEnabled(false);
    }
}
