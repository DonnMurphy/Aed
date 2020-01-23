package com.jambons.aed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class ScanUtils {
    private IntentIntegrator qrScan;
    private final Activity ScanActivity;
    Context ScanContext;

    ScanUtils(Context appContext, Activity appActivity) {
        qrScan = new IntentIntegrator(appActivity);
        qrScan.initiateScan();
        Log.wtf("Start Scan:", "INITIATE SCAN!!");
        this.ScanActivity = appActivity;
        this.ScanContext = appContext;

    }

    public void scanCode(){
        qrScan.initiateScan();

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.wtf("onActivity Message:", "I am in on activity result!!");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {

                toastAsync("Result Not Found");
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews

                    toastAsync(obj.getString("message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    toastAsync(result.getContents());
                }
            }
        } else {
           // super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void toastAsync(String message) {
        ScanActivity.runOnUiThread(() -> {
            Toast.makeText(ScanContext, message, Toast.LENGTH_LONG).show();
            Log.wtf("Scan Message:", "MESSAGE" + message);
        });
    }
}
