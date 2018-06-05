package com.bc.example;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bc.core.nanj.listener.GetNANJWalletListener;
import com.bc.core.nanj.NANJConvert;
import com.bc.core.nanj.listener.NANJRateListener;
import com.bc.core.nanj.listener.NANJTransactionListener;
import com.bc.core.nanj.NANJWalletManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.Executors;

/**
 * ____________________________________
 * <p>
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/24/18
 * ____________________________________
 */
public class MyWalletFragment extends Fragment {

    private AppCompatTextView tvaddress;
    private AppCompatTextView amountUsd;
    private AppCompatImageView ivAddressWallet;
    private AppCompatTextView nanjRate;

    private NANJWalletManager _nanjWalletManager;
    private WalletHandle walletHandle = new WalletHandle();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_wallet, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        _nanjWalletManager = ((NANJApplication) Objects.requireNonNull(getActivity()).getApplication()).getNanjWalletManager();
        tvaddress = view.findViewById(R.id.address);
        amountUsd = view.findViewById(R.id.amountUsd);
        ivAddressWallet = view.findViewById(R.id.imAddressWallet);
        nanjRate = view.findViewById(R.id.nanjRate);
        view.findViewById(R.id.sendNANJCoin).setOnClickListener(
                view1 -> {
                    if (_nanjWalletManager.getWallet() != null) {
                        startActivity(new Intent(view1.getContext(), SendCoinActivity.class));
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        intView();
    }

    @SuppressLint("SetTextI18n")
    private void intView() {
        if (_nanjWalletManager != null && _nanjWalletManager.getWallet() != null) {

            _nanjWalletManager.getWallet().getNANJWalletAsync(new GetNANJWalletListener() {
                @Override
                public void onError() {

                }

                @Override
                public void onSuccess(@NonNull String address) {
                    initView(address);
                }
            });



        }
    }

    private void initView(String address) {
        tvaddress.setText(String.format("Address: %s", address));

        Executors.newCachedThreadPool().execute(() -> {
            Bitmap mIcon_val = null;
            try {
                URL newurl = new URL("http://api.qrserver.com/v1/create-qr-code/?data=" +address);
                mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String coin = _nanjWalletManager.getWallet().getAmountNanj().toString();
            Log.d("MyWalletFragment", "  coin  " + coin);
            if (getActivity() != null) {
                BigDecimal realCoin = NANJConvert.fromWei(coin, NANJConvert.Unit.NANJ);
                Bitmap finalMIcon_val = mIcon_val;
                getActivity().runOnUiThread(() -> {
                    amountUsd.setText(String.format(getString(R.string.txt_amount_nanj), realCoin));
                    if (finalMIcon_val != null) {
                        ivAddressWallet.setImageBitmap(finalMIcon_val);
                    }
                });

                _nanjWalletManager.getNANJRate(new NANJRateListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(@NonNull BigDecimal value) {
                        nanjRate.setText(
                                "Yen: " + realCoin.multiply(value).toBigInteger()
                        );
                    }

                    @Override
                    public void onFailure(String e) {

                    }
                });
            }

        });
    }

    @SuppressLint("InflateParams")
    private void sendNANJCoinDialog() {
        Context context = getContext();
        if (context == null) return;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_send_nanj_coin, null);
        AppCompatEditText edAddress = view.findViewById(R.id.address);
        AppCompatEditText amountEth = view.findViewById(R.id.amountEth);
        view.findViewById(R.id.qrcode).setOnClickListener(view1 -> {
            if (_nanjWalletManager.getWallet() != null) {
                _nanjWalletManager.getWallet().sendNANJCoinByQrCode(MyWalletFragment.this);
            }
        });
        new AlertDialog.Builder(context)
                .setTitle("Send NANJ Coin")
                .setView(view)
                .setOnDismissListener(dialogInterface -> {
                    walletHandle.setWalletAddressListener(null);
                })
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Send", (dialogInterface, i) ->
                        Objects.requireNonNull(_nanjWalletManager.getWallet()).sentNANJCoin(
                                edAddress.getText().toString(),
                                amountEth.getText().toString(),
                                new NANJTransactionListener() {
                                    @Override
                                    public void onTransferSuccess() {
                                        intView();
                                    }

                                    @Override
                                    public void onTransferFailure() {

                                    }
                                }
                        ))
                .show();
        walletHandle.setWalletAddressListener(edAddress::setText);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        walletHandle.onActivityResult(requestCode, resultCode, data);
    }
}
