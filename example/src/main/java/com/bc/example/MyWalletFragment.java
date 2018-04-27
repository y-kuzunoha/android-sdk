package com.bc.example;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bc.core.nanj.NANJWalletManager;

import java.math.BigInteger;

/**
 ____________________________________

 Generator: Hieu.TV - tvhieuit@gmail.com
 CreatedAt: 4/24/18
 ____________________________________
 */
public class MyWalletFragment extends Fragment {

	private NANJWalletManager _nanjWalletManager;
	private AppCompatTextView address;
	private AppCompatTextView amountEth;

	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my_wallet, container, false);
	}

	public void onViewCreated(@NonNull View view, Bundle bundle) {
		super.onViewCreated(view, bundle);
		_nanjWalletManager = ((NANJApplication)getActivity().getApplication()).getNanjWalletManager();
		address = view.findViewById(R.id.address);
		amountEth = view.findViewById(R.id.amountEth);
	}

	@Override
	public void onResume() {
		super.onResume();
		intView();
	}

	private void intView() {
		if(_nanjWalletManager != null && _nanjWalletManager.getWallet() != null) {
			address.setText(_nanjWalletManager.getWallet().getAddress());
			new Handler().post(() -> {
				BigInteger bigInteger = _nanjWalletManager.getWallet().getAmountEth();
				new Handler(Looper.getMainLooper()).post(() ->
					amountEth.setText(String.format(getString(R.string.txt_amount_eth), bigInteger))
				);
			});
		}
	}
}
