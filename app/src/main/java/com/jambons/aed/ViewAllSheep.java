package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ViewAllSheep extends AppCompatActivity {
    // Volly Get and Post code based on tutorial:
    //" Android Volly Get and Post Tutorial"
    // https://youtube.com/watch?wzK19AaEgzk
    ArrayList<Sheep> sheeps;
    ArrayList<Sheep> sheep;
    Context appContext;
    EthUtils eth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_sheep);
        appContext = getApplicationContext();

        //eth = new EthUtils(this.getContext(), this.getActivity());

        //       eth.connectToEthNetwork();
        //eth.createWallet();
        //eth.getAddress();


        //View view = inflater.inflate(R.layout.fragment_view_deck, container, false);
        RecyclerView rvCards = this.findViewById(R.id.rvCardList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCards.getContext(),
                DividerItemDecoration.VERTICAL);
        rvCards.addItemDecoration(dividerItemDecoration);
        // Initialize cards

        RestUtils.getInstance(getApplicationContext()).getAllSheep(new SheepRestListener<ArrayList<Sheep>>() {
            @Override
            public void getResult(ArrayList<Sheep> Object) {
                sheeps = Object;
                Log.wtf("WHERE ARE THE SHEEP", sheeps.toString());
                deck_adapter adapter = new deck_adapter(appContext, sheeps);

                // Set layout manager to position the items
                // LinearLayoutManager layoutManager = new LinearLayoutManager(c);
                rvCards.setLayoutManager(new LinearLayoutManager(appContext));
                // Attach the adapter to the recyclerview to populate items
                rvCards.setAdapter(adapter);
            }
        });
        // Create adapter passing in the sample user data

        // That's all!


        //return view;
        // return super.onCreateView(inflater, container, savedInstanceState);

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



}


