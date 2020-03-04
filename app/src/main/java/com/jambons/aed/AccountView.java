package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.util.List;
// Get / User from firebase code based on tutorial:
//"#4 Firebase Authentication Tutorial - Displaying User Info" - SImplified Coding
//https://www.youtube.com/watch?v=f6hdvMMeVSE
public class AccountView extends AppCompatActivity {
    FirebaseAuth mAuth;
    String fireAuthUserId;
    TextView tvAccountName, tvAccountBalance, tvAccountEmail, tvIsVerified;
    ImageView ivAccountImage;
    private FirebaseFirestore db;
    //EthUtils eth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_view);
        mAuth = FirebaseAuth.getInstance();

        // Setting View Eleemnts
        tvAccountName = findViewById(R.id.tvAccountName);
        TextView tvWalletNetwork = findViewById(R.id.tvWalletNetwork);
        TextView tvSmartContractAddress = findViewById(R.id.tvSmartContract);
        tvAccountBalance = findViewById(R.id.tvAccountBalance);
        tvIsVerified = findViewById(R.id.tvIsVerified);
        tvAccountEmail = findViewById(R.id.tvAccountEmail);

        String userBalance = "Empty";
        String chainId = "Blokker";
        Button btnSignOut = findViewById(R.id.btnSignOut);
        Button btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        ivAccountImage = findViewById(R.id.ivAccountImage);

        chainId = "Ropsten Network!";//eth.getChain();
        tvSmartContractAddress.setText("0x0"); //TODO - Set this to equal to the used smart contract on rposten and let them click on it?? ////eth.getAddress1());
        tvWalletNetwork.setText(chainId); //TODO - Maybe Change this to a function to check which network -- OR HARDCODE INTO RES/STRINGS??
        loadUserInformation();
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(AccountView.this, LoginActivity.class));
            }
        });

        btnUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //eth.createASheep();
                //TODO - signout method here

                startActivity(new Intent(AccountView.this, AccountUpdate.class));
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
    private void getUserBalance(){
        fireAuthUserId = mAuth.getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();


        db.collection("Users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                       // progressBar.setVisibility(View.GONE);

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : list) {
                                Log.wtf("User", fireAuthUserId);
                                Log.wtf("Doc", d.getId());

                                if(fireAuthUserId.equals(d.getId())){
                                    //foundFlag = true;
                                    //fireStoreDocId = d.getId();
                                    Log.wtf("Result", d.get("accountBalance").toString());
                                    tvAccountBalance.setText(d.get("accountBalance").toString() + " Donie Bucks");

                                }
                                //  User u = d.toObject(User.class);
                                //  u.setDocId(d.getId());
                                // productList.add(p);

                            }
                           // if(foundFlag == true && !(fireStoreDocId == null)){
                           //     tvAccountBalance.setText(fireStoreDocId, fireAuthUserId, userName);
                           // }else{
                           //     createFirestoreUserDoc(fireAuthUserId, userName);
                           // }


                        }


                    }
                });
    }


    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(ivAccountImage);
            }

            if (user.getDisplayName() != null) {
                tvAccountName.setText(user.getDisplayName());
            }

            if (user.getEmail() != null) {
                tvAccountEmail.setText(user.getEmail());
            }
            getUserBalance();
            if (user.isEmailVerified()) {
                tvIsVerified.setText("Email Verified");
            } else {
                tvIsVerified.setText("Email Not Verified (Click to Verify)");
                tvIsVerified.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AccountView.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }
    }
