package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;

// Get / Update User from firebase code based on tutorial:
//"#4 Firebase Authentication Tutorial - Displaying User Info" - SImplified Coding
//https://www.youtube.com/watch?v=f6hdvMMeVSE
//#3 Firebase Authentication Tutorial - Saving User Information
//https://www.youtube.com/watch?v=qVaBY92sOSI
public class AccountUpdate extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 101;

    TextView textView;
    ImageView imageView;
    EditText editText;

    Uri uriProfileImage;
    ProgressBar progressBar;
    boolean detailsPresentFlag;

    String profileImageUrl;
    String fireStoreDocId;

    boolean foundFlag;


    Toolbar topToolbar;
    private String accountBalance, userName, userId;
    MenuItem mnDonieWallet, mnUserName;
    private FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_update);
        foundFlag = false;
        detailsPresentFlag = false;


        //TOOLBAR METHODS
        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);
        mAuth = FirebaseAuth.getInstance();
        userName = mAuth.getCurrentUser().getDisplayName();
        userId = mAuth.getCurrentUser().getUid();
        getUserBalance();

        Log.wtf("User", mAuth.getCurrentUser().getEmail());
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        editText = (EditText) findViewById(R.id.editTextDisplayName);
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        textView = (TextView) findViewById(R.id.textViewVerified);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        loadUserInformation();

        if((profileImageUrl != null) && (userName != null)){
            detailsPresentFlag = true;
        }

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
                if((profileImageUrl != null) && (userName != null)){
                    detailsPresentFlag = true;
                }
                if(detailsPresentFlag) {
                    startActivity(new Intent(AccountUpdate.this, ViewDeck.class));
                } else {
                    Toast.makeText(AccountUpdate.this, "Please Complete Your Profile Before Continuing", Toast.LENGTH_LONG) ;
                }
            }
        });

        // NAVIGATION BAR CODE TODO - MOVE TO FRAGMENT
        // OnClickListeners For Bottom Nav - TODO Code Refs!!!
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_scan:
                        if(detailsPresentFlag) {
                            Intent I = new Intent(getApplicationContext(), ScanMenu.class);
                            I.putExtra("is_transfer", false);
                            startActivity(I);
                        } else {
                            Toast.makeText(AccountUpdate.this, "Please Complete Your Profile Before Continuing", Toast.LENGTH_LONG);
                        }
                        //ScanUtils qrScanner;
                        // qrScanner = new ScanUtils(getApplicationContext(), appActivity);
                        break;
                    case R.id.action_view_deck:
                        if(detailsPresentFlag) {
                            Intent J = new Intent(getApplicationContext(), ViewDeck.class);
                            startActivity(J);
                        } else {
                            Toast.makeText(AccountUpdate.this, "Please Complete Your Profile Before Continuing", Toast.LENGTH_LONG);
                        }
                        break;
                    case R.id.action_open_dual:
                        if(detailsPresentFlag) {
                            Intent K = new Intent(getApplicationContext(), CardDual.class);
                            startActivity(K);
                        } else {
                            Toast.makeText(AccountUpdate.this, "Please Complete Your Profile Before Continuing", Toast.LENGTH_LONG);
                        }
                        break;
                    case R.id.action_view_auctions:
                        if(detailsPresentFlag) {
                            Intent W = new Intent(getApplicationContext(), ViewAuctions.class);
                            startActivity(W);
                        } else {
                            Toast.makeText(AccountUpdate.this, "Please Complete Your Profile Before Continuing", Toast.LENGTH_LONG);
                        }
                        break;
                    case R.id.action_view_all:
                        if(detailsPresentFlag) {
                            Intent M = new Intent(getApplicationContext(), ViewAllSheep.class);
                            startActivity(M);
                        } else {
                            Toast.makeText(AccountUpdate.this, "Please Complete Your Profile Before Continuing", Toast.LENGTH_LONG);
                        }
                        break;
                }
                return true;
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                profileImageUrl = user.getPhotoUrl().toString();
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imageView);
            }

            if (user.getDisplayName() != null) {
                userName = user.getDisplayName();
                editText.setText(user.getDisplayName());
            }

            if(userName != null && profileImageUrl != null){
                detailsPresentFlag = true;
            }

            if (user.isEmailVerified()) {
                textView.setText("Email Verified");
            } else {
                textView.setText("Email Not Verified (Click to Verify)");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AccountUpdate.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
            }
        }
    }


    private void saveUserInformation() {


        String displayName = editText.getText().toString();

        if (displayName.isEmpty()) {
            editText.setError("Name required");
            editText.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        checkFireStoreForUser();
        Toast.makeText(AccountUpdate.this, "Profile Updated: " +profileImageUrl, Toast.LENGTH_LONG).show();
        if (user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                            }
                        }
                    });
        }

    }
    // Code Is based on Tutorial 3
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);

                 uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void createFirestoreUserDoc(String userId, String userName){
        db = FirebaseFirestore.getInstance();
        CollectionReference dbUsers = db.collection("Users");

        User newUser = new User(
                userId,
                editText.getText().toString(),
                1000,
                profileImageUrl
        );
    //TODO - Can Replace the Update method with this (.set acts as both create and overwrite so dont need to differenciate)
        dbUsers.document(userId).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AccountUpdate.this, "New Profile", Toast.LENGTH_LONG).show();
                Log.wtf("Result", "CREATION SUCCESS!");
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.wtf("Result", e.getMessage());
                    }
                });

    }
    private void updateUserRecord(String docId,String userId, String userName) {
        db = FirebaseFirestore.getInstance();

            db.collection("Users").document(docId)
                    .update(
                            "userId", userId,
                            "userName", userName,
                            "profileImage", profileImageUrl
                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });

    }
    private void checkFireStoreForUser(){
        final String fireAuthUserId = mAuth.getCurrentUser().getUid();
        Log.wtf("IsCheckingFor User", "YEEEET");
        db = FirebaseFirestore.getInstance();


        db.collection("Users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        progressBar.setVisibility(View.GONE);

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : list) {
                                Log.wtf("IsDoc", d.getId());
                                if(fireAuthUserId.equals(d.getId())){
                                    foundFlag = true;
                                    fireStoreDocId = d.getId();

                                }
                            }
                            if(foundFlag == true && !(fireStoreDocId == null)){
                                Log.wtf("Result", "UPDATE");
                                updateUserRecord(fireStoreDocId, fireAuthUserId, userName);
                            }else{
                                Log.wtf("Result", "CREATE");
                                createFirestoreUserDoc(fireAuthUserId, userName);
                            }
                        }
                    }
                });
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference fireStoreRef = FirebaseStorage.getInstance().getReference();
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("AccountImages/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {
            progressBar.setVisibility(View.VISIBLE);
    UploadTask  uploadTask = profileImageRef.putFile(uriProfileImage);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();

                    }

                    // Continue with the task to get the download URL
                    Log.wtf("Please JESUSaa", profileImageUrl);
                    profileImageUrl = profileImageRef.getDownloadUrl().toString();
                    return profileImageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        progressBar.setVisibility(View.GONE);
                        profileImageUrl = downloadUri.toString();
                        Log.wtf("Please JESUS", profileImageUrl);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AccountUpdate.this, "ERROR With File Upload", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            loadUserInformation();
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
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
                if(detailsPresentFlag) {
                    startActivity(new Intent(this, AccountView.class));
                    return true;
                } else {
                    Toast.makeText(AccountUpdate.this, "Please Complete Your Profile Before Continuing", Toast.LENGTH_LONG);
                }
            case R.id.signOutItem:
                if(detailsPresentFlag) {
                    mAuth.signOut();
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    Toast.makeText(AccountUpdate.this, "Please Complete Your Profile Before Continuing", Toast.LENGTH_LONG);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
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

}
