package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText username, password;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.button_login);
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);

        progressDialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((username.length() == 0) || (password.length() == 0)){
                    Toast.makeText(getApplicationContext(), "Required fields are missing", Toast.LENGTH_LONG).show();
                }else{
                    userLogin();
                }
            }
        });
    }

    private void userLogin() {
        final String usrnm = username.getText().toString().trim();
        final String pwd = password.getText().toString().trim();

        progressDialog.setMessage("Loading data user..");
        progressDialog.show();

        final String url = "http://mpdev.000webhostapp.com/loginJentik.php?username='"+usrnm+"'&password='"+pwd+"'";

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.d("url",url);
                            Log.d("response",response);
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if (!jsonObject.isNull("username")) { //not error
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(
                                        jsonObject.getString("username"),
                                        jsonObject.getString("nama"),
                                        jsonObject.getString("alamat"),
                                        jsonObject.getString("no_telp"),
                                        jsonObject.getString("password"),
                                        jsonObject.getInt("kode_level")
                                );

                                if(SharedPrefManager.getInstance(getApplicationContext()).getKodeLevel() == 1){
                                    startActivity(new Intent(LoginActivity.this, MenuAdminActivity.class));
                                }else{
                                    startActivity(new Intent(LoginActivity.this, MenuUserActivity.class));
                                }
                                finish();
                            }else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "login gagal, cek username dan password",
                                        Toast.LENGTH_LONG
                                ).show();
                                password.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );


        UserRequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
