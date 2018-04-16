package com.bc.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.bc.core.nanj.NanjWalletManager;
import com.bc.core.ui.barcodereader.BarcodeActivity;
import com.bc.core.ui.nfc.NfcActivity;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.barcode).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(), BarcodeActivity.class));
			}
		});

		findViewById(R.id.nfc).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(), NfcActivity.class));
			}
		});

		createWallet();
	}

	private void createWallet() {
		NanjWalletManager nanjWalletManager = new NanjWalletManager();
		nanjWalletManager.createWallet("your password", getFilesDir().getAbsoluteFile());
	}
}
