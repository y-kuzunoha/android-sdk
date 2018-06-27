package com.bc.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bc.core.nanj.NANJWallet;
import com.bc.core.nanj.NANJWalletManager;

import java.util.List;

/**
 * ____________________________________
 * <p>
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/23/18
 * ____________________________________
 */
public class WalletsFragment extends Fragment {

    private NANJWalletManager nanjWalletManager;
    private WalletAdapter walletAdapter;
    private String _password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        walletAdapter = new WalletAdapter(view.getContext());
        RecyclerView walletList = view.findViewById(R.id.walletList);
        walletList.addItemDecoration(
                new DividerItemDecoration(
                        view.getContext(),
                        LinearLayoutManager.VERTICAL
                )
        );
        walletAdapter.setOnItemClickListener((position, wallet) -> {
            if (!TextUtils.isEmpty(wallet.getNanjAddress())) {
                nanjWalletManager.enableWallet(wallet);
            } else {
                Toast.makeText(getContext(), "Initializing nanj wallet, please wait", Toast.LENGTH_LONG).show();
            }
        });
        walletAdapter.setOnRemoveWalletListener(wallet -> {
            nanjWalletManager.removeWallet(wallet);
            walletAdapter.setData(nanjWalletManager.getWalletList());
        });
        walletAdapter.setOnBackupWalletListener(wallet ->
                backupWallet(wallet.getPrivateKey())
        );
        walletList.setAdapter(walletAdapter);
    }

    private void backupWallet(String privateKey) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, privateKey);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Backup wallet"));
    }

    public void setNanjWalletManager(NANJWalletManager nanjWalletManager) {
        this.nanjWalletManager = nanjWalletManager;
        setData(nanjWalletManager.getWalletList());
    }

    public void setData(List<NANJWallet> wallets) {
        walletAdapter.setData(wallets);
    }

    public void setPassword(String _password) {
        this._password = _password;
    }
}
