package com.bc.example;

import android.app.Application;

import com.nanjcoin.sdk.model.NANJConfigModel;
import com.nanjcoin.sdk.nanj.NANJWalletManager;

/**
 * ____________________________________
 * <p>
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/25/18
 * ____________________________________
 */
public class NANJApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new NANJWalletManager.Builder()
                .setContext(getApplicationContext())
                .setNANJAppId("575958089608922877")
                .setNANJSecret("fF5MSugBFsUEoTiFIiRdUa1rFc5Y8119JVzyWUzJ")
                .build();
    }


}
