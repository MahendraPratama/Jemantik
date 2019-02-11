package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BerandaActivity extends AppCompatActivity {
    private View mMainView;
    String JSON_STRING;
    private ImageButton btnlogin;
    private TextView dt;
    final ArrayList<HashMap<String, Object>> list = new ArrayList<>();
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);


        gridView = (GridView) findViewById(R.id.gridView_listBeritaBeranda);
        btnlogin = (ImageButton) findViewById(R.id.btn_login);
        dt = (TextView) findViewById(R.id.dateview);

        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            if(SharedPrefManager.getInstance(this).getKodeLevel() == 1){
                startActivity(new Intent(this, MenuAdminActivity.class));
            }else
            {
                startActivity(new Intent(this, MenuUserActivity.class));
            }
            finish();
            return; // return untuk tidak mengeksekusi line code dibawah kalau user sudah login
        }

        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy");//formating according to my need
        String date = formatter.format(today);
        dt.setText(date);

        getJSON(BerandaActivity.this);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BerandaActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    public void getJSON(final Activity activity) {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity,
                        "Fetching Data", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                show(activity);

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest("https://mpdev.000webhostapp.com/getBerita.php");

                return s;

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void show(final Activity activity) {
        try {
            if(list.isEmpty()){
                JSONArray result = new JSONArray(JSON_STRING);
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);
                    String idberita = jo.getString("id_berita");
                    String judulberita = jo.getString("judul_berita");
                    String pathgambar = jo.getString("path_gambar");
                    String isiberita = jo.getString("isi_berita");

                    HashMap<String, Object> ItemBerita = new HashMap<>();
                    ItemBerita.put("id_berita", idberita);
                    ItemBerita.put("judul_berita", judulberita);
                    ItemBerita.put("path_gambar", pathgambar);
                    ItemBerita.put("isi_berita", isiberita);
                    list.add(ItemBerita);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(
                        activity.LAYOUT_INFLATER_SERVICE);
                View gridview;
                gridview = inflater.inflate(R.layout.item_single_berita, null);
                TextView jdlberita = (TextView) gridview.findViewById(R.id.list_judulberita);
                jdlberita.setText((CharSequence) list.get(position).get("judul_berita"));

                ImageView gambarBerita = (ImageView) gridview.findViewById(R.id.list_gambarberita);
                Glide.with(activity)
                        .load(list.get(position).get("path_gambar"))
                        .placeholder(R.drawable.placeholder_berita_jemantik)
                        .into(gambarBerita);
                return gridview;
            }
        });
        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent i = new Intent(activity, ContinueReadingBeritaActivity.class);
                i.putExtra("idberita",list.get(position).get("id_berita").toString());

                String path = list.get(position).get("path_gambar").toString();
                i.putExtra("gambar", path);
                i.putExtra("judul", list.get(position).get("judul_berita").toString());
                i.putExtra("isi", list.get(position).get("isi_berita").toString());

                startActivity(i);
            }
        });

    }
}
