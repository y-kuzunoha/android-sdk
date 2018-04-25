package com.bc.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bc.core.nanj.NANJWalletListener;
import com.bc.core.nanj.NANJWalletManager;

import java.util.ArrayList;

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/23/18
 * ____________________________________
 */
public class WalletsFragment extends Fragment implements NANJWalletListener {

	private NANJWalletManager nanjWalletManager;
	private WalletAdapter walletAdapter = new WalletAdapter();
	private String _password;
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_wallets, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		RecyclerView walletList = view.findViewById(R.id.walletList);
		walletList.addItemDecoration(
			new DividerItemDecoration(
				view.getContext(),
				LinearLayoutManager.VERTICAL
			)
		);
		walletList.setAdapter(walletAdapter);
	}

	public void setNanjWalletManager(NANJWalletManager nanjWalletManager) {
		this.nanjWalletManager = nanjWalletManager;
		nanjWalletManager.nanjWalletListener = this;
		walletAdapter.setData(nanjWalletManager.getWalletList());
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

	public void setPassword(String _password) {
		this._password = _password;
	}
}
