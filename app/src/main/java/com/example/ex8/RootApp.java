package com.example.ex8;
import com.google.gson.Gson;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class RootApp extends Application {
    private static RootApp instance = null;
    SharedPreferences sp =null;
    public SharedPreferences getSp(){ return sp; }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sp= this.getSharedPreferences("local_db_todo", MODE_PRIVATE);
    }
    public static RootApp getInstance(){
        return instance;
    }

    public int loadFromSp(String id){
        return sp.getInt(id,2);
    }

    public void saveInSp(String id,int mid_calc){
        SharedPreferences.Editor editor= sp.edit();
        editor.putInt(id,mid_calc);
        editor.apply();
    }
    public void clearSp(){
        SharedPreferences.Editor editor= sp.edit();
        editor.clear();
        editor.apply();
    }
}
