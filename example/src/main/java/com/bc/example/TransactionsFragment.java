package com.bc.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bc.core.nanj.NANJTransactionsListener;
import com.bc.core.nanj.NANJWalletManager;
import com.bc.core.nanj.Transaction;

import java.util.List;

/**
 * ____________________________________
 * <p>
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/23/18
 * ____________________________________
 */
public class TransactionsFragment extends Fragment {

    private TransactionAdapter transactionAdapter;
    private NANJWalletManager _nanjWalletManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new RecyclerView(inflater.getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _nanjWalletManager = ((NANJApplication) getActivity().getApplication()).getNanjWalletManager();
        transactionAdapter = new TransactionAdapter(_nanjWalletManager.getWallet().getAddress());
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(transactionAdapter);

        _nanjWalletManager.getWallet().getTransactions(new NANJTransactionsListener() {
            @Override
            public void onTransferSuccess(List<Transaction> transactions) {
                transactionAdapter.setData(transactions);
            }

            @Override
            public void onTransferFailure() {

            }
        });
    }
}
