package com.bc.example;

import android.app.Application;

import com.bc.core.nanj.NANJWalletManager;

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
		nanjWalletManager = new NANJWalletManager(this);
	}

	public NANJWalletManager getNanjWalletManager() {
		return nanjWalletManager;
	}
}
