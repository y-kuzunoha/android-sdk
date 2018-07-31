package com.bc.example;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * ____________________________________
 * <p>
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/23/18
 * ____________________________________
 */
public class TabViewPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] fragments = {new MyWalletFragment(), new TransactionsFragment()};

    TabViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void onResume(int position) {
        fragments[position].onResume();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Wallet";
        }
        return "Transactions";
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}
