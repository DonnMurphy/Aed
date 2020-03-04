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
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
//import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
//import org.web3j.tx.ChainIdLong;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.Security;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import okhttp3.Interceptor;

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

    private String sheepHelperAddress;
    private Credentials userCredentials;

    //TODO - Figure out if this still has a purpose since the infrastructure change
    EthUtils(Context appContext, Activity appActivity) {
        this.ethContext = appContext;
        this.ethActivity = appActivity;
        //walletPath = this.ethContext.getFilesDir().getAbsolutePath();
       // Log.wtf("Path", walletPath);
       // walletDir = new File(walletPath).getAbsoluteFile();
       // this.walletFile = walletDir.listFiles()[1];

        try {
            // contract addresses
          //  this.sheepFactoryAddress = "0x271270Dd83B64Cfc9C3570983a3E0D5BbF2AC6c8";
          //  this.sheepHelperAddress = "0x1294e8D7cE19aDc1a5F7bCD056E9539329e0d712";
            this.infuraUrl = "https://ropsten.infura.io/v3/0f42ed98ccbc4d5aa6c4872ba8ebe005";
            this.connectToEthNetwork();
        } catch (Exception e) {
            toastAsync("Error: " + e.getMessage());
        }
    }

    public String getAccountBalance(String AccountAddress) {
        String AccountBal = "SKINT";
        try {
            EthGetBalance ethGetBalance = web3
                    .ethGetBalance(AccountAddress, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

            BigInteger wei = ethGetBalance.getBalance();
            AccountBal = wei.toString();
        } catch(Exception e) {
            Log.wtf("ERROR", e);
            e.printStackTrace();
        }
        return AccountBal;
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




    public void toastAsync(String message) {
        ethActivity.runOnUiThread(() -> {
            Toast.makeText(ethContext, message, Toast.LENGTH_LONG).show();
        });

    }


}


