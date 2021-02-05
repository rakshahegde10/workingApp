package com.nativehealth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.util.Log;
import java.util.*;

public class ReactModule extends ReactContextBaseJavaModule {

    //constructor
    public ReactModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    //Mandatory function getName that specifies the module name
    @Override
    public String getName() {
        return "nativehealth";
    }



    //Custom function that we are going to export to JS
    @ReactMethod
    public void getDeviceName(Callback cb) {
        try{
            cb.invoke(null, android.os.Build.MODEL);
        }catch (Exception e){
            cb.invoke(e.toString(), null);
        }
    }


    //Custom function that we are going to export to JS
    @ReactMethod
    public void getAuthorizations() {
        try{
            final Activity activity = getCurrentActivity();

            if(activity != null && activity instanceof MainActivity){
                ((MainActivity) activity).setFitnessOption();
                ((MainActivity) activity).checkFitInstalled();
            }
        }catch (Exception e){
            Log.i("error", "error!!!!");
        }
    }
}
