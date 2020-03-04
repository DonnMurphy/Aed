package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Locale;

public class ViewAuction extends AppCompatActivity {
    private TextView tvSheepName, tvSheepOwner, tvCurrentPrice, tvStartingPrice, tvEndingPrice, tvSheepID, tvStartedAt;
    private ImageView ivSheepImage;
    private Button btnBidOnSheep, btnCancelAuction;
    private String userId;
    private String bidAmount, auctionStatus, finalBid, winnerId;
    private String response, currentPriceString, startedAt, auctionDuration;
    private int minBid;
    FirebaseAuth mAuth;
    private String auctionId, sellerId, sheepId;
    private ImageView imgAnimatedCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_auction);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
         auctionId = getIntent().getExtras().getString("auction_id");
         sheepId = getIntent().getExtras().getString("sheep_id");
         sellerId = getIntent().getExtras().getString("seller_id");
         auctionDuration = getIntent().getExtras().getString("auction_duration");
        String startingPrice = getIntent().getExtras().getString("starting_price");
        // int nb_episode = getIntent().getExtras().getInt("anime_nb_episode") ;
        String endingPrice = getIntent().getExtras().getString("ending_price");
        startedAt = getIntent().getExtras().getString("started_at");
        auctionStatus = getIntent().getExtras().getString("auction_status");
        finalBid = getIntent().getExtras().getString("auction_final_bid");
        winnerId = getIntent().getExtras().getString("auction_winner_id");

        //TODO - ADD IN CARD THAT IS HIDDEN UNLESS AUCTION STATUS IS FINISHED AND REMOVE BUTTONS IF TRUE

        // Setting the Values of the Text Views
        ivSheepImage = findViewById(R.id.ivSheepDisplay);
        // tvSheepID = findViewById(R.id.tvSheepId);
        tvStartingPrice = findViewById(R.id.tvStartingPrice);
        tvEndingPrice = findViewById(R.id.tvEndingPrice);
        tvSheepName = findViewById(R.id.tvSheepName);
        tvCurrentPrice = findViewById(R.id.tvCurrentPrice);
        tvStartedAt = findViewById(R.id.tvStartedAt);
        tvSheepOwner = findViewById(R.id.tvSellerName);
        btnBidOnSheep = findViewById(R.id.btnAuctionBidOnSheep);
        btnCancelAuction = findViewById(R.id.btnAuctionCancelAuction);
        Log.wtf("ABOUT TO GET THE PRICE FOR SHEEP: ", sheepId);
        RestUtils.getInstance(getApplicationContext()).getAuctionCurrentPrice(sheepId, new SheepRestListener<String>() {
            @Override
            public void getResult(String response) {
                currentPriceString = response;
                minBid = (Integer.valueOf(currentPriceString) + 10);
                bidAmount = String.valueOf(minBid);
                Log.wtf("What is the price", response);
                tvCurrentPrice.setText(currentPriceString);
            }
        });
        btnBidOnSheep.setVisibility(View.GONE);
        btnCancelAuction.setVisibility(View.GONE);
        //   tvSheepUID = f indViewById(R.id.tvSheepUID);
        Log.wtf("WHERE IS TE SHEEP ID:", sheepId);
        if(userId.equals(sellerId)){
            btnCancelAuction.setVisibility(View.VISIBLE);
        }
        //TODO ADD IN AUCTION IS LIVE CHECK TO PREVENT SPENDING ON OLD AUCTIONs
        //BIT OF AN ISSUE WITH SMART CONTRACTS
        if(!userId.equals(sellerId)){
            btnBidOnSheep.setVisibility(View.VISIBLE);
        }
        tvSheepName.setText(sheepId);
        tvCurrentPrice.setText(currentPriceString);
        tvSheepOwner.setText(sellerId);
        tvStartingPrice.setText(startingPrice);
        tvEndingPrice.setText(endingPrice);
        tvStartedAt.setText(startedAt);

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.sheep_dab).error(R.drawable.sheep_dab);

        btnBidOnSheep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Get Current Price On Load

                Log.wtf("OMG WORK PLZ BID", bidAmount + "/" + sheepId +"/" +currentPriceString +"/");
                if (bidAmount != null && sheepId != null && userId != null && currentPriceString != null) {
                    //etStartPrice.setText(null);
                    //etEndPrice.setText(null);
                    //etAuctionDuration.setText(null);
                    //TODO SHOW A PROGRESS INDICATOR OR SOMETHING
                    //Actually Call the method
                    Log.wtf("Inside Rest messsage", "Ok What is happening here");
                    btnBidOnSheep.setVisibility(View.GONE);
                    startLoadAnimation("We Are Bidding On The Auction Please Wait");
                    //TODO add in verification of numbers / trimming to above code
                    RestUtils.getInstance(getApplicationContext()).postBidRequest(userId, sheepId, bidAmount, new SheepRestListener<String>() {
                        @Override
                        public void getResult(String Object) {
                            response = Object;
                            Log.wtf("RESULT OF BIDDING ON SHEEP", response.toString());
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
            // set image using Glide
            // Glide.with(this).load(sheepImage).apply(requestOptions).into(ivSheepImage);

        });


        btnCancelAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Get Current Price On Load

                Log.wtf("OMG WORK PLZ Cancel", bidAmount);
                if (sellerId != null && sellerId.equals(userId)) {
                    //etStartPrice.setText(null);
                    //etEndPrice.setText(null);
                    //etAuctionDuration.setText(null);
                    //TODO HIDE BUTTON AND SHOW A PROGRESS INDICATOR OR SOMETHING
                    //Actually Call the method
                    Log.wtf("Inside Rest messsage", "Ok What is happening here");
                    btnCancelAuction.setVisibility(View.GONE);
                    //TODO add in verification of numbers / trimming to above code

                    startLoadAnimation("We Are Cancelling The Auction Please Wait");
                    RestUtils.getInstance(getApplicationContext()).postBidRequest(userId, sheepId, bidAmount, new SheepRestListener<String>() {
                        @Override
                        public void getResult(String Object) {
                            response = Object;
                            Log.wtf("RESULT OF BIDDING ON SHEEP", response.toString());
                            //deck_adapter adapter = new deck_adapter(appContext, sheeps);
                            //TODO - ADD IN A PROGRESS BAR WHILE ITS BEING ADDED TO THE BLOCKCHAIN AND THEN GO TO AUCITONS PAGE

                            // Set layout manager to position the items
                            // LinearLayoutManager layoutManager = new LinearLayoutManager(c);
                            //rvCards.setLayoutManager(new LinearLayoutManager(appContext));
                            // Attach the adapter to the recyclerview to populate items
                            //rvCards.setAdapter(adapter);
                        }
                    });


                } else{
                    //TODO PLACE TOAST HERE TELLING USER THAT THEY CANNOT CANCEL AUCTION
                }
            }
            // set image using Glide
            // Glide.with(this).load(sheepImage).apply(requestOptions).into(ivSheepImage);

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
                        //TODO JAYSUS TAKE A LOOK AT THIS
                        startLoadAnimation("We Are Cancelling Load Animation Please Wait");
                        //Intent W = new Intent(getApplicationContext(),ViewAuctions.class);
                        //startActivity(W);
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

    private void stopLoadAnimation(){
        setContentView(R.layout.activity_view_auction);
    }
}
