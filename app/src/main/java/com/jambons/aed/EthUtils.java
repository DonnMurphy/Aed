package com.jambons.aed;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.Security;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class EthUtils {

    private Web3j web3;
    //FIXME: Add your own password here
    private final String password = "medium";
    private final Activity ethActivity;
    private String walletPath;
    private File walletDir;
    private File walletFile;
    private BigInteger gasLimit;
    private BigInteger gasPrice;
    private String infuraUrl;
    Context ethContext;
    private SheepHelper sheepHelper;
    private String sheepFactoryAddress;
    private String sheepOwnershipAddress;
    private String sheepHelperAddress;
    private Credentials userCredentials;


    EthUtils(Context appContext, Activity appActivity) {
        this.ethContext = appContext;
        this.ethActivity = appActivity;
        walletPath = this.ethContext.getFilesDir().getAbsolutePath();
        Log.wtf("Path", walletPath);
        walletDir = new File(walletPath).getAbsoluteFile();
        this.walletFile = walletDir.listFiles()[1];


        //Log.wtf("WEB 3 ISSUES", web3.toString());

        try {
            // contract addresses
            this.sheepFactoryAddress = "0xeE96E4c67AF958eF9eBbe03FB6C885D6E3BADC08";
            this.sheepHelperAddress = "0x1e1971E5e258493eAAA1bD32AB3F194f005682e5";
            this.infuraUrl = "https://ropsten.infura.io/v3/0f42ed98ccbc4d5aa6c4872ba8ebe005";
            this.connectToEthNetwork();
            gasLimit = BigInteger.valueOf(20_000_000_000L);
            gasPrice = BigInteger.valueOf(4300000);
            this.userCredentials = loadTestCredientials();//WalletUtils.loadCredentials(password, walletFile);
            // Log.wtf("bal", web3.ethGetBalance(userCredentials.getAddress(), new DefaultBlockParameter()));
            //setupBouncyCastle();

        } catch (Exception e) {
            toastAsync("Error: " + e.getMessage());
        }
    }

    public void createASheep() {
        AsyncTaskRunner runner = new AsyncTaskRunner();
        //String sleepTime = time.getText().toString();
        runner.execute();
    }

    public void connectToEthNetwork() {
        toastAsync("Connecting to Ethereum network...");
        // FIXME: Add your own API key here
        Log.wtf("INFURA", this.infuraUrl);
        this.web3 = Web3j.build(new HttpService(this.infuraUrl));
        try {
            Web3ClientVersion clientVersion = this.web3.web3ClientVersion().sendAsync().get();
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

    public String getAddress1() {
        String address;
        address = "NONE";
        try {
            Log.wtf("start", "Start Here" + walletDir.listFiles());
            Credentials credentials = WalletUtils.loadCredentials(password, walletFile);

            // String x = credentials.toString();
            // Log.wtf("credientIALS", "FILES?");
            // toastAsync("Your address is " + credentials.getAddress());
            address = credentials.getAddress();
        } catch (Exception e) {
            toastAsync("ERROR: " + e.getMessage());
        }
        return address;
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

    public Credentials loadTestCredientials(){
        String publicKey = "0x1B6e1cdF8cBDac646dD548E46B8FE0A8b7B72852";
        String privateKey = "59675267A950E89A2BB4CB35A46F738C5BB3BA387AD6279F8A4EE5A187159774";
        String hexPrivateKey = String.format("%040x", new BigInteger(1, privateKey.getBytes()));
        String hexPublicKey = String.format("%040x", new BigInteger(1, publicKey.getBytes()));
       // Credentials creds = Credentials.create(hexPrivateKey, hexPublicKey);
        Credentials creds = Credentials.create(privateKey);
        return creds;
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


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            String sheepFactoryAddress = "0xeE96E4c67AF958eF9eBbe03FB6C885D6E3BADC08";
            String sheepHelperAddress = "0x1e1971E5e258493eAAA1bD32AB3F194f005682e5";
            String infuraUrl = "https://ropsten.infura.io/v3/0f42ed98ccbc4d5aa6c4872ba8ebe005";
            Web3j web3 = Web3j.build(new HttpService(infuraUrl));
            //this.connectToEthNetwork();
            gasLimit = BigInteger.valueOf(20_000_000_000L);
            gasPrice = BigInteger.valueOf(4300000);
            // Credentials userCredentials = WalletUtils.loadCredentials(password, walletFile);

                ContractGasProvider gassy = new StaticGasProvider(BigInteger.valueOf(40000), BigInteger.valueOf(100000));

            try {
                //   Log.wtf("GAS PRICWE", this.web3.ethGasPrice().send().toString());
                SheepFactory sheepFactory = SheepFactory.load(sheepFactoryAddress, web3, userCredentials, gassy);
                Log.wtf("THE ETHER Address!!!", userCredentials.getAddress());
                Log.wtf("THE NEtwork", web3.getClass().toString());//sheepFactory.getDeployedAddress("Ropsten"));
               // CompletableFuture <TransactionReceipt> billyRecieptFuture = sheepFactory.createRandomSheep("billy").ethCall();
                if (sheepFactory == null) {
                    Log.wtf("ITS NULLLLLL", "NULLLL");
                } else {
                    Log.wtf("ITS NOT NULLLLLL", sheepFactory.getContractAddress());
                    //  Boolean valida = sheepFactory.isValid();
                    //  Log.wtf("IS IT VALID???", valida.toString());
                   // CompletableFuture<TransactionReceipt> billyRecieptFuture = sheepFactory.createRandomSheep("billyaa").sendAsync();
                  //  billyRecieptFuture.thenAccept(transactionReceipt -> {
                   //  Log.wtf("KILL ME PLZ", transactionReceipt.getContractAddress());
                    ;
                   /*billyRecieptFuture.thenAcceptAsync(transactionReceipt -> {
                       Log.wtf("KILL ME PLZ", transactionReceipt.getContractAddress());
                        Log.wtf("Billy RECIPT", "HOOO CHRIST");
                       Log.wtf("I SHOULD HAVE DONE COMMERCE", transactionReceipt.toString());
                     //  eth.getBlockByNumber("0x0", true)
                       //transactionReceipt.toString());
                    }).exceptionally(transactionReceipt  -> {
                        Log.wtf("SOMETHING HAS GONE WRONG factory", transactionReceipt);
                        return null;
                    });
                    */

                }


                SheepHelper sheepHelper = new SheepHelper(sheepHelperAddress, web3, userCredentials, gassy);

                Log.wtf("Lord SAVE ME ", sheepHelper.getContractAddress());
              //  RemoteCall<BigInteger> remoteCall = sheepHelper.getSheepsByOwner(userCredentials.getAddress());
                CompletableFuture<TransactionReceipt> futureSheeps = sheepHelper.getSheepsByOwner(userCredentials.getAddress()).sendAsync();
                futureSheeps.thenAccept(transactionReceipt -> {
                    Log.wtf("Lord SAVE TADHG NAGE3 ", sheepHelper.getContractAddress());
                    String mysheeps = transactionReceipt.toString();
                    Log.wtf("DA RECIPT LAD", mysheeps);
                }).exceptionally(transactionReceipt  -> {
                    Log.wtf("SOMETHING HAS GONE WRONG htlper", transactionReceipt);
                    return null;
                });
                //ownedSheeps = futureSheeps.get();

               // String value = ownedSheeps.toString();
                // toastAsync(value);
               // Log.wtf("THE VALUE", value);
            } catch (Exception e) {
                Log.wtf("ERROR", e);
                e.printStackTrace();
            }











       /* publishProgress("Sleeping..."); // Calls onProgressUpdate()
        try {
            int time = Integer.parseInt(params[0])*1000;

            Thread.sleep(time);
            resp = "Slept for " + params[0] + " seconds";
        } catch (InterruptedException e) {
            e.printStackTrace();
            resp = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            resp = e.getMessage();
        }
       return resp;
        */
       String val = "meme";
        return val;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            //progressDialog.dismiss();
            //finalResult.setText(result);
        }


        @Override
        protected void onPreExecute() {
            //progressDialog = ProgressDialog.show(MainActivity.this,
            //      "ProgressDialog",
            //    "Wait for "+time.getText().toString()+ " seconds");
        }


        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);

        }
    }
}


