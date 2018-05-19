package com.bc.example;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bc.core.nanj.NANJConvert;
import com.bc.core.nanj.NANJTransactionListener;
import com.bc.core.nanj.NANJWalletManager;

import java.util.Objects;


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
		AppCompatTextView status = findViewById(R.id.status);
		findViewById(R.id.qr).setOnClickListener(v -> {
			if(_nanjWalletManager.getWallet() != null) {
				_nanjWalletManager.getWallet().sendNANJCoinByQrCode(this);
			}
		});
		AppCompatEditText edAddress = findViewById(R.id.address);
		AppCompatEditText edAmount = findViewById(R.id.amount);
		walletHandle.setWalletAddressListener(edAddress::setText);
		findViewById(R.id.send).setOnClickListener(v2 -> {
			String address = edAddress.getText().toString();
			status.setText("Nanj coin sending to address: " + address);
			Objects.requireNonNull(_nanjWalletManager.getWallet()).sentNANJCoin(
				address,
				NANJConvert.INSTANCE.toWei(edAmount.getText().toString(), NANJConvert.Unit.NANJ).toString(),
				new NANJTransactionListener() {
					@Override
					public void onTransferSuccess() {
						finish();
						status.setText("Nanj coin sent success!");
					}

					@Override
					public void onTransferFailure() {
						status.setText("Nanj coin sent failure!");
						Toast.makeText(SendCoinActivity.this, "Send coin error", Toast.LENGTH_LONG).show();
					}
				}
			);
		});

		AppCompatTextView nfc = findViewById(R.id.nfc);
		nfc.setVisibility(NfcAdapter.getDefaultAdapter(this) == null? View.GONE:View.VISIBLE);
		nfc.setOnClickListener(v -> {
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
