package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.fido.fido2.api.common.RequestOptions;
import com.google.android.gms.fido.fido2.api.common.TokenBindingIdValue;
import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class HomeFragment extends Fragment {
    private View mMainView;
    String JSON_STRING;
    private ImageButton addberita,refresh;
    final ArrayList<HashMap<String, Object>> list = new ArrayList<>();
    GridView gridView;
    TextView dt;
    LinearLayout menuberita;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getJSON(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainView =  inflater.inflate(R.layout.fragment_home, container, false);
        //setContentView(R.layout.activity_data_kader);

        gridView = (GridView) mMainView.findViewById(R.id.gridView_listBerita);

        refresh = (ImageButton) mMainView.findViewById(R.id.btn_refreshberita);
        dt = (TextView) mMainView.findViewById(R.id.dateview);
        menuberita = (LinearLayout) mMainView.findViewById(R.id.menu_add_berita);
        if(SharedPrefManager.getInstance(getContext()).getKodeLevel() == 2){
            menuberita.setVisibility(View.INVISIBLE);
        }else
        {
            addberita = (ImageButton) mMainView.findViewById(R.id.btn_addberita);
            addberita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),AddBeritaActivity.class));
                }
            });
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getJSON(getActivity());
            }
        });
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy");//formating according to my need
        String date = formatter.format(today);
        dt.setText(date);
        if(JSON_STRING!=null){
            show(getActivity());
        }

        return mMainView;
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
                //Log.d("==",":"+s);
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
                    String tglberita = jo.getString("tanggal_berita");

                    HashMap<String, Object> ItemBerita = new HashMap<>();
                    ItemBerita.put("id_berita", idberita);
                    ItemBerita.put("judul_berita", judulberita);
                    ItemBerita.put("path_gambar", pathgambar);
                    ItemBerita.put("isi_berita", isiberita);
                    ItemBerita.put("tanggal_berita", tglberita);
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
                i.putExtra("tgl", list.get(position).get("tanggal_berita").toString());

                startActivity(i);

            }
        });

    }
}
