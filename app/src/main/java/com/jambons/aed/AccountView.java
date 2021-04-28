package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.accounts.Account;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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
import com.google.zxing.WriterException;

import org.json.JSONObject;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

// Get / User from firebase code based on tutorial:
//"#4 Firebase Authentication Tutorial - Displaying User Info" - SImplified Coding
//https://www.youtube.com/watch?v=f6hdvMMeVSE
public class AccountView extends AppCompatActivity {
    String fireAuthUserId;
    TextView tvAccountName, tvAccountBalance, tvAccountEmail,  tvSassyViewer,tvIsVerified;
    ImageView ivAccountImage;
    //EthUtils eth;

    //For QR Code Generation
    ImageView ivAccountCode;
    CardView crdAccountInfo, crdAccountTitle;
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;
    Bitmap accountBitmap;
    String profileImage;
    Button  btnUpdateAccount, btnTopUpAccount, btnShareQrCode;

    //For Transfer Functionality
    String transferSheepId, transferSheepName;
    Button btnTransferSheep;

    int userBal;
    boolean greedyFlag;


    Toolbar topToolbar;
    private String accountBalance, userName, userId, currentUserId;
    MenuItem mnDonieWallet, mnUserName;
    private FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_view);
        //TOOLBAR METHODS
        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);
        mAuth = FirebaseAuth.getInstance();
        //userName = mAuth.getCurrentUser().getDisplayName();
        currentUserId = mAuth.getCurrentUser().getUid().trim();

        userId = getIntent().getExtras().getString("user_id").trim();
        Log.wtf("USER ID COMPARISON: ", userId + " -------" + currentUserId);

        // Setting View Eleemnts
        crdAccountInfo = findViewById(R.id.user_info);
        crdAccountTitle = findViewById(R.id.crdAccountTitle);
        tvAccountName = findViewById(R.id.tvAccountName);
        TextView tvWalletNetwork = findViewById(R.id.tvWalletNetwork);
        TextView tvSmartContractAddress = findViewById(R.id.tvSmartContract);
        tvAccountBalance = findViewById(R.id.tvAccountBalance);
        tvIsVerified = findViewById(R.id.tvIsVerified);
        tvAccountEmail = findViewById(R.id.tvAccountEmail);


        btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        btnTopUpAccount = findViewById(R.id.btnTopUpAccount);
        btnShareQrCode = findViewById(R.id.btnShareQrCode);
        tvSassyViewer = findViewById(R.id.tvViewAccountSassy);

        String userBalance = "Empty";
        String chainId = "Blokker";


        Button btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        ivAccountImage = findViewById(R.id.ivAccountImage);
        ivAccountCode = findViewById(R.id.ivAccountQrCode);

        btnTransferSheep = findViewById(R.id.btnConfirmSheepTransfer);
        chainId = "Ropsten Network!";//eth.getChain();
        tvSmartContractAddress.setText("0x0aC2E7ce9dC2598B521dC7C492be488a2eB6e9fa"); //TODO - Set this to equal to the used smart contract on rposten and let them click on it?? ////eth.getAddress1());
        tvWalletNetwork.setText(chainId); //TODO - Maybe Change this to a function to check which network -- OR HARDCODE INTO RES/STRINGS??
        loadUserDetails(userId);
        loadUserInformation();


        if(getIntent().getExtras().getBoolean("isTransfer", false) == true){
            // to, from, tokenId
            transferSheepId = getIntent().getExtras().getString("sheep_id").trim();
            transferSheepName = getIntent().getExtras().getString("sheep_name").trim();
            //TODO - Maybe add in the image but small?

            btnTransferSheep.setVisibility(View.VISIBLE);


        } else if(!(currentUserId.equals(userId))){
            tvSassyViewer.setVisibility(View.VISIBLE);
        }
        btnTopUpAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btnTopUpAccount.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Your account has been topped up by DB$ 10000", Toast.LENGTH_SHORT).show();
                topUpAccountBalance();
            }
        });

        btnShareQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUserQrCode();
            }
        });

        btnTransferSheep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Log.wtf("OMG WORK PLZ BID", userId + "/" + currentUserId +"/" + transferSheepId +"/");
                        if (userId != null && currentUserId != null && transferSheepId != null) {
                            //etStartPrice.setText(null);
                            //etEndPrice.setText(null);
                            //etAuctionDuration.setText(null);
                            //TODO SHOW A PROGRESS INDICATOR OR SOMETHING
                            //Actually Call the method
                            Log.wtf("Inside Rest messsage", "Ok What is happening here");
                            btnTransferSheep.setVisibility(View.GONE);
                            //startLoadAnimation("We Are Transferring The Card Please Wait");
                            //TODO add in verification of numbers / trimming to above code
                            startLoadAnimation("Transferring Card Please Wait");
                            RestUtils.getInstance(getApplicationContext()).postTransferRequest( currentUserId, userId, transferSheepId, new SheepRestListener<JSONObject>() {
                                @Override
                                public void getResult(JSONObject Object) {
                                    JSONObject response = Object;
                                    Log.wtf("RESULT OF BIDDING ON SHEEP", response.toString());
                                    //deck_adapter adapter = new deck_adapter(appContext, sheeps);
                                    //TODO - ADD IN A PROGRESS BAR WHILE ITS BEING ADDED TO THE BLOCKCHAIN AND THEN GO TO AUCITONS PAGE
                                   stopLoadAnimation(R.layout.activity_account_view);


                                }
                            });


                        }
                    }
                    // set image using Glide
                    // Glide.with(this).load(sheepImage).apply(requestOptions).into(ivSheepImage);

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

    private void shareUserQrCode(){
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        } else {
            Drawable mDrawable = ivAccountCode.getDrawable();
            Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

            String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image Description", null);
            Uri uri = Uri.parse(path);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Share Image"));
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
                            Intent J = new Intent(getApplicationContext(), ViewAuction.class);
                            startActivity(J);

                            // Thread.stop();
                        }
                    });
                }
            }
        });
        t.start();
    }

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();


        if (user != null) {
           /* if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(ivAccountImage);
            }

            if (user.getDisplayName() != null) {
                tvAccountName.setText(user.getDisplayName());
            }
            */
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
                startActivity(new Intent(this, AccountView.class));
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

    private void loadUserDetails(String inUserId){
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
                                    userName = d.get("userName").toString();
                                    //userId = d.get("userId").toString();
                                    accountBalance = "DB$ " +d.get("accountBalance").toString();
                                    profileImage = d.get("profileImage").toString();

                                    tvAccountName.setText(userName);
                                    tvAccountBalance.setText(accountBalance);
                                    btnTransferSheep.setText("Transfer " +transferSheepName + " to " + userName);

                                    Glide.with(getApplicationContext())
                                            .load(profileImage.toString())
                                            .into(ivAccountImage);

                                    createQrCode(userId);
                                    Log.wtf("USER ID COMPARISON: ", userId + " -------" + currentUserId);
                                    if(!(currentUserId.equals(userId))){
                                        crdAccountInfo.setVisibility(View.GONE);
                                        crdAccountTitle.setVisibility(View.GONE);

                                        btnUpdateAccount.setVisibility(View.GONE);
                                        btnTopUpAccount.setVisibility(View.GONE);
                                        btnShareQrCode.setVisibility(View.GONE);
                                    }


                                }
                            }
                        }
                    }
                });
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

    public void createQrCode(String code_user_id) {
        Bitmap bitmap;
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        try {
            JSONObject qrCodeBody = new JSONObject();
            qrCodeBody.put("application", "cryptids_fyp");
            qrCodeBody.put("code_type", "user");
            qrCodeBody.put("user_id", code_user_id);
            Log.wtf("THE JSON BODY Cancel", qrCodeBody.toString());

            // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
            QRGEncoder qrgEncoder = new QRGEncoder(qrCodeBody.toString(), null, QRGContents.Type.TEXT, smallerDimension);
            qrgEncoder.setColorBlack(R.color.colorAccent);
            qrgEncoder.setColorWhite(Color.WHITE);

            try {
                // Getting QR-Code as Bitmap
                bitmap = qrgEncoder.getBitmap();
                // Setting Bitmap to ImageView
                ivAccountCode.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.wtf("SOMETHING WENT WRONG", e.toString());
            }
        } catch (Exception e) {
            Log.wtf("SOMETHING WENT WRONG", e.toString());
        }
    }

    private void topUpAccountBalance() {

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

                                if (currentUserId.equals(d.getId())) {
                                    //foundFlag = true;
                                    //fireStoreDocId = d.getId();
                                    Log.wtf("Result", d.get("accountBalance").toString());

                                    //userId = d.get("userId").toString();
                                    userBal = Integer.valueOf(d.get("accountBalance").toString());
                                    if (greedyFlag == false) {
                                        userBal = userBal + 10000;

                                        db.collection("Users").document(userId)
                                                .update(
                                                        "accountBalance", userBal
                                                )
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                });
                                        Toast.makeText(getApplicationContext(), "You Balance Has Been Topped Up", Toast.LENGTH_LONG);
                                        //TODO - See if i can make a joke about mobile add-ons

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Dont Be Greedy! Make More Donie Bucks Through Auctions! Whoo Capitalism", Toast.LENGTH_LONG);
                                    }
                                }
                            }
                        }
                    }
                });
        }

    }
