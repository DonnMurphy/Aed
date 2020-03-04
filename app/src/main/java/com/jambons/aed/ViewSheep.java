package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Locale;

public class ViewSheep extends AppCompatActivity {
    private TextView tvSheepName, tvSheepDp, tvSheepHp, tvSheepOwner, tvSheepUID, tvMintDate;
    private ImageView ivSheepImage;
    private Button btnRegisterSheep, btnReleaseSheep, btnAuctionSheep;
    private String sheepScanned, userId, sheepId, sheepMintTime;
    private String sheepName, sheepUID, sheepHp, sheepDp, sheepOwner,sheepImage, response, releasedOwner;
    FirebaseAuth mAuth;
    //String fireAuthUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sheep);
        releasedOwner = getResources().getString(R.string.sheep_released_identifer).trim();
        sheepScanned = null;
        //TODO For testing release later
        //TODO PLEASE GET THE SHEEPS ID SOON
        //TODO ADD IN SHEEPID TO CREATE SHEEP METHOD
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        sheepId = getIntent().getExtras().getString("sheep_id").trim();

        sheepName  = getIntent().getExtras().getString("sheep_name").trim();
        sheepUID = getIntent().getExtras().getString("sheep_uid").trim();
        sheepHp = getIntent().getExtras().getString("sheep_hp").trim() ;
        sheepDp = getIntent().getExtras().getString("sheep_dp").trim();
       // int nb_episode = getIntent().getExtras().getInt("anime_nb_episode") ;
        sheepOwner = getIntent().getExtras().getString("sheep_owner").trim() ;
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


        tvSheepName.setText(sheepName);
        tvSheepDp.setText(sheepDp);
        tvSheepHp.setText(sheepHp);
        tvSheepOwner.setText(sheepOwner);
        tvSheepUID.setText(sheepUID);
        tvMintDate.setText(getDateFromTimeStamp(sheepMintTime));
        btnRegisterSheep.setVisibility(View.GONE);
        btnReleaseSheep.setVisibility(View.GONE);
        btnAuctionSheep.setVisibility(View.GONE);

        if(sheepScanned != null) {
            if (sheepScanned.equals("true") && sheepOwner.equals(getResources().getString(R.string.sheep_released_identifer))) {
                btnRegisterSheep.setVisibility(View.VISIBLE);
            }
        }

        if(userId.equals(sheepOwner)){
            btnReleaseSheep.setVisibility(View.VISIBLE);
            btnAuctionSheep.setVisibility(View.VISIBLE);
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
                    RestUtils.getInstance(getApplicationContext()).registerSheepRequest(userId,sheepId, new SheepRestListener<String>() {
                        @Override
                        public void getResult(String Object) {
                            response = Object;
                            Log.wtf("RESULT OF Registering Sheep", response);
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
                    //TODO add in verification of numbers / trimming to above code
                    //TODO HIDE THE RELEASE AND AUCTION BUTTONS AFTER PRESSING AND REPLACE WITH LOADING ICONS
                    RestUtils.getInstance(getApplicationContext()).releaseSheepRequest(userId,sheepId, new SheepRestListener<String>() {
                        @Override
                        public void getResult(String Object) {
                            response = Object;
                            Log.wtf("RESULT OF Registering Sheep", response);
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
    private String getDateFromTimeStamp(String timestamp){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.valueOf(timestamp));
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        return date;
    }
}
