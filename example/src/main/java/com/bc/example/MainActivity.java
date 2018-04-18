package com.bc.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.bc.core.nanj.NANJWalletManager;
import com.bc.core.ui.barcodereader.NANJQrCodeActivity;
import com.bc.core.ui.nfc.NANJNfcActivity;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.barcode).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(), NANJQrCodeActivity.class));
			}
		});

		findViewById(R.id.nfc).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(), NANJNfcActivity.class));
			}
		});

		createWallet();
	}

	private void createWallet() {
		NANJWalletManager nanjWalletManager = new NANJWalletManager();
		nanjWalletManager.createWallet("vanhieupr0", getFilesDir().getAbsoluteFile());
	}
}
