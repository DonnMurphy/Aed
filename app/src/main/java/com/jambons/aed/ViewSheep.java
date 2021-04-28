package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ViewSheep extends AppCompatActivity {
    private TextView tvSheepName, tvSheepDp, tvSheepHp, tvSheepOwner, tvSheepUID, tvMintDate;
    private ImageView ivSheepImage;
    private Button btnRegisterSheep, btnReleaseSheep, btnAuctionSheep, btnTransferSheep;
    private String sheepScanned, sheepId, sheepMintTime;
    private String sheepName, sheepUID, sheepHp, sheepDp, sheepOwner,sheepImage,  releasedOwner, escrowOwner;
private JSONObject response;
    Toolbar topToolbar;
    private String sheepOwnerName;
    private String accountBalance, userName, userId;
    MenuItem mnDonieWallet, mnUserName;
    private FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sheep);
        releasedOwner = getResources().getString(R.string.sheep_released_identifer).trim();
        escrowOwner = getResources().getString(R.string.sheep_escrow_identifer).trim();
        sheepScanned = null;
        //TODO For testing release later
        //TODO PLEASE GET THE SHEEPS ID SOON
        //TODO ADD IN SHEEPID TO CREATE SHEEP METHOD
        //TOOLBAR METHODS
        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);
        mAuth = FirebaseAuth.getInstance();
        userName = mAuth.getCurrentUser().getDisplayName();
        userId = mAuth.getCurrentUser().getUid();
        getUserBalance();

        FireUtils.getInstance(getApplicationContext());

        sheepId = getIntent().getExtras().getString("sheep_id").trim();

        sheepName  = getIntent().getExtras().getString("sheep_name").trim();
        sheepUID = getIntent().getExtras().getString("sheep_uid").trim();
        sheepHp = getIntent().getExtras().getString("sheep_hp").trim() ;
        sheepDp = getIntent().getExtras().getString("sheep_dp").trim();
       // int nb_episode = getIntent().getExtras().getInt("anime_nb_episode") ;
        sheepOwner = getIntent().getExtras().getString("sheep_owner").trim() ;
        if(sheepOwner.equals(escrowOwner)){
            sheepOwnerName = escrowOwner;
        } else if(sheepOwner.equals(releasedOwner)) {
            sheepOwnerName = releasedOwner;

        }else{

            sheepOwnerName = FireUtils.getInstance().getUserNameById(sheepOwner);
            Log.wtf("SHEEEP OWNER Id: ", sheepOwner);
            Log.wtf("SHEEEP OWNER NAME: ", sheepOwnerName);
        }
         sheepImage = getIntent().getExtras().getString("sheep_imageLink").trim();
        sheepMintTime = getIntent().getExtras().getString("sheep_mint_time").trim();
        if(getIntent().getExtras().getString("sheep_scanned") != null ) {
            sheepScanned = getIntent().getExtras().getString("sheep_scanned").trim();
            //sheepId = getIntent().getExtras().getString("sheep_id").trim();
        }



        // Setting the Values of the Text Views
        ivSheepImage = findViewById(R.id.ivSheepDisplay);
        tvSheepName = findViewById(R.id.tvSheepName);
        tvSheepHp = findViewById(R.id.tvSheepHP);
        tvSheepDp = findViewById(R.id.tvSheepDP);
        tvSheepOwner = findViewById(R.id.tvOwnerName);
        tvSheepUID = findViewById(R.id.tvSheepUID);
        tvMintDate = findViewById(R.id.tvSheepMintedDate);
        btnRegisterSheep = findViewById(R.id.btnRegisterSheep);
        btnReleaseSheep = findViewById(R.id.btnReleaseSheep);
        btnAuctionSheep = findViewById(R.id.btnCreateSheepAuctionIntent);
        btnTransferSheep = findViewById(R.id.btnTransferSheep);


        tvSheepName.setText(sheepName);
        tvSheepDp.setText(sheepDp);
        tvSheepHp.setText(sheepHp);
        tvSheepOwner.setText(sheepOwnerName);
        tvSheepUID.setText(sheepUID);
        tvMintDate.setText(getDateFromTimeStamp(sheepMintTime));
        btnRegisterSheep.setVisibility(View.GONE);
        btnReleaseSheep.setVisibility(View.GONE);
        btnAuctionSheep.setVisibility(View.GONE);
        btnTransferSheep.setVisibility(View.GONE);

        if(sheepScanned != null) {
            if (sheepScanned.equals("true") && sheepOwner.equals(getResources().getString(R.string.sheep_released_identifer))) {
                btnRegisterSheep.setVisibility(View.VISIBLE);
            }
        }

        if(userId.equals(sheepOwner)){
            btnReleaseSheep.setVisibility(View.VISIBLE);
            btnAuctionSheep.setVisibility(View.VISIBLE);
            btnTransferSheep.setVisibility(View.VISIBLE);
        }

        btnAuctionSheep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createAuctionIntent = new Intent(getApplicationContext(),CreateNewAuctionActivity.class);
                createAuctionIntent.putExtra("sheep_name",sheepName);
                //TODO GET SHEEP ID FOR HERE PLZ
                createAuctionIntent.putExtra("sheep_id",sheepId);
                createAuctionIntent.putExtra("owner_id", sheepOwner);
               // viewSheepIntent.putExtra("sheep_uid",shee);
               // viewSheepIntent.putExtra("sheep_hp",sheepDeck.get(viewHolder.getAdapterPosition()).getSheepHp());
               // viewSheepIntent.putExtra("sheep_dp",sheepDeck.get(viewHolder.getAdapterPosition()).getSheepDp());
               // viewSheepIntent.putExtra("sheep_owner",sheepDeck.get(viewHolder.getAdapterPosition()).getSheepOwner());
                createAuctionIntent.putExtra("sheep_image_link",sheepImage);
                startActivity(createAuctionIntent);
            }
        });

        btnRegisterSheep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("OMG WORK PLz", userId + " / " + sheepId + " /" + sheepOwner  + " / " + releasedOwner);
                if(sheepId != null && userId != null && sheepOwner.trim().equals(releasedOwner.trim())){
                   // etStartPrice.setText(null);
                   // etEndPrice.setText(null);
                    //etAuctionDuration.setText(null);
                    //Actually Call the method
                    Log.wtf("Inside Rest messsage","Ok What is happening here");
                    btnRegisterSheep.setVisibility(View.GONE);
                    //TODO add in verification of numbers / trimming to above code
                    startLoadAnimation("Registering Card Please Wait");
                    RestUtils.getInstance(getApplicationContext()).registerSheepRequest(userId,sheepId, new SheepRestListener<JSONObject>() {
                        @Override
                        public void getResult(JSONObject Object) {
                            stopLoadAnimation(R.layout.activity_view_sheep);
                            response = Object;
                            Log.wtf("RESULT OF Registering Sheep", response.toString());
                            //deck_adapter adapter = new deck_adapter(appContext, sheeps);

                            // Set layout manager to position the items
                            // LinearLayoutManager layoutManager = new LinearLayoutManager(c);
                            //rvCards.setLayoutManager(new LinearLayoutManager(appContext));
                            // Attach the adapter to the recyclerview to populate items
                            //rvCards.setAdapter(adapter);
                        }
                    });



                }
            }
        });

        btnReleaseSheep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("OMG WORK PLz", userId + " / " + sheepId + " / bollocks" + sheepOwner);
                if(sheepId != null && userId != null && sheepOwner.trim().equals(userId.trim())){
                    // etStartPrice.setText(null);
                    // etEndPrice.setText(null);
                    //etAuctionDuration.setText(null);
                    //Actually Call the method
                    Log.wtf("Inside Rest messsage","Ok What is happening here");
                    btnReleaseSheep.setVisibility(View.GONE);
                    btnAuctionSheep.setVisibility(View.GONE);
                    startLoadAnimation("Releasing Card Please Wait");
                    //TODO add in verification of numbers / trimming to above code
                    //TODO HIDE THE RELEASE AND AUCTION BUTTONS AFTER PRESSING AND REPLACE WITH LOADING ICONS
                    RestUtils.getInstance(getApplicationContext()).releaseSheepRequest(userId,sheepId, new SheepRestListener<JSONObject>() {
                        @Override
                        public void getResult(JSONObject Object) {
                            stopLoadAnimation(R.layout.activity_view_sheep);
                            response = Object;
                            Log.wtf("RESULT OF Registering Sheep", response.toString());
                            //deck_adapter adapter = new deck_adapter(appContext, sheeps);
                            //TODO - ADD IN A PROGRESS BAR WHILE ITS BEING ADDED TO THE BLOCKCHAIN AND THEN GO TO AUCITONS PAGE

                            // Set layout manager to position the items
                            // LinearLayoutManager layoutManager = new LinearLayoutManager(c);
                            //rvCards.setLayoutManager(new LinearLayoutManager(appContext));
                            // Attach the adapter to the recyclerview to populate items
                            //rvCards.setAdapter(adapter);
                        }
                    });



                }
            }
        });
        btnTransferSheep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transferSheepIntentScan = new Intent(getApplicationContext(),ScanMenu.class);
                transferSheepIntentScan.putExtra("sheep_name",sheepName);
                //TODO GET SHEEP ID FOR HERE PLZ
                transferSheepIntentScan.putExtra("sheep_id",sheepId);
                transferSheepIntentScan.putExtra("is_transfer",true);
                startActivity(transferSheepIntentScan);

            }
        });

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.sheep_dab).error(R.drawable.sheep_dab);


        // set image using Glide
        Glide.with(this).load(sheepImage).apply(requestOptions).into(ivSheepImage);

        // OnClickListeners For Bottom Nav - TODO Code Refs!!!
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_scan:
                        Intent I = new Intent(getApplicationContext(),ScanMenu.class);
                        I.putExtra("is_transfer",false);
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
    private String getDateFromTimeStamp(String timestamp){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.valueOf(timestamp) * 1000L);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        return date;
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
                finish();
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
    private void startLoadAnimation(String loadingText){
        setContentView(R.layout.loading_screen);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        TextView tvLoadingText = findViewById(R.id.tvLoadingText);
        tvLoadingText.setText(loadingText);
        findViewById(R.id.ivSpinCard).startAnimation(rotateAnimation);
    }
    private void stopLoadAnimation(int default_id) {
        final Thread t = new Thread(new Runnable() {
            public void run() {
                //if (Thread.interrupted()) {
                    // We've been interrupted: no more crunching.
                //    return;
                //} else {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //Runnable run

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Success!!", Toast.LENGTH_LONG);
                            //new Handler().postDelayed(this, 2000);
                            Intent J = new Intent(getApplicationContext(), ViewDeck.class);
                            startActivity(J);

                            // Thread.stop();
                        }
                    });
                }
            //}
        });
        t.start();
    }
}
