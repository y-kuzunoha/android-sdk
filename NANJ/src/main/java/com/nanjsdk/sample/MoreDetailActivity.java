package com.nanjsdk.sample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.DateFormat;
import android.view.View;

import com.nanjcoin.sdk.model.NANJTransaction;
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

        NANJTransaction data = getIntent().getParcelableExtra("NANJTransaction");
        boolean isSent = Objects.equals(data.getFrom(), Objects.requireNonNull(nanjWalletManager.getWallet()).getNanjAddress());
        String add = isSent ? "-" : "";
        AppCompatTextView sender = findViewById(R.id.coin);
        sender.setText(add + NANJConvert.fromWei(Objects.requireNonNull(data.getValue()), NANJConvert.Unit.NANJ).toPlainString() + data.getSymbol());
        sender.setTextColor(ContextCompat.getColor(this, isSent ? android.R.color.holo_red_dark : android.R.color.holo_green_dark));
        ((AppCompatTextView) findViewById(R.id.fee)).setText(data.getTxHash());
        ((AppCompatTextView) findViewById(R.id.transfer)).setText(data.getFrom());
        ((AppCompatTextView) findViewById(R.id.recipient)).setText(data.getTo());
        AppCompatTextView fee = findViewById(R.id.fee);
        if (isSent) {
            fee.setText("-" + NANJConvert.fromWei(Objects.requireNonNull(data.getTxFee()), NANJConvert.Unit.NANJ).toPlainString() + data.getSymbol());
        } else {
            fee.setVisibility(View.GONE);
            findViewById(R.id.feeTitle).setVisibility(View.GONE);
            findViewById(R.id.feeLine).setVisibility(View.GONE);
        }
        ((AppCompatTextView) findViewById(R.id.date)).setText(DateFormat.format("dd MMM yyyy kk:mm:ss", data.getTimeStamp() * 1000));
        ((AppCompatTextView) findViewById(R.id.msg)).setText(data.getMessage());
        findViewById(R.id.moredetail).setOnClickListener(v -> {

            String url = data.getURLOnEtherscan();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

    }
}
