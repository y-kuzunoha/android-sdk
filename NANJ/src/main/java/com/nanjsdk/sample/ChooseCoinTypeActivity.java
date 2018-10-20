package com.nanjsdk.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nanjcoin.sdk.model.Erc20;
import com.nanjcoin.sdk.nanj.NANJWalletManager;

import java.util.List;

public class ChooseCoinTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = new RecyclerView(this);
        setContentView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Erc20> erc20List = NANJWalletManager.instance.getErc20List();
        if (erc20List.size() > 0) {
            CoinTypeAdapter adapter = new CoinTypeAdapter();
            adapter.setData(NANJWalletManager.instance.getErc20List());
            adapter.setOnItemClickListener((o, p) -> NANJWalletManager.instance.setErc20(p));
            recyclerView.setAdapter(adapter);
        }
    }
}
