package com.bc.example;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.bc.core.nanj.NANJWalletListener;
import com.bc.core.nanj.NANJWalletManager;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity implements NANJWalletListener {

	//Views define 
	private Loading _progressDialog;

	//Variables define
	private String _password = Const.DEFAULT;
	private NANJWalletManager nanjWalletManager;
	private WalletAdapter walletAdapter = new WalletAdapter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		nanjWalletManager = new NANJWalletManager(this, this);
		
		RecyclerView walletList = findViewById(R.id.walletList);
		walletList.addItemDecoration(
			new DividerItemDecoration(
				this,
				LinearLayoutManager.VERTICAL
			)
		);
		walletAdapter.setData(nanjWalletManager.getWallets());
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
				importWalletFromJson();
				break;

			case R.id.menuPrivateKeyImport:
				importWalletFromPrivateKey();
				break;
			case R.id.menuGetBalance:
				getBalanceEth();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void createWallet() {
		_progressDialog.show();
		nanjWalletManager.createWallet(_password/*, getFilesDir().getAbsoluteFile()*/);
	}

	private void importWalletFromPrivateKey() {
		View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_import_private_key, null, false);
		AppCompatEditText edPrivateKey = contentView.findViewById(R.id.edPrivateKey);
		new AlertDialog.Builder(this)
			.setTitle("Input private key")
			.setView(contentView)
			.setNegativeButton("Cancel", null)
			.setPositiveButton("Ok", (dialog, which) -> {
				String privateKey = edPrivateKey.getText().toString();
				if (!TextUtils.isEmpty(privateKey)) {
					_progressDialog.show();
					nanjWalletManager.importWallet(privateKey/*"19051355975941725161733792530046853610926205457968420220901640107349227711776"*/);
				}}).show();
	}
	
	private void importWalletFromJson() {
		View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_import_json, null, false);
		AppCompatEditText jsonEd = contentView.findViewById(R.id.edPrivateKey);
		new AlertDialog.Builder(this)
			.setTitle("Input Json")
			.setView(contentView)
			.setNegativeButton("Cancel", null)
			.setPositiveButton("Ok", (dialog, which) -> {
				String json = jsonEd.getText().toString();
				if (!TextUtils.isEmpty(json)) {
					_progressDialog.show();
					nanjWalletManager.importWallet(_password, json);
				}}).show();
	}

	private void updateWalletView() {
		walletAdapter.setData(nanjWalletManager.getWallets());
	}

	@Override
	public void onCreateWalletSuccess(@NonNull String privateKey) {
		updateWalletView();
		_progressDialog.dismiss();
		backupPrivateKey(privateKey);
	}

	@Override
	public void onCreateWalletFailure() {
		_progressDialog.dismiss();
		Toast.makeText(this, "create wallet is failure", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onImportWalletSuccess() {
		updateWalletView();
		_progressDialog.dismiss();
	}

	@Override
	public void onImportWalletFailure() {
		_progressDialog.dismiss();
		Toast.makeText(this, "import wallet is failure", Toast.LENGTH_LONG).show();
	}
	
	private void backupPrivateKey(String privateKey) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, privateKey);
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, "Backup private key"));
	}
	
	private void getBalanceEth() {
		Log.i("ETH", "getBalanceEth: " + nanjWalletManager.getWallet().getAmountEth());
	}
}
