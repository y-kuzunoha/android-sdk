package com.bc.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.MenuItem;
import android.widget.Toast;

import com.bc.core.nanj.NANJTransactionListener;
import com.bc.core.nanj.NANJWalletManager;


/**
 ____________________________________

 Generator: Hieu.TV - tvhieuit@gmail.com
 CreatedAt: 5/4/18
 ____________________________________
 */
public class SendCoinActivity extends AppCompatActivity {

	private NANJWalletManager _nanjWalletManager;
	private WalletHandle walletHandle = new WalletHandle();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_nanj_coin);
		setupActionBar();
		_nanjWalletManager = ((NANJApplication) getApplication()).getNanjWalletManager();
		findViewById(R.id.qr).setOnClickListener(v -> {
			if(_nanjWalletManager.getWallet() != null) {
				_nanjWalletManager.getWallet().sendNANJCoinByQrCode(this);
			}
		});
		AppCompatEditText edAddress = findViewById(R.id.address);
		AppCompatEditText edAmount = findViewById(R.id.amount);
		walletHandle.setWalletAddressListener(edAddress::setText);
		findViewById(R.id.send).setOnClickListener(v2 -> {
			_nanjWalletManager.getWallet().sentNANJCoin(
				edAddress.getText().toString(),
				edAmount.getText().toString(),
				new NANJTransactionListener() {
					@Override
					public void onTransferSuccess() {
						finish();
					}

					@Override
					public void onTransferFailure() {
						Toast.makeText(SendCoinActivity.this, "Send coin error", Toast.LENGTH_LONG).show();
					}
				}
			);
		});

		findViewById(R.id.nfc).setOnClickListener(v -> {
			if(_nanjWalletManager.getWallet() != null) {
				_nanjWalletManager.getWallet().sendNANJCoinByNfcCode(this);
			}
		});
	}

	private void setupActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if(actionBar == null) return;
		actionBar.setTitle("Send NANJ coin");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeAsUpIndicator(R.drawable.ic_back_24px);
		actionBar.setHomeActionContentDescription("Back");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		walletHandle.onActivityResult(requestCode, resultCode, data);
	}
}
