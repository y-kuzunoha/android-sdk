package com.bc.example;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.nanjcoin.sdk.nanj.NANJWalletManager;
import com.nanjcoin.sdk.nanj.listener.SendNANJCoinListener;


/**
 * ____________________________________
 * <p>
 * Generator: NANJ Team - support@nanjcoin.com
 * CreatedAt: 5/4/18
 * ____________________________________
 */
public class SendCoinActivity extends AppCompatActivity {

    private NANJWalletManager _nanjWalletManager;
    private WalletHandler walletHandler = new WalletHandler();
    private Loading loading;

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_nanj_coin);
        setupActionBar();
        loading = new Loading(this);
        _nanjWalletManager = NANJWalletManager.instance;
        AppCompatTextView status = findViewById(R.id.status);
        findViewById(R.id.qr).setOnClickListener(v -> {
            if (_nanjWalletManager.getWallet() != null) {
                _nanjWalletManager.getWallet().sendNANJCoinByQrCode(this);
            }
        });
        AppCompatEditText edAddress = findViewById(R.id.address);
        AppCompatEditText edAmount = findViewById(R.id.amount);
        EditText edMsg = findViewById(R.id.edMsg);
        walletHandler.setWalletAddressListener(edAddress::setText);
        findViewById(R.id.send).setOnClickListener((View v2) -> {
            String address = edAddress.getText().toString();
            status.setText("Nanj coin sending to address: " + address);
            loading.show();
            _nanjWalletManager.getWallet().sendNANJCoin(
                    address,
                    edAmount.getText().toString(),
                    edMsg.getText().toString(),
                    new SendNANJCoinListener() {
                        @Override
                        public void onError() {
                            runOnUiThread(() -> {
                                        loading.dismiss();
                                        status.setText("Failure!");
                                    }
                            );
                        }

                        @Override
                        public void onSuccess() {
                            runOnUiThread(() -> {
                                status.setText("Sent success!");
                                loading.dismiss();
                            });
                        }
                    }
            );
        });

        AppCompatTextView nfc = findViewById(R.id.nfc);
        nfc.setVisibility(NfcAdapter.getDefaultAdapter(this) == null ? View.GONE : View.VISIBLE);
        nfc.setOnClickListener(v -> {
            if (_nanjWalletManager.getWallet() != null) {
                _nanjWalletManager.getWallet().sendNANJCoinByNfcCode(this);
            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setTitle("Send NANJ coin");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_24px);
        actionBar.setHomeActionContentDescription("Back");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        walletHandler.onActivityResult(requestCode, resultCode, data);
    }
}
