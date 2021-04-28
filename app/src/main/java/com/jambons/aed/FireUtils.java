package com.jambons.aed;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FireUtils {
    private static FireUtils instance = null;

    private static final String prefixURL = "http://some/url/prefix/";


    private FireUtils(Context context)
    {
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        accountBalance = getUserBalance();
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized FireUtils getInstance(Context context)
    {
        if (null == instance)
            instance = new FireUtils(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized FireUtils getInstance()
    {
        if (null == instance)
        {
            throw new IllegalStateException(FireUtils.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }






    private String accountBalance, userName, userId;
    //MenuItem mnDonieWallet, mnUserName;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    public String getUserBalance(){
        //fireAuthUserId = mAuth.getCurrentUser().getUid();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

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
                                   // mnDonieWallet.setTitle(accountBalance);

                                }
                            }
                        }
                    }
                });
        return accountBalance;
    }

    public String getCurrentUserName(){
        return mAuth.getCurrentUser().getUid();
    }

    public String getUserNameById(String fireUserId){
        //fireAuthUserId = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        db.collection("Users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Log.wtf("User", fireUserId);
                                Log.wtf("Doc", d.getId());

                                if(fireUserId.equals(d.getId())){
                                    //foundFlag = true;
                                    //fireStoreDocId = d.getId();
                                    Log.wtf("Result", d.get("accountBalance").toString());
                                    //accountBalance = "DB$ " +d.get("accountBalance").toString();
                                    //mnDonieWallet.setTitle(accountBalance);
                                    userName = d.get("userName").toString();
                                }
                            }
                        }
                    }
                });
        return userName;
    }

    public String getDateFromTimeStamp(String timestamp){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.valueOf(timestamp));
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        return date;
    }


  /*  private void startLoadAnimation(String loadingText){
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

    private void stopLoadAnimation(int default_id){
        //setContentView(R.layout.activity_view_auction);
        setContentView(default_id);
    }
*/
}
