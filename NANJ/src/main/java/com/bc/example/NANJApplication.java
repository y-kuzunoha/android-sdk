package com.bc.example;

import android.app.Application;

//import com.facebook.stetho.Stetho;
import com.nanjcoin.sdk.model.NANJConfigModel;
import com.nanjcoin.sdk.nanj.NANJWalletManager;

/**
 * ____________________________________
 * <p>
 * Generator: NANJ Team - support@nanjcoin.com
 * CreatedAt: 4/25/18
 * ____________________________________
 */
public class NANJApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Stetho.initializeWithDefaults(this);
        new NANJWalletManager.Builder()
                .setContext(getApplicationContext())
                .setDevelopmentMode(true)
                .setNANJAppId("575958089608922877")
                .setNANJSecret("fF5MSugBFsUEoTiFIiRdUa1rFc5Y8119JVzyWUzJ")
                .build();
    }


}
