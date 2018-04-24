package com.bc.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 ____________________________________

 Generator: Hieu.TV - tvhieuit@gmail.com
 CreatedAt: 4/24/18
 ____________________________________
 */
public class MyWalletFragment extends Fragment {

	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my_wallet, container, false);
	}

	public void onViewCreated(@NonNull View view, Bundle bundle) {
		super.onViewCreated(view, bundle);
	}
}
