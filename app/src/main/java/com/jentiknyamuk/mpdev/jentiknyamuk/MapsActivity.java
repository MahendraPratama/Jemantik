package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapsActivity extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnPolygonClickListener {
    ArrayList<HashMap<String, Object>> listDataMap = new ArrayList<>();
    ArrayList<HashMap<String, Object>> listDataJentik = new ArrayList<>();
    private String[] array_nm_kel = {"BONGSARI","NGEMPLAK","BOJONGSALAMAN","CABEAN"
                                        ,"GISIKDRONO","KALIBANTENG KIDUL","KALIBANTENG KULON"
                                        , "KARANGAYU","KEMBANGARUM","KRAPYAK","KROBOKAN"
                                        ,"MANYARAN","SALAMANMLOYO","TAMBAKHARJO","TAWANGMAS","TAWANGSARI"};
    private GoogleMap mMap;
    private double latitude, longitude;
    private String Kel;
    private String nmKel;
    private double abj;
    private int sumKK;
    private View mMainView;

    private Button btnClose;
    private LinearLayout popup;
    private TextView nmkel, idxjentik, jKK, ket;
    private MapView mapFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainView =  inflater.inflate(R.layout.activity_maps, container, false);
        //setContentView(R.layout.activity_maps);

        idxjentik = (TextView) mMainView.findViewById(R.id.popup_abj);
        popup = (LinearLayout) mMainView.findViewById(R.id.popup_window);
        nmkel = (TextView) mMainView.findViewById(R.id.popup_nm_kel);
        jKK = (TextView) mMainView.findViewById(R.id.popup_jml_kk);
        ket = (TextView) mMainView.findViewById(R.id.popup_keterangan);
        btnClose = (Button) mMainView.findViewById(R.id.btn_close);
        popup.setVisibility(View.INVISIBLE);
        get_data_ABJ();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (MapView) mMainView.findViewById(R.id.map);
        mapFragment.onCreate(savedInstanceState);
        mapFragment.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mMainView;
    }

    private void get_data_LatLng(){
        final String url = "http://mpdev.000webhostapp.com/get_map.php";
        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray result = new JSONArray(response);
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jo = result.getJSONObject(i);
                                latitude = jo.getDouble("lat");
                                longitude = jo.getDouble("lng");
                                Kel = jo.getString("kelurahan");
                                HashMap<String, Object> dataServer = new HashMap<>();
                                dataServer.put("lat", latitude);
                                dataServer.put("lng", longitude);
                                dataServer.put("kelurahan",Kel);
                                listDataMap.add(dataServer);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mapFragment.getMapAsync(MapsActivity.this);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        UserRequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void get_data_ABJ(){
        final String url = "http://mpdev.000webhostapp.com/getAvg_jentikPerKel.php";
        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray result = new JSONArray(response);
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jo = result.getJSONObject(i);
                                nmKel = jo.getString("kelurahan");
                                abj = jo.getDouble("abj");
                                sumKK = jo.getInt("sumkk");
                                HashMap<String, Object> dataServer = new HashMap<>();
                                dataServer.put("kelurahan", nmKel);
                                dataServer.put("abj", abj);
                                dataServer.put("sumkk",sumKK);
                                listDataJentik.add(dataServer);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        get_data_LatLng();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        UserRequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private double lat,lng,indeksJentik;
    //private List<LatLng>[] MapKelurahan = new List[array_nm_kel.length];
    //private Iterable<LatLng>[] itr = new Iterable[array_nm_kel.length];
    //private Polygon[] arrayPolygon = new Polygon[array_nm_kel.length];
    private ArrayList<Polygon> arrayPolygon = new ArrayList<>();
    private ArrayList<Iterable<LatLng>> itr = new ArrayList<>();
    private ArrayList<List<LatLng>> MapKelurahan = new ArrayList<>();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int n = 0 ; n < array_nm_kel.length; n++){
            List<LatLng> map = new ArrayList<>();
            MapKelurahan.add(n, map);
            for (int i = 0; i < listDataMap.size() ; i++){
                if(listDataMap.get(i).get("kelurahan").toString().startsWith(array_nm_kel[n])){ //array_nm_kel[n]
                    lat = (double) listDataMap.get(i).get("lat");
                    lng = (double) listDataMap.get(i).get("lng");
                    MapKelurahan.get(n).add(new LatLng(lat,lng));
                }
            }
        }

        for (int n = 0; n < array_nm_kel.length; n++){
            if (!MapKelurahan.get(n).isEmpty()){
                Log.d(">>>>>>>>>>>>>>>>>>>>",""+n);
                final int finalN = n;
                itr.add(n,new Iterable<LatLng>() {
                    @NonNull
                    @Override
                    public Iterator<LatLng> iterator() {
                        return MapKelurahan.get(finalN).iterator();
                    }
                });
                indeksJentik = (double) listDataJentik.get(n).get("abj");
                arrayPolygon.add(n,mMap.addPolygon(new PolygonOptions()
                        .clickable(true)
                        .addAll(
                                itr.get(n)
                        )
                ));
                arrayPolygon.get(n).setTag(tagPoly(indeksJentik));
                CustomPolygon.stylePolygon(arrayPolygon.get(n));
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12));
        mMap.setOnPolygonClickListener(this);
    }

    public String tagPoly(double idx){
        String retVal;
        if(idx >= 0 && idx < 0.2 ){
            retVal = "Sangat Aman";
        }else if(idx >= 0.2 && idx < 0.4){
            retVal = "Aman";
        }else if(idx >= 0.4  && idx < 0.6){
            retVal = "Waspada";
        }else if(idx >= 0.6 && idx < 0.8){
            retVal = "Bahaya";
        }else if(idx >= 0.8 && idx <= 1){
            retVal = "Sangat Bahaya";
        }else{
            retVal = "default";
        }
        return retVal;
    }
    @Override
    public void onPolygonClick(Polygon polygon) {
        int poly = Integer.parseInt(polygon.getId().substring(2,polygon.getId().length()));
        int nPolygon = (poly < 15) ? poly : (poly % 16) ;
        Log.d("kelurahan = ", polygon.getId()) ;

        for (int n = 0; n < array_nm_kel.length; n++){
            arrayPolygon.get(n).setTag("default");
            CustomPolygon.stylePolygon(arrayPolygon.get(n));
        }
        arrayPolygon.get(nPolygon).setTag(tagPoly((double)listDataJentik.get(nPolygon).get("abj")));
        CustomPolygon.stylePolygon(arrayPolygon.get(nPolygon));

        popup.setVisibility(View.VISIBLE);
        nmkel.setText(array_nm_kel[nPolygon]);
        idxjentik.setText(listDataJentik.get(nPolygon).get("abj").toString());
        jKK.setText(listDataJentik.get(nPolygon).get("sumkk").toString());
        ket.setText(tagPoly((double)listDataJentik.get(nPolygon).get("abj")));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getMiddle(polygon.getPoints()), 14));

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.setVisibility(View.INVISIBLE);
                for (int n = 0; n < array_nm_kel.length; n++){
                    arrayPolygon.get(n).setTag(tagPoly((double)listDataJentik.get(n).get("abj")));
                    CustomPolygon.stylePolygon(arrayPolygon.get(n));
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12));
            }
        });
    }

    public LatLng getMiddle(List<LatLng> list){
        double lat=0,lng=0;

        for (int i = 0; i < list.size(); i++){
            lat += list.get(i).latitude;
            lng += list.get(i).longitude;
        }

        lat = lat/list.size();
        lng = lng/list.size();

        return new LatLng(lat,lng);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        arrayPolygon.removeAll(arrayPolygon);
        itr.removeAll(itr);
        MapKelurahan.removeAll(MapKelurahan);
        mapFragment.removeAllViews();
        listDataMap.removeAll(listDataMap);
        listDataJentik.removeAll(listDataJentik);
    }
}
