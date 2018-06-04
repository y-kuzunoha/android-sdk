package com.bc.example;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;
import com.bc.core.nanj.listener.NANJCreateWalletListener;
import com.bc.core.nanj.listener.NANJImportWalletListener;
import com.bc.core.nanj.NANJWallet;
import com.bc.core.nanj.NANJWalletManager;
import com.google.gson.Gson;

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/25/18
 * ____________________________________
 */
public class WalletsActivity extends AppCompatActivity {

	private Loading _progressDialog;
	private NANJWalletManager nanjWalletManager;
	private WalletsFragment _walletsFragment;
	private String _password;
	private SharedPreferences _sharedPreferences;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallets);
		_sharedPreferences = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
		_progressDialog = new Loading(this);
		_password = getIntent().getStringExtra(Const.BUNDLE_KEY_PASSWORD);
		nanjWalletManager = ((NANJApplication) getApplication()).getNanjWalletManager();
		_walletsFragment = (WalletsFragment) getSupportFragmentManager().findFragmentById(R.id.walletsFragment);
		_walletsFragment.setPassword(_password);
		_walletsFragment.setNanjWalletManager(nanjWalletManager);
		setupActionBar();
		findViewById(R.id.btnCreateWallet).setOnClickListener(view -> createWallet());
		findViewById(R.id.btnImportWallet).setOnClickListener(view -> importWalletDialog());
	}

	private void setupActionBar() {
//		setSupportActionBar(findViewById(R.id.toolbar));
		setTitle("Wallet list");
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeAsUpIndicator(R.drawable.ic_back_24px);
			actionBar.setHomeActionContentDescription("Back");
		}
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

	private void createWallet() {
		_progressDialog.show();
		nanjWalletManager.createWallet(
			_password,
			new NANJCreateWalletListener() {
				@Override
				public void onCreateWalletSuccess(@NonNull String backup, @NonNull NANJWallet wallet) {
					_progressDialog.dismiss();
					backupWallet(backup);
					_sharedPreferences.edit()
						.putString(Const.STORAGE_CURRENT_WALLET, new Gson().toJson(wallet))
						.apply();
					_walletsFragment.setData(nanjWalletManager.getWalletList());
				}
				@Override
				public void onCreateWalletFailure() {
					_progressDialog.dismiss();
					Toast.makeText(WalletsActivity.this, "Create wallet failure.", Toast.LENGTH_LONG).show();
				}
			}
		);
	}

	private void backupWallet(String wallet) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, wallet);
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, "Backup wallet"));
	}

	@SuppressLint("InflateParams")
	private void importWalletDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_import_json, null);
		RadioButton radioButton = view.findViewById(R.id.rdJson);
		AppCompatEditText edText = view.findViewById(R.id.edPrivateKey);
		new AlertDialog.Builder(this)
			.setTitle("Import wallet")
			.setView(view)
			.setNegativeButton("Cancel", null)
			.setPositiveButton("Import", (dialogInterface, i) -> {
				String s = edText.getText().toString();
				_progressDialog.show();
				if (radioButton.isChecked()) {
					nanjWalletManager.importWallet(_password, s, nanjImportWalletListener);
				} else {
					nanjWalletManager.importWallet(s, nanjImportWalletListener);
				}
			})
			.show();
	}

	private NANJImportWalletListener nanjImportWalletListener = new NANJImportWalletListener() {
		@Override
		public void onImportWalletSuccess() {
			_progressDialog.dismiss();
			_walletsFragment.setData(nanjWalletManager.getWalletList());
		}

		@Override
		public void onImportWalletFailure() {
			_progressDialog.dismiss();
			Toast.makeText(WalletsActivity.this, "Import wallet failure.", Toast.LENGTH_LONG).show();
		}
	};
}
