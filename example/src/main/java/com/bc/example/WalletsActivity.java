package com.bc.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.bc.core.nanj.NANJWalletListener;
import com.bc.core.nanj.NANJWalletManager;

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/25/18
 * ____________________________________
 */
public class WalletsActivity extends AppCompatActivity implements NANJWalletListener {

	private NANJWalletManager nanjWalletManager;
	private WalletsFragment _walletsFragment;
	private String _password;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallets);
		_password = getIntent().getStringExtra(Const.BUNDLE_KEY_PASSWORD);
		nanjWalletManager = ((NANJApplication) getApplication()).getNanjWalletManager();
		_walletsFragment = (WalletsFragment) getSupportFragmentManager().findFragmentById(R.id.walletsFragment);
		_walletsFragment.setPassword(_password);
		_walletsFragment.setNanjWalletManager(nanjWalletManager);
		setupActionBar();
		findViewById(R.id.btnCreateWallet).setOnClickListener(view -> {
			createWallet();
		});
	}
	private void setupActionBar() {
		setSupportActionBar(findViewById(R.id.toolbar));
		setTitle("Wallet list");
		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null) {
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

	@Override
	public void onCreateWalletSuccess(String privateKey) {
		
	}

	@Override
	public void onCreateWalletFailure() {

	}

	@Override
	public void onImportWalletSuccess() {

	}

	@Override
	public void onImportWalletFailure() {

	}

	private void createWallet() {
		nanjWalletManager.createWallet(_password/*, getFilesDir().getAbsoluteFile()*/);
	}
}
