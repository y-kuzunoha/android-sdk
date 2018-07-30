package com.bc.example;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.DateFormat;
import android.view.View;

import com.nanjcoin.sdk.model.Transaction;
import com.nanjcoin.sdk.nanj.NANJConvert;
import com.nanjcoin.sdk.nanj.NANJWalletManager;

import java.util.Objects;

public class MoreDetailActivity extends AppCompatActivity {

    private NANJWalletManager nanjWalletManager = NANJWalletManager.instance;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_detail);

        Transaction data = getIntent().getParcelableExtra("Transaction");
        boolean isSent = Objects.equals(data.getFrom(), nanjWalletManager.getWallet().getNanjAddress());
        String add = isSent ? "-" : "";
        ((AppCompatTextView) findViewById(R.id.coin)).setText(add + NANJConvert.fromWei(data.getValue(), NANJConvert.Unit.NANJ).toPlainString() + data.getSymbol());
        ((AppCompatTextView) findViewById(R.id.fee)).setText(data.getTxHash());
        ((AppCompatTextView) findViewById(R.id.transfer)).setText(data.getFrom());
        ((AppCompatTextView) findViewById(R.id.recipient)).setText(data.getTo());
        AppCompatTextView fee = findViewById(R.id.fee);
        if (isSent) {
            fee.setText("-" + NANJConvert.fromWei(data.getTxFee(), NANJConvert.Unit.NANJ).toPlainString() + data.getSymbol());
        } else {
            fee.setVisibility(View.GONE);
            findViewById(R.id.feeTitle).setVisibility(View.GONE);
            findViewById(R.id.feeLine).setVisibility(View.GONE);
        }
        ((AppCompatTextView) findViewById(R.id.date)).setText(DateFormat.format("dd MMM yyyy kk:mm:ss", data.getTimeStamp() * 1000));
        ((AppCompatTextView) findViewById(R.id.msg)).setText(data.getMessage());
        findViewById(R.id.moredetail).setOnClickListener(v -> {
            String url = "https://ropsten.etherscan.io/tx/" + data.getTxHash();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

    }
}
