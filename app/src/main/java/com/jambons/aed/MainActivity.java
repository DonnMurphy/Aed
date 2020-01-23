package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ViewDeck()).commit();
        }
       // Button btnViewDeck = findViewById(R.id.btnViewDeck);

       // Button btnToast = findViewById(R.id.btnToast);

        Log.wtf("hey", "heyeyeye");

       /* btnViewDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("hey", "heyeyeye");
                Intent intent = new Intent(v.getContext(), ViewDeck.class);
                v.getContext().startActivity(intent);
            }
        });
        btnToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("hey", "heyeyeye");
                Toast toast = Toast.makeText(getApplicationContext(), "LOLOLOL",  Toast.LENGTH_LONG);
                toast.show();
            }
        });


    */
    // OnClickListeners For Bottom Nav - TODO Code Refs!!!
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_scan:
                        Intent I = new Intent(getApplicationContext(),ScanMenu.class);
                        startActivity(I);
                        break;
                    case R.id.action_view_deck:
                        Intent J = new Intent(getApplicationContext(),ViewDeck2.class);
                        startActivity(J);
                        break;
                    case R.id.action_account:
                        Toast.makeText(MainActivity.this, "Account", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_settings:
                        Intent j = new Intent(getApplicationContext(), new_nav_test.class);
                        startActivity(j);
                        break;
                    case R.id.action_view_all:
                        Toast.makeText(MainActivity.this, "View All Cards", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_scan:
               // Intent launchIntent = BarcodeReaderActivity.getLaunchIntent(this, true, false);
                //s/tartActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                //        new ScannerFragment()).commit();
                //Intent I = new Intent(getApplicationContext(),ScanMenu.class);
                //startActivity(I);
                Intent j = new Intent(getApplicationContext(), new_nav_test.class);
                startActivity(j);
                break;

            case R.id.nav_view_deck:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ViewDeck()).commit();
                //Toast.makeText(this, "View Deck", Toast.LENGTH_LONG).show();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }



}
