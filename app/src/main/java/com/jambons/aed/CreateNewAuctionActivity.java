package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

public class CreateNewAuctionActivity extends AppCompatActivity {
    private EditText etStartPrice, etEndPrice, etAuctionDuration;
    private TextView tvSheepName, tvOwnerName;
    private ImageView ivSheepImage;
    private Button btnFinalizeAuction;
    private String startingPrice, endingPrice, auctionDuration;
    private  String userId, response;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_auction);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        //Set Values of the Controls
        etStartPrice = findViewById(R.id.etCreateAuctionStartingPrice);
        etEndPrice = findViewById(R.id.etCreateAuctionEndingPrice);
        etAuctionDuration = findViewById(R.id.etCreateAuctionDuration);
        btnFinalizeAuction = findViewById(R.id.btnCreateAuctionFinalizeAuction);
        ivSheepImage = findViewById(R.id.ivCreateAuctionSheepImage);

        tvSheepName = findViewById(R.id.tvCreateAuctionSheepName);
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
                    RestUtils.getInstance(getApplicationContext()).createNewAuction(userId, sheepId, startingPrice, endingPrice, auctionDuration, new SheepRestListener<String>() {
                        @Override
                        public void getResult(String Object) {
                            response = Object;
                            Log.wtf("RESULT OF CREATING AUCTION", response.toString());
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
}
