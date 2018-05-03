package com.bc.example;

import android.app.Application;

import com.bc.core.nanj.NANJWalletManager;
import com.facebook.stetho.Stetho;

/**
 * ____________________________________
 *
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
		nanjWalletManager = new NANJWalletManager(this);
	}

	public NANJWalletManager getNanjWalletManager() {
		return nanjWalletManager;
	}
}
