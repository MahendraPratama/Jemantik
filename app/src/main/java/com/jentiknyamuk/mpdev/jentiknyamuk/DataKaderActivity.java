package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class DataKaderActivity extends AppCompatActivity {
    final ArrayList<HashMap<String, Object>> list = new ArrayList<>();
    private GridView gridView;
    private Toolbar toolbar;
    final String url = "http://mpdev.000webhostapp.com/get_data_user.php";
    private String nama, username, password, alamat, no_telp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_kader);
        toolbar = (Toolbar) findViewById(R.id.datakader_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Kader");
        getJSON();
    }

    private void getJSON() {
        final ProgressDialog loading;
        loading = ProgressDialog.show(DataKaderActivity.this, "Fetching Data", "Wait...", false, false);
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
                });
        UserRequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void show(String response) {
        try {
            JSONArray result = new JSONArray(response);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                nama = jo.getString("nama");
                username = jo.getString("username");
                password = jo.getString("password");
                alamat = jo.getString("alamat");
                no_telp = jo.getString("no_telp");
                HashMap<String, Object> ListKader = new HashMap<>();
                ListKader.put("nama", nama);
                ListKader.put("username", username);
                ListKader.put("password", password);
                ListKader.put("alamat", alamat);
                ListKader.put("no_telp", no_telp);
                list.add(ListKader);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gridView = (GridView) findViewById(R.id.gridView_dataKader);
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
                LayoutInflater inflater = (LayoutInflater) DataKaderActivity.this.getSystemService(
                        DataKaderActivity.this.LAYOUT_INFLATER_SERVICE);
                View gridview;
                gridview = inflater.inflate(R.layout.item_single_kader,null);
                TextView _nm_KK = (TextView)gridview.findViewById(R.id.list_namaKader);
                _nm_KK.setText((CharSequence) list.get(position).get("nama"));
                ImageButton edt = (ImageButton)gridview.findViewById(R.id.btnEdtKader);
                ImageButton del = (ImageButton)gridview.findViewById(R.id.btnDelKader);
                final int fPos = position;
                edt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = null;
                        i = new Intent(DataKaderActivity.this, TambahKaderActivity.class);
                        i.putExtra("nama", list.get(fPos).get("nama").toString());
                        i.putExtra("username",list.get(fPos).get("username").toString());
                        i.putExtra("password",list.get(fPos).get("password").toString());
                        i.putExtra("alamat",list.get(fPos).get("alamat").toString());
                        i.putExtra("no_telp",list.get(fPos).get("no_telp").toString());
                        startActivity(i);
                    }
                });
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String us,nm;
                        us = list.get(fPos).get("username").toString();
                        nm = list.get(fPos).get("nama").toString();
                        deleteKader(us,nm);
                    }
                });
                return gridview;
            }
        });
    }
    public void deleteKader(final String username, String namaKader){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        final String url = "https://mpdev.000webhostapp.com/delKader.php";
                        final StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                            getJSON();
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
                                params.put("us", username);

                                return params;
                            }
                        };
                        UserRequestHandler.getInstance(DataKaderActivity.this).addToRequestQueue(stringRequest);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(DataKaderActivity.this);
        builder.setMessage("Are you sure want to delete "+namaKader+" ?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
