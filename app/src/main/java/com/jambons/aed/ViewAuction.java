package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

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
import android.widget.LinearLayout;
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
import org.web3j.abi.datatypes.Int;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ViewAuction extends AppCompatActivity {
    private TextView tvSheepName, tvSheepOwner, tvCurrentPrice, tvStartingPrice;
    private TextView tvEndingPrice, tvSheepID, tvStartedAt, tvTimeRemaining;
    private TextView tvAuctionWinner, tvFinalBid, tvAuctionTitle;
    private ImageView ivSheepImage;
    private Button btnBidOnSheep, btnCancelAuction;
    private String bidAmount, auctionStatus, finalBid, winnerId, auctionSheepName, auctionSheepUid, auctionSheepImageLink;
    private String currentPriceString, startedAt, auctionDuration, auctionStartedAt, auctionTimeRemaining, winnerName;
    private int minBid;
    int userBal;
    private LinearLayout llWinner, llFinalBid, llRemainingTime;
    private CardView crdAuctionTitle;
    private JSONObject response;


    private String auctionId, sellerId, sheepId, sellerName;
    private ImageView imgAnimatedCard;

    Toolbar topToolbar;
    private String accountBalance, userName, userId;
    MenuItem mnDonieWallet, mnUserName;
    private FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_auction);

        //TOOLBAR METHODS
        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);

        FireUtils.getInstance(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        userName = mAuth.getCurrentUser().getDisplayName();
        userId = mAuth.getCurrentUser().getUid();
        getUserBalance();

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
        auctionSheepName = getIntent().getExtras().getString("auction_sheep_name");
        auctionSheepUid = getIntent().getExtras().getString("auction_sheep_uid");
        auctionSheepImageLink = getIntent().getExtras().getString("auction_sheep_image_link");
        auctionStartedAt = getDateFromTimeStamp(startedAt);
        auctionTimeRemaining = getTimeRemaining(startedAt, auctionDuration);

        sellerName= FireUtils.getInstance().getUserNameById(sellerId);
        winnerName = FireUtils.getInstance().getUserNameById(winnerId);

        //TODO - ADD IN CARD THAT IS HIDDEN UNLESS AUCTION STATUS IS FINISHED AND REMOVE BUTTONS IF TRUE

        // Setting the Values of the Text Views
        ivSheepImage = findViewById(R.id.ivAuctionSheepDisplay);
        // tvSheepID = findViewById(R.id.tvSheepId);
        tvStartingPrice = findViewById(R.id.tvStartingPrice);
        tvEndingPrice = findViewById(R.id.tvEndingPrice);
        tvSheepName = findViewById(R.id.tvAuctionSheepName);
        tvCurrentPrice = findViewById(R.id.tvCurrentPrice);
        tvStartedAt = findViewById(R.id.tvStartedAt);
        tvSheepOwner = findViewById(R.id.tvSellerName);
        tvTimeRemaining = findViewById(R.id.tvRemainingTime);
        btnBidOnSheep = findViewById(R.id.btnAuctionBidOnSheep);
        btnCancelAuction = findViewById(R.id.btnAuctionCancelAuction);
        tvAuctionWinner = findViewById(R.id.tvAuctionWinner);
        tvFinalBid = findViewById(R.id.tvFinalBid);
        llWinner = findViewById(R.id.llWinner);
        llFinalBid = findViewById(R.id.llFinalBid);
        llRemainingTime = findViewById(R.id.llRemainingTime);
        crdAuctionTitle = findViewById(R.id.crdAuctionTitle);
        tvAuctionTitle = findViewById(R.id.tvAuctionTitle);





        // Setting Layout Based On Auction Status
        if(auctionStatus.equals("LIVE")){
            llFinalBid.setVisibility(View.GONE);
            crdAuctionTitle.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            llWinner.setVisibility(View.GONE);
            tvAuctionTitle.setText("Auction Live");
            Log.wtf("ABOUT TO GET THE PRICE FOR SHEEP: ", sheepId);
            RestUtils.getInstance(getApplicationContext()).getAuctionCurrentPrice(sheepId, new SheepRestListener<String>() {
                @Override
                public void getResult(String response) {
                    currentPriceString = response;
                    minBid = (Integer.valueOf(currentPriceString) + 10);
                    bidAmount = String.valueOf(minBid);
                    Log.wtf("What is the price", response);
                    tvCurrentPrice.setText("DB$ " + currentPriceString);
                }
            });
        } else if (auctionStatus.equals("CANCELLED")) {
            crdAuctionTitle.setCardBackgroundColor(getResources().getColor(R.color.colorWarningRed));
            llFinalBid.setVisibility(View.GONE);
            llWinner.setVisibility(View.GONE);
            llRemainingTime.setVisibility(View.GONE);
            tvAuctionTitle.setText("Auction Cancelled");
            tvCurrentPrice.setText("Auction Cancelled By Owner");
        } else if (auctionStatus.equals("COMPLETED")) {
            crdAuctionTitle.setCardBackgroundColor(getResources().getColor(R.color.colorSuccessGreen));
            llFinalBid.setVisibility(View.VISIBLE);
            llWinner.setVisibility(View.VISIBLE);
            llRemainingTime.setVisibility(View.GONE);
            tvAuctionTitle.setText("Auction Complete");
            tvCurrentPrice.setText("Winning Bid: DB$ " + finalBid);
        }

        btnBidOnSheep.setVisibility(View.GONE);
        btnCancelAuction.setVisibility(View.GONE);
        //   tvSheepUID = f indViewById(R.id.tvSheepUID);
        Log.wtf("WHERE IS TE SHEEP ID:", sheepId);
        if(userId.equals(sellerId) && auctionStatus.equals("LIVE")){
            btnCancelAuction.setVisibility(View.VISIBLE);
        }
        //TODO ADD IN AUCTION IS LIVE CHECK TO PREVENT SPENDING ON OLD AUCTIONs
        //BIT OF AN ISSUE WITH SMART CONTRACTS
        if(!userId.equals(sellerId)&& auctionStatus.equals("LIVE")){
            btnBidOnSheep.setVisibility(View.VISIBLE);
        }
        tvSheepName.setText(auctionSheepName);
        //tvCurrentPrice.setText("DB$ " +currentPriceString);
        tvSheepOwner.setText(sellerName);
        tvStartingPrice.setText("DB$ " +startingPrice);
        tvEndingPrice.setText("DB$ " + endingPrice);
        tvStartedAt.setText(auctionStartedAt);
        tvTimeRemaining.setText(auctionTimeRemaining);
        tvFinalBid.setText(finalBid);
        tvAuctionWinner.setText(winnerName);


        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.sheep_dab).error(R.drawable.sheep_dab);
        Glide.with(this).load(auctionSheepImageLink).apply(requestOptions).into(ivSheepImage);

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
                    if(Integer.valueOf(bidAmount) < Integer.valueOf(userBal)) {
                        Log.wtf("Inside Rest messsage", "Ok What is happening here");
                        btnBidOnSheep.setVisibility(View.GONE);
                        startLoadAnimation("We Are Bidding On The Auction Please Wait");
                        //TODO add in verification of numbers / trimming to above code
                        RestUtils.getInstance(getApplicationContext()).postBidRequest(userId, sheepId, bidAmount, new SheepRestListener<JSONObject>() {
                            @Override
                            public void getResult(JSONObject Object) {
                                response = Object;
                                Log.wtf("RESULT OF BIDDING ON SHEEP", response.toString());
                                //deck_adapter adapter = new deck_adapter(appContext, sheeps);
                                //TODO - ADD IN A PROGRESS BAR WHILE ITS BEING ADDED TO THE BLOCKCHAIN AND THEN GO TO AUCITONS PAGE
                                stopLoadAnimation(R.layout.activity_view_auction);
                                // Set layout manager to position the items
                                // LinearLayoutManager layoutManager = new LinearLayoutManager(c);
                                //rvCards.setLayoutManager(new LinearLayoutManager(appContext));
                                // Attach the adapter to the recyclerview to populate items
                                //rvCards.setAdapter(adapter);
                            }
                        });
                        transferDonieBucks(userId, "DEBIT");
                        transferDonieBucks(sellerId, "CREDIT");
                        getUserBalance();
                    } else {
                        Toast.makeText(getApplicationContext(), "Your Wallet Does Not Have Enough Funds For This Transaction. Wait For the Price to drop or Topup Your Account By Pressing Your Account Balance", Toast.LENGTH_LONG);
                    }
                }
            }
            // set image using Glide


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
                    RestUtils.getInstance(getApplicationContext()).cancelAuctionRequest(userId, sheepId, new SheepRestListener<JSONObject>() {
                        @Override
                        public void getResult(JSONObject Object) {
                            response = Object;
                            Log.wtf("RESULT OF Cancelling Auction", response.toString());
                            stopLoadAnimation(R.layout.activity_view_auction);
                            Toast.makeText(getApplicationContext(), "Auction Cancellation Successful", Toast.LENGTH_LONG);
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
        bottomNavigationView.setSelectedItemId(R.id.action_view_auctions);
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
                        Intent W = new Intent(getApplicationContext(),ViewAuctions.class);
                        startActivity(W);
                        break;
                    case R.id.action_view_all:

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

    private String getTimeRemaining(String timestamp, String auctionDuration){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        Long tsNow = System.currentTimeMillis()/1000;
        Long startTime = Long.valueOf(timestamp);
        Long duration = Long.valueOf(auctionDuration);
        String timeRemaining;

        Long endTimestamp = startTime + duration;

        Long timeDifference = endTimestamp - tsNow;

        Log.wtf("TIME STAMP", timeDifference.toString());

        if(timeDifference > 0) {
            timeRemaining = String.format("%d hrs %d min %d sec",
                    TimeUnit.SECONDS.toHours(timeDifference),
                    TimeUnit.SECONDS.toMinutes(timeDifference) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(timeDifference)),
                    timeDifference -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(timeDifference))
            );
        }else {
            timeRemaining = "N/A";
        }
        return timeRemaining;
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
                if (Thread.interrupted()) {
                    // We've been interrupted: no more crunching.
                    return;
                } else {
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
                            Intent J = new Intent(getApplicationContext(), ViewAuctions.class);
                            startActivity(J);

                            // Thread.stop();
                        }
                    });
                }
            }
        });
        t.start();
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
                                    userBal = Integer.valueOf(d.get("accountBalance").toString());
                                    mnDonieWallet.setTitle(accountBalance);
                                }
                            }
                        }
                    }
                });
    }

    private void transferDonieBucks(String transactionUserId, String transactionType){

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

                                if(transactionUserId.equals(d.getId())){
                                    //foundFlag = true;
                                    //fireStoreDocId = d.getId();
                                    Log.wtf("Result", d.get("accountBalance").toString());
                                    userName = d.get("userName").toString();
                                    //userId = d.get("userId").toString();
                                    userBal = Integer.valueOf(d.get("accountBalance").toString());
                                    if(transactionType.equals("DEBIT")){
                                        userBal = userBal - Integer.valueOf(bidAmount);
                                        Log.wtf("DEBIT TRANSACTION", "DEBIT TRANSACTION STARTED FOR USER " + transactionUserId + " Of Value " + bidAmount);

                                        db.collection("Users").document(transactionUserId)
                                                .update(
                                                        "accountBalance", userBal
                                                )
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //TODO Maybe Add Something In Here
                                                    }
                                                });
                                    }else if(transactionType.equals("CREDIT")){
                                        userBal = userBal + Integer.valueOf(bidAmount);
                                        Log.wtf("CREDIT TRANSACTION", "CREDIT TRANSACTION STARTED FOR USER " + transactionUserId + " Of Value " + bidAmount);
                                        db.collection("Users").document(transactionUserId)
                                                .update(
                                                        "accountBalance", userBal
                                                )
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                });

                                    }
                                }
                            }
                        }
                    }
                });
    }
}
