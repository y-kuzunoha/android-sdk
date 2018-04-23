package com.bc.example;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/23/18
 * ____________________________________
 */
public class TabViewPagerAdapter extends FragmentStatePagerAdapter {
	
	public TabViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		if(position == 0) {
			return "Wallets";
		}
		return "Transactions";
	}

	@Override
	public Fragment getItem(int position) {
		if(position == 0) {
			return new WalletsFragment();
		}
		return new TransactionsFragment();
	}

	@Override
	public int getCount() {
		return 2;
	}
}
