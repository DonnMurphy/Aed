package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ScanMenu extends AppCompatActivity {
    private IntentIntegrator qrScan;
    ImageView btnStartScan;
    TextView tvScanResults;

    Toolbar topToolbar;
    private String accountBalance, userName, userId;
    MenuItem mnDonieWallet, mnUserName;
    private FirebaseFirestore db;
    FirebaseAuth mAuth;
    Boolean isTransfer;
    String sheepName, sheepId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_menu);
        btnStartScan = findViewById(R.id.btnStartScan);
        tvScanResults = (TextView) findViewById(R.id.tvScanResults);
        btnStartScan.setImageResource(R.drawable.ic_start_scan);

        //TOOLBAR METHODS
        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);
        mAuth = FirebaseAuth.getInstance();
        userName = mAuth.getCurrentUser().getDisplayName();
        userId = mAuth.getCurrentUser().getUid();
        getUserBalance();

        qrScan = new IntentIntegrator(this);
        if(getIntent().getExtras().getBoolean("is_transfer", false) == true)  {

            isTransfer = getIntent().getExtras().getBoolean("is_transfer");
            sheepName = getIntent().getExtras().getString("sheep_name");
            sheepId = getIntent().getExtras().getString("sheep_id");
            Log.wtf("TRANSFER SCAN PRECHECK", isTransfer + " -- " + sheepName + " -- " + sheepId);
        }

        if (getIntent().getExtras().getBoolean("is_transfer", false) == true){
            tvScanResults.setText("Scan a users QR Code to transfer the Card to them!");
        } else {
            tvScanResults.setText("Scan a Card to view/register it or Scan a User Profile to view their Account!");
        }
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
        bottomNavigationView.setSelectedItemId(R.id.action_scan);
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
                    case R.id.action_open_dual:
                        Intent K = new Intent(getApplicationContext(),CardDual.class);
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
                    //tvScanResults.setText(obj.toString());
                    if (obj.getString("application") != null) {
                        if(obj.getString("code_type") != null) {
                            if(obj.getString("code_type").equals("sheep")) {
                                if (obj.getString("sheep_id") != null) {
                                    showSheep(obj.getString("sheep_id"));
                                    Toast.makeText(ScanMenu.this, "Sheep Id Is:" + obj.getString("sheep_id"), Toast.LENGTH_LONG);

                                } // FIrst If
                            } else if(obj.getString("code_type").equals("user")) {
                                if (obj.getString("user_id") != null) {
                                    showUser(obj.getString("user_id"));
                                    Toast.makeText(ScanMenu.this, "User Id Is:" + obj.getString("user_id"), Toast.LENGTH_LONG);

                                } // FIrst If
                            }

                        }
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


    private void showSheep(String sheepId){
        RestUtils.getInstance(getApplicationContext()).getSheepById(sheepId, new SheepRestListener<Sheep>() {
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
    }

    private void showUser(String userId){
        //TODO This code should do something
        Intent viewAccountIntent = new Intent(getApplicationContext(), AccountView.class);
        viewAccountIntent.putExtra("user_id",userId);
        if(isTransfer != null ) {
            Log.wtf("TRANSFER SCAN CHECK", isTransfer + " -- " + sheepName + " -- " + sheepId);
            viewAccountIntent.putExtra("sheep_id",sheepId);
            viewAccountIntent.putExtra("sheep_name",sheepName);
            viewAccountIntent.putExtra("isTransfer", isTransfer);
            sheepId = null;
            sheepName = null;
            isTransfer = null;
            Log.wtf("TRANSFER SCAN POSTCHECK", isTransfer + " -- " + sheepName + " -- " + sheepId);
            //sheepId = getIntent().getExtras().getString("sheep_id").trim();
        }

        startActivity(viewAccountIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_toolbar_menu, menu);
        mnDonieWallet = topToolbar.getMenu().findItem(R.id.AccountBalanceItem);
        mnDonieWallet.setTitle(accountBalance);
        mnUserName = topToolbar.getMenu().findItem(R.id.viewUserName);
        mnUserName.setTitle(userName);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewAccountItem:
                Intent viewAccountIntent = new Intent(getApplicationContext(),AccountView.class);
                viewAccountIntent.putExtra("user_id",userId);
                startActivity(viewAccountIntent);
                return true;
            case R.id.signOutItem:
                mAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.viewChatItem:
                Intent jk = new Intent(getApplicationContext(), AuctionMessageThread.class);
                startActivity(jk);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getUserBalance(){
        //fireAuthUserId = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        db.collection("Users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Log.wtf("User", userId);
                                Log.wtf("Doc", d.getId());

                                if(userId.equals(d.getId())){
                                    //foundFlag = true;
                                    //fireStoreDocId = d.getId();
                                    Log.wtf("Result", d.get("accountBalance").toString());
                                    accountBalance = "DB$ " +d.get("accountBalance").toString();
                                    mnDonieWallet.setTitle(accountBalance);
                                }
                            }
                        }
                    }
                });
    }


}
