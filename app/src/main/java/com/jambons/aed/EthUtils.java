package com.jambons.aed;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.Security;

public class EthUtils {

    private Web3j web3;
    //FIXME: Add your own password here
    private final String password = "medium";
    private final Activity ethActivity;
    private String walletPath;
    private File walletDir;
    private File walletFile;
    Context ethContext;


    EthUtils(Context appContext, Activity appActivity) {
        this.ethContext = appContext;
        this.ethActivity = appActivity;
        walletPath = this.ethContext.getFilesDir().getAbsolutePath();

        Log.wtf("Path", walletPath);
        walletDir = new File(walletPath).getAbsoluteFile();
        this.walletFile = walletDir.listFiles()[1];

        //setupBouncyCastle();
    }


    public void connectToEthNetwork() {
        toastAsync("Connecting to Ethereum network...");
        // FIXME: Add your own API key here
        web3 = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/0f42ed98ccbc4d5aa6c4872ba8ebe005"));
        try {
            Web3ClientVersion clientVersion = web3.web3ClientVersion().sendAsync().get();
            if (!clientVersion.hasError()) {
                toastAsync("Connected!");
            } else {
                toastAsync(clientVersion.getError().getMessage());
            }
        } catch (Exception e) {
            toastAsync(e.getMessage());
        }
    }

    public void createWallet() {
        try {
            setupBouncyCastle();
            String file_string = WalletUtils.generateNewWalletFile(password, walletDir);
            walletFile = walletDir.listFiles()[1];
            boolean a = walletDir.isFile();
            Log.wtf("pre-finaldestination", walletFile.toString());
            Log.wtf("final Destination", file_string);
           // walletDir = walletDir.getAbsoluteFile();
            walletDir.getAbsoluteFile();
            toastAsync("walletCreated");

        } catch (Exception e) {
            toastAsync("Error: " + e.getMessage());

        }
    }

    public void getAddress() {
        try {
            Log.wtf("start", "Start Here" + walletDir.listFiles());
            Credentials credentials = WalletUtils.loadCredentials(password, walletFile);

           // String x = credentials.toString();
            Log.wtf("credientIALS", "FILES?");
            toastAsync("Your address is " + credentials.getAddress());
        } catch (Exception e) {
            toastAsync("ERROR: " + e.getMessage());
    }
    }

    public void sendTransaction() {
        try {
            Credentials credentials = WalletUtils.loadCredentials(password, walletDir);
            TransactionReceipt receipt = Transfer.sendFunds(web3, credentials, "0x31B98D14007bDEe637298086988A0bBd31184523", new BigDecimal(1), Convert.Unit.ETHER).sendAsync().get();
            toastAsync("Transaction complete: " + receipt.getTransactionHash());
        } catch (Exception e) {
            toastAsync(e.getMessage());
        }
    }

    public void toastAsync(String message) {
        ethActivity.runOnUiThread(() -> {
            Toast.makeText(ethContext, message, Toast.LENGTH_LONG).show();
        });

    }

    // This is implemented because the current Web3 lib does not work due to cryptography changes from google
    // when lib is updated then remove this
    public void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }
        // Get a handler that can be used to post to the main thread
        //Handler mainHandler = new Handler(Looper.getMainLooper());

        //Runnable toastAsync = new Runnable(Context ethContext, String message) {
          //  @Override
           // public void run() {

            //} // This is your code
        //};

      //  mainHandler.post(myRunnable);
    //}

}

