package com.bc.example;

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
import com.bc.core.nanj.NANJTransactionsListener;
import com.bc.core.nanj.NANJWalletManager;
import com.bc.core.nanj.Transaction;
import java.util.List;
import java.util.Objects;

/**
 * ____________________________________
 * <p>
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/23/18
 * ____________________________________
 */
public class TransactionsFragment extends Fragment {

    private static final int ITEMS_PAGE = 20;

    private TransactionAdapter transactionAdapter;
    private NANJWalletManager _nanjWalletManager;
    private Boolean isLoading = true;
    private int page = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new RecyclerView(inflater.getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view;

        _nanjWalletManager = ((NANJApplication) Objects.requireNonNull(getActivity()).getApplication()).getNanjWalletManager();
        String address = "";
        if (_nanjWalletManager.getWallet() != null) {
            address = _nanjWalletManager.getWallet().getAddress();
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
                    int visiblesItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if(visiblesItemCount + pastVisiblesItems + 2 >= totalItemCount) {
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
        page = 0;
        transactionAdapter.clearData();
    }

    private void getTransactions() {

        if (_nanjWalletManager.getWallet() != null) {
            transactionAdapter.setAddress(_nanjWalletManager.getWallet().getAddress());
            isLoading = true;
            _nanjWalletManager.getWallet().getTransactions(page, ITEMS_PAGE, new NANJTransactionsListener() {
                @Override
                public void onTransferSuccess(List<Transaction> transactions) {
                    transactionAdapter.setData(transactions);
                    isLoading = false;
                }

                @Override
                public void onTransferFailure() {
                    isLoading = false;
                }
            });
        }
    }
}
