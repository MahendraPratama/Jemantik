package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InputHasilPantauActivity extends AppCompatActivity {
    private Button btn_save;
    private EditText nm_kk, no_kk, tglPantau;
    private RadioGroup bak,kolam,kaleng,tempayan,lain;
    private TextView t_bak, t_kolam, t_kaleng, t_tempayan, t_lain;
    private ProgressDialog progressDialog;
    private double latitude, longitude;
    private String nomor_kk, nama_kk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_hasil_pantau);

        nomor_kk = getIntent().getStringExtra("no_kk");
        nama_kk = getIntent().getStringExtra("nama_kk");
        btn_save = (Button) findViewById(R.id.btn_simpan);
        nm_kk = (EditText) findViewById(R.id.idp_nmKK);
        no_kk = (EditText) findViewById(R.id.idp_noKK);
        nm_kk.setText(nama_kk);
        no_kk.setText(nomor_kk);

        bak = (RadioGroup) findViewById(R.id.idp_bakmandi);
        kolam = (RadioGroup) findViewById(R.id.idp_kolam);
        kaleng = (RadioGroup) findViewById(R.id.idp_kaleng);
        tempayan = (RadioGroup) findViewById(R.id.idp_tempayan);
        lain = (RadioGroup) findViewById(R.id.idp_lain);

        t_bak = (TextView) findViewById(R.id.sts_bak);
        t_kolam = (TextView) findViewById(R.id.sts_kolam);
        t_kaleng= (TextView) findViewById(R.id.sts_kaleng);
        t_tempayan = (TextView) findViewById(R.id.sts_tempayan);
        t_lain = (TextView) findViewById(R.id.sts_lain);


        final Calendar calendar = Calendar.getInstance();
        tglPantau = (EditText) findViewById(R.id.idp_tglPantau);
        tglPantau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(InputHasilPantauActivity.this
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, month, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        tglPantau.setText(dateFormat.format(newDate.getTime()).toString());
                    }

                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

            }
        });


        t_bak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bak.clearCheck();
            }
        });

        t_kolam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kolam.clearCheck();
            }
        });

        t_kaleng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kaleng.clearCheck();
            }
        });

        t_tempayan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempayan.clearCheck();
            }
        });

        t_lain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lain.clearCheck();
            }
        });



        progressDialog = new ProgressDialog(this);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int jbak = 0,jkolam = 0,jkaleng = 0,jtempayan=0,jlain=0, selectedID;
                int JmlKontainer=0, JmlPositif=0;
                RadioGroup[] radioGroups = {bak,kolam,kaleng,tempayan,lain};
                for (int i = 0 ; i < radioGroups.length ; i++){
                    selectedID = radioGroups[i].getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton) findViewById(selectedID);
                        if(rb != null){
                            JmlKontainer++;
                            Log.i("rb>>", rb.getText().toString());
                            if(rb.getText().toString().startsWith("(+)") ){
                                switch (i){
                                    case 0 : jbak = 1;break;
                                    case 1 : jkolam = 1; break;
                                    case 2 : jkaleng= 1; break;
                                    case 3 : jtempayan = 1; break;
                                    case 4 : jlain = 1; break;
                                }
                                JmlPositif++;
                            }
                        }

                }

                if(JmlKontainer == 0){
                    Toast.makeText(InputHasilPantauActivity.this
                    ,"Pilih minimal satu kontainer",Toast.LENGTH_LONG).show();
                }
                Log.i("ko","kol : "+jkolam+"bak"+jbak+"tempy"+jtempayan+"kal"+jkaleng+"lain"+jlain);
                Log.i("rb>>", ""+JmlKontainer);
                Log.i("rb>>", ""+JmlPositif);

                insert_data(tglPantau.getText().toString()
                            ,jbak
                            ,jkolam
                            ,jkaleng
                            ,jtempayan
                            ,jlain
                            ,JmlKontainer
                            ,JmlPositif);


            }
        });



    }



    private void insert_data(final String tgl, final int jbak, final int jkolam, final int jkaleng
            , final int jtempayan, final int jlain, final int jkontainer, final int jpositif) {
        final float jkon, jpos;
        jkon = ((float) jkontainer);
        jpos = ((float) jpositif);
        final String m_namaKK = nama_kk;
        final String m_noKK = nomor_kk;
        final float indexJentik;
        indexJentik = jpos / jkon;


        progressDialog.setMessage("Menyimpan Data");
        progressDialog.show();

        final String url = "http://mpdev.000webhostapp.com/insert_data_pantau.php";
        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
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
                params.put("no_kk", m_noKK);
                params.put("tanggal",tgl);
                params.put("nama_kk", m_namaKK);
                params.put("kontainer_bak",""+jbak);
                params.put("kontainer_kolam",""+jkolam);
                params.put("kontainer_tempayan",""+jtempayan);
                params.put("kontainer_kaleng",""+jkaleng);
                params.put("kontainer_lain",""+jlain);
                params.put("jml_kontainer",""+jkontainer);
                params.put("jml_positif",""+jpositif);
                params.put("indexJentik",""+indexJentik);

                return params;
            }
        };
        UserRequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

}
