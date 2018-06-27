package com.bc.example;

import android.app.Application;

import com.bc.core.BaseNANJApplication;
import com.bc.core.nanj.NANJConfig;
import com.bc.core.nanj.NANJWalletManager;
import com.facebook.stetho.Stetho;

/**
 * ____________________________________
 * <p>
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/25/18
 * ____________________________________
 */
public class NANJApplication extends Application {

    private NANJWalletManager nanjWalletManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        nanjWalletManager = new NANJWalletManager.Builder()
                .setContext(getApplicationContext())
                .setNANJAppId("575958089608922877")
                .setNANJSecret("fF5MSugBFsUEoTiFIiRdUa1rFc5Y8119JVzyWUzJ")
                .build();
    }

    public NANJWalletManager getNanjWalletManager() {
        return nanjWalletManager;
    }
}
