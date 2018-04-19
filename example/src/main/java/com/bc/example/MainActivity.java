package com.bc.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bc.core.nanj.NANJWalletListener;
import com.bc.core.nanj.NANJWalletManager;

public class MainActivity extends AppCompatActivity implements NANJWalletListener {

	//Views define 
	private Loading _progressDialog;

	//Variables define
	private String _password = Const.DEFAULT;
	private NANJWalletManager nanjWalletManager = new NANJWalletManager(this);
	private WalletAdapter walletAdapter = new WalletAdapter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RecyclerView walletList = findViewById(R.id.walletList);
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
				importWalletFromPrivateKey();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void createWallet() {
		_progressDialog.show();
		nanjWalletManager.createWallet(_password, getFilesDir().getAbsoluteFile());
	}

	private void importWalletFromPrivateKey() {
		_progressDialog.show();
		// import private key is correct
		nanjWalletManager.importWallet("19051355975941725161733792530046853610926205457968420220901640107349227711776");
		// import private key is not correct
//		nanjWalletManager.importWallet("123qwefghfkj");
	}

	private void updateWalletView() {
		walletAdapter.setData(nanjWalletManager.getWallets());
	}

	@Override
	public void onCreateWalletSuccess() {
		updateWalletView();
		_progressDialog.dismiss();
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
}
