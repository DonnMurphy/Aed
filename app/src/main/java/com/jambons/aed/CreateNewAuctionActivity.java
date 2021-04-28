package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.List;

public class CreateNewAuctionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private EditText etStartPrice, etEndPrice, etAuctionDuration;
    private TextView tvSheepName, tvOwnerName;
    private ImageView ivSheepImage;
    private Button btnFinalizeAuction;
    private String startingPrice, endingPrice, auctionDuration;
    private  String userId, response;
    Toolbar topToolbar;
    private String accountBalance, userName;
    private Spinner spnAuctionTimeFrames;
    MenuItem mnDonieWallet, mnUserName;
    private FirebaseFirestore db;
    FirebaseAuth mAuth;
    String strTimeFrame;
    private Handler loadHandler;

    private static final String[] auctionTimeFrames = {"Hours", "Days", "Weeks"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_auction);

        //TOOLBAR METHODS
        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);
        mAuth = FirebaseAuth.getInstance();
        userName = mAuth.getCurrentUser().getDisplayName();
        userId = mAuth.getCurrentUser().getUid();
        getUserBalance();

        //Set Values of the Controls
        etStartPrice = findViewById(R.id.etCreateAuctionStartingPrice);
        etEndPrice = findViewById(R.id.etCreateAuctionEndingPrice);
        etAuctionDuration = findViewById(R.id.etCreateAuctionDuration);
        btnFinalizeAuction = findViewById(R.id.btnCreateAuctionFinalizeAuction);
        ivSheepImage = findViewById(R.id.ivCreateAuctionSheepImage);

        tvSheepName = findViewById(R.id.tvCreateAuctionSheepName);


        spnAuctionTimeFrames = (Spinner) findViewById(R.id.spnTime);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,auctionTimeFrames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAuctionTimeFrames.setAdapter(dataAdapter);

        spnAuctionTimeFrames.setOnItemSelectedListener(this);
       // tvOwnerName = findViewById(R.id.tvCreateAuctionSellerName);

        String sheepId  = getIntent().getExtras().getString("sheep_id");
        String sheepName  = getIntent().getExtras().getString("sheep_name");
        String ownerId = userId;//getIntent().getExtras().getString("owner_id");
        String sheepImageLink = getIntent().getExtras().getString("sheep_image_link");


        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.sheep_dab).error(R.drawable.sheep_dab);


        // set image using Glide
        Glide.with(this).load(sheepImageLink).apply(requestOptions).into(ivSheepImage);


//        tvOwnerName.setText(ownerId);
        tvSheepName.setText(sheepName);

        btnFinalizeAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etStartPrice.getText().toString() == null){

                    Toast.makeText(CreateNewAuctionActivity.this, "Start Price Is Empty Please fill in all auction fields", Toast.LENGTH_LONG);
                    etStartPrice.findFocus();
                    finish();
                }

                if(etEndPrice.getText().toString() == null){

                    Toast.makeText(CreateNewAuctionActivity.this, "End Price Is Empty Please fill in all auction fields", Toast.LENGTH_LONG);
                    etEndPrice.findFocus();
                    finish();
                }

                if(etAuctionDuration.getText().toString() == null){

                    Toast.makeText(CreateNewAuctionActivity.this, "Auction Duration Is Empty Please fill in all auction fields", Toast.LENGTH_LONG);
                    etAuctionDuration.findFocus();
                    finish();
                }
                //TODO - Add in code to make sure I cant creeate a free or instant auction!!!
                // Test Values make sense
                endingPrice = etEndPrice.getText().toString().trim();
                startingPrice = etStartPrice.getText().toString().trim();
                auctionDuration = etAuctionDuration.getText().toString().trim();
                auctionDuration = timeFrameToSeconds(auctionDuration, strTimeFrame);


                if(Double.valueOf(startingPrice) <= Double.valueOf(endingPrice)){
                    Toast.makeText(CreateNewAuctionActivity.this, "Starting Price Must Always be higher than the Ending price", Toast.LENGTH_LONG);
                    etStartPrice.findFocus();
                    finish();
                }

                if(Integer.valueOf(auctionDuration) < 60){
                    Toast.makeText(CreateNewAuctionActivity.this, "Auctions Cannot be less than one minute", Toast.LENGTH_LONG);
                    etAuctionDuration.findFocus();
                    finish();
                }

                Log.wtf("OMG WORK PLz", startingPrice + " / " + endingPrice + " /" + auctionDuration);
                if(auctionDuration != null && startingPrice != null && endingPrice != null){
                    etStartPrice.setText(null);
                    etEndPrice.setText(null);
                    etAuctionDuration.setText(null);
                    //Actually Call the method
                    Log.wtf("Inside Rest messsage","Ok What is happening here");
                    btnFinalizeAuction.setVisibility(View.GONE);
                    //TODO add in verification of numbers / trimming to above code
                    startLoadAnimation("Creating Auction For " + sheepName );
                    RestUtils.getInstance(getApplicationContext()).createNewAuction(userId, sheepId, startingPrice, endingPrice, auctionDuration, new SheepRestListener<JSONObject>() {
                        @Override
                        public void getResult(JSONObject Object) {
                            response = Object.toString();
                            Log.wtf("RESULT OF CREATING AUCTION", response.toString());
                            //deck_adapter adapter = new deck_adapter(appContext, sheeps);
                            stopLoadAnimation(R.layout.activity_create_new_auction);
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

    private String timeFrameToSeconds(String numVal, String timeFrame){
        int timeMultipler = 0;
        int durationSeconds = 0;
        if(timeFrame.equals("Hours")){
           timeMultipler = 60 * 60;
        } else if(timeFrame.equals("Days")){
            timeMultipler = 24 * 60 * 60;
        }if(timeFrame.equals("Weeks")){
            timeMultipler = 7 * 24 *60 * 60;
        }

       durationSeconds = Integer.valueOf(numVal) * timeMultipler;


       return String.valueOf(durationSeconds);

    }

    private void stopLoadAnimation(int default_id){
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
                            Toast.makeText(getApplicationContext(), "Successfully created Auction! It may take a bit to load", Toast.LENGTH_LONG);
                            //new Handler().postDelayed(this, 2000);
                            Intent J = new Intent(getApplicationContext(), ViewDeck.class);
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
                                    mnDonieWallet.setTitle(accountBalance);
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
               strTimeFrame = "Hours";

                break;
            case 1:
                strTimeFrame = "Days";

                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                strTimeFrame = "Weeks";
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}
