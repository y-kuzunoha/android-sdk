package com.bc.example;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.bc.core.nanj.NANJWalletManager;
import com.bc.core.ui.barcodereader.NANJQrCodeActivity;
import com.bc.core.ui.nfc.NANJNfcActivity;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Emitter;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
	
	//Views define 
	private Loading _progressDialog;
	private RecyclerView walletList;
	
	//Variables define
	private String _password = Const.DEFAULT;
	private NANJWalletManager nanjWalletManager = new NANJWalletManager();
	private WalletAdapter walletAdapter = new WalletAdapter();

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		walletList = findViewById(R.id.walletList);
		walletList.addItemDecoration(
			new DividerItemDecoration(
				this,
				LinearLayoutManager.VERTICAL
			)
		);
		walletList.setAdapter(walletAdapter);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			_password = bundle.getString(Const.BUNDLE_KEY_PASSWORD, Const.DEFAULT);
		}

		_progressDialog = new Loading(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.top_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.e("menu click", "onContextItemSelected: " + item.getItemId());
		switch (item.getItemId()) {
			case R.id.menuNewWallet:
				createWallet();
				break;
				
			case R.id.menuJsonImport:
				break;
				
			case R.id.menuPrivateKeyImport:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void createWallet() {
		_progressDialog.show();
		AsyncTask.execute(() -> {
			nanjWalletManager.createWallet(_password, getFilesDir().getAbsoluteFile());
			runOnUiThread(() -> {
				updateWalletView();
				_progressDialog.dismiss();
			});
		});
	}
	
	private void updateWalletView() {
		walletAdapter.setData(nanjWalletManager.getWallets());
	}
}
