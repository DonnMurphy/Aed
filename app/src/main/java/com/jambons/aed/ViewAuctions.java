package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAuctions extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<Auction> auctions;
    ArrayList<Sheep> sheep;
    Context appContext;
    EthUtils eth;
    private Spinner spnAuctionItems;
    RecyclerView rvAuctions;

    Toolbar topToolbar;
    private String accountBalance, userName, userId;
    MenuItem mnDonieWallet, mnUserName;
    private FirebaseFirestore db;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    private static final String[] auctionViews = {"Live Auctions", "My Auctions", "All Auctions"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_auctions);
        appContext = getApplicationContext();

        //TOOLBAR METHODS
        topToolbar = findViewById(R.id.top_toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        setSupportActionBar(topToolbar);
        mAuth = FirebaseAuth.getInstance();
        userName = mAuth.getCurrentUser().getDisplayName();
        userId = mAuth.getCurrentUser().getUid();

        spnAuctionItems = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, auctionViews);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAuctionItems.setAdapter(dataAdapter);

        spnAuctionItems.setOnItemSelectedListener(this);

        //.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        getUserBalance();
        //eth = new EthUtils(this.getContext(), this.getActivity());

        //       eth.connectToEthNetwork();
        //eth.createWallet();
        //eth.getAddress();


        //View view = inflater.inflate(R.layout.fragment_view_deck, container, false);
        rvAuctions = this.findViewById(R.id.rvAuctionList);
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvAuctions.getContext(),
        //        DividerItemDecoration.VERTICAL);
        //rvAuctions.addItemDecoration(dividerItemDecoration);
        // Initialize cards
        progressBar.setVisibility(View.VISIBLE);
        RestUtils.getInstance(getApplicationContext()).getLiveAuctions(new SheepRestListener<ArrayList<Auction>>() {
            @Override
            public void getResult(ArrayList<Auction> Object) {
                auctions = Object;
                Log.wtf("WHERE ARE THE SHEEP", auctions.toString());
                auctions_adapter adapter = new auctions_adapter(appContext, auctions);

                // Set layout manager to position the items
                // LinearLayoutManager layoutManager = new LinearLayoutManager(c);
                rvAuctions.setLayoutManager(new LinearLayoutManager(appContext));
                // Attach the adapter to the recyclerview to populate items
                rvAuctions.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }
        });
        // Create adapter passing in the sample user data


        //return view;
        // return super.onCreateView(inflater, container, savedInstanceState);

        // NAVIGATION BAR CODE TODO - MOVE TO FRAGMENT
        // OnClickListeners For Bottom Nav - TODO Code Refs!!!
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_view_auctions);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_scan:
                        Intent I = new Intent(getApplicationContext(), ScanMenu.class);
                        I.putExtra("is_transfer", false);
                        startActivity(I);
                        //ScanUtils qrScanner;
                        // qrScanner = new ScanUtils(getApplicationContext(), appActivity);
                        break;
                    case R.id.action_view_deck:
                        Intent J = new Intent(getApplicationContext(), ViewDeck.class);
                        startActivity(J);
                        break;
                    case R.id.action_open_dual:
                        Intent K = new Intent(getApplicationContext(), CardDual.class);
                        startActivity(K);
                        break;
                    case R.id.action_view_auctions:
                        //Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        //break;
                        Intent W = new Intent(getApplicationContext(), ViewAuctions.class);
                        startActivity(W);
                        break;
                    case R.id.action_view_all:
                        // Toast.makeText(MainActivity.this, "View All Cards", Toast.LENGTH_SHORT).show();
                        //break;
                        Intent M = new Intent(getApplicationContext(), ViewAllSheep.class);
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
                Intent viewAccountIntent = new Intent(getApplicationContext(), AccountView.class);
                viewAccountIntent.putExtra("user_id", userId);
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

    private void getUserBalance() {
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

                                if (userId.equals(d.getId())) {
                                    //foundFlag = true;
                                    //fireStoreDocId = d.getId();
                                    Log.wtf("Result", d.get("accountBalance").toString());
                                    accountBalance = "DB$ " + d.get("accountBalance").toString();
                                    mnDonieWallet.setTitle(accountBalance);
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        String auctionView = parent.getItemAtPosition(position).toString();


        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                progressBar.setVisibility(View.VISIBLE);
                RestUtils.getInstance(getApplicationContext()).getLiveAuctions(new SheepRestListener<ArrayList<Auction>>() {
                    @Override
                    public void getResult(ArrayList<Auction> Object) {
                        auctions = Object;
                        Log.wtf("WHERE ARE THE SHEEP", auctions.toString());
                        auctions_adapter adapter = new auctions_adapter(appContext, auctions);

                        // Set layout manager to position the items
                        // LinearLayoutManager layoutManager = new LinearLayoutManager(c);
                        rvAuctions.setLayoutManager(new LinearLayoutManager(appContext));
                        // Attach the adapter to the recyclerview to populate items
                        rvAuctions.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                });
                break;
            case 1:
                progressBar.setVisibility(View.VISIBLE);
                RestUtils.getInstance(getApplicationContext()).getAuctionsByOwner(userId, new SheepRestListener<ArrayList<Auction>>() {
                    @Override
                    public void getResult(ArrayList<Auction> Object) {
                        auctions = Object;
                        Log.wtf("WHERE ARE THE SHEEP", auctions.toString());
                        auctions_adapter adapter = new auctions_adapter(appContext, auctions);

                        // Set layout manager to position the items
                        // LinearLayoutManager layoutManager = new LinearLayoutManager(c);
                        rvAuctions.setLayoutManager(new LinearLayoutManager(appContext));
                        // Attach the adapter to the recyclerview to populate items
                        rvAuctions.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                });
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                progressBar.setVisibility(View.VISIBLE);
                RestUtils.getInstance(getApplicationContext()).getAllAuctions(new SheepRestListener<ArrayList<Auction>>() {
                    @Override
                    public void getResult(ArrayList<Auction> Object) {
                        auctions = Object;
                        Log.wtf("WHERE ARE THE SHEEP", auctions.toString());
                        auctions_adapter adapter = new auctions_adapter(appContext, auctions);

                        // Set layout manager to position the items
                        // LinearLayoutManager layoutManager = new LinearLayoutManager(c);
                        rvAuctions.setLayoutManager(new LinearLayoutManager(appContext));
                        // Attach the adapter to the recyclerview to populate items
                        rvAuctions.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                });
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }


}
