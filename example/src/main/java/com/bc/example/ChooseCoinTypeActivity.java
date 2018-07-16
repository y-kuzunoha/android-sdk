package com.bc.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bc.core.model.NANJConfigModel;
import com.bc.core.nanj.NANJWalletManager;

public class ChooseCoinTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = new RecyclerView(this);
        setContentView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NANJConfigModel cf = NANJWalletManager.instance.getConfig();
        if (cf != null && cf.getData() != null) {
            CoinTypeAdapter adapter = new CoinTypeAdapter();
            adapter.setData(NANJWalletManager.instance.getConfig().getData().getErc20s());
            adapter.setOnItemClickListener((o, p) -> NANJWalletManager.instance.setErc20(p));
            recyclerView.setAdapter(adapter);
        }
    }
}
