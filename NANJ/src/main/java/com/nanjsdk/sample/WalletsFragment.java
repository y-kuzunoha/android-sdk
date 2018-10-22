package com.nanjsdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nanjcoin.sdk.nanj.NANJWallet;
import com.nanjcoin.sdk.nanj.NANJWalletManager;

import java.util.List;
import java.util.Objects;

/**
 * ____________________________________
 * <p>
 * Generator: NANJ Team - support@nanjcoin.com
 * CreatedAt: 4/23/18
 * ____________________________________
 */
public class WalletsFragment extends Fragment {

    private NANJWalletManager nanjWalletManager;
    private WalletAdapter walletAdapter;
    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new LoadingDialog(view.getContext());
        walletAdapter = new WalletAdapter();
        RecyclerView walletList = view.findViewById(R.id.walletList);
        walletList.addItemDecoration(
                new DividerItemDecoration(
                        view.getContext(),
                        LinearLayoutManager.VERTICAL
                )
        );
        walletAdapter.setWalletAdapterListener(new WalletAdapter.WalletAdapterListener() {
            @Override
            public void onItemClick(int position, NANJWallet wallet) {
                if (!TextUtils.isEmpty(wallet.getNanjAddress())) {
                    loadingDialog.show();
                    new Thread(() -> {
                        Log.d("wtf", "onViewCreated: 123");
                        nanjWalletManager.enableWallet(wallet);
                        Log.d("wtf", "onViewCreated: 321");
                        Objects.requireNonNull(getActivity()).runOnUiThread(() -> loadingDialog.dismiss());
                        getActivity().finish();
                    }).start();
                } else {
                    Toast.makeText(getContext(), "Initializing nanj wallet, please wait", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onBackupWalletClick(NANJWallet wallet, boolean isPrivateKey) {
                WalletsFragment.this.onBackupWalletClick(wallet,isPrivateKey);
            }

            @Override
            public void onRemoveWalletClick(NANJWallet wallet) {
                nanjWalletManager.removeWallet(wallet);
                walletAdapter.setData(nanjWalletManager.getWalletList());
            }

            @Override
            public void onRetreivedNANJWalletAddress(String walletAddress) {
                getActivity().runOnUiThread(() -> {
                    walletAdapter.notifyDataSetChanged();
                });
            }
        });
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

    private void onBackupWalletClick(NANJWallet wallet, boolean isPrivateKey) {
        String p = wallet.getPrivateKey();
        if (!isPrivateKey && p != null) {
            p = NANJWalletManager.instance.convertPrivateKeyToKeystore(p);
        }
        backupWallet(p);
    }
}
