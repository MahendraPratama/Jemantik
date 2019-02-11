package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.content.Context;
        import android.content.SharedPreferences;


//Store User data
public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME    = "mysharedpref";
    private static final String KEY_NAMA        = "nama";
    private static final String KEY_ALAMAT      = "alamat";
    private static final String KEY_NO_TELP        = "no_telp";
    private static final String KEY_KODE_LEVEL   = "kode_level";
    private static final String KEY_USERNAME     = "username";
    private static final String KEY_PASSWORD     = "password";



    private SharedPrefManager(Context context){
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(String username, String nama, String alamat, String no_telp, String password, int kode_level){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor     = sharedPreferences.edit();

        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NAMA, nama);
        editor.putString(KEY_ALAMAT, alamat);
        editor.putString(KEY_NO_TELP, no_telp);
        editor.putString(KEY_PASSWORD, password);
        editor.putInt(KEY_KODE_LEVEL, kode_level);


        editor.apply();

        return true;
    }

    public boolean update(String nama, String alamat, String no_telp, String password){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor     = sharedPreferences.edit();

        editor.putString(KEY_NAMA, nama);
        editor.putString(KEY_ALAMAT, alamat);
        editor.putString(KEY_NO_TELP, no_telp);
        editor.putString(KEY_PASSWORD, password);

        editor.commit();

        return true;
    }

    //Check user Login
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USERNAME,null) != null){
            return true;
        }
        return false;
    }

    //Check user Logout
    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();

        return true;
    }

    public String getUsername(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME,null);
    }
    public String getPassword(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PASSWORD,null);
    }
    public String getAlamat(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ALAMAT,null);
    }
    public String getNoTelp(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NO_TELP,null);
    }
    public String getNama(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAMA,null);
    }

    public int getKodeLevel(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_KODE_LEVEL,0);
    }

}
