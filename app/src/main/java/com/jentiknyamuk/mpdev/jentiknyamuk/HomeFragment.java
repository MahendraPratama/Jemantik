package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.fido.fido2.api.common.RequestOptions;
import com.google.android.gms.fido.fido2.api.common.TokenBindingIdValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeFragment extends Fragment {
    private View mMainView;
    String JSON_STRING;
    private ImageButton addberita;
    final ArrayList<HashMap<String, Object>> list = new ArrayList<>();
    GridView gridView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainView =  inflater.inflate(R.layout.fragment_home, container, false);
        //setContentView(R.layout.activity_data_kader);

        gridView = (GridView) mMainView.findViewById(R.id.gridView_listBerita);
        addberita = (ImageButton) mMainView.findViewById(R.id.btn_addberita);
        addberita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AddBeritaActivity.class));
            }
        });
        getJSON();
        return mMainView;
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity()
                        , "Fetching Data", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                show();

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

    private void show() {
        try {
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
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(
                        getActivity().LAYOUT_INFLATER_SERVICE);
                View gridview;
                gridview = inflater.inflate(R.layout.item_single_berita, null);
                TextView jdlberita = (TextView) gridview.findViewById(R.id.list_judulberita);
                jdlberita.setText((CharSequence) list.get(position).get("judul_berita"));

                ImageView gambarBerita = (ImageView) gridview.findViewById(R.id.list_gambarberita);
                Glide.with(getActivity())
                        .load(list.get(position).get("path_gambar"))
                        .placeholder(R.drawable.placeholder_berita_jemantik)
                        .into(gambarBerita);
                return gridview;
            }
        });
        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent i = new Intent(getActivity(), ContinueReadingBeritaActivity.class);
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
