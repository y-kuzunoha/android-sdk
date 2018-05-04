package com.bc.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;
import com.bc.core.nanj.NANJWalletManager;

public class MainActivity extends AppCompatActivity {

	//Views define
	
	//Variables define
	private String _password = Const.DEFAULT;
	private NANJWalletManager nanjWalletManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("ETH SDK");
		nanjWalletManager = ((NANJApplication) getApplication()).getNanjWalletManager();
		initView();
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			_password = bundle.getString(Const.BUNDLE_KEY_PASSWORD, Const.DEFAULT);
		}
	}
	
	private void initView() {
		TabLayout tableLayout = findViewById(R.id.tabLayout);
		ViewPager viewPager = findViewById(R.id.pagerTabLayout);
		tableLayout.setupWithViewPager(viewPager);
		viewPager.setAdapter(
			new TabViewPagerAdapter(
				getSupportFragmentManager(),
				nanjWalletManager
			)
		);
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
				Intent intent = new Intent(this, WalletsActivity.class);
				intent.putExtra(Const.BUNDLE_KEY_PASSWORD, _password);
				startActivityForResult(intent, 100);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
