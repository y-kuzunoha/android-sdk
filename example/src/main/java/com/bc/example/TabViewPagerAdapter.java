package com.bc.example;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.bc.core.nanj.NANJWalletManager;

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/23/18
 * ____________________________________
 */
public class TabViewPagerAdapter extends FragmentStatePagerAdapter {

	private NANJWalletManager nanjWalletManager;
	
	public TabViewPagerAdapter(FragmentManager fm, NANJWalletManager nanjWalletManager) {
		super(fm);
		this.nanjWalletManager = nanjWalletManager;
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		if(position == 0) {
			return "Wallet";
		}
		return "Transactions";
	}

	@Override
	public Fragment getItem(int position) {
		if(position == 0) {
			return new MyWalletFragment();
		}
		return new TransactionsFragment();
	}

	@Override
	public int getCount() {
		return 2;
	}
}
