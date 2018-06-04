package com.bc.example;

import com.bc.core.BaseNANJApplication;
import com.bc.core.nanj.NANJWalletManager;
import com.facebook.stetho.Stetho;

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/25/18
 * ____________________________________
 */
public class NANJApplication extends BaseNANJApplication{

	public NANJWalletManager nanjWalletManager;
	
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
