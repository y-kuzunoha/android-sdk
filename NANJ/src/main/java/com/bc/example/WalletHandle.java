package com.bc.example;

import android.content.Intent;
import android.util.Log;

import com.nanjcoin.sdk.nanj.NANJWallet;

/**
 ____________________________________

 Generator: Hieu.TV - tvhieuit@gmail.com
 CreatedAt: 4/28/18
 ____________________________________
 */
public class WalletHandle {
	interface WalletAddressListener {
		void onWalletAddress(String address);
	}

	private WalletAddressListener walletAddressListener;

	public void setWalletAddressListener(WalletAddressListener walletAddressListener) {
		this.walletAddressListener = walletAddressListener;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("TAGS", "onActivityResult: " +requestCode+ " - " + resultCode);
		if(walletAddressListener != null
			&& (requestCode == NANJWallet.NFC_REQUEST_CODE || requestCode == NANJWallet.QRCODE_REQUEST_CODE)
			&&(resultCode == NANJWallet.NFC_RESULT_CODE || resultCode == NANJWallet.QRCODE_RESULT_CODE)) {
			String address = data.getStringExtra(NANJWallet.WALLET_ADDRESS);
			walletAddressListener.onWalletAddress(address);
		}
	}

}
