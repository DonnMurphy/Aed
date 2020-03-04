package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ScanMenu extends AppCompatActivity {
    private IntentIntegrator qrScan;
    FloatingActionButton btnStartScan;
    TextView tvScanResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_menu);
        btnStartScan = (FloatingActionButton) findViewById(R.id.btnStartScan);
        tvScanResults = (TextView) findViewById(R.id.tvScanResults);


        qrScan = new IntentIntegrator(this);
        btnStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //IntentIntegrator integrator = new IntentIntegrator(activity);
                //integrator.setPrompt("Start scanning");
                //integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                //integrator.setOrientationLocked(false);
                //integrator.initiateScan();
                //integrator.initiateScan();
                qrScan.initiateScan();
            }
        });





        // NAVIGATION BAR CODE TODO - MOVE TO FRAGMENT
        // OnClickListeners For Bottom Nav - TODO Code Refs!!!
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_scan:
                        Intent I = new Intent(getApplicationContext(),ScanMenu.class);
                        startActivity(I);
                        //ScanUtils qrScanner;
                        // qrScanner = new ScanUtils(getApplicationContext(), appActivity);
                        break;
                    case R.id.action_view_deck:
                        Intent J = new Intent(getApplicationContext(), ViewDeck.class);
                        startActivity(J);
                        break;
                    case R.id.action_account:
                        Intent K = new Intent(getApplicationContext(),AccountView.class);
                        startActivity(K);
                        break;
                    case R.id.action_view_auctions:
                        //Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        //break;
                        Intent W = new Intent(getApplicationContext(),ViewAuctions.class);
                        startActivity(W);
                        break;
                    case R.id.action_view_all:
                        // Toast.makeText(MainActivity.this, "View All Cards", Toast.LENGTH_SHORT).show();
                        //break;
                        Intent M = new Intent(getApplicationContext(),ViewAllSheep.class);
                        startActivity(M);
                        break;
                }
                return true;
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    //tvScanResults.setText(obj.getString("message"));
                    tvScanResults.setText(obj.toString());
                    if (obj.getString("application") != null) {
                        if (obj.getString("sheep_id") != null) {
                            Toast.makeText(ScanMenu.this, "Sheep Id Is:" + obj.getString("sheep_id"), Toast.LENGTH_LONG);
                            RestUtils.getInstance(getApplicationContext()).getSheepById(obj.getString("sheep_id"), new SheepRestListener<Sheep>() {
                                @Override
                                public void getResult(Sheep Object) {
                                    Sheep scanSheep = Object;
                                    Log.wtf("LOOK UPON MY SHEEPIE", scanSheep.getSheepName());
                                    // deck_adapter adapter = new deck_adapter(appContext, sheeps);

                                    // Set layout manager to position the items
                                    // LinearLayoutManager layoutManager = new LinearLayoutManager(c);
                                    //rvCards.setLayoutManager(new LinearLayoutManager(appContext));
                                    // Attach the adapter to the recyclerview to populate items
                                    //rvCards.setAdapter(adapter);
                                    Intent viewSheepIntent = new Intent(getApplicationContext(), ViewSheep.class);
                                    viewSheepIntent.putExtra("sheep_id",scanSheep.getSheepId());
                                    viewSheepIntent.putExtra("sheep_name", scanSheep.getSheepName());
                                    viewSheepIntent.putExtra("sheep_uid", scanSheep.getSheepUID());
                                    viewSheepIntent.putExtra("sheep_hp", scanSheep.getSheepHp());
                                    viewSheepIntent.putExtra("sheep_dp", scanSheep.getSheepDp());
                                    viewSheepIntent.putExtra("sheep_owner", scanSheep.getSheepOwner());
                                    viewSheepIntent.putExtra("sheep_imageLink", scanSheep.getSheepImageLink());
                                    viewSheepIntent.putExtra("sheep_mint_time", scanSheep.getSheepMintedDate());
                                    viewSheepIntent.putExtra("sheep_scanned", "true");

                                    //TODO - BAD CODING PRACTISE - IF SOLUTION FOUND APPLY
                                    viewSheepIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    getApplicationContext().startActivity(viewSheepIntent);
                                }
                            });
                        } // FIrst If
                    } // Second If

                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast

                    Toast.makeText(this, "Scan Error: This Scanner only works with Cryptids Playing Cards - Please use correct QR Codes", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
