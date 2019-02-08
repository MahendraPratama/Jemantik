package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CariKKInputActivity extends AppCompatActivity {
private Button Go;
private EditText cariKelurahan, cariRT, cariRW;
private String JSON_STRING, no_KK, nm_KK, almt, kel, rt, rw;
    final ArrayList<HashMap<String, Object>> list = new ArrayList<>();
    private GridView gridView;
    final String url = "http://mpdev.000webhostapp.com/get_data_kk.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_kkinput);

        Go = (Button) findViewById(R.id.btn_go_search);
        cariKelurahan = (EditText) findViewById(R.id.cari_dari_kelurahan);
        cariRT = (EditText) findViewById(R.id.cari_dari_rt);
        cariRW = (EditText) findViewById(R.id.cari_dari_rw);


        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String crKel = cariKelurahan.getText().toString().trim();
                String crRT = cariRT.getText().toString().trim();
                String crRW = cariRW.getText().toString().trim();

                getJSON(crKel,crRT,crRW);
            }
        });
        getJSON("","","");
    }

    private void getJSON(final String i_kelurahanKK, final String i_rtKK, final String i_rwKK) {
        final ProgressDialog loading;
        loading = ProgressDialog.show(CariKKInputActivity.this, "Fetching Data", "Wait...", false, false);
        list.clear();
        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            show(response);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kelurahan", i_kelurahanKK);
                params.put("rt", i_rtKK);
                params.put("rw", i_rwKK);

                return params;
            }
        };
        UserRequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void show(String response) {
        try {
            JSONArray result = new JSONArray(response);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                nm_KK = jo.getString("nama_kk");
                no_KK = jo.getString("no_kk");
                almt = jo.getString("alamat");
                kel = jo.getString("kelurahan");
                rt = jo.getString("rt");
                rw = jo.getString("rw");

                HashMap<String, Object> ListDatKK = new HashMap<>();
                ListDatKK.put("nama_kk", nm_KK);
                ListDatKK.put("no_kk", no_KK);
                ListDatKK.put("alamat", almt);
                ListDatKK.put("kelurahan", kel);
                ListDatKK.put("rt", rt);
                ListDatKK.put("rw", rw);
                list.add(ListDatKK);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gridView = (GridView) findViewById(R.id.gridView_listKK);
        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) CariKKInputActivity.this.getSystemService(
                        CariKKInputActivity.this.LAYOUT_INFLATER_SERVICE);
                View gridview;
                if(SharedPrefManager.getInstance(getApplicationContext()).getKodeLevel() == 2){
                    gridview = inflater.inflate(R.layout.item_single_kk,null);
                    TextView _nm_KK = (TextView)gridview.findViewById(R.id.list_namaKK);
                    _nm_KK.setText((CharSequence) list.get(position).get("nama_kk"));
                    TextView _no_KK = (TextView)gridview.findViewById(R.id.list_noKK);
                    _no_KK.setText((CharSequence) list.get(position).get("no_kk"));
                    TextView _rt = (TextView)gridview.findViewById(R.id.list_rt);
                    _rt.setText((CharSequence) list.get(position).get("rt"));
                    TextView _rw = (TextView)gridview.findViewById(R.id.list_rw);
                    _rw.setText((CharSequence) list.get(position).get("rw"));
                    return gridview;
                }
                else
                {
                    gridview = inflater.inflate(R.layout.item_single_kk_admin,null);
                    TextView _nm_KK = (TextView)gridview.findViewById(R.id.list_namaKK);
                    _nm_KK.setText((CharSequence) list.get(position).get("nama_kk"));
                    TextView _no_KK = (TextView)gridview.findViewById(R.id.list_noKK);
                    _no_KK.setText((CharSequence) list.get(position).get("no_kk"));
                    Button btnEdit = (Button) gridview.findViewById(R.id.btnEdtKK);
                    Button btnDel = (Button) gridview.findViewById(R.id.btnDelKK);
                    final int fPos = position;
                    btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = null;
                            i = new Intent(CariKKInputActivity.this, TambahDataKKActivity.class);
                            i.putExtra("no_kk", list.get(fPos).get("no_kk").toString());
                            i.putExtra("nama_kk",list.get(fPos).get("nama_kk").toString());
                            i.putExtra("alamat",list.get(fPos).get("alamat").toString());
                            i.putExtra("kelurahan",list.get(fPos).get("kelurahan").toString());
                            i.putExtra("rt",list.get(fPos).get("rt").toString());
                            i.putExtra("rw",list.get(fPos).get("rw").toString());
                            startActivity(i);
                        }
                    });
                    btnDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String nmkk = list.get(fPos).get("nama_kk").toString();
                            String nokk = list.get(fPos).get("no_kk").toString();
                            deleteKK(nokk, nmkk);
                        }
                    });

                    return gridview;
                }


            }
        });

        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(SharedPrefManager.getInstance(getApplicationContext()).getKodeLevel() == 2){
                    Intent i = null;
                    i = new Intent(CariKKInputActivity.this, InputHasilPantauActivity.class);
                    i.putExtra("no_kk", list.get(position).get("no_kk").toString());
                    i.putExtra("nama_kk",list.get(position).get("nama_kk").toString());
                    startActivity(i);
                }
            }
        });
    }

    public void deleteKK(final String noKK, String namaKK){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        final String url = "http://mpdev.000webhostapp.com/delKK.php";
                        final StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                            getJSON("","","");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("no_kk", noKK);

                                return params;
                            }
                        };
                        UserRequestHandler.getInstance(CariKKInputActivity.this).addToRequestQueue(stringRequest);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(CariKKInputActivity.this);
        builder.setMessage("Are you sure want to delete "+namaKK+" ?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
