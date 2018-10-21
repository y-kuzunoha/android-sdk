package com.nanjsdk.sample;

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
import android.widget.LinearLayout;

import com.nanjcoin.sdk.model.DataTransaction;
import com.nanjcoin.sdk.nanj.listener.NANJTransactionsListener;
import com.nanjcoin.sdk.nanj.NANJWalletManager;

import java.util.Objects;

/**
 * ____________________________________
 * <p>
 * Generator: NANJ Team - support@nanjcoin.com
 * CreatedAt: 4/23/18
 * ____________________________________
 */
public class TransactionsFragment extends Fragment {

    private static final int ITEMS_PAGE = 10;

    private TransactionAdapter transactionAdapter;
    private NANJWalletManager _nanjWalletManager;
    private Boolean isLoading = true;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new RecyclerView(inflater.getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view;

        _nanjWalletManager = NANJWalletManager.instance;
        String address = "";
        if (_nanjWalletManager.getWallet() != null) {
            address = _nanjWalletManager.getWallet().getNanjAddress();
        }
        transactionAdapter = new TransactionAdapter(address);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(transactionAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!isLoading && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItem = layoutManager.findFirstVisibleItemPosition();
                    if(visibleItemCount + pastVisibleItem + 2 >= totalItemCount) {
                        page = page + 1;
                        getTransactions();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        resetTransaction();
        getTransactions();
    }

    private void resetTransaction() {
        page = 1;
        transactionAdapter.clearData();
    }

    private void getTransactions() {

        if (_nanjWalletManager.getWallet() != null) {
            transactionAdapter.setAddress(_nanjWalletManager.getWallet().getNanjAddress());
            isLoading = true;
            _nanjWalletManager.getWallet().getTransactions(page, ITEMS_PAGE, new NANJTransactionsListener() {
                @Override
                public void onTransferSuccess(DataTransaction data) {
                    isLoading = false;
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> transactionAdapter.setData(data.getTransactions()));
                }

                @Override
                public void onTransferFailure() {
                    isLoading = false;
                }
            });
        }
    }
}
